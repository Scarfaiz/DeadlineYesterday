package com.example.jeavie.deadlineyesterday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeavie.deadlineyesterday.R;

public class HistoryFragment extends Fragment{

    View v;

    public HistoryFragment() { // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

}