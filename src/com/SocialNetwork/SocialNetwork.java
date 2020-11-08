package com.SocialNetwork;

import java.util.*;

public class SocialNetwork {
    private final Map<String, Set<String>> social;  //utenti registrati + followers
    private final Map<String, Integer> influencers; //utenti registrati + numero di followers
    private final HashMap<Integer,Post> posts;      //post approvati

    private int id_posts=1;

    public SocialNetwork() {
        social= new HashMap<>();
        posts = new HashMap<>();
        influencers = new HashMap<>();
    }

    public Map<String, Set<String>> guessFollowers(List<Post> ps){
        Map<String, Set<String>> followers = new HashMap<>();
        for (Post post : ps){
            followers.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers());
            followers.putAll(post.getLikes());
        }

        return followers;
    }

    public List<String> influencers(){
        List<Map.Entry<String, Integer>> sorted_influencers = new ArrayList<>(influencers.entrySet());
        sorted_influencers.sort(Map.Entry.comparingByValue());  //ordine crescente

        List<String> result= new ArrayList<>();

        for(int i=sorted_influencers.size()-1; i>=0; i--){      //parto dalla fine e scorro al contrario
            result.add(sorted_influencers.get(i).getKey());
        }

        return result;
    }

    public Set<String> getMetionedusers(){
        Set<String> metionedUsers = new HashSet<>();
        for(Post ps : posts.values()){
            metionedUsers.addAll(ps.getMetionedUsers());
        }

        return metionedUsers;
    }
    public Set<String> getMetionedusers(List<Post> ps){
        Set<String> metionedUsers = new HashSet<>();

        for(Post post : ps){
            metionedUsers.addAll(post.getMetionedUsers());
        }
        return metionedUsers;
    }

    public List<Post> writtenBy(String username){
        List<Post> list = new ArrayList<>();
        for(Post post : posts.values()){
            if(post.getAuthor().getUsername().equals(username)){
                list.add(post);
            }
        }
        return list;
    }

    public List<Post> writtenBy(List<Post> ps, String username){
        List<Post> list = new ArrayList<>();
        for(Post post : ps){
            if(post.getAuthor().getUsername().equals(username)){
                list.add(post);
            }
        }
        return list;
    }
    public List<Post> containing (List<String> words){
        List<Post> result= new ArrayList<>();

        for(Post post: posts.values()){
            for (String word : words) {
                if (post.getText().contains(word)) {
                    result.add(post);
                    break;
                }
            }
        }

        return result;
    }

    public void post(Utente author, String text) throws Exception {
        posts.put(id_posts, new Post(id_posts, author, text));
        id_posts++;
    }

    public void addLike(int id_post, Utente utente) throws Exception {
        Post post= posts.get(id_post);

        if(utente.equals(post.getAuthor()))
            throw new Exception("Non ci si può seguire da soli su questo Social!");

        if(!social.containsKey(utente.getUsername()))
            throw new Exception("Utente non registrato nel Social");

        post.addLike(utente);

        social.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers());
        influencers.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers().size());
    }

    public Utente createUser(String username) throws Exception {
        Utente utente = new Utente(username);

        if(social.containsKey(username)){
            throw new Exception("Username già in uso");
        }

        social.put(username, new HashSet<>());
        influencers.put(username, 0);
        return utente;
    }

    public void createUser(Utente utente) throws Exception {
        createUser(utente.getUsername());
    }

    public void printAllPosts(){
        System.out.println("===========================");
        for (Post post : posts.values()){
            System.out.println("ID: "+post.getId());
            System.out.println(post.getAuthor().getUsername() + " ha scritto:");
            System.out.println('"'+post.getText()+'"');
            System.out.println();
            System.out.println("Like ricevuti: "+post.getLikes().size());
            System.out.println("===========================");
        }
    }
    public void printPost(int id){
        System.out.println("===========================");
        Post post = posts.get(id);
        System.out.println("ID: "+post.getId());
        System.out.println(post.getAuthor().getUsername() + " ha scritto:");
        System.out.println('"'+post.getText()+'"');
        System.out.println();
        System.out.println("Likes: ");
        System.out.println(post.getLikes().keySet());
    }

    public void printSocial(){
        System.out.println(social);
    }
}

