package com.example.sportivemate.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportFirebase;
import com.example.sportivemate.model.SportModel;
import com.example.sportivemate.model.StoreModel;

import static android.app.Activity.RESULT_OK;


public class AddSportFragment extends Fragment {


    View view;
    Button save;
    Button takePhoto;
    ImageView image;
    EditText name;
    EditText description;
    Bitmap imageBitmap;
    ProgressBar progressBar;

    public AddSportFragment() {
        // Required empty public constructor
    }

    public static AddSportFragment newInstance(String param1, String param2) {
        AddSportFragment fragment = new AddSportFragment();
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
        view = inflater.inflate(R.layout.fragment_add_sport, container, false);
        takePhoto = view.findViewById(R.id.new_sport_take_photo_btn);
        save = view.findViewById(R.id.new_sport_save_btn);
        name = view.findViewById(R.id.add_sport_fragment_new_sport_name);
        description = view.findViewById(R.id.add_sport_fragment_new_description);
        image = view.findViewById(R.id.new_sport_image_v);
        progressBar = view.findViewById(R.id.new_sport_progress);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    takePhoto.setClickable(false);
                    save.setClickable(false);
                    StoreModel.uploadImage(imageBitmap, name.getText().toString(), new StoreModel.Listener() {
                        @Override
                        public void onSuccess(String url) {
                            final Sport sport = new Sport(name.getText().toString(),description.getText().toString(),url);
                            SportFirebase.addSport(sport, new SportModel.Listener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                    SportModel.instance.refreshSportsList(null);
                                    Navigation.findNavController(getActivity(),R.id.nav_host_home).navigateUp();
                                }
                            });
                        }

                        @Override
                        public void onFail() {

                        }
                    },"sports_images");


            }
        });
        return view;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getActivity() != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    imageBitmap = (Bitmap) extras.get("data");
                    image.setImageBitmap(imageBitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}