package com.recipie.rahulrecipie.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.recipie.rahulrecipie.R;
import com.recipie.rahulrecipie.databinding.MainActivityBinding;
import com.recipie.rahulrecipie.fragment.MainFragment;
import com.recipie.rahulrecipie.viewmodel.MainActivityViewModel;

public class MainActivity extends BaseActivity {

    /**
     * Main Fragment
     */
    private MainFragment fragment;

    /**
     * ViewModel
     */
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Data binding
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = new MainActivityViewModel();
        binding.setViewModel(mainActivityViewModel);

        if (savedInstanceState == null) {
            transactToChildFragment();
        }
    }

    public void transactToChildFragment() {
        try {
            fragment = new MainFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out, android.R.anim.fade_out, android.R.anim.fade_in);

            fragmentTransaction.add(R.id.container_fragment, fragment, "MAIN_FRAGMENT");

            fragmentTransaction.commit();
        } catch (Exception e) {
        }
    }

}
