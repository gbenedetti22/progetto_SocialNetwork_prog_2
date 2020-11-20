package com.SocialNetwork;

import com.SocialNetwork.CustomException.*;
import com.SocialNetwork.Interfaces.ISocial;
import java.util.*;

public class SocialNetwork implements ISocial {
    private final Map<String, Set<String>> social;      //utenti registrati + followers
    private final HashMap<String, Integer> influencers; //utenti registrati + numero di followers
    private final HashMap<Integer,Post> posts;          //post approvati
    private final Set<String> metionedusers;            //utenti che hanno postato almeno una volta

    private int id_posts=1;

    /**
     * AF: a(social,influencers,posts,metionedusers) = (f:(String) -> (followers),<br>
     *                                                  f:(String)->(followers.size),<br>
     *                                                  f:(Integer)->(Post),<br>
     *                                                  f:[0, metionedusers.size]->(String) tale che ∃x (x ∈ metionedusers ∧ ∀y (y ∈ metionedusers -> y!=x) ∧ (∃post ∈ posts.values . post.author=x)<br>
     * followers -> (String) tale che ∃p1,p2 ∈ ps . p1.author=a ∧ p2.author=b ∧ a≠b ∧ p1.id=x ∈ N ∧ p2.contains("like:" + x)
     */
    /**
     * MODIFIES: this
     */
    public SocialNetwork() {
        social= new HashMap<>();
        posts = new HashMap<>();
        influencers = new HashMap<>();
        metionedusers = new HashSet<>();
    }

    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws SocialFollowBackException, SocialDuplicatePostException {
        Map<String, Set<String>> reteSociale = new HashMap<>();
        HashMap<Integer,Post> posts = new HashMap<>();  //per tenere traccia dei Post precedenti

        for (Post post : ps) {
            if(posts.containsKey(post.getId()))
                throw new SocialDuplicatePostException();

            String newFollower = post.getAuthor();

            posts.put(post.getId(), post);
            if(post.getText().startsWith("like:")){
                String[] splittedText=post.getText().split(":");
                try {
                    int idPost = Integer.parseInt(splittedText[1].trim());
                    Post likedPost=posts.get(idPost);
                    if(likedPost != null) {
                        if(likedPost.getAuthor().equals(newFollower))
                            throw new SocialFollowBackException("Non ci si può seguire da soli su questo Social!");
                        reteSociale.get(likedPost.getAuthor()).add(newFollower);
                    }
                }catch(NumberFormatException ignored){}
            }

            if (!reteSociale.containsKey(newFollower)) {
                reteSociale.put(newFollower, new HashSet<>());
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

    public Set<String> getMentionedUsers(List<Post> ps) throws SocialPostArgumentException {
        Set<String> list = new HashSet<>();
        for(Post post : ps){
            if(post == null)
                throw new SocialPostArgumentException("Un Post non può essere null");
            list.add(post.getAuthor());
        }

        return list;
    }
    public List<Post> writtenBy(String username) throws SocialUserArgumentException {
        if(username.isEmpty() || username.trim().length() == 0)
            throw  new SocialUserArgumentException("username non può essere vuota o contenere solo spazi vuoti");

        List<Post> list = new ArrayList<>();
        for(Post post : posts.values()){
            if(post.getAuthor().equals(username)){
                list.add(post);
            }
        }
        return list;
    }
    public List<Post> writtenBy(List<Post> ps, String username) throws SocialUserArgumentException {
        if(username.isEmpty() || username.trim().length() == 0)
            throw  new SocialUserArgumentException("username non può essere vuota o contenere solo spazi vuoti");

        List<Post> list = new ArrayList<>();
        for(Post post : ps){
            if(post.getAuthor().equals(username)){
                list.add(post);
            }
        }
        return list;
    }
    public List<Post> containing (List<String> words) throws IllegalArgumentException{
        List<Post> result= new ArrayList<>();

        for(Post post: posts.values()){
            for (String word : words) {
                if(word == null)
                    throw new IllegalArgumentException("string null non ammesse");

                if (post.getText().contains(word)) {
                    result.add(post);
                    break;
                }
            }
        }

        return result;
    }

    protected HashMap<Integer, Post> getPosts() {
        return posts;
    }

    public Post post(String author, String text) throws SocialUserException, SocialPostArgumentException, SocialFollowBackException {
        if(!social.containsKey(author))
            throw new SocialUserException("Utente non registrato");

        if(author.length()==0 || text.length()==0)
            throw new SocialPostArgumentException("author e text non possono essere vuoti");

        if(text.startsWith("like:")){
            String[] splittedText=text.split(":");
            try {
                int idPost = Integer.parseInt(splittedText[1].trim());
                Post post=posts.get(idPost);        //ritorna null se get non trova nulla
                if(post != null) {                  //se io pubblico like:1234 ma 1234 non corrisponde a nessun post
                                                    //viene comunque pubblicato, ma viene scartato dall analisi dei like
                    if (post.getAuthor().equals(author))
                        throw new SocialFollowBackException("Non ci si può seguire da soli su questo Social!");

                    social.get(post.getAuthor()).add(author);
                    influencers.put(post.getAuthor(), social.get(post.getAuthor()).size());
                }
            }catch (NumberFormatException ignored){}

        }

        Post newPost = new Post(id_posts, author, text);
        posts.put(id_posts, newPost);
        metionedusers.add(author);
        id_posts++;
        return newPost;
    }

    public String createUser(String username) throws SocialDuplicateUserException, SocialUserArgumentException {
        if(username.isEmpty())
            throw new SocialUserArgumentException("Il campo username, non può essere vuoto");

        if(social.containsKey(username)){
            throw new SocialDuplicateUserException();
        }

        social.put(username, new HashSet<>());
        return username;
    }

    public void printAllPosts(){
        System.out.println("===========================");
        for (Post post : posts.values()){
            System.out.println("ID: "+post.getId());
            System.out.println(post.getAuthor() + " ha scritto:");
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
        System.out.println(post.getAuthor() + " ha scritto:");
        System.out.println('"'+post.getText()+'"');
        System.out.println(post.getTimestamp());
        System.out.println();
        System.out.println("Likes: ");
    }

    public void printSocial(){      //debug
        System.out.println(social);
    }
}

