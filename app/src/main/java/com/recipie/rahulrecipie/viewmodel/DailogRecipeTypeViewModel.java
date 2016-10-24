package com.recipie.rahulrecipie.viewmodel;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.fragment.MainFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by developers on 22/10/16.
 */

public class DailogRecipeTypeViewModel extends BaseViewModel {

    /**
     * Parent Fragment
     */
    private MainFragment fragment;

    /**
     * List Recipe Type
     */
    private List<String> recipeType = Collections.emptyList();

    /**
     * View
     */
    private View view;

    /**
     * Constructor
     */
    public DailogRecipeTypeViewModel(MainFragment fragment, ArrayList<String> recipeType, View view) {
        this.fragment = fragment;
        this.recipeType = recipeType;
        this.view = view;

        popupList();
    }

    public void popupList() {
        try {

            ArrayAdapter<String> adapterTypeSelection = new ArrayAdapter<String>(fragment.getActivity(),
                    R.layout.item_recipetype
                    ,
                    R.id.textViewRecipeType, recipeType);
            ListView listview = (ListView) view.findViewById(
                    R.id.listviewRecipe);
            listview.setAdapter(adapterTypeSelection);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    sendResponse(2, recipeType.get(position));

                }
            });
        } catch (Exception e) {

        }

    }

    public void sendResponse(int type, String recipe) {
        if (fragment != null) {
            fragment.getMainFragmentViewModel().closeDailog(2);
            fragment.getMainFragmentViewModel().selectedRecipeType(recipe);
        }
    }
}
