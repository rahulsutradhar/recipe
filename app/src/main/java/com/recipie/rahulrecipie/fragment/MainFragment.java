package com.recipie.rahulrecipie.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.activity.BaseActivity;
import com.recipie.rahulrecipie.databinding.MainFragmentBinding;
import com.recipie.rahulrecipie.viewmodel.MainFragmentViewModel;


import static android.app.Activity.RESULT_OK;
import static com.recipie.rahulrecipie.global.Constant.CLICK_PICTURE;
import static com.recipie.rahulrecipie.global.Constant.SELECT_PICTURE;

/**
 * Created by developers on 21/10/16.
 */

public class MainFragment extends BaseFragment {

    /**
     * View Model
     */
    private MainFragmentViewModel mainFragmentViewModel;

    /**
     * Toolbar
     */
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //databinding
        MainFragmentBinding binding = DataBindingUtil.bind(view);

        mainFragmentViewModel = new MainFragmentViewModel(this);
        mainFragmentViewModel.setTitle("Recipe Upload");
        setMainFragmentViewModel(mainFragmentViewModel);
        binding.setViewModel(mainFragmentViewModel);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        constructToolbar();
        mainFragmentViewModel.bindBackgroundImage();
        mainFragmentViewModel.fetchRecipeTypes();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                mainFragmentViewModel.getSlectedImagePath(selectedImageUri);
            } else if (requestCode == CLICK_PICTURE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri clickedUri = mainFragmentViewModel.getClickedImageUri(photo);
                mainFragmentViewModel.getSlectedImagePath(clickedUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            mainFragmentViewModel.openGaleryIntent();
        } else if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mainFragmentViewModel.openCameraIntent();
        }
    }

    /**
     * Construct the toolbar
     */
    public void constructToolbar() {
        try {
            Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
            ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
            this.toolbar = toolbar;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MainFragmentViewModel getMainFragmentViewModel() {
        return mainFragmentViewModel;
    }

    public void setMainFragmentViewModel(MainFragmentViewModel mainFragmentViewModel) {
        this.mainFragmentViewModel = mainFragmentViewModel;
    }
}
