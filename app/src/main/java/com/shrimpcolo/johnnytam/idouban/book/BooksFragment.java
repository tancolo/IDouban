package com.shrimpcolo.johnnytam.idouban.book;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shrimpcolo.johnnytam.idouban.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {


    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

}
