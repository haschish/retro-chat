package ru.haschish.retrochat;

public class Message {
    private String nickname;
    private String text;
    private int time;

    public Message(String nickname, int time, String text) {
        this.nickname = nickname;
        this.time = time;
        this.text = text;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
