package com.shrimpcolo.johnnytam.idouban.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.moviewebsite.WebViewActivity;
import com.shrimpcolo.johnnytam.idouban.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private String mUrl;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment createInstance(String info, int type) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.INTENT_EXTRA_FRAGMENT_INFO, info);
        args.putInt(AppConstants.INTENT_EXTRA_FRAGMENT_TYPE, type);//0: 影片信息Tab; 1: 简介Tab
        movieDetailFragment.setArguments(args);

//        Log.d(TAG, "\n info: " + info + " type: " + type);

        return movieDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView textInfo = (TextView) view.findViewById(R.id.tvInfo);
        textInfo.setText(getArguments().getString(AppConstants.INTENT_EXTRA_FRAGMENT_INFO));

        if(AppConstants.TYPE_MOVIE_WEBSITE == getArguments().getInt(AppConstants.INTENT_EXTRA_FRAGMENT_TYPE)) {
            textInfo.setOnClickListener(this);
            mUrl = textInfo.getText().toString();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvInfo:
                Log.e(HomeActivity.TAG, "===> website click!!!!");
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(AppConstants.INTENT_EXTRA_WEBSITE_URL, mUrl);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
