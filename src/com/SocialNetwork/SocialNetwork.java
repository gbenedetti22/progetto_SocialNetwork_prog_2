package com.SocialNetwork;

import com.SocialNetwork.CustomException.*;
import com.SocialNetwork.Interfaces.ISocial;
import java.util.*;

public class SocialNetwork implements ISocial {
    private final Map<String, Set<String>> social;      //utenti registrati + followers
    private final HashMap<String, Integer> influencers; //utenti registrati + numero di followers
    private final HashMap<Integer,Post> posts;          //post approvati
    private final Set<String> metionedUsers;            //utenti che hanno postato almeno una volta

    private int id_posts=1;
    /*
     * AF: a(social,influencers,posts,metionedusers) = (social:(String) -> (followers)
     *                                                      f(u) = { followers di u } se u ∈ social.keySet(),
     *                                                  influencers:(String) -> (Integer)
     *                                                      f(x)= (social(x).size) se x ∈ social.keySet(),
     *                                                  posts:(Integer)->(Post),
     *                                                      f(y)= {x ∈ Post | ∃x.author ∈ social.keySet}
     *                                                  metionedusers:[0, metionedusers.size]->(String)=
     *                                                      { u | u ∈ String ∧ (∃p | p ∈ posts.values ∧ u = p.author) }
     * followers(a,b) se ∃p1,p2 ∈ posts.values | p1.author=a ∧ p2.author=b ∧ a≠b ∧ p1.id=x ∈ N ∧ p2.text.contains("like:" + x)
     *
     * MODIFIES: this
     */
    public SocialNetwork() {
        social= new HashMap<>();
        posts = new HashMap<>();
        influencers = new HashMap<>();
        metionedUsers = new HashSet<>();
    }

    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws SocialFollowBackException, SocialDuplicatePostException {
        Map<String, Set<String>> reteSociale = new HashMap<>();
        HashMap<Integer,Post> posts = new HashMap<>();  //man mano che scorro la lista ps, tengo traccia dei Post
                                                        //così quando trovo un post che comincia con "like:+id", lo prendo
                                                        //direttamente da qua

        for (Post post : ps) {
            if(posts.containsKey(post.getId()))         //se trovo 2 post con la stessa key, lancio un Exception
                throw new SocialDuplicatePostException();

            String newFollower = post.getAuthor();

            posts.put(post.getId(), post);
            if(post.getText().startsWith("like:")){     //prendo il Post "likedPost" a cui voglio mettere like
                                                        //se l autore non è lo stesso di chi ha scritto il Post "post" ("newFollower")
                                                        //allora prendo i followers dell autore di "likedPost" e ci aggiungo
                                                        //l autore di "post"("newFollower")
                String[] splittedText=post.getText().split(":");
                try {
                    int idPost = Integer.parseInt(splittedText[1].trim());
                    Post likedPost=posts.get(idPost);
                    if(likedPost != null) {             //se non trovo il Post a cui mettere like, "post" viene pubblicato comunque
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
        sorted_influencers.sort(Map.Entry.comparingByValue());  //ordino gli utenti per ordine crescente
                                                                // in base al numero di follower che hanno
        List<String> result= new ArrayList<>();

        for(int i=sorted_influencers.size()-1; i>=0; i--){
            result.add(sorted_influencers.get(i).getKey());
        }

        return result;
    }
    public Set<String> getMentionedUsers(){
        return metionedUsers;
    }

    public Set<String> getMentionedUsers(List<Post> ps) {
        Set<String> list = new HashSet<>();
        for(Post post : ps){                                    //per ogni post in ps
            list.add(post.getAuthor());
        }

        return list;
    }
    public List<Post> writtenBy(String username) throws SocialUserArgumentException {
        if(username.isEmpty() || username.trim().length() == 0)
            throw  new SocialUserArgumentException("username non può essere vuota o contenere solo spazi vuoti");

        List<Post> list = new ArrayList<>();
        for(Post post : posts.values()){                        //per ogni post in posts<Integer,Post>
            if(post.getAuthor().equals(username)){
                list.add(post);                                 //lista dei post scritti da username
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
    public List<Post> containing (List<String> words) {
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

    protected HashMap<Integer, Post> getPosts() {
        return posts;
    }

    public Post post(String author, String text) throws SocialUserException, SocialPostArgumentException, SocialFollowBackException {
        if(!social.containsKey(author))
            throw new SocialUserException("Utente non registrato");

        if(author.length()==0 || text.length()==0)
            throw new SocialPostArgumentException("author e text non possono essere vuoti");

        if(text.startsWith("like:")){               //stesso meccanismo della guessFollower, ma fatto su tutta la rete
                                                    //(quindi usando la HashMap social)
            String[] splittedText=text.split(":");
            try {
                int idPost = Integer.parseInt(splittedText[1].trim());
                Post post=posts.get(idPost);        //ritorna null se get non trova nulla
                if(post != null) {                  //se io pubblico like:1234 ma 1234 non corrisponde a nessun post
                                                    //viene comunque pubblicato, ma viene scartato dall analisi dei like
                    if (post.getAuthor().equals(author))
                        throw new SocialFollowBackException("Non ci si può seguire da soli su questo Social!");

                    social.get(post.getAuthor()).add(author);   //aggiungo "author" ai follower dell autore del Post (post.getAuthor())
                                                                //a cui "author" ha messo like
                                                                //(postando un Post del tipo "like:+id_post")
                    influencers.put(post.getAuthor(), social.get(post.getAuthor()).size()); //aggiorno il numero di followers
                                                                                            //di post.getAuthor()
                    influencers.put(author,0);                  //il like è un post, quindi all inzio l autore di questo
                                                                //post avrà 0 likes
                }
            }catch (NumberFormatException ignored){}

        }

        Post newPost = new Post(id_posts, author, text);
        posts.put(id_posts, newPost);                           //pubblico il post
        metionedUsers.add(author);
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

    public void printSocial(){                                  //debug
        System.out.println(social);
    }
}

