package com.SocialNetwork;

import java.sql.Timestamp;
import java.util.*;

public class Post {
    private int id;
    private Utente author;
    private String text;
    private Timestamp timestamp;
    private Map<String, Set<String>> likes;
    private Set<String> metionedUsers;

    public Post(int id, Utente author, String text) throws Exception {
        this.id = id;
        this.author = author;
        this.text = text;
        if(text.length() > 140)
            throw new Exception("Testo troppo lungo");

        metionedUsers=buildTags();
        this.timestamp = new Timestamp(System.currentTimeMillis());
        likes=new HashMap<>();

    }

    private Set<String> buildTags() {
        Set<String> users = new HashSet<>();

        String[] splitted_text = text.split(" ");
        for (String tag : splitted_text) {
            if (tag.startsWith("@"))
                users.add(tag);

        }
        return users;
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

    public Set<String> getMetionedUsers() {
        return metionedUsers;
    }

    public Map<String, Set<String>> getLikes() {
        return likes;
    }

    public void addLike(Utente utente){
        likes.put(utente.getUsername(), utente.getFollowers());
        author.getFollowers().add(utente.getUsername());
    }
}
