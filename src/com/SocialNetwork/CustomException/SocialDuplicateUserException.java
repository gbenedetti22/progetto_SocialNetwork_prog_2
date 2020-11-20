package com.SocialNetwork.CustomException;

public class SocialDuplicateUserException extends Exception {
    public SocialDuplicateUserException(){
        super("Duplicate Users found (same username)");
    }
}
