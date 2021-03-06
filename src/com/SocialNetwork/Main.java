package com.SocialNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private final static SocialNetwork socialNetwork = new SocialNetwork();

    public static void main(String[] args) throws Exception {
        Post p = new Post(100, "utente1", "like:1");
        String crash = socialNetwork.createUser("crash");
        String coco = socialNetwork.createUser("coco");
        String cortex = socialNetwork.createUser("cortex");
        String brio = socialNetwork.createUser("brio");
        String gabry1 = socialNetwork.createUser("gabry");
        //String gabry2 = "gabry";    //questa è la solita persona creata prima

        Post cortexPost = socialNetwork.post(cortex, "Ti prenderò Crash!");
        Post crashPost = socialNetwork.post(crash, "Ok!");

        Post like1 = socialNetwork.post(crash, "like:" + cortexPost.getId());
        Post like3 = socialNetwork.post(cortex, "like:abcd");//viene preso come Post normale
        Post like4 = socialNetwork.post(brio, "like:" + crashPost.getId());
        Post like5 = socialNetwork.post(coco, "like:" + crashPost.getId());
        Post like6 = socialNetwork.post(crash, "like:" + cortexPost.getId()); //non viene contato, essendo duplice
        Post like7 = socialNetwork.post(crash, "like:" + cortexPost.getId()); //non viene contato, essendo duplice

        System.out.print("Social: ");
        socialNetwork.printSocial();  //stampa tutti gli utenti con i relativi followers
        System.out.println("(influencers) Lista delle persone più influenti: "+socialNetwork.influencers());
                                                                                                //deve stampare
                                                                                                //1.crash
                                                                                                //2.cortex
                                                                                                //brio
                                                                                                //coco
        System.out.println("(metionedUsers) Hanno postato almeno una volta: "+socialNetwork.getMentionedUsers());
        System.out.println("(writtenBy) crash ha scritto: "+socialNetwork.writtenBy(crash));
        ArrayList<String> words = new ArrayList<>();
        words.add("like");
        System.out.println("(containing) i Post con \"like:\" "+socialNetwork.containing(words));

//========================================================================================================================
        List<Post> list = new ArrayList<>();
        Post like_guess = socialNetwork.post(gabry1, "like:" + cortexPost.getId());
        Post followBackException = new Post(100, cortex, "like: "+cortexPost.getId());

        list.add(cortexPost);
        list.add(crashPost);
        list.add(like1);        //crash mette like a cortex
        list.add(like3);        //cortex scrive like:abcd
        list.add(like4);        //brio mette like a crash
        list.add(p);            //viene scartato, in quanto il like è stato messo prima del Post
        list.add(like_guess);   //gabry mette like a cortex
        Collections.shuffle(list);  //mescolo tutto

//        list.add(followBackException);

        System.out.println("(guessFollowers) rete sociale derivata: "+socialNetwork.guessFollowers(list));
        System.out.println("[metionedUsers(ps)] utenti mezionati in list: "+ socialNetwork.getMentionedUsers(list));
//========================================================================================================================
        FilteredSocialNetwork filteredSocialNetwork = new FilteredSocialNetwork();
        String rillaRoo=filteredSocialNetwork.createUser("rilla_roo");
        String tiny=filteredSocialNetwork.createUser("tiny");

        Post tinyOffensivePost=filteredSocialNetwork.post(tiny, "aaarrghh!");
        filteredSocialNetwork.post(rillaRoo, "rep:"+tinyOffensivePost.getId());

        System.out.println("(filteredSocialNetwork) Post offensivi: "+filteredSocialNetwork.getBlacklist());
        filteredSocialNetwork.removePostfromBlacklist(tinyOffensivePost);
    }
}
