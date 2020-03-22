package me.sankalpchauhan.kanbanboard.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.File;
import java.util.Date;
import java.util.Map;

import me.sankalpchauhan.kanbanboard.model.Card;
import me.sankalpchauhan.kanbanboard.repository.CardActivityRepository;

public class CardActivityViewModel extends AndroidViewModel {
    CardActivityRepository cardActivityRepository;
    public LiveData<String> urlLiveData;
    public LiveData<Card> cardLiveData;

    public CardActivityViewModel(@NonNull Application application) {
        super(application);
        cardActivityRepository = new CardActivityRepository();
    }

    public void getImageUrl(Context context, File destination, String listid){
        urlLiveData = cardActivityRepository.uploadImage(context, destination, listid);
    }

    public void createCard(Context context, String boardId, String title, String listId, String attachmentUrl, Date dueDate){
        cardLiveData = cardActivityRepository.createCard(context, boardId, title, listId, attachmentUrl, dueDate);
    }

    public void updateCard(Context context, Map<String, Object> updatedMap, String boardId, String listId, String cardId){
        cardActivityRepository.updateCard(context, updatedMap, boardId, listId, cardId);
    }

    public void archiveCard(Context context, String boardId, String listId, String cardId, Card card){
        cardActivityRepository.archiveCard(context, boardId, listId, cardId, card);
    }


}
