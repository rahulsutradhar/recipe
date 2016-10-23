package com.recipie.rahulrecipie.viewmodel;

import android.view.View;
import android.widget.Toast;

import com.recipie.rahulrecipie.fragment.MainFragment;

/**
 * Created by developers on 22/10/16.
 */

public class DailogMenuViewModel extends BaseViewModel {

    /**
     * MainFragment
     */
    private MainFragment fragment;

    /**
     * Constructor
     */
    public DailogMenuViewModel(MainFragment fragment) {
        this.fragment = fragment;
    }

    public View.OnClickListener onChooseCamera() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(1);
            }
        };
    }

    public View.OnClickListener onChooseGallery() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(2);
            }
        };
    }

    public void sendResponse(int type) {
        if (fragment != null) {
            fragment.getMainFragmentViewModel().itemChooseInMenuDailog(type);
            fragment.getMainFragmentViewModel().closeDailog(1);
        }
    }

}
