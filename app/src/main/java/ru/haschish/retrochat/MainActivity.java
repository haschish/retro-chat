package ru.haschish.retrochat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText host = (EditText) findViewById(R.id.host);
        final EditText port = (EditText) findViewById(R.id.port);
        final EditText nickname = (EditText) findViewById(R.id.messsageNickname);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_HOST, host.getText().toString());
                intent.putExtra(ChatActivity.EXTRA_PORT, Integer.parseInt(port.getText().toString()));
                intent.putExtra(ChatActivity.EXTRA_NICKNAME, nickname.getText().toString());

                startActivity(intent);
            }
        });
    }
}
