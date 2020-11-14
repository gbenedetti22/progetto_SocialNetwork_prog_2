package com.SocialNetwork;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static SocialNetwork socialNetwork = new SocialNetwork();

    public static void main(String[] args) throws Exception {
        String crash = socialNetwork.createUser("crash");
        String coco = socialNetwork.createUser("coco");
        String cortex = socialNetwork.createUser("cortex");
        String brio = socialNetwork.createUser("brio");
        String gabry1 = socialNetwork.createUser("gabry");
        String gabry2 = "gabry";    //questa è la solita persona creata prima

        Post cortexPost = socialNetwork.post(cortex, "Ti prenderò Crash!");
        Post crashPost = socialNetwork.post(crash, "Ok!");
        Post ps = null;

        Post like1 = socialNetwork.post(crash, "like:" + cortexPost.getId());
        Post like3 = socialNetwork.post(cortex, "like:abcd" + crashPost.getId());
        Post like4 = socialNetwork.post(brio, "like:" + crashPost.getId());

        socialNetwork.printSocial();
//========================================================================================================================
        List<Post> list = new ArrayList<>();
        Post like_guess = socialNetwork.post(gabry1, "like:" + cortexPost.getId());

//        list.add(ps);                                 //SocialPostException
        list.add(cortexPost);
        list.add(crashPost);
        list.add(like1);
        list.add(like3);
        list.add(like4);
        list.add(like_guess);
//        list.add(new Post(1,brio, "ciao belli"));     //da errore

        System.out.println(socialNetwork.guessFollowers(list));
    }

}
