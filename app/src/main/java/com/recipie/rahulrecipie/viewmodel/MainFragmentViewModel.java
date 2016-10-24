package com.recipie.rahulrecipie.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.TextWatcher;
import android.util.Log;
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

import android.support.v4.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.apiclient.api.GetRecipeAPI;
import com.recipie.rahulrecipie.apiclient.response.RecipeResponse;
import com.recipie.rahulrecipie.databinding.CookingTimeDailogBinding;
import com.recipie.rahulrecipie.databinding.DailogMenuBinding;
import com.recipie.rahulrecipie.databinding.PersonServedDailogBinding;
import com.recipie.rahulrecipie.databinding.RecipeTypeDailogBinding;
import com.recipie.rahulrecipie.fragment.MainFragment;
import com.recipie.rahulrecipie.helper.SimpleTextWatcher;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
     * Person served
     */
    private String personServed;

    /**
     * Cooking time
     */
    private String cookingTime;

    /**
     * Selected image visibility
     */
    private boolean selectedImageVisibility;

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
    private AlertDialog alertDialogPersonServed;

    /**
     * BackgroundImageview
     */
    private ImageView imageViewSelectedImage;

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
     * ArrayList
     */
    private ArrayList<String> listRecipeType;

    /**
     * Constructor
     */
    public MainFragmentViewModel(MainFragment fragment) {
        this.fragment = fragment;
        this.recipeName = "";
        this.recipeType = "Choose your recipe type";
        this.personServed = "Serves";
        this.cookingTime = "Cooking Time";
        this.selectedImageVisibility = false;
        this.listRecipeType = new ArrayList<>();
    }

    public void bindBackgroundImage() {
        ImageView imageView = (ImageView) fragment.getActivity().findViewById(R.id.background);
        imageViewSelectedImage = (ImageView) fragment.getActivity().findViewById(R.id.background_selected);
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
                if (selectedImageVisibility == true) {
                    selectedImageVisibility = false;
                    notifyChange();
                } else {
                    Toast.makeText(fragment.getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                }
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
                if (!listRecipeType.isEmpty()) {
                    dailogRecipeType();
                } else {
                    Toast.makeText(fragment.getActivity(), "Something went wrong, cannot open dailog", Toast.LENGTH_SHORT).show();
                }
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (fragment.getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (fragment.getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
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
                dailogPersonServed();
            }
        };
    }

    public void dailogRecipeType() {

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        RecipeTypeDailogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(fragment.getActivity()), R.layout.dailog_recipe_type, container, false);

        DailogRecipeTypeViewModel dailogRecipeTypeViewModel = new DailogRecipeTypeViewModel(fragment, listRecipeType, binding.getRoot());
        binding.setViewModel(dailogRecipeTypeViewModel);

        builder.setView(binding.getRoot());

        alertDialogRecipeType = builder.create();
        alertDialogRecipeType.setCancelable(true);
        alertDialogRecipeType.setCanceledOnTouchOutside(true);
        alertDialogRecipeType.show();
    }

    public void dailogPersonServed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        PersonServedDailogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(fragment.getActivity()), R.layout.dailog_person_served, container, false);

        DailogPersonServedViewModel dailogPersonServedViewModel = new DailogPersonServedViewModel(fragment, binding.getRoot());
        binding.setViewModel(dailogPersonServedViewModel);
        builder.setView(binding.getRoot());

        alertDialogPersonServed = builder.create();
        alertDialogPersonServed.setCancelable(true);
        alertDialogPersonServed.setCanceledOnTouchOutside(true);
        alertDialogPersonServed.show();
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
                if (isCameraPermissionGranted()) {
                    openCameraIntent();
                }
                break;
            case 2:
                if (isStoragePermissionGranted()) {
                    openGaleryIntent();
                }
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
                break;
            case 4:
                alertDialogPersonServed.dismiss();
                break;
        }
    }

    public void setCookingTime(String cookingTimeText) {
        cookingTime = cookingTimeText;
        notifyChange();
    }

    //recipe type
    public void selectedRecipeType(String type) {
        recipeType = type;
        notifyChange();
    }

    //set person served
    public void setPersonServedText(String personText) {
        personServed = personText;
        notifyChange();
    }

    //camera intent
    public void openCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(intent, CLICK_PICTURE);
    }

    //galery intent
    public void openGaleryIntent() {
        // Galery intent
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        fragment.startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    //get selected image path
    public void getSlectedImagePath(Uri uri) {
        selectedImagePath = getPath(uri);
        // Toast.makeText(fragment.getActivity(), "Image " + selectedImagePath, Toast.LENGTH_SHORT).show();
        displayImage(selectedImagePath);

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
        Cursor cursor = fragment.getActivity().getContentResolver().query(uri, projection, null, null, null);
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

    //display selected image in imageview
    public void displayImage(String imagePath) {
        try {
            if (imagePath != null) {
                File imageFile = new File(imagePath);

                if (imageFile.exists()) {
                    selectedImageVisibility = true;
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imageViewSelectedImage.setImageBitmap(myBitmap);
                    notifyChange();
                } else {
                    selectedImageVisibility = false;
                    Toast.makeText(fragment.getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    notifyChange();
                }
            }
        } catch (Exception e) {
            Toast.makeText(fragment.getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Request server to fetch recipe data
     */
    public void fetchRecipeTypes() {
        Callback<RecipeResponse> callback = new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Response<RecipeResponse> response,
                                   Retrofit retrofit) {

                try {
                    if (response.isSuccess()) {

                        RecipeResponse recipeResponse = response.body();
                        HashMap<String, String> map = recipeResponse;

                        //iterate the map to get the keys
                        if (map != null) {
                            Iterator it = map.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                listRecipeType.add(pair.getValue() + "");
                                Log.d(pair.getKey() + " ", " " + pair.getValue());
                                it.remove(); // avoids a ConcurrentModificationException
                            }
                        }

                    } else {
                        if (response != null && !response.isSuccess() && response.errorBody() != null) {
                            //DO ERROR HANDLING HERE
                            Toast.makeText(fragment.getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getActivity(), "Exception", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(fragment.getActivity(), "Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        //parameters
        Bundle params = new Bundle();
        params.putCharSequence("recipe_id", "1");

        GetRecipeAPI getRecipeAPI = GetRecipeAPI.build(params);
        getRecipeAPI.execute(callback);
    }

    /**
     * Parse the key vlue pair of the json response
     */
    public void parseJson() {

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

    public boolean isSelectedImageVisibility() {
        return selectedImageVisibility;
    }

    public void setSelectedImageVisibility(boolean selectedImageVisibility) {
        this.selectedImageVisibility = selectedImageVisibility;
    }

    public String getSelectedImagePath() {
        return selectedImagePath;
    }

    public void setSelectedImagePath(String selectedImagePath) {
        this.selectedImagePath = selectedImagePath;
    }

    public String getPersonServed() {
        return personServed;
    }

    public void setPersonServed(String personServed) {
        this.personServed = personServed;
    }

    public String getCookingTime() {
        return cookingTime;
    }
}
