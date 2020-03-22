package me.sankalpchauhan.kanbanboard.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.sankalpchauhan.kanbanboard.R;
import me.sankalpchauhan.kanbanboard.adapters.BoardListAdapter;
import me.sankalpchauhan.kanbanboard.adapters.CardAdapter;
import me.sankalpchauhan.kanbanboard.fragments.ListCreateBottomSheet;
import me.sankalpchauhan.kanbanboard.model.Board;
import me.sankalpchauhan.kanbanboard.model.BoardList;
import me.sankalpchauhan.kanbanboard.model.Card;
import me.sankalpchauhan.kanbanboard.util.Constants;
import me.sankalpchauhan.kanbanboard.util.FirestoreReorderableItemTouchHelperCallback;
import me.sankalpchauhan.kanbanboard.viewmodel.BoardActivityViewModel;

import static me.sankalpchauhan.kanbanboard.util.Constants.BOARD_LIST;
import static me.sankalpchauhan.kanbanboard.util.Constants.CARD_LIST;
import static me.sankalpchauhan.kanbanboard.util.Constants.PERSONAL_BOARDS;
import static me.sankalpchauhan.kanbanboard.util.Constants.USERS;

public class BoardActivity extends AppCompatActivity {
    Toolbar toolbar;
    String id;
    Board board;
    BoardActivityViewModel boardActivityViewModel;
    FloatingActionButton listCreateFAB;
    BoardListAdapter boardListAdapter;
    ItemTouchHelper boardTouchHelper;
    RecyclerView rvList;

    //Remove these from here after testing
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference database = rootRef.collection(USERS);
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        getIntentData();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(board.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        listCreateFAB = findViewById(R.id.list_add_fab);
        rvList = findViewById(R.id.rv_list_item);
        setUpRecyclerView(rvList);
        initBoardActivityViewModel();

        listCreateFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListCreateBottomSheet listCreateBottomSheet = new ListCreateBottomSheet();
                listCreateBottomSheet.show(getSupportFragmentManager(), "listcreatebottomsheet");
            }
        });
    }

    public void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("BoardId");
        board = (Board) bundle.getSerializable("Board");
    }

    private void initBoardActivityViewModel() {
        boardActivityViewModel = new ViewModelProvider(this).get(BoardActivityViewModel.class);
    }

    public void addListToDB(String title){
        boardActivityViewModel.createList(this, id, title);
    }


    public void setUpRecyclerView(RecyclerView recyclerView){
        final CollectionReference boardListCollection = database.document(firebaseAuth.getCurrentUser().getUid()).collection(PERSONAL_BOARDS).document(id).collection(BOARD_LIST);
        Log.e(Constants.TAG, id);
        FirestoreRecyclerOptions<BoardList> boardOptions = new FirestoreRecyclerOptions.Builder<BoardList>()
                .setQuery(boardListCollection.orderBy("position"), BoardList.class)
                .build();
        boardListAdapter = new BoardListAdapter(boardOptions, this);
        recyclerView.setHasFixedSize(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setAdapter(boardListAdapter = new BoardListAdapter(boardOptions, this){
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                //setupEmptyView(mExperienceList, mExperienceEmpty, getItemCount());
            }
        });
        boardTouchHelper = new ItemTouchHelper(new FirestoreReorderableItemTouchHelperCallback<>(this, boardListAdapter, boardListCollection));
        boardTouchHelper.attachToRecyclerView(recyclerView);

        boardListAdapter.setOnItemClickListner(new BoardListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent i = new Intent(BoardActivity.this, CardActivity.class);
                String listid = documentSnapshot.getId();
                BoardList boardList = documentSnapshot.toObject(BoardList.class);
                Bundle b = new Bundle();
                b.putString("listId", listid);
                b.putString("boardId", id);
                b.putSerializable("BoardList", boardList);
                i.putExtras(b);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        boardListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        boardListAdapter.stopListening();
    }

    public String getBoardId(){
        return id;
    }
}
