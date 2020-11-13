package com.SocialNetwork;

import com.SocialNetwork.CustomException.SocialPostException;
import com.SocialNetwork.CustomException.SocialUserException;
import com.SocialNetwork.Interfaces.ISocial;

import java.util.*;

public class SocialNetwork implements ISocial {
    private final Map<String, Set<String>> social;      //utenti registrati + followers
    private final HashMap<String, Integer> influencers; //utenti registrati + numero di followers
    private final HashMap<Integer,Post> posts;          //post approvati
    private final Set<String> metionedusers;            //utenti che hanno postato almeno una volta

    private int id_posts=1;

    public SocialNetwork() {
        social= new HashMap<>();
        posts = new HashMap<>();
        influencers = new HashMap<>();
        metionedusers = new HashSet<>();
    }

    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws SocialPostException {
        Map<String, Set<String>> reteSociale = new HashMap<>();
        HashMap<Integer,Post> posts = new HashMap<>();

        for (Post post : ps) {
            if(post == null)
                throw new SocialPostException("Errore nella lettura del Post");

            Utente utente = new Utente(post.getAuthor().getUsername());

            if(posts.containsKey(post.getId()))
                throw new SocialPostException("Questo Post è già presente nella rete locale");

            posts.put(post.getId(), post);
            if(post.getText().startsWith("like:")){
                String[] splittedText=post.getText().split(":");
                try {
                    int idPost = Integer.parseInt(splittedText[1].trim());
                    Utente newFollower=post.getAuthor();
                    reteSociale.get(posts.get(idPost).getAuthor().getUsername()).add(newFollower.getUsername());
                }catch(NumberFormatException ignored){}
            }

            if (!reteSociale.containsKey(utente.getUsername())) {
                reteSociale.put(utente.getUsername(), utente.getFollowers());
            }
        }

        return reteSociale;
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
    public Set<String> getMentionedUsers(){
        return metionedusers;
    }

    public Set<String> getMentionedUsers(List<Post> ps){
        Set<String> list = new HashSet<>();
        for(Post post : ps){
            list.add(post.getAuthor().getUsername());
        }

        return list;
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

    public Post post(Utente author, String text) throws SocialUserException, SocialPostException {
        if(author == null || text == null)
            throw new SocialPostException("Errore nell inserimento dell autore o del testo");

        if(!social.containsKey(author.getUsername()))
            throw new SocialUserException("Utente non registrato");

        if(text.startsWith("like:")){
            String[] splittedText=text.split(":");
            try {
                int idPost = Integer.parseInt(splittedText[1].trim());
                Post post=posts.get(idPost);        //ritorna null se get non trova nulla
                if(post == null)
                    throw new SocialPostException("PostID: " + idPost + " non trovato");

                if(post.getAuthor().getUsername().equals(author.getUsername()))
                    throw new SocialPostException("Non ci si può seguire da soli su questo Social!");

                post.getAuthor().getFollowers().add(author.getUsername());
                social.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers());
                influencers.put(post.getAuthor().getUsername(), post.getAuthor().getFollowers().size());
            }catch (NumberFormatException ignored){}

        }

        Post newPost = new Post(id_posts, author, text);
        posts.put(id_posts, newPost);
        metionedusers.add(author.getUsername());
        id_posts++;
        return newPost;
    }

    public Utente createUser(String username) throws SocialUserException {
        if(username == null)
            throw new SocialUserException("L utente non può essere null");
        Utente utente = new Utente(username);

        if(social.containsKey(username)){
            throw new SocialUserException("Username già in uso");
        }

        social.put(username, new HashSet<>());
        influencers.put(username, 0);
        return utente;
    }

    public void createUser(Utente utente) throws SocialUserException {
        createUser(utente.getUsername());
    }

    public void printAllPosts(){
        System.out.println("===========================");
        for (Post post : posts.values()){
            System.out.println("ID: "+post.getId());
            System.out.println(post.getAuthor().getUsername() + " ha scritto:");
            System.out.println('"'+post.getText()+'"');
            System.out.println(post.getTimestamp());
            System.out.println();
            System.out.println("===========================");
        }
    }
    public void printPost(int id){
        System.out.println("===========================");
        Post post = posts.get(id);
        System.out.println("ID: "+post.getId());
        System.out.println(post.getAuthor().getUsername() + " ha scritto:");
        System.out.println('"'+post.getText()+'"');
        System.out.println(post.getTimestamp());
        System.out.println();
        System.out.println("Likes: ");
    }

    public void printSocial(){      //debug
        System.out.println(social);
    }
}

