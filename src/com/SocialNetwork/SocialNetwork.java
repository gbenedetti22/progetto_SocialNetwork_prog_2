package com.SocialNetwork;

import com.SocialNetwork.CustomException.*;
import com.SocialNetwork.Interfaces.ISocial;
import java.util.*;

public class SocialNetwork implements ISocial {
    private final Map<String, Set<String>> social;          //utenti registrati + seguiti
    private final HashMap<String, Set<String>> followers;   //utenti registrati + followers
    private final HashMap<Integer, Post> posts;             //post approvati
    private final Set<String> metionedUsers;                //utenti che hanno postato almeno una volta

    private int id_posts = 1;

    /*
     * AF: a(social,followers,posts,metionedusers) = (social:(String) -> (seguiti)
     *                                                      f(u) = { seguiti da u } se u ∈ social.keySet(),
     *                                                  followers:(String) -> (followers)
     *                                                      f(u) = { followers di u } se u ∈ social.keySet(),
     *                                                  posts:(Integer)->(Post),
     *                                                      f(y)= {x ∈ Post | ∃x.author ∈ social.keySet}
     *                                                  metionedusers:[0, metionedusers.size]->(String)=
     *                                                      { u | u ∈ String ∧ (∃p | p ∈ posts.values ∧ u = p.author) }
     * seguiti(a,b) se ∃p1,p2 ∈ posts.values |
     * p1.author=a ∧ p2.author=b ∧ a≠b ∧ ∃x ∈ N (p1.id=x ∧ p2.text.contains("like:" + x)) -> a è seguito da b
     *
     * MODIFIES: this
     */
    public SocialNetwork() {
        social = new HashMap<>();
        posts = new HashMap<>();
        followers = new HashMap<>();
        metionedUsers = new HashSet<>();
    }

    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws SocialFollowBackException, SocialDuplicatePostException {
        Map<String, Set<String>> reteSociale = new HashMap<>();
        HashMap<Integer,Post> posts = new HashMap<>();
        HashMap<Integer,Post> likes = new HashMap<>();

        for (Post post : ps){   //separo i likes dai Post
            if(posts.containsKey(post.getId()) || likes.containsKey(post.getId()))
                throw new SocialDuplicatePostException();
            reteSociale.put(post.getAuthor(), new HashSet<>()); //registro l utente nella rete sociale fittizia

            if(post.getText().contains("like:")){
                likes.put(post.getId(),post);
            }else {
                posts.put(post.getId(),post);
            }
        }

        for (Post like : likes.values()){    //per ogni like messo ad un Post, controllo se quel Post esiste e se gli autori
            //corrispondono (cioè se si è messo like da solo).
            //Se tutto questo è rispettato allora
            //l autore che ha postato il like, finisce tra i follower dell autore del Post a cui
            //ha messo like
            String[] splittedText=like.getText().split(":");
            try {
                int idPost = Integer.parseInt(splittedText[1].trim());
                Post likedPost = posts.get(idPost);   //prendo il Post a cui like.author ha messo like

                if (likedPost != null && like.getTimestamp().after(likedPost.getTimestamp())) {//se il like è stato messo dopo il Post
                    if (likedPost.getAuthor().equals(like.getAuthor()))
                        throw new SocialFollowBackException("Non ci si può seguire da soli su questo Social!");

                    //metto like.author tra i followers dell autore del Post a cui lui ha messo like
                    reteSociale.get(likedPost.getAuthor()).add(like.getAuthor());
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {}
        }

        return reteSociale;
    }

    public List<String> influencers() {
        HashMap<String, Integer> influencers = new HashMap<>();
        for(Map.Entry<String, Set<String>> user : followers.entrySet()){
            influencers.put(user.getKey(), user.getValue().size());
        }

        List<Map.Entry<String, Integer>> sorted_influencers = new ArrayList<>(influencers.entrySet());
        sorted_influencers.sort(Map.Entry.comparingByValue());  //ordino gli utenti per ordine crescente
                                                                // in base al numero di follower che hanno
        List<String> result = new ArrayList<>();

        for (int i = sorted_influencers.size() - 1; i >= 0; i--) {
            result.add(sorted_influencers.get(i).getKey());
        }

        return result;
    }

    public Set<String> getMentionedUsers() {
        return metionedUsers;
    }

    public Set<String> getMentionedUsers(List<Post> ps) {
        Set<String> list = new HashSet<>();
        for (Post post : ps) {                                    //per ogni post in ps
            list.add(post.getAuthor());
        }

        return list;
    }

    public List<Post> writtenBy(String username) throws SocialUserArgumentException {
        if (username.isEmpty() || username.trim().length() == 0)
            throw new SocialUserArgumentException("username non può essere vuota o contenere solo spazi vuoti");

        List<Post> list = new ArrayList<>();
        for (Post post : posts.values()) {                        //per ogni post in posts<Integer,Post>
            if (post.getAuthor().equals(username)) {
                list.add(post);                                 //lista dei post scritti da username
            }
        }
        return list;
    }

    public List<Post> writtenBy(List<Post> ps, String username) throws SocialUserArgumentException {
        if (username.isEmpty() || username.trim().length() == 0)
            throw new SocialUserArgumentException("username non può essere vuota o contenere solo spazi vuoti");

        List<Post> list = new ArrayList<>();
        for (Post post : ps) {
            if (post.getAuthor().equals(username)) {
                list.add(post);
            }
        }
        return list;
    }

    public List<Post> containing(List<String> words) {
        List<Post> result = new ArrayList<>();

        for (Post post : posts.values()) {
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
        if (!social.containsKey(author))
            throw new SocialUserException("Utente non registrato");

        if (author.length() == 0 || text.length() == 0)
            throw new SocialPostArgumentException("author e text non possono essere vuoti");

        if (text.startsWith("like:")) {     //se il post comincia con "like:", author segue l autore del post likato
            String[] splittedText = text.split(":");
            try {
                int idPost = Integer.parseInt(splittedText[1].trim());
                Post post = posts.get(idPost); //ritorna null se get non trova nulla
                if (post != null) {                  //se io pubblico like:1234 ma 1234 non corrisponde a nessun post
                                                    //viene comunque pubblicato, ma viene scartato dall analisi dei like
                    if (post.getAuthor().equals(author))
                        throw new SocialFollowBackException("Non ci si può seguire da soli su questo Social!");

                    social.get(author).add(post.getAuthor());
                    followers.get(post.getAuthor()).add(author); //aggiorno i followers di post.getAuthor()
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {}
        }

        Post newPost = new Post(id_posts, author, text);
        posts.put(id_posts, newPost);   //pubblico il post
        metionedUsers.add(author);
        id_posts++;
        return newPost;
    }

    public String createUser(String username) throws SocialDuplicateUserException, SocialUserArgumentException {
        if (username.isEmpty())
            throw new SocialUserArgumentException("Il campo username, non può essere vuoto");

        if (social.containsKey(username)) {
            throw new SocialDuplicateUserException();
        }

        social.put(username, new HashSet<>());
        followers.put(username, new HashSet<>());   //all inizio, l utente appena registrato non ha followers
        return username;
    }

    public void printSocial() { //debug
        System.out.println(social);
    }
}

