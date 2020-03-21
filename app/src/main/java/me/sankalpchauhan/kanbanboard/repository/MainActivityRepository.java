package me.sankalpchauhan.kanbanboard.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import me.sankalpchauhan.kanbanboard.KanbanApp;
import me.sankalpchauhan.kanbanboard.model.Board;

import static me.sankalpchauhan.kanbanboard.util.Constants.PERSONAL_BOARDS;
import static me.sankalpchauhan.kanbanboard.util.Constants.USERS;

public class MainActivityRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference database = rootRef.collection(USERS);

    public MutableLiveData<Board> createPersonalBoard(Context context, String UserUID, String boardTitle, String boardType){
        MutableLiveData<Board> newBoardMutableLiveData = new MutableLiveData<>();
        CollectionReference boardreference = database.document(UserUID).collection(PERSONAL_BOARDS);
        Board b = new Board(boardTitle, boardType);
        boardreference.add(b).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                newBoardMutableLiveData.setValue(b);
                Toast.makeText(context, "Board "+boardTitle+" Created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return newBoardMutableLiveData;
    }
}
