package com.recipie.rahulrecipie.viewmodel;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.fragment.MainFragment;

/**
 * Created by developers on 25/10/16.
 */

public class DailogPersonServedViewModel extends BaseViewModel {

    /**
     * Fragment
     */
    private MainFragment fragment;

    /**
     * View
     */
    private View view;

    String[] types = {"1 Person", "2 Person", "3 Person", "4 Person", "5 Person", "6 Person", "7 Person",
            "8 Person", "9 Person", "10 Person"};

    /**
     * Constructor
     */
    public DailogPersonServedViewModel(MainFragment fragment, View view) {
        this.fragment = fragment;
        this.view = view;

        populateList();
    }

    public void populateList() {
        try {

            ArrayAdapter<String> adapterTypeSelection = new ArrayAdapter<String>(fragment.getActivity(),
                    R.layout.item_recipetype
                    ,
                    R.id.textViewRecipeType, types);
            ListView listview = (ListView) view.findViewById(
                    R.id.listviewPerson);
            listview.setAdapter(adapterTypeSelection);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    sendResponse(4, types[position]);

                }
            });
        } catch (Exception e) {

        }
    }

    public void sendResponse(int type, String personText) {
        if (fragment != null) {
            fragment.getMainFragmentViewModel().closeDailog(type);
            fragment.getMainFragmentViewModel().setPersonServedText(personText);
        }

    }
}
