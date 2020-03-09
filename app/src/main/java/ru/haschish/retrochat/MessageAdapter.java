package ru.haschish.retrochat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ListItemHolder> {
    private ChatActivity chatActivity;
    private List<String> list;

    public MessageAdapter(ChatActivity chatActivity, List<String> list) {
        this.chatActivity = chatActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public MessageAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, parent, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ListItemHolder holder, int position) {
        String message = list.get(position);

        holder.text.setText(message);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ListItemHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
        }
    }
}
