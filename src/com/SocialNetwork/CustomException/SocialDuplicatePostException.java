package com.SocialNetwork.CustomException;

public class SocialDuplicatePostException extends Exception {
    public SocialDuplicatePostException() {
        super("Duplicate Posts found (same ID)");
    }
}
