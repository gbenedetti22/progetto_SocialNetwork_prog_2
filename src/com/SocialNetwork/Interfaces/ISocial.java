package com.SocialNetwork.Interfaces;

import com.SocialNetwork.CustomException.*;
import com.SocialNetwork.Post;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*	OVERVIEW: Collezione mutabile di <Post,followers> dove followers sono tutti gli utenti che hanno
 pubblicato almeno una volta un Post contenente la String "like:"+id ∈ N

 Typical Element: <Post,followers> dove l insieme posts contiene i Post e social contiene gli utenti con
 i relativi followers

 Rep Invariant:
 *  social != null ∧ social.size >= 0 ∧
    ∀x ∈ social.keySet() => x != null ∧ x.length > 0 ∧
    ∀y ∈ social.values() => y != null ∧ y.size >= 0 ∧
    ∀z ∈ y => z != null ∧ z.length > 0

 *  influencers != null ∧ influencers.size >= 0 ∧
    ∀x ∈ influencers.keySet() => x != null ∧ x.length > 0 ∧
    ∀y ∈ influencers.values() => y != null ∧ y.size >= 0

 *  posts != null ∧ posts.size >= 0 ∧
    ∀x ∈ posts.keySet() => x != null ∧ x >= 0 ∧
    ∀y ∈ posts.values() => y != null
 
 *  metionedusers != null ∧ metionedusers.size >= 0 ∧
    ∀x ∈ metionedusers => x != null ∧ x.length > 0
 */

public interface ISocial {
    /*
     * REQUIRES: ps ≠ null ∧ ∀p ∈ ps . p ≠ null ∧ p instanceof Post)
     * EFFECTS: restituisce una Map<Key,Value> contenente come Key: gli autori del Post e come Value:i suoi followers.
     * Un utente a segue un utente b, se esiste un post di a tale per cui 
     * il suo post contiene la dicitura "like:"+ un intero x
     * e un post di b, il cui id è quello stesso numero x.
     * Formalmente:
     * ∃p1,p2 ∈ ps . p1.author=a ∧ p2.author=b ∧ a≠b ∧ p1.id=x ∈ N ∧ p2.contains("like:" + x) -> f(a,b)
     *     f:(u x u)->(u x followers)
     *     dato a ⊆ u ∧ b ⊆ u
     *     f(a,b)=ab
     *
     * THROWS:
     * SocialDuplicatePostException: se ∃x ∈ ps ∧ y ∈ ps . x.getId() = y.getId()
     * SocialFollowBackException: ∃p1,p2 ∈ ps . p1.author=a ∧ p2.author=b ∧ a=b ∧ p1.id=x ∈ N ∧ p2.contains("like:" + x)
     * NullPointerException: (unchecked exception) se ps = null V ∃p ∈ ps . p=null
     */
    Map<String, Set<String>> guessFollowers(List<Post> ps) throws  SocialDuplicatePostException, SocialFollowBackException;

    /*
     * EFFECTS: restituisce una lista contenente gli utenti della rete,
     * ordinati in modo decrescente per numero di Followers
     */
    List<String> influencers();

    /*
     * EFFECTS: Ritorna un Set contenenti gli utenti che hanno postato almeno una volta nella rete sociale
     */
    Set<String> getMentionedUsers();

    /*
     * REQUIRES: ps ≠ null ∧ ∀p ∈ ps . p ≠ null
     * EFFECTS: Ritorna un Set contenente gli autori dei post all interno della lista ps.
     * Più formalmente: ∀p ∈ ps -> p.author
     * 
     * THROWS:
     * SocialPostArgumentException: se ∃p ∈ ps . p=null V ps=null
     * NullPointerException: (unchecked exception) se ps = null V ∃p ∈ ps . p=null
     */
    Set<String> getMentionedUsers(List<Post> ps) throws SocialPostArgumentException;

    /*
     * REQUIRES: username ≠ null ∧ username.length > 0 ∧ username.trim().length > 0
     * EFFECTS: restituisce una lista di Post scritti dall autore username.
     * Formalmente: ∀p ∈ posts ∧ p.getAuthor = username
     * 
     * THROWS:
     * SocialUserArgumentException: se username = null v username.length = 0 ∨ username.trim().length > 0
     * NullPointerException: se username=null (unchecked exception)
     */
    List<Post> writtenBy(String username) throws SocialUserArgumentException;

    /*
     * REQUIRES: ps ≠ null ∧ (∀p . p ∈ ps ∧ p ≠ null)
     * username ≠ null ∧ username.length > 0 ∧ username.trim().length > 0
     * EFFECTS: restituisce gli autori di tutti i post contenuti nella List<Post> ps,
     * passata come parametro
     * Formalmente: ∀p ∈ ps ∧ p.getAuthor = username
     *
     * THROWS:
     * SocialUserArgumentException: se ps = null ∨ (∃p ∈ ps . p = null) ∨ username = null
     * NullPointerException: (unchecked exception) se ps = null V ∃p ∈ ps . p=null v username = null
     * v username.length = 0 ∨ username.trim().length > 0
     */
    List<Post> writtenBy(List<Post> ps, String username) throws SocialUserArgumentException;;

    /*
     * REQUIRES: words ≠ null ∧ (∀w . w ∈ words ∧ w ≠ null)
     * EFFECTS: restituisce l insieme dei Post che contengono al loro interno una delle String contenute in words.
     * Formalmente: ∀p ∈ posts (∃w . w ∈ words ∧ p.getText.contains(w))
     *
     * THROWS:
     * NullPointerException: (unchecked exception) se words = null V ∃w ∈ words . w=null
     */
    List<Post> containing(List<String> words);

    /*
     * REQUIRES: author ≠ null ∧ text ≠ null ∧ author.length > 0 ∧ text.length > 0
     * MODIFIES:this
     * EFFECTS: crea un Post p1 utilizzando author e text come parametri del costruttore e
     * assegna un intero come id, il quale verrà dopo incrementato.
     * Inoltre aggiunge p1 in un insieme posts e p1.author in un insieme metionedUsers.
     * Se text contiene la dicitura "like:+int id", allora il Post p2 con quell id specificato viene preso 
     * dall insieme posts e viene aggiunta la value String p1.author nell insieme social, che ha come key
     * p2.author. Infine viene aggiornato l insieme influencers, il quale è definito come la funzione che associa ad
     * ogni utente il relativo numero di followers. Ritorna un riferimento al Post appena creato.
     * Più formalmente: definito posts come l insieme dei post, metionedUsers come
     * l insieme degli autori di questi post, social come la funzione che associa ad ogni utente i relativi followers,
     * influencers come la funzione che associa ad ogni utente, il numero di followers, p1 ∈ Post come il post appena
     * creato e p2 ∈ Post come il Post a cui viene messo il like:
     * posts.put(p1) ∧ metionedUsers.add(p1.author) ∧ social.get(p2.author).add(p1.author)
     * ∧ influencers.put(p2.author, social.get(p2.author).size()).
     * Definisco un like, come:
     * ∃p1,p2 ∈ posts . p1.author=a ∧ p2.author=b ∧ a≠b ∧ p1.id=x ∈ N ∧ p2.text.startsWith("like:" + x) -> f(a,b)
     * f:(u x u)->(u x followers)
     * dato a ⊆ u ∧ b ⊆ u
     * f(a,b)=ab
     *
     * THROWS:
     *  SocialUserException: se author ∉ social
     *  SocialPostArgumentException: se author.length==0 ∧ text.length==0
     *  NullPointerException: (unchecked exception) se author = null V text=null
     *  SocialFollowBackException: ∃p1,p2 ∈ posts . p1.author=a ∧ p2.author=b ∧ a=b ∧ p1.id=x ∈ N ∧ p2.contains("like:" + x)
     */
    Post post(String author, String text) throws SocialUserException, SocialPostArgumentException, SocialFollowBackException;

    /*
     * REQUIRES: username ≠ null ∧ username.length > 0
     * MODIFIES:this
     * EFFECTS: aggiunge alla map social l utente come key e un Set vuoto come value.
     * Questo simula la registrazione dell utente all interno del social.
     *
     * THROWS:
     * SocialDuplicateUserException: se ∃x ∈ social.keySet() ∧ y ∈ social.keySet() . x = y
     * NullPointerException: (unchecked exception) se username = null
     * SocialUserArgumentException: se username.length == 0
     */
    String createUser(String username) throws SocialDuplicateUserException, SocialUserArgumentException;
}
