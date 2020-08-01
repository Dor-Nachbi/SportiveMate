package com.example.sportivemate.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Sport;

public class SportPostsListFragment extends Fragment {

    TextView name;
    Sport sport;


    public SportPostsListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SportPostsListFragment newInstance(String param1, String param2) {
        SportPostsListFragment fragment = new SportPostsListFragment();
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
        View view = inflater.inflate(R.layout.fragment_sport_posts_list, container, false);
        name = view.findViewById(R.id.sport_post_list_tv);
        sport = SportPostsListFragmentArgs.fromBundle(getArguments()).getSport();
        update_display();
        return view;

    }

    private void update_display() {
        name.setText(sport.getName());
    }
}