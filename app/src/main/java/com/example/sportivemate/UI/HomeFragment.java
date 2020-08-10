package com.example.sportivemate.UI;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sportivemate.R;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportModel;
import com.example.sportivemate.model.User;
import com.example.sportivemate.model.UserFirebase;
import com.example.sportivemate.model.UserModel;
import com.squareup.picasso.Picasso;


import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView sportsList;
    private List<Sport> sportsData = new LinkedList<>();
    private HomeFragmentViewModel viewModel;
    private SportsListAdapter adapter;
    private LiveData<List<Sport>> liveData;


    public interface Delegate {
        void OnItemSelected(Sport sport);
    }

    Delegate parent;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sportsList = view.findViewById(R.id.sports_list_list);
        sportsList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        sportsList.setLayoutManager(layoutManager);

        adapter = new SportsListAdapter();
        sportsList.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Sport sport = sportsData.get(position);
                parent.OnItemSelected(sport);
            }
        });

        sportsList.addItemDecoration(new DividerItemDecoration(sportsList.getContext(), layoutManager.getOrientation()));

        liveData = viewModel.getLiveData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Sport>>() {
            @Override
            public void onChanged(List<Sport> sports) {
                sportsData = sports;
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.sports_list);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new SportModel.CompleteListener() {
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

        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

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

    static class SportRowViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public SportRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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
            name = itemView.findViewById(R.id.row_sport_name_tv);
            image = itemView.findViewById(R.id.sport_imageView);
        }

        void bind(Sport sport) {
            name.setText(sport.getName());
            if(sport.getImageUrl() != null && sport.getImageUrl() != "") {
                Picasso.get().load(sport.getImageUrl()).placeholder(R.drawable.ic_launcher_background).into(image);
            }else{
                image.setImageResource(R.drawable.ic_launcher_background);
            }

        }
    }

    class SportsListAdapter extends RecyclerView.Adapter<SportRowViewHolder> {
        private OnItemClickListener listener;

        void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
        @NonNull
        @Override
        public SportRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row_sport, parent, false);
            return new SportRowViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull SportRowViewHolder holder, int position) {
            Sport sport = sportsData.get(position);
            holder.bind(sport);
        }

        @Override
        public int getItemCount() {
            return sportsData.size();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        UserFirebase.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                inflater.inflate(R.menu.sport_list_menu,menu);
                if(!data.getEmail().toString().equals("1@1.com")) {
                    MenuItem addSport = menu.findItem(R.id.sport_list_menu_add_sport);
                    addSport.setVisible(false);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sport_list_menu_user_details:
                Navigation.findNavController(getActivity(), R.id.nav_host_home).navigate(R.id.userDetailsFragment);
                return super.onOptionsItemSelected(item);
            case R.id.sport_list_menu_add_sport:
                Navigation.findNavController(getActivity(), R.id.nav_host_home).navigate(R.id.addSportFragment);
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
