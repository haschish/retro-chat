package ru.haschish.retrochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    public final static String EXTRA_HOST = "host";
    public final static String EXTRA_PORT = "port";
    public final static String EXTRA_NICKNAME = "nickname";

    private RecyclerView recyclerView;
    private ChatServer chatServer;
    ArrayList<String> messageList = new ArrayList<>();
    MessageAdapter adapter;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String host = intent.getStringExtra(EXTRA_HOST);
        int port = intent.getIntExtra(EXTRA_PORT, 12345);
        final String nickname = intent.getStringExtra(EXTRA_NICKNAME);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        addMessage((String)msg.obj);
                        break;
                }
            }
        };

        chatServer = new ChatServer(host, port, handler);
        chatServer.connect();

        Button btnSend = (Button) findViewById(R.id.btnSend);
        final EditText editTextMessage = (EditText) findViewById(R.id.message);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = editTextMessage.getText().toString();
                editTextMessage.setText("");
                chatServer.send(nickname + ": " + message);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MessageAdapter(this, messageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void addMessage(String message) {
        Log.i("ChatActivity->addMessage", message);
        messageList.add(message);
        while (messageList.size() > 33) {
            messageList.remove(0);
        }
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatServer.close();
    }
}
