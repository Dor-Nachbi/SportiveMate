package com.example.sportivemate.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgs;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sportivemate.MainActivity;
import com.example.sportivemate.R;
import com.example.sportivemate.model.AppLocalDb;
import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.PostFireBase;
import com.example.sportivemate.model.PostModel;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportFirebase;
import com.example.sportivemate.model.SportModel;
import com.example.sportivemate.model.User;
import com.example.sportivemate.model.UserFirebase;
import com.example.sportivemate.model.UserModel;


public class AddPostFragment extends Fragment {
    EditText phoneNumber;
    TextView name;
    Spinner city;
    EditText description;
    View view;
    Button save;
    ProgressBar progressBar;
    String imageUrl;
    String ownerId;
    String sportName;
    Sport sport;
    static String username;

    public AddPostFragment() {
    }

    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
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
        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        phoneNumber = view.findViewById(R.id.add_post_fragment_new_phone);
        name = view.findViewById(R.id.post_name_tv);
        name.setText(AddPostFragmentArgs.fromBundle(getArguments()).getUsername());
        city = view.findViewById(R.id.add_post_fragment_new_city);
        description = view.findViewById(R.id.add_post_fragment_new_description);
        save = view.findViewById(R.id.new_post_save_btn);
        sport = AddPostFragmentArgs.fromBundle(getArguments()).getSport();
        sportName = AddPostFragmentArgs.fromBundle(getArguments()).getSport().getName();
        progressBar = view.findViewById(R.id.new_post_progress);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UserFirebase.getCurrentUserDetails(new UserModel.Listener<User>() {
                    @Override
                    public void onComplete(User data) {
                        ownerId = data.getId();
                        imageUrl = data.getImageUrl();
                        Log.d("TAG",imageUrl);
                        Post post = new Post(name.getText().toString(),description.getText().toString(),imageUrl,
                                sportName,ownerId,city.getSelectedItem().toString(),phoneNumber.getText().toString());
                        PostModel.instance.addPost(post, new PostModel.Listener<Post>() {
                            @Override
                            public void onComplete(Post data) {
                                PostModel.instance.refreshSportPostsList(sport, new PostModel.CompleteListener() {
                                    @Override
                                    public void onComplete() {
                                        Navigation.findNavController(getActivity(),R.id.nav_host_home).navigateUp();
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle();
    }
}