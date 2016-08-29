package com.shrimpcolo.johnnytam.idouban.fragment;


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
import com.shrimpcolo.johnnytam.idouban.entity.UserInfo;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMeFragment extends Fragment implements HomeActivity.ISetupProfile{
    private ImageView profileImage;
    private TextView profileName;

    public AboutMeFragment() {
        // Required empty public constructor
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
    public void setupProfile(UserInfo userInfo) {
        Log.e(HomeActivity.TAG, "1 setupProfile!!!");
        if(userInfo != null) {
            Picasso.with(getActivity()).load(userInfo.getUserIcon()).into(profileImage);
            profileName.setText(userInfo.getUserName());
        }
    }
}
