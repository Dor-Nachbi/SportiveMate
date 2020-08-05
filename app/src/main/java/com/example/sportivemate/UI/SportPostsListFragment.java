package com.example.sportivemate.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.PostFireBase;
import com.example.sportivemate.model.PostModel;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportFirebase;
import com.example.sportivemate.model.SportModel;
import com.example.sportivemate.model.StoreModel;
import com.example.sportivemate.model.User;
import com.example.sportivemate.model.UserFirebase;
import com.example.sportivemate.model.UserModel;
import com.squareup.picasso.Picasso;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SportPostsListFragment extends Fragment {

    private RecyclerView postsList;
    private List<Post> postsData = new LinkedList<>();
    private SportPostListViewModel viewModel;
    private PostsListAdapter adapter;
    private LiveData<List<Post>> liveData;
    Sport sport;
    String username;
    String userID;


    public interface Delegate {
        void OnItemSelected(Post post);
    }

    Delegate parent;

    public SportPostsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_posts_list, container, false);
        postsList = view.findViewById(R.id.posts_list_list);
        postsList.setHasFixedSize(true);
        sport = SportPostsListFragmentArgs.fromBundle(getArguments()).getSport();
        UserFirebase.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                username = data.getFullName();
                userID = data.getId();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        postsList.setLayoutManager(layoutManager);

        adapter = new PostsListAdapter();
        postsList.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Post post = postsData.get(position);
                parent.OnItemSelected(post);
            }
        });

        postsList.addItemDecoration(new DividerItemDecoration(postsList.getContext(), layoutManager.getOrientation()));

        liveData = viewModel.getLiveData(sport);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                postsData = posts;
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.posts_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(sport, new PostModel.CompleteListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Delegate) {
            parent = (Delegate) getActivity();
        } else {
            throw new RuntimeException("Parent activity must implement Delegate interface");
        }

        viewModel = new ViewModelProvider(this).get(SportPostListViewModel.class);

        setHasOptionsMenu(true);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }

    interface OnItemClickListener {
        void onClick(int position);
    }

    static class PostRowViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView time;
        TextView city;

        public PostRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });
            name = itemView.findViewById(R.id.row_post_name_tv);
            UserFirebase.getCurrentUserDetails(new UserModel.Listener<User>() {
                @Override
                public void onComplete(User data) {
                    name.setText(data.getFullName());
                }
            });
            image = itemView.findViewById(R.id.post_imageView);
            time = itemView.findViewById(R.id.post_time_tv);
            city = itemView.findViewById(R.id.post_city_tv);

        }

        void bind(Post post) {
            name.setText(post.getName());
            String t = ""+post.getDate();
            time.setText(t);
            city.setText(post.getCity());
            if(post.getImageUrl() != null && post.getImageUrl() != "") {
                Picasso.get().load(post.getImageUrl()).placeholder(R.drawable.ic_launcher_background).into(image);
            }else{
                image.setImageResource(R.drawable.ic_launcher_background);
            }

        }
    }

    class PostsListAdapter extends RecyclerView.Adapter<PostRowViewHolder> {
        private OnItemClickListener listener;

        void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public PostRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row_post, parent, false);
            return new PostRowViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull PostRowViewHolder holder, int position) {
            Post post = postsData.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            return postsData.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sport_list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Navigation.findNavController(getActivity(),R.id.nav_host_home).
                navigate(SportPostsListFragmentDirections.actionSportPostsListFragmentToAddPostFragment(sport,username));
        return super.onOptionsItemSelected(item);
    }
}