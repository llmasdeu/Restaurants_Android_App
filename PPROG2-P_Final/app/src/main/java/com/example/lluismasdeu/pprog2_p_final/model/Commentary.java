package com.example.lluismasdeu.pprog2_p_final.model;

/**
 * Clase encargada de guardar los datos de un comentario.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class Commentary {
    private int id;
    private String username;
    private String commentary;

    /**
     * Constructor de la clase.
     * @param id Identificador numérico del comentario.
     * @param username Nombre de usuario.
     * @param commentary Comentario.
     */
    public Commentary(int id, String username, String commentary) {
        this.id = id;
        this.username = username;
        this.commentary = commentary;
    }

    /**
     * Constructor de la clase.
     * @param username Nombre de usuario.
     * @param commentary Comentario.
     */
    public Commentary(String username, String commentary) {
        this.username = username;
        this.commentary = commentary;
    }

    /**
     * Getter del identificador numérico del comentario.
     * @return Identificador numérico del comentario.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter del nombre de usuario.
     * @return Nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter del comentario.
     * @return Comentario.
     */
    public String getCommentary() {
        return commentary;
    }
}
