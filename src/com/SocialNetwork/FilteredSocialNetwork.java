package com.SocialNetwork;

import com.SocialNetwork.CustomException.SocialFollowBackException;
import com.SocialNetwork.CustomException.SocialPostArgumentException;
import com.SocialNetwork.CustomException.SocialUserException;
import com.SocialNetwork.Interfaces.IFilteredSocial;

import java.util.ArrayList;

public class FilteredSocialNetwork extends SocialNetwork implements IFilteredSocial {
    private final ArrayList<Post> blacklist = new ArrayList<>();

    @Override
    public Post post(String author, String text) throws SocialPostArgumentException, SocialUserException, SocialFollowBackException {
        Post post=super.post(author, text);
        if(text.startsWith("rep:")){
            String[] splitted_text=text.split(":");
            try {
                int reportedPostID = Integer.parseInt(splitted_text[1].trim());
                Post repPost= getPosts().get(reportedPostID);
                if(repPost != null)
                    blacklist.add(repPost);
            }catch(NumberFormatException ignored){}
        }
        return post;
    }

    public ArrayList<Post> getBlacklist() {
        return blacklist;
    }
}
