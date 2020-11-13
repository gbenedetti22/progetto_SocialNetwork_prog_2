package com.SocialNetwork.Interfaces;

import com.SocialNetwork.Utente;

public interface IPost {
    int getId();
    Utente getAuthor();
    String getText();
    String getTimestamp();
}
