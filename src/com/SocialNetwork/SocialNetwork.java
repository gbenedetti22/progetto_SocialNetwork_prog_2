package com.SocialNetwork;

import java.util.*;

public class SocialNetwork {
    private final Map<String, Set<String>> social;
    private final HashMap<Integer,Post> posts;
    private final ArrayList<String> users;
    private int id_posts=1;

    public SocialNetwork() {
        social= new HashMap<>();
        posts = new HashMap<>();
        users= new ArrayList<>();
    }

    public Map<String, Set<String>> guessFollowers(List<Post> ps){
        Map<String, Set<String>> followers = new HashMap<>();
        for (Post post : ps){
            followers.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers());
            followers.putAll(post.getLikes());
        }

        return followers;
    }

    public void post(Utente author, String text) throws Exception {
        posts.put(id_posts, new Post(id_posts, author, text));
        id_posts++;
    }

    public Post getPost(int id) {
        return posts.get(id);
    }

    public void printAllPosts(){
        for (Post post : posts.values()){
            System.out.println("===========================");
            System.out.println("ID: "+post.getId());
            System.out.println(post.getAuthor().getUsername() + " ha scritto:");
            System.out.println('"'+post.getText()+'"');
            System.out.println();
            System.out.println("Like ricevuti: "+post.getLikes().size());
            System.out.println("===========================");
        }
    }
    public void printPost(int id){
        Post post = posts.get(id);
        System.out.println("ID: "+post.getId());
        System.out.println(post.getAuthor().getUsername() + " ha scritto:");
        System.out.println('"'+post.getText()+'"');
        System.out.println();
        System.out.println("Likes: ");
        System.out.println(post.getLikes().keySet());
    }

    public void addLike(int id_post, Utente utente){
        Post post= posts.get(id_post);
        post.addLike(utente);

        social.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers());
    }

    public Utente createUser(String username) throws Exception {
        Utente utente = new Utente(username);

        if(users.contains(username)){
            throw new Exception("Username già in uso");
        }
        users.add(username);
        return utente;
    }

    public void createUser(Utente utente) throws Exception {
        createUser(utente.getUsername());
    }
}

