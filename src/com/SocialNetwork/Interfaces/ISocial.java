package com.SocialNetwork.Interfaces;

import com.SocialNetwork.CustomException.SocialPostException;
import com.SocialNetwork.CustomException.SocialUserException;
import com.SocialNetwork.Post;
import com.SocialNetwork.Utente;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISocial {
    Map<String, Set<String>> guessFollowers(List<Post> ps) throws SocialPostException;
    List<String> influencers();
    Set<String> getMentionedUsers();
    Set<String> getMentionedUsers(List<Post> ps);
    List<Post> writtenBy(String username);
    List<Post> writtenBy(List<Post> ps, String username);
    List<Post> containing(List<String> words);

    Post post(Utente author, String text) throws SocialUserException, SocialPostException;
    Utente createUser(String username) throws SocialUserException;
    void createUser(Utente utente) throws SocialUserException;
    void printAllPosts();
    void printPost(int id);
}
