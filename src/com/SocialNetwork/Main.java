package com.SocialNetwork;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private final static SocialNetwork socialNetwork = new SocialNetwork();

    public static void main(String[] args) throws Exception {
        Utente crash = socialNetwork.createUser("crash");
        Utente coco = socialNetwork.createUser("coco");
        Utente cortex = socialNetwork.createUser("cortex");
        Utente brio = socialNetwork.createUser("brio");
        Utente gabry1 = socialNetwork.createUser("gabry");
        Utente gabry2 = new Utente("gabry");    //questa è la solita persona creata prima

        Post cortexPost=socialNetwork.post(cortex, "Ti prenderò Crash! @crash");
        Post cocoPost = socialNetwork.post(coco, "Crash si è rotto il computer");
        Post gabryPost= socialNetwork.post(gabry2, "Questo è un test!");

        socialNetwork.addLike(cortexPost.getId(), crash);
        socialNetwork.addLike(1, brio);

        socialNetwork.addLike(cocoPost.getId(), crash);

        socialNetwork.addLike(gabryPost.getId(), cortex);
        socialNetwork.addLike(gabryPost.getId(), crash);
        socialNetwork.addLike(gabryPost.getId(), brio);

        System.out.println("influencers: "+socialNetwork.influencers());
//========================================================================================================================
        Utente dino = new Utente("dino");       //utente che non appartiene al social, esiste ma non si è registrato
        Utente steve_jobs = new Utente("jobs");

        Post post1 = new Post(1, steve_jobs, "Ciao mondo");
        Post post2 = new Post(1, gabry2, "Apple merda @jobs");
        Post post3 = new Post(1, gabry2, "Scherzo @jobs");

        post1.addLike(gabry2);

        post2.addLike(dino);
        post2.addLike(steve_jobs);
        post2.addLike(coco);

        post3.addLike(crash);
//        post3.addLike(gabry1);    // da errore

        ArrayList<Post> list = new ArrayList<>();
        list.add(post1);
        list.add(post2);
        list.add(post3);
        System.out.println(socialNetwork.guessFollowers(list));
    }
}
