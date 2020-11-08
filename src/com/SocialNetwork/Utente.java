package com.SocialNetwork;

import java.util.HashSet;
import java.util.Set;

public class Utente{
    private String username;
    private final Set<String> followers;

    public Utente(String username) throws Exception {
        this.username = username;
        followers = new HashSet<>();
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void addFollower(Utente utente){
        followers.add(utente.getUsername());
    }

    public String getUsername() {
        return username;
    }
}
