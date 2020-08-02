package com.example.sportivemate.UI;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.PostModel;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportModel;

import java.util.List;

public class SportPostListViewModel extends ViewModel {
    private LiveData<List<Post>> liveData;

    public LiveData<List<Post>> getLiveData(Sport sport) {
        if (liveData == null)
            liveData = PostModel.instance.getAllPosts(sport);
        return liveData;
    }

    public void refresh(Sport sport, PostModel.CompleteListener listener) {
        PostModel.instance.refreshSportPostsList(sport, listener);
    }
}
