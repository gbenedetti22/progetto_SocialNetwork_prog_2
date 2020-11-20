package com.SocialNetwork.Interfaces;

import com.SocialNetwork.CustomException.*;
import com.SocialNetwork.Post;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**	<p> <h1>OVERVIEW</h1> Collezione mutabile di <Post,followers> dove followers sono tutti gli utenti che hanno
 pubblicato almeno una volta un Post contenente la String "like:"+id ∈ N</p>

 <p> <h1>Typical Element</h1><Post,followers> dove l insieme posts contiene i Post e social contiene gli utenti con
 i relativi followers</p>

 <p> <h1>Rep Invariant</h1>
 <p>social != null ∧ social.size >= 0 ∧<br>
 ∀x ∈ social.keySet() => x != null ∧ x.length > 0 ∧ <br>
 ∀y ∈ social.values() => y != null ∧ y.size >= 0 ∧<br>
 ∀z ∈ y => z != null ∧ z.length > 0</p>
 <br>
 <p>influencers != null ∧ influencers.size >= 0 ∧<br>
 ∀x ∈ influencers.keySet() => x != null ∧ x.length > 0 ∧<br>
 ∀y ∈ influencers.values() => y != null ∧ y.size >= 0 </p>
 <br>
 <p>posts != null ∧ posts.size >= 0 ∧<br>
 ∀x ∈ posts.keySet() => x != null ∧ x >= 0 ∧<br>
 ∀y ∈ posts.values() => y != null</p>
 <br>
 <p>metionedusers != null ∧ metionedusers.size >= 0 ∧<br>
 ∀x ∈ metionedusers => x != null ∧ x.length > 0<br></p>
 */

public interface ISocial {
    /**
     * REQUIRES: ps ≠ null ∧ ∀p ∈ ps . p ≠ null ∧ p instanceof Post)<br>
     * EFFECTS: restituisce una Map<Key,Value> contenente come Key: gli autori del Post e come Value:i suoi followers.<br>
     * Un utente a segue un utente b, se esiste un post di a tale per cui <br>
     * il suo post contiene la dicitura "like:"+ un intero x<br>
     * e un post di b, il cui id è quello stesso numero x.<br>
     *<br>
     * @return ∃p1,p2 ∈ ps . p1.author=a ∧ p2.author=b ∧ a≠b ∧ p1.id=x ∈ N ∧ p2.contains("like:" + x) -> f(a,b)<br>
     *     f:(u x u)->(u x followers)<br>
     *     dato a ⊆ u ∧ b ⊆ u<br>
     *     f(a,b)=ab
     * @throws SocialDuplicatePostException se ∃ x ∈ ps e y ∈ ps . x.getId() = y.getId()
     * @throws NullPointerException (unchecked exception) se ps = null V ∃p ∈ ps . p=null
     */
    Map<String, Set<String>> guessFollowers(List<Post> ps) throws  SocialDuplicatePostException, SocialFollowBackException;

    /**
     * EFFECTS: restituisce una lista contenente gli utenti della rete, <br>
     * ordinati in modo decrescente per numero di Followers<br>
     *<br>
     * @return
     */
    List<String> influencers();

    /**
     * EFFECTS: Ritorna un Set contenenti gli utenti che hanno postato almeno una volta nella rete sociale<br>
     * @return l insieme metionedUsers
     */
    Set<String> getMentionedUsers();

    /**
     * REQUIRES: ps ≠ null ∧ ∀p ∈ ps . p ≠ null<br>
     * EFFECTS: Ritorna un Set contenente gli autori dei post all interno della lista ps<br>
     *<br>
     * @return ∀p ∈ ps . p.author
     * @throws SocialPostArgumentException se ∃p ∈ ps . p=null V ps=null
     * @throws NullPointerException (unchecked exception) se ps = null V ∃p ∈ ps . p=null
     */
    Set<String> getMentionedUsers(List<Post> ps) throws SocialPostArgumentException;

    /**
     * REQUIRES: username ≠ null ∧ username.length > 0 ∧ username.trim().length > 0<br>
     * @throws SocialUserArgumentException: se username = null v username.length = 0 ∨ username.trim().length > 0<br>
     * @throws NullPointerException se username=null (unchecked exception)
     * EFFECTS: restituisce una lista di Post scritti dall autore username<br>
     * Formalmente: ∀ post p . p.getAuthor = username
     */
    List<Post> writtenBy(String username) throws SocialUserArgumentException;

    /**
     * REQUIRES: ps ≠ null ∧ (∀p . p ∈ ps ∧ p ≠ null)<br>
     * username ≠ null ∧ username.length > 0 ∧ username.trim().length > 0<br>
     * @throws SocialUserArgumentException: se ps = null ∨ (∃p ∈ ps . p = null) ∨ username = null<br>
     * @throws NullPointerException (unchecked exception) se ps = null V ∃p ∈ ps . p=null v username = null
     * v username.length = 0 ∨ username.trim().length > 0<br>
     * EFFECTS: restituisce gli autori di tutti i post contenuti nella List<Post> ps,<br>
     * passata come parametro<br>
     * Formalmente: ∀ post p . p ∈ ps ∧ p.getAuthor = username
     */
    List<Post> writtenBy(List<Post> ps, String username) throws SocialUserArgumentException;;

    /**
     * REQUIRES: words ≠ null ∧ (∀w . w ∈ words ∧ w ≠ null)<br>
     * EFFECTS: restituisce l insieme dei Post che contengono al loro interno una delle String contenute in words
     * @throws IllegalArgumentException se words = null v (∀w . w ∈ words ∧ w = null)
     * @throws NullPointerException (unchecked exception) se words = null V ∃w ∈ words . w=null
     * @return ∀ post p (∃w . w ∈ words ∧ p.getText.contains(w))
     */
    List<Post> containing(List<String> words) throws IllegalArgumentException;

    /**
     * REQUIRES: author ≠ null ∧ text ≠ null ∧ author.length > 0 ∧ text.length > 0<br>
     * MODIFIES:this<br>
     * EFFECTS: crea un Post p1 utilizzando author e text come parametri del costruttore e<br>
     * assegna un intero come id, il quale verrà dopo incrementato.<br>
     * Inoltre aggiunge p1 in un insieme posts e p1.author in un insieme metionedUsers.<br>
     * Se text contiene la dicitura "like:+int id", allora il Post p2 con quell id specificato viene preso <br>
     * dall insieme posts e viene aggiunta la value String p1.author nell insieme social, che ha come key
     * p2.author. Infine viene aggiornato l insieme influencers, il quale è definito come la funzione che associa ad
     * ogni utente il relativo numero di followers<br>
     * Più formalmente: definito posts come l insieme dei post, metionedUsers come
     * l insieme degli autori di questi post, social come la funzione che associa ad ogni utente i relativi followers,
     * influencers come la funzione che associa ad ogni utente, il numero di followers, p1 ∈ Post come il post appena
     * creato e p2 ∈ Post come il Post a cui viene messo il like:<br>
     * posts.put(p1) ∧ metionedUsers.add(p1.author) ∧ social.get(p2.author).add(p1.author)
     * ∧ influencers.put(p2.author, social.get(p2.author).size()).
     * Definisco un like, come:
     * ∃p1,p2 ∈ posts . p1.author=a ∧ p2.author=b ∧ a≠b ∧ p1.id=x ∈ N ∧ p2.text.startsWith("like:" + x) -> f(a,b)<br>
     * f:(u x u)->(u x followers)<br>
     * dato a ⊆ u ∧ b ⊆ u<br>
     * f(a,b)=ab
     * @return Un riferimento al Post appena pubblicato
     * @throws SocialUserException se author ∉ social
     * @throws SocialPostArgumentException se author.length==0 ∧ text.length==0
     * @throws NullPointerException (unchecked exception) se author = null V text=null
     * @throws SocialFollowBackException se ∃p ∈ posts . p.author=author (o p.author.equals(author))
     */
    Post post(String author, String text) throws SocialUserException, SocialPostArgumentException, SocialFollowBackException;

    /**
     * REQUIRES: username ≠ null ∧ username.length > 0
     * MODIFIES:this<br>
     * EFFECTS: aggiunge alla map social l utente come key e un Set vuoto come value.<br>
     * Questo simula la registrazione dell utente all interno del social.
     * @param username username dell utente
     * @return Un riferimento all Utente appena pubblicato
     * @throws SocialDuplicateUserException
     * @throws NullPointerException (unchecked exception) se username = null
     * @throws SocialUserArgumentException
     */
    String createUser(String username) throws SocialDuplicateUserException, SocialUserArgumentException;
}
