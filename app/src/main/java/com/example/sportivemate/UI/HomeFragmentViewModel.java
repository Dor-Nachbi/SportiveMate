package com.example.sportivemate.UI;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.sportivemate.model.Sport;
import com.example.sportivemate.model.SportModel;

import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private LiveData<List<Sport>> liveData;

    public LiveData<List<Sport>> getLiveData() {
        if (liveData == null)
            liveData = SportModel.instance.getAllSports();
        return liveData;
    }

    public void refresh(SportModel.CompleteListener listener) {
        SportModel.instance.refreshSportsList(listener);
    }
}