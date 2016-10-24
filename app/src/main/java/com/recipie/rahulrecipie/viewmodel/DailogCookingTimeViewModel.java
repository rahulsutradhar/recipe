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
     * Time
     */
    private String hoursChoosed;
    private String minuteChoosed;

    /**
     * Constructor
     */
    public DailogCookingTimeViewModel(MainFragment fragment, View view) {
        this.fragment = fragment;
        this.view = view;
        this.hoursChoosed = "0 hours";
        this.minuteChoosed = "0 min";

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


        numberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                if (oldValue != newValue) {
                    hoursChoosed = String.valueOf(newValue) + "hours";
                } else {
                    hoursChoosed = String.valueOf(oldValue) + "hours";
                }
            }
        });

        numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                if (oldValue != newValue) {
                    minuteChoosed = String.valueOf(newValue * 5) + " min";
                } else {
                    minuteChoosed = String.valueOf(oldValue * 5) + " min";
                }
            }
        });

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
                sendCookingTime();
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

    public void sendCookingTime() {
        if (fragment != null) {
            if (hoursChoosed != null && minuteChoosed != null) {
                fragment.getMainFragmentViewModel().setCookingTime(hoursChoosed + " " + minuteChoosed);
            } else if (hoursChoosed == null) {
                fragment.getMainFragmentViewModel().setCookingTime(minuteChoosed);
            } else if (minuteChoosed == null) {
                fragment.getMainFragmentViewModel().setCookingTime(hoursChoosed);
            } else {
                fragment.getMainFragmentViewModel().setCookingTime("0 hours 0 min");
            }
        }
    }
}

