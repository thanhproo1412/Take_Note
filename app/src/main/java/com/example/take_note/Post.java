package com.example.take_note;

public class Post {
    private String userName;
    private String content;
    private int imageResId;
    private String time;

    public Post(String userName, String content, int imageResId, String time) {
        this.userName = userName;
        this.content = content;
        this.imageResId = imageResId;
        this.time = time;
    }

    // Getters
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public int getImageResId() { return imageResId; }
    public String getTime() { return time; }
}
