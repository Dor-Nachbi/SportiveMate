package com.example.sportivemate.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Post;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class PostDetailsFragment extends Fragment {
    View view;
    Post post;
    TextView time;
    TextView name;
    TextView sportName;
    ImageView image;
    TextView description;
    TextView phoneNumber;
    TextView city;


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
        post = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        time = view.findViewById(R.id.post_details_time);
        description = view.findViewById(R.id.post_details_description);
        name = view.findViewById(R.id.post_details_name);
        sportName = view.findViewById(R.id.post_details_sport_name);
        image = view.findViewById(R.id.post_detail_image);
        phoneNumber = view.findViewById(R.id.post_detail_phone);
        city = view.findViewById(R.id.post_detail_city);
        city.setText(post.getCity());
        String longV = ""+post.getDate();
        long millisecond = Long.parseLong(longV);
        time.setText(DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(millisecond)).toString());
        name.setText(post.getName());
        sportName.setText(post.getSportName());
        phoneNumber.setText(post.getPhoneNumber());
        description.setText(post.getDescription());
        if(post.getImageUrl() != null && !post.getImageUrl().equals("")) {
            Picasso.get().load(post.getImageUrl()).placeholder(R.drawable.ic_launcher_background).into(image);
        }else{
            image.setImageResource(R.drawable.ic_launcher_background);
        }
        return view;
    }
}