package ru.haschish.retrochat;

import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import android.os.Handler;


public class ChatServer {
    private Socket socket;
    private String host;
    private int port;
    private BufferedReader in;
    private Handler handler;
    private Thread thread;
    private Boolean running;

    public ChatServer(String host, int port, Handler handler) {
        this.host = host;
        this.port = port;
        this.handler = handler;

    }

    public void connect() {
        running = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(host, port);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    while(running && (line = in.readLine()) != null) {
                        Log.i("ChatServer->readLine", line);
                        Message msg = handler.obtainMessage(0, 0, 0, line);
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    if (running) {
                       throw new RuntimeException("Невозможно создать сокет: " + e.getMessage());
                    }
                }
            }
        });
        thread.start();
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            running = false;
            thread.interrupt();
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("ChatServer", "Невозможно закрыть сокет: " + e.getMessage());
            }
        }
        socket = null;
        in = null;
    }

    public void send(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String m = message + "\n";
                try {
                    socket.getOutputStream().write(m.getBytes());
                } catch (IOException e) {
                    Log.e("ChatServer", "Невозможно отправить данные: " + e.getMessage());
                }
            }
        }).start();
    }
}
