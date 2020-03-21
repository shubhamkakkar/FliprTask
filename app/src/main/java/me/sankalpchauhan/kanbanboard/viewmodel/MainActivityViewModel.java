package me.sankalpchauhan.kanbanboard.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import me.sankalpchauhan.kanbanboard.model.Board;
import me.sankalpchauhan.kanbanboard.repository.LoginRepository;
import me.sankalpchauhan.kanbanboard.repository.MainActivityRepository;
import me.sankalpchauhan.kanbanboard.util.Constants;

public class MainActivityViewModel extends AndroidViewModel {
    MainActivityRepository mainActivityRepository;
    public LiveData<Board> boardLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mainActivityRepository = new MainActivityRepository();
    }

    public void createBoard(Context context, String UserUID, String boardTitle, String boardType){
        Log.e(Constants.TAG, UserUID+" "+boardType+" "+boardTitle);
        boardLiveData = mainActivityRepository.createPersonalBoard(context, UserUID, boardTitle, boardType);
    }
}
