package com.shrimpcolo.johnnytam.idouban.book;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailFragment extends Fragment {

    public BookDetailFragment() {
        // Required empty public constructor
    }

    public static BookDetailFragment newInstance(String info) {
        Bundle args = new Bundle();
        BookDetailFragment fragment = new BookDetailFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        tvInfo.setText(getArguments().getString("info"));
        return view;
    }

}
