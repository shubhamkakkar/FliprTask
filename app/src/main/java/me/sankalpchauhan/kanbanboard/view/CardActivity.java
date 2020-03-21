package me.sankalpchauhan.kanbanboard.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import me.sankalpchauhan.kanbanboard.R;
import me.sankalpchauhan.kanbanboard.model.Board;
import me.sankalpchauhan.kanbanboard.model.BoardList;

public class CardActivity extends AppCompatActivity {
    String boardid, listid;
    BoardList boardList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getIntentData();
    }

    public void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        boardid = bundle.getString("BoardId");
        listid = bundle.getString("ListId");
        boardList = (BoardList) bundle.getSerializable("BoardList");
    }
}
