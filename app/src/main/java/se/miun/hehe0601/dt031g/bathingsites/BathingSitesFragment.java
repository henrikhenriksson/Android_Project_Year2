package se.miun.hehe0601.dt031g.bathingsites;

/*
 * FileName: BathingSitesFragment.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file holds a fragment for the bathingsite app project
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-11
 */


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Source: https://www.youtube.com/watch?time_continue=4&v=MdwhhksnOAM&feature=emb_logo
public class BathingSitesFragment extends Fragment {

    public BathingSitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bathing_sites, container, false);
    }
}
