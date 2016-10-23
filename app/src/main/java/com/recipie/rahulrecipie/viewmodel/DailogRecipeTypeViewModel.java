package com.recipie.rahulrecipie.viewmodel;

import android.view.View;

import com.recipie.rahulrecipie.fragment.MainFragment;

/**
 * Created by developers on 22/10/16.
 */

public class DailogRecipeTypeViewModel extends BaseViewModel {

    /**
     * Parent Fragment
     */
    private MainFragment fragment;

    /**
     * Constructor
     */
    public DailogRecipeTypeViewModel(MainFragment fragment) {
        this.fragment = fragment;
    }

    public View.OnClickListener onChooseBreakFast() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(1);
            }
        };
    }

    public View.OnClickListener onChooseLunch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(2);
            }
        };
    }

    public View.OnClickListener onChooseDesert() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(3);
            }
        };
    }

    public View.OnClickListener onChooseSalad() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(4);
            }
        };
    }

    public void sendResponse(int type) {
        if (fragment != null) {
            fragment.getMainFragmentViewModel().selectedRecipeType(type);
            fragment.getMainFragmentViewModel().closeDailog(2);
        }
    }
}
