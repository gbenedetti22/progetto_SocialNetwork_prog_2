package com.SocialNetwork;

import com.SocialNetwork.CustomException.SocialPostException;
import com.SocialNetwork.Interfaces.IPost;

import java.sql.Timestamp;

public final class Post implements IPost {
    private final int id;
    private final Utente author;
    private final String text;
    private final Timestamp timestamp;

    public Post(int id, Utente author, String text) throws SocialPostException {
        this.id = id;
        this.author = author;
        this.text = text;
        if(text.length() > 140)
            throw new SocialPostException("Testo troppo lungo");
        this.timestamp = new Timestamp(System.currentTimeMillis());
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
}
