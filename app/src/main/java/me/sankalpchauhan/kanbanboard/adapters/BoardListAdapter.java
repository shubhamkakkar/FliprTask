package me.sankalpchauhan.kanbanboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.sankalpchauhan.kanbanboard.R;
import me.sankalpchauhan.kanbanboard.model.BoardList;
import me.sankalpchauhan.kanbanboard.model.Card;
import me.sankalpchauhan.kanbanboard.util.FirestoreReorderableItemTouchHelperCallback;
import me.sankalpchauhan.kanbanboard.util.IgnoreChangesFirestoreRecyclerAdapter;

import static me.sankalpchauhan.kanbanboard.util.Constants.BOARD_LIST;
import static me.sankalpchauhan.kanbanboard.util.Constants.CARD_LIST;
import static me.sankalpchauhan.kanbanboard.util.Constants.PERSONAL_BOARDS;
import static me.sankalpchauhan.kanbanboard.util.Constants.USERS;

public class BoardListAdapter extends IgnoreChangesFirestoreRecyclerAdapter<BoardList, BoardListAdapter.ListViewHolder> {
    private OnItemClickListener listener;
    //Remove these from here after testing
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference database = rootRef.collection(USERS);
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public BoardListAdapter(@NonNull FirestoreRecyclerOptions<BoardList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BoardListAdapter.ListViewHolder listViewHolder, int position, @NonNull BoardList boardList) {
        listViewHolder.toolbar.setTitle(boardList.getTitle());

    }

    @NonNull
    @Override
    public BoardListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_list_item, parent, false);
        return new ListViewHolder(v);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        Button mCreateCardBTN;
        Toolbar toolbar;
        RecyclerView cardRv;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            mCreateCardBTN = itemView.findViewById(R.id.create_card_BTN);
            toolbar = itemView.findViewById(R.id.toolbar);
            mCreateCardBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(OnItemClickListener listner) {
        this.listener = listner;
    }

//    public void setUpRecyclerView(RecyclerView recyclerView, CardAdapter cardAdapter, ItemTouchHelper cardTouchHelper, Context context){
//        final CollectionReference boardListCollection = database.document(firebaseAuth.getCurrentUser().getUid()).collection(PERSONAL_BOARDS).document(boardid).collection(BOARD_LIST).document(listid).collection(CARD_LIST);
//        //Log.e(Constants.TAG, id);
//        FirestoreRecyclerOptions<Card> cardOptions = new FirestoreRecyclerOptions.Builder<Card>()
//                .setQuery(boardListCollection.orderBy("position"), Card.class)
//                .build();
//        cardAdapter = new CardAdapter(cardOptions);
//        recyclerView.setHasFixedSize(false);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(cardAdapter = new CardAdapter(cardOptions){
//            @Override
//            public void onDataChanged() {
//                super.onDataChanged();
//                //setupEmptyView(mExperienceList, mExperienceEmpty, getItemCount());
//            }
//        });
////        cardTouchHelper = new ItemTouchHelper(new FirestoreReorderableItemTouchHelperCallback<>(context, cardAdapter, boardListCollection));
////        cardTouchHelper.attachToRecyclerView(recyclerView);
//    }
}
