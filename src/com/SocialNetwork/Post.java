package com.SocialNetwork;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Post {
    private int id;
    private Utente author;
    private String text;
    private Timestamp timestamp;
    private Map<String, Set<String>> likes;

    public Post(int id, Utente author, String text) throws Exception {
        this.id = id;
        this.author = author;
        this.text = text;
        if(text.length() > 140)
            throw new Exception("Testo troppo lungo");

        this.timestamp = new Timestamp(System.currentTimeMillis());
        likes=new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Utente getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public Map<String, Set<String>> getLikes() {
        return likes;
    }

    public void addLike(Utente utente){
        likes.put(utente.getUsername(), utente.getFollowers());
        author.getFollowers().add(utente.getUsername());
    }
}
