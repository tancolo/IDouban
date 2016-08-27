package com.shrimpcolo.johnnytam.idouban.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.entity.QQEntity;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMeFragment extends Fragment implements HomeActivity.ISetupProfile{
    private QQEntity qqEntity = null;
    private ImageView profileImage;
    private TextView profileName;

    public AboutMeFragment() {
        // Required empty public constructor
    }

//    public AboutMeFragment(QQEntity qqEntity) {
//        this.qqEntity = qqEntity;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(HomeActivity.TAG, "1 onAttach!!!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(HomeActivity.TAG, "1 onCreate!!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(HomeActivity.TAG, "1 onStart!!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(HomeActivity.TAG, "1 onResume!!!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(HomeActivity.TAG, "1 onCreateView!!!");
        return inflater.inflate(R.layout.fragment_about_me, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(HomeActivity.TAG, "1 onActivityCreated!!!");

        profileImage = (ImageView)getActivity().findViewById(R.id.img_profile);
        profileName = (TextView)getActivity().findViewById(R.id.txt_author);

    }

    @Override
    public void setupProfile(QQEntity qqEntity) {
        Log.e(HomeActivity.TAG, "1 setupProfile!!!");
        if(qqEntity != null) {
            Picasso.with(getActivity()).load(qqEntity.getFigureurl_qq_2()).into(profileImage);
            profileName.setText(qqEntity.getNickname());
        }
    }
}
