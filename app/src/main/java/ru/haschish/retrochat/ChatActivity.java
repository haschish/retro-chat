package ru.haschish.retrochat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    public final static String EXTRA_HOST = "host";
    public final static String EXTRA_PORT = "port";
    public final static String EXTRA_NICKNAME = "nickname";

    private TextView allMessages;
    private EditText message;
    private Button btnSend;
    private RecyclerView recyclerView;

    private String host;
    private int port;
    private String nickname;

    private ChatServer chatServer;
    ArrayList<String> messageList = new ArrayList<>();
    MessageAdapter adapter;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        message = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.btnSend);

        Intent intent = getIntent();
        String host = intent.getStringExtra(EXTRA_HOST);
        int port = intent.getIntExtra(EXTRA_PORT, 3000);
        final String nickname = intent.getStringExtra(EXTRA_NICKNAME);

        chatServer = new ChatServer(host, port);

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                chatServer.connect();
                try {
                    String line;
                    while((line = chatServer.in.readLine()) != null) {
                        Log.i("ChatActivity", "readline: " + line);
                        line = line.substring(0, line.length() - 1);
                        messageList.add(line);
                    }
                } catch (IOException e) {

                }

            }
        });
        thread.start();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    chatServer.send(message.getText().toString());
                    }
                }).start();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MessageAdapter(this, messageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        CountDownTimer timer = new CountDownTimer(Long.MAX_VALUE, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("ChatActivity", "tick");
                adapter.notifyDataSetChanged();
            }

            public void onFinish() {
            }
        };
        timer.start();

    }

    private void sendMessage(String message) {

    }

    private void handleMessage(String message) {
        Log.i("ChatActivity", message);
    }
}
