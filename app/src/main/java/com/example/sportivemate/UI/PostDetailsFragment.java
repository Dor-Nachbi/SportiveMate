package com.example.sportivemate.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.PostFireBase;
import com.example.sportivemate.model.PostModel;
import com.example.sportivemate.model.SportModel;
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        PostFireBase.getPostById(post.getId(), new PostModel.Listener<Post>() {
            @Override
            public void onComplete(Post data) {
                phoneNumber.setText(data.getPhoneNumber());
                description.setText(data.getDescription());
                city.setText(data.getCity());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_post_menu_item){
            new AlertDialog.Builder(getContext(),R.style.AlertDialog).setTitle("Delete Post").setMessage(
                    "Are ou sure you want to delete this post?").setPositiveButton(
                    "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PostModel.instance.deletePost(post, new PostModel.Listener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                    Navigation.findNavController(view).navigateUp();
                                }
                            });
                        }
                    }
            ).setNegativeButton("No",null).setIconAttribute(android.R.attr.alertDialogIcon).show();
        }
        else if(item.getItemId() == R.id.edit_post_menu_item)
        {
            Log.d("TAG", "edit");
            Navigation.findNavController(getActivity(),R.id.nav_host_home).
                    navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToPostEditFragment(post.getId()));
        }
        return super.onOptionsItemSelected(item);
    }
}