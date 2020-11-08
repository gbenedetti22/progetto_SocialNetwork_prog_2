package com.SocialNetwork;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static SocialNetwork socialNetwork = new SocialNetwork();

    public static void main(String[] args) throws Exception {
        Utente carmine = socialNetwork.createUser("carmine12");
        Utente pietro = socialNetwork.createUser("pietro_smusi");
        Utente gabriele = socialNetwork.createUser("gabry98");
        Utente lollo = socialNetwork.createUser("lollo76");
        Utente nino = socialNetwork.createUser("nino_nino");
        Utente crash = socialNetwork.createUser("bandicoot");
        Utente cortex = socialNetwork.createUser("cortex");
        Utente steve_jobs = socialNetwork.createUser("jobs");

        socialNetwork.post(steve_jobs, "Hello World");
        socialNetwork.post(cortex, "Ti prenderò Crash! @crash");
        socialNetwork.post(gabriele, "Voglio la laurea");
        socialNetwork.post(carmine, "Oggi ho fatto la pasta");
        socialNetwork.post(gabriele, "perfavore");

        socialNetwork.addLike(1, gabriele);
        socialNetwork.addLike(1, lollo);
        socialNetwork.addLike(1, pietro);

        socialNetwork.addLike(2, gabriele);
        socialNetwork.addLike(2, crash);
        socialNetwork.addLike(2, nino);
        socialNetwork.addLike(2, steve_jobs);

        socialNetwork.addLike(3, steve_jobs);
        socialNetwork.addLike(3, lollo);
        socialNetwork.addLike(3, nino);
        socialNetwork.addLike(3, pietro);
        socialNetwork.addLike(3, cortex);

        Post post1 = new Post(1, pietro, "ciao a tutti, da @pietro e @crash");
        Post post2 = new Post(1, steve_jobs, "Ciao mondo da");
        Post post3 = new Post(1, gabriele, "Apple merda @jobs");
        Post post4 = new Post(1, gabriele, "Scherzo @jobs");

        post1.addLike(gabriele);

        post2.addLike(gabriele);
        post2.addLike(lollo);
        post2.addLike(nino);

        post3.addLike(crash);
        post3.addLike(lollo);
        post3.addLike(cortex);

        ArrayList<Post> list = new ArrayList<>();
        list.add(post1);
        list.add(post2);
        list.add(post3);
        list.add(post4);
        ArrayList<String> words = new ArrayList<>();
        words.add("laurea");
        words.add("crash");

        socialNetwork.printAllPosts();
        System.out.println("guess Follower: "+socialNetwork.guessFollowers(list));
        System.out.println("Gli influencers sono: "+socialNetwork.influencers());
        System.out.println("Gli utenti mezionati sul social sono: "+socialNetwork.getMetionedusers());
        System.out.println("Gli utenti mezionati sulla lista sono: "+socialNetwork.getMetionedusers(list));
        System.out.println("Gabriele ha postato: "+socialNetwork.writtenBy(gabriele.getUsername()));
        System.out.println("Nella lista, Gabriele ha postato: "+socialNetwork.writtenBy(list, gabriele.getUsername()));
        System.out.println("La parola laurea e crash si trova in: "+socialNetwork.containing(words));
    }
}
