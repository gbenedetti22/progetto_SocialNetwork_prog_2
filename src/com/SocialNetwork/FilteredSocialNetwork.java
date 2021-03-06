package com.SocialNetwork;

import com.SocialNetwork.CustomException.SocialFollowBackException;
import com.SocialNetwork.CustomException.SocialPostArgumentException;
import com.SocialNetwork.CustomException.SocialUserException;
import com.SocialNetwork.Interfaces.IFilteredSocial;

import java.util.ArrayList;
/*
    OVERVIEW: Collezione mutabile di Post, segnalati inserendo la String "rep: "+ id ∈ N nel campo text del metodo post,
    ereditato della superclasse SocialNetwork.
    AF: a(blacklist)=(blacklist[0,blacklist.size]->(Post)=
        { p1 | p1 ∈ posts.values ∧ (∃p2 ∈ posts.values | p2.getText.contains("rep: "+ p1.getId)) }
 */
public class FilteredSocialNetwork extends SocialNetwork implements IFilteredSocial {
    private final ArrayList<Post> blacklist = new ArrayList<>();

    public FilteredSocialNetwork() {}

    @Override
    public Post post(String author, String text) throws SocialPostArgumentException, SocialUserException, SocialFollowBackException {
        Post post=super.post(author, text);
        if(text.startsWith("rep:")){    //controllo se il Post da segnalare esiste,
                                        // e solo in quel caso lo metto nella blacklist
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

    public void removePostfromBlacklist(Post p){
        blacklist.remove(p);
    }

    public ArrayList<Post> getBlacklist() {
        return blacklist;
    }
}
