package com.shrimpcolo.johnnytam.idouban.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.activity.detail.WebViewActivity;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener{

    private String url;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(String info, int type) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString("info", info);
        args.putInt("type", type);//0: 影片信息 Tab; 1: 简介 Tab
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView textInfo = (TextView) view.findViewById(R.id.tvInfo);
        textInfo.setText(getArguments().getString("info"));
        if(1 == getArguments().getInt("type")) {
            textInfo.setOnClickListener(this);
            url = textInfo.getText().toString();
        }
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvInfo:
                Log.e(HomeActivity.TAG, "===> website click!!!!");
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("website", url);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
