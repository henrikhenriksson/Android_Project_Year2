package se.miun.hehe0601.dt031g.bathingsites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Source: https://www.youtube.com/watch?time_continue=4&v=MdwhhksnOAM&feature=emb_logo
public class BathingSitesFragment extends Fragment {
//
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
