package com.SocialNetwork;

import com.SocialNetwork.CustomException.SocialPostException;

import java.sql.Timestamp;
import java.util.*;

public class Post {
    private final int id;
    private final Utente author;
    private final String text;
    private final Timestamp timestamp;
    private final Map<String, Set<String>> likes;
    private final Set<String> metionedUsers;

    public Post(int id, Utente author, String text) throws SocialPostException {
        this.id = id;
        this.author = author;
        this.text = text;
        if(text.length() > 140)
            throw new SocialPostException("Testo troppo lungo");

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

    public void addLike(Utente utente) throws SocialPostException {
        if(utente.getUsername().equals(author.getUsername()))
            throw new SocialPostException("Non ci si può seguire da soli su questo Social!");

        likes.put(utente.getUsername(), utente.getFollowers());
    }
}
