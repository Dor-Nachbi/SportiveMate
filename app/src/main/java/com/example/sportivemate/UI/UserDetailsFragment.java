package com.example.sportivemate.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.User;
import com.example.sportivemate.model.UserFirebase;
import com.example.sportivemate.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class UserDetailsFragment extends Fragment {
    Button showMyPosts;
    TextView userName;
    TextView fullName;
    TextView time;
    ImageView userPic;
    Delegate parent;
    public interface Delegate {
        void OnShowUserPostsClicked();
    }
    public UserDetailsFragment() {
        // Required empty public constructor
    }

    public static UserDetailsFragment newInstance(String param1, String param2) {
        UserDetailsFragment fragment = new UserDetailsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        showMyPosts = view.findViewById(R.id.user_details_show_posts);
        userName = view.findViewById(R.id.user_details_user_name);
        fullName = view.findViewById(R.id.user_details_full_name);
        time = view.findViewById(R.id.user_details_time);
        userPic = view.findViewById(R.id.user_detail_image);

        UserFirebase.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                userName.setText(data.getEmail());
                fullName.setText(data.getFullName());
                if (data.getImageUrl() != null && !data.getImageUrl().equals("")) {
                    Picasso.get().load(data.getImageUrl()).placeholder(R.drawable.ic_launcher_background).into(userPic);
                    //time.setText(DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(Long.parseLong(data.ge))).toString());
                }
                else {
                    userPic.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        });
        showMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.OnShowUserPostsClicked();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SportPostsListFragment.Delegate) {
            parent = (UserDetailsFragment.Delegate) getActivity();
        } else {
            throw new RuntimeException("Parent activity must implement Delegate interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }
}