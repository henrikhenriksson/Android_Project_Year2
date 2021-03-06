package se.miun.hehe0601.dt031g.bathingsites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputLayout;

/*
 * FileName: NewBathingSiteActivity.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds the new BathingSite Activity fragment for the bathingsite app project
 * Responsible for creating new bathingsite objects.
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-12
 */


public class NewBathingSiteActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_bathing_site_activity, container, false);
    }

//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }

}
