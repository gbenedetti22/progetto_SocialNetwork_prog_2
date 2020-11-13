package com.SocialNetwork;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static SocialNetwork socialNetwork = new SocialNetwork();

    public static void main(String[] args) throws Exception {
        Utente crash = socialNetwork.createUser("crash");
        Utente coco = socialNetwork.createUser("coco");
        Utente cortex = socialNetwork.createUser("cortex");
        Utente brio = socialNetwork.createUser("brio");
        Utente gabry1 = socialNetwork.createUser("gabry");
        Utente gabry2 = new Utente("gabry");    //questa è la solita persona creata prima

        Post cortexPost = socialNetwork.post(cortex, "Ti prenderò Crash!");
        Post crashPost = socialNetwork.post(crash, "Ok!");
        Post ps = null;

        Post like1 = socialNetwork.post(crash, "like:" + cortexPost.getId());
        Post like3 = socialNetwork.post(cortex, "like:" + crashPost.getId());
        Post like4 = socialNetwork.post(brio, "like:" + crashPost.getId());

//========================================================================================================================
//        List<Post> list = new ArrayList<>();
////        list.add(ps);                                 //SocialPostException
//        list.add(cortexPost);
//        list.add(crashPost);
//        list.add(like1);
//        list.add(like3);
//        list.add(like4);
////        list.add(new Post(1,brio, "ciao belli"));     //da errore
//
//        socialNetwork.printSocial();
//        System.out.println(socialNetwork.guessFollowers(list));
    }

    private void guessFollowers(){}


}
