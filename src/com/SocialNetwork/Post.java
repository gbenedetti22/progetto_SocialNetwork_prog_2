package com.SocialNetwork;

import com.SocialNetwork.CustomException.SocialPostArgumentException;
import com.SocialNetwork.Interfaces.IPost;

import java.sql.Timestamp;

public final class Post implements IPost {
    private final int id;
    private final String author;
    private final String text;
    private final Timestamp timestamp;

    /**
     *REQUIRES: id ≠ null ∧ author ≠ null ∧ text ≠ null ∧ text.length < 140<br>
     *MODIFIES:this<br>
     * @throws SocialPostArgumentException se text.length() > 140
     * @throws NullPointerException (unchecked exception) se id = null V author = null V text = null
     */
    public Post(int id, String author, String text) throws SocialPostArgumentException {
        this.id = id;
        this.author = author;
        this.text = text;
        if(text.length() > 140)
            throw new SocialPostArgumentException("Testo troppo lungo");
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }
}
