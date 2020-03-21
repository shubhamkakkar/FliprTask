package me.sankalpchauhan.kanbanboard.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import me.sankalpchauhan.kanbanboard.R;
import me.sankalpchauhan.kanbanboard.model.BoardList;
import me.sankalpchauhan.kanbanboard.util.IgnoreChangesFirestoreRecyclerAdapter;

public class BoardListAdapter extends IgnoreChangesFirestoreRecyclerAdapter<BoardList, BoardListAdapter.ListViewHolder> {
    private OnItemClickListener listener;

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
}
