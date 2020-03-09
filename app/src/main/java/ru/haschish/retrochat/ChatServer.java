package ru.haschish.retrochat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private Socket socket;
    private String host;
    private int port;
    public BufferedReader in;
    private BufferedWriter out;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Невозможно создать сокет: " + e.getMessage());
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("ChatServer", "Невозможно создать сокет: " + e.getMessage());
            }
        }
        socket = null;
    }

    public void send(String message) {
        message += "\n";
        try {
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            Log.e("ChatServer", "Невозможно отправить данные: " + e.getMessage());
        }
    }

    public ArrayList<String> read() {
        ArrayList<String> list = new ArrayList<>();
        try {
            Log.i("ChatServer", "try readLine");
            String line;
            while((line = in.readLine()) != null) {
                Log.i("ChatServer", "while: " + line);
                line = line.substring(0, line.length() - 1);
                list.add(line);
                Log.i("ChatServer", "end while");
            }

            Log.i("ChatServer", "out while");

        } catch (IOException e) {
            Log.e("ChatServer", "Невозможно прочитать данные: " + e.getMessage());
        }

        Log.i("ChatServer", "return");
        return list;
    }

}
