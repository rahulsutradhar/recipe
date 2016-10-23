package com.recipie.rahulrecipie.viewmodel;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.NumberPicker;

import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.fragment.MainFragment;

/**
 * Created by developers on 22/10/16.
 */

public class DailogCookingTimeViewModel extends BaseViewModel {

    /**
     * Main Fragment
     */
    private MainFragment fragment;

    /**
     * Number picker
     */
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMin;

    /**
     * View
     */
    private View view;

    /**
     * Constructor
     */
    public DailogCookingTimeViewModel(MainFragment fragment, View view) {
        this.fragment = fragment;
        this.view = view;


        bindNumberPicker();
    }

    public void bindNumberPicker() {
        numberPickerHour = (NumberPicker) view.findViewById(R.id.numberPickerHour);
        numberPickerMin = (NumberPicker) view.findViewById(R.id.numberPickerMinute);

        numberPickerHour.setMaxValue(12);
        numberPickerHour.setMinValue(0);

        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(12);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 5;
                return "" + temp;
            }
        };
        numberPickerMin.setFormatter(formatter);

        setDividerColor(numberPickerHour, R.color.background_main);
        setDividerColor(numberPickerMin, R.color.background_main);
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public View.OnClickListener onClickDone() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open dailog to choose types
                sendResponse(3);
            }
        };
    }

    public View.OnClickListener onClickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open dailog to choose types
                sendResponse(3);
            }
        };
    }

    public void sendResponse(int type) {
        if (fragment != null) {
            fragment.getMainFragmentViewModel().closeDailog(type);
        }
    }
}

