package com.SocialNetwork.Interfaces;

import java.sql.Timestamp;

public interface IPost {
    /*
     * OVERVIEW: un Post è un oggetto immutable contenente un id univoco ∈ N, un autore, un testo di massimo 140 caratteri
     * e un timestamp che rappresenta data e ora della pubblicazione
     * TE: <id,author,text,timestamp> dove author,text sono scritti dall utente e passati come parametro al costruttore,
     * id e timestamp sono generati automaticamente
     */
    
    /*
     *EFFECTS: restituisce l id univoco del post
     */
    int getId();
    
    /*
     *EFFECTS: restituisce l autore del Post
     */
    String getAuthor();
    
    /*
     *EFFECTS: restituisce il testo scritto e pubblicato dall utente
     */
    String getText();
    
    /*
     *EFFECTS: restituisce il timestamp del post
     */
    Timestamp getTimestamp();
}
