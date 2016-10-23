package com.recipie.rahulrecipie.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.databinding.CookingTimeDailogBinding;
import com.recipie.rahulrecipie.databinding.DailogMenuBinding;
import com.recipie.rahulrecipie.databinding.RecipeTypeDailogBinding;
import com.recipie.rahulrecipie.fragment.MainFragment;
import com.recipie.rahulrecipie.helper.SimpleTextWatcher;

import static com.recipie.rahulrecipie.global.Constant.*;

/**
 * Created by developers on 21/10/16.
 */

public class MainFragmentViewModel extends BaseViewModel {

    /**
     * Title
     */
    private String title;

    private static final float BLUR_RADIUS = 25f;

    /**
     * MainFragment
     */
    private MainFragment fragment;

    /**
     * ArrayAdapter
     */
    private ArrayAdapter<String> adapterTypeSelection;

    private ViewGroup container;

    /**
     * Alert Dailog
     */
    private AlertDialog alertDialogMenu;
    private AlertDialog alertDialogRecipeType;
    private AlertDialog alertDialogCookingTime;

    /**
     * Image Path
     */
    private String selectedImagePath;


    /**
     * Recipe Name
     */
    private String recipeName;

    /**
     * Recipe Type
     */
    private String recipeType;

    /**
     * Constructor
     */
    public MainFragmentViewModel(MainFragment fragment) {
        this.fragment = fragment;
        this.recipeName = "";
        this.recipeType = "";
    }

    public void bindBackgroundImage() {
        ImageView imageView = (ImageView) fragment.getActivity().findViewById(R.id.background);
        Bitmap bitmap = BitmapFactory.decodeResource(fragment.getActivity().getResources(),
                R.drawable.background_imge);
        Bitmap blurBitmap = blur(bitmap);
        imageView.setImageBitmap(blurBitmap);
        imageView.setHorizontalFadingEdgeEnabled(true);
        imageView.setFadingEdgeLength(40);
        imageView.setHorizontalFadingEdgeEnabled(true);
        imageView.setVerticalFadingEdgeEnabled(true);

    }

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        RenderScript renderScript = RenderScript.create(fragment.getActivity());

        for (int i = 0; i < 10; i++) {
            Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
            Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

            //Intrinsic Gausian blur filter
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        }
        return outputBitmap;
    }

    public TextWatcher getRecipeNameWatcher() {
        return new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String text) {
                recipeName = text;
                notifyChange();
            }
        };
    }

    public View.OnClickListener onClickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(fragment.getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(fragment.getActivity(), "Next -> get feedback", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public View.OnClickListener onClickRecipeType() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open dailog to choose types
                dailogRecipeType();
            }
        };
    }

    public View.OnClickListener onClickCamera() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open a dailogbox
                dailogMenuChooser();
            }
        };
    }

    public View.OnClickListener onClickCookingTime() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open a dailogbox
                dailogCookingTime();
            }
        };
    }

    public View.OnClickListener onClickServe() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open popup
                int[] location = new int[2];
                v.getLocationOnScreen(location);

                // Initialize the Point with x, and y positions
                Point point = new Point();
                point.x = location[0];
                point.y = location[1];

                setUpPopupWindow(v, point);
            }
        };
    }

    //popup to choose number of people served
    public void setUpPopupWindow(View v, Point point) {

        int popupwindowHeight = LinearLayout.LayoutParams.WRAP_CONTENT;

        LayoutInflater layoutInflater = (LayoutInflater) fragment.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(
                R.layout.popup_window_card, null);


        // Creating the PopupWindow
        final PopupWindow pwindow = new PopupWindow(fragment.getActivity());
        pwindow.setContentView(layout);
        pwindow.setHeight(popupwindowHeight);
        pwindow.setFocusable(true);
        pwindow.setOutsideTouchable(true);

        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            pwindow.setElevation(10f);
            pwindow.setBackgroundDrawable(new ColorDrawable(fragment.getActivity().getResources().getColor(R.color.white)));
        } else {
            pwindow.setBackgroundDrawable(fragment.getActivity().getResources().getDrawable(R.drawable.background_window_popup_shadow));
        }

        int OFFSET_X = 0;
        int OFFSET_Y = 400;

        String[] types = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        adapterTypeSelection = new ArrayAdapter<String>(fragment.getActivity(),
                R.layout.item_popup_window
                ,
                R.id.textView, types);
        ListView listview = (ListView) pwindow.getContentView().findViewById(
                R.id.listview);
        listview.setAdapter(adapterTypeSelection);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                pwindow.dismiss();
            }
        });

        pwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                //TODO dismiss settings
            }

        });

        pwindow.setWidth(300);
        pwindow.showAtLocation(layout, Gravity.NO_GRAVITY, point.x + OFFSET_X, point.y - OFFSET_Y);
    }


    public void dailogRecipeType() {

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        RecipeTypeDailogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(fragment.getActivity()), R.layout.dailog_recipe_type, container, false);

        DailogRecipeTypeViewModel dailogRecipeTypeViewModel = new DailogRecipeTypeViewModel(fragment);
        binding.setViewModel(dailogRecipeTypeViewModel);

        builder.setView(binding.getRoot());

        alertDialogRecipeType = builder.create();
        alertDialogRecipeType.setCancelable(true);
        alertDialogRecipeType.setCanceledOnTouchOutside(true);
        alertDialogRecipeType.show();
    }

    public void dailogMenuChooser() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(fragment.getActivity());
        DailogMenuBinding binding = DataBindingUtil.inflate(LayoutInflater.from(fragment.getActivity()), R.layout.dailog_menu, container, false);

        DailogMenuViewModel dailogMenuViewModel = new DailogMenuViewModel(fragment);
        binding.setViewModel(dailogMenuViewModel);
        builder.setView(binding.getRoot());

        alertDialogMenu = builder.create();
        alertDialogMenu.setCancelable(false);
        alertDialogMenu.setCanceledOnTouchOutside(true);
        alertDialogMenu.show();

    }

    public void dailogCookingTime() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(fragment.getActivity());
        CookingTimeDailogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(fragment.getActivity()), R.layout.dailog_cooking_time, container, false);

        DailogCookingTimeViewModel dailogCookingTimeViewModel = new DailogCookingTimeViewModel(fragment, binding.getRoot());
        binding.setViewModel(dailogCookingTimeViewModel);
        builder.setView(binding.getRoot());

        alertDialogCookingTime = builder.create();
        alertDialogCookingTime.setCancelable(false);
        alertDialogCookingTime.setCanceledOnTouchOutside(true);
        alertDialogCookingTime.show();
    }

    //to upload image
    public void itemChooseInMenuDailog(int type) {
        switch (type) {
            case 1:
                openCameraIntent();
                break;
            case 2:
                openGaleryIntent();
                break;
        }
    }

    public void closeDailog(int type) {
        switch (type) {
            case 1:
                //close menu dailog
                alertDialogMenu.dismiss();
                break;
            case 2:
                //close recipe type dailog
                alertDialogRecipeType.dismiss();
                break;
            case 3:
                //close number picker dailog
                alertDialogCookingTime.dismiss();
        }
    }

    //recipe type
    public void selectedRecipeType(int type) {
        switch (type) {
            case 1:
                recipeType = "BreakFast";
                notifyChange();
                break;
            case 2:
                recipeType = "Lunch";
                notifyChange();
                break;
            case 3:
                recipeType = "Desert";
                notifyChange();
                break;
            case 4:
                recipeType = "Salad";
                notifyChange();
                break;
            default:
                recipeType = "Food";
                notifyChange();
                break;
        }
    }

    //camera intent
    public void openCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(intent, CLICK_PICTURE);
    }

    //galery intent
    public void openGaleryIntent() {
        // Galery intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    //get selected image path
    public void getSlectedImagePath(Uri uri) {
        selectedImagePath = getPath(uri);
        Toast.makeText(fragment.getActivity(), selectedImagePath + " path", Toast.LENGTH_SHORT).show();
    }

    //get the actual path of the image
    public String getPath(Uri uri) {
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = fragment.getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();

    }

    //get the uri of clicked image
    public Uri getClickedImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(fragment.getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /******************************
     * Getter Setter
     ******************************/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MainFragment getFragment() {
        return fragment;
    }

    public void setFragment(MainFragment fragment) {
        this.fragment = fragment;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }
}
