package com.SocialNetwork;

import java.util.HashSet;
import java.util.Set;

public class Utente{
    private final String username;
    private final Set<String> followers;

    public Utente(String username) {
        this.username = username;
        followers = new HashSet<>();
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public String getUsername() {
        return username;
    }
}
