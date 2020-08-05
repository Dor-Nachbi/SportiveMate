package com.example.sportivemate.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Post;


public class PostDetailsFragment extends Fragment {
    View view;
    Post post;


    public PostDetailsFragment() {
        // Required empty public constructor
    }


    public static PostDetailsFragment newInstance(String param1, String param2) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_details, container, false);
        long date = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost().getDate();
        Log.d("TAG", ""+date);
        return view;
    }
}