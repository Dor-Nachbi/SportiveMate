package com.example.sportivemate.UI;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.PostModel;
import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportModel;
import com.example.sportivemate.model.UserModel;

import java.util.List;

public class SportPostListViewModel extends ViewModel {
    private LiveData<List<Post>> liveData;

    public LiveData<List<Post>> getLiveData(Sport sport) {
        if (liveData == null)
            liveData = PostModel.instance.getAllPosts(sport);
        return liveData;
    }
    public LiveData<List<Post>> getUserPostsLiveData() {
        if (liveData == null)
            liveData = PostModel.instance.getUserPosts(UserModel.instance.getCurrentUserId());
        return liveData;
    }
    public void refresh(Sport sport, PostModel.CompleteListener listener,boolean isUserPosts) {
        if(isUserPosts)
            PostModel.instance.refreshUserPostsList(UserModel.instance.getCurrentUserId(), listener);
        else
            PostModel.instance.refreshSportPostsList(sport, listener);
    }
}
