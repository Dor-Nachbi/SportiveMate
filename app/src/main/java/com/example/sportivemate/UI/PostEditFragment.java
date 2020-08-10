package com.example.sportivemate.UI;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.KeyboardShortcutInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sportivemate.MainActivity;
import com.example.sportivemate.R;
import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.PostFireBase;
import com.example.sportivemate.model.PostModel;
import com.example.sportivemate.model.Sport;

import static androidx.core.content.ContextCompat.getSystemService;


public class PostEditFragment extends Fragment {

    String postID;
    EditText phoneNumber;
    TextView name;
    Spinner city;
    EditText description;
    View view;
    Button save;
    Post post;
    ProgressBar progressBar;


    public PostEditFragment() {
    }

    public static PostEditFragment newInstance(String param1, String param2) {
        PostEditFragment fragment = new PostEditFragment();
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
        view = inflater.inflate(R.layout.fragment_post_edit, container, false);
        description = view.findViewById(R.id.edit_post_fragment_new_description);
        city = view.findViewById(R.id.edit_post_fragment_new_city);
        name = view.findViewById(R.id.edit_post_name_tv);
        phoneNumber = view.findViewById(R.id.edit_post_fragment_new_phone);
        save = view.findViewById(R.id.edit_post_save_btn);
        postID = PostEditFragmentArgs.fromBundle(getArguments()).getPostID();
        progressBar = view.findViewById(R.id.new_edit_post_progress);
        PostFireBase.getPostById(postID, new PostModel.Listener<Post>() {
            @Override
            public void onComplete(Post data) {
                post = data;
                name.setText(data.getName());
                description.setText(data.getDescription());
                phoneNumber.setText(data.getPhoneNumber());
                int i = 0;
                for (i = 0; i < city.getCount(); i++) {
                    if (city.getItemAtPosition(i).toString().equalsIgnoreCase(post.getCity()))
                        break;
                }
                city.setSelection(i);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        post.setPhoneNumber(phoneNumber.getText().toString());
                        post.setCity(city.getSelectedItem().toString());
                        Log.d("TAG", description.getText().toString());
                        post.setDescription(description.getText().toString());
                        PostModel.instance.updatePost(post, new PostModel.Listener<Boolean>() {
                            @Override
                            public void onComplete(Boolean data) {
                                Navigation.findNavController(getActivity(),R.id.nav_host_home).navigateUp();
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