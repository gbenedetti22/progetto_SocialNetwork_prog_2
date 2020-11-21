package com.SocialNetwork.Interfaces;

import com.SocialNetwork.CustomException.SocialFollowBackException;
import com.SocialNetwork.CustomException.SocialPostArgumentException;
import com.SocialNetwork.CustomException.SocialUserException;
import com.SocialNetwork.Post;

import java.util.ArrayList;

public interface IFilteredSocial extends ISocial {
    /**
     * REQUIRES: author ≠ null ∧ text ≠ null
     * EFFECTS: viene controllato se il post che si vuole pubblicare, comincia con "rep + id ∈ N".<br>
     * In quel caso, viene "segnalato" il post in questione se esiste.<br>
     * Quando un Post viene segnalato, viene aggiunto in una lista, la quale contiene infatti tutti i Post da revisionare
     * perchè segnalati da altri utenti per contenuti offensivi.
     * Ritorna una reference al Post appena pubblicato.
     *
     * THROWS:
     * SocialUserException: se author ∉ social
     * SocialPostArgumentException: se author.length==0 ∧ text.length==0
     * NullPointerException: (unchecked exception) se author = null V text=null
     * SocialFollowBackException: se ∃p ∈ posts . p.author=author (o p.author.equals(author))
     */
    @Override
    Post post(String author, String text) throws SocialPostArgumentException, SocialUserException, SocialFollowBackException;

    /**
     * EFFECTS: ritorna la lista contenente i Post segnalati da un utente
     */
    ArrayList<Post> getBlacklist();
}
