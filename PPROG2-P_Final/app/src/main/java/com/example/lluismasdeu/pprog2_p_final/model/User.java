package com.example.lluismasdeu.pprog2_p_final.model;

/**
 * Clase encargada de almacenar los datos de los usuarios.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class User {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String gender;
    private String description;
    private String imageFile;

    /**
     * Constructor de la clase.
     * @param name Nombre del usuario.
     * @param surname Apellido del usuario.
     * @param gender Género del usuario.
     * @param description Descripción del usuario.
     * @param imageFile Nombre del fichero de la imagen de perfil del usuario.
     */
    public User(String name, String surname,
                String gender, String description,String imageFile) {

        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.description = description;
        this.imageFile=imageFile;
    }
    /**
     * Constructor de la clase.
     * @param username Nombre de usuario.
     * @param email Dirección de correo electrónico del usuario.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Constructor de la clase.
     * @param username Nombre de usuario.
     * @param email Dirección de correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor de la clase.
     * @param name Nombre del usuario.
     * @param surname Apellido del usuario.
     * @param username Nombre de usuario.
     * @param email Dirección de correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param gender Género del usuario.
     * @param description Descripción del usuario.
     * @param imageFile Nombre del fichero de la imagen de perfil del usuario.
     */
    public User(String name, String surname, String username, String email, String password,
                String gender, String description, String imageFile) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.description = description;
        this.imageFile = imageFile;
    }

    /**
     * Constructor de la clase.
     * @param id Identificador numérico del usuario.
     * @param name Nombre del usuario.
     * @param surname Apellido del usuario.
     * @param username Nombre de usuario.
     * @param email Dirección de correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param gender Género del usuario.
     * @param description Descripción del usuario.
     * @param imageFile Nombre del fichero de la imagen de perfil del usuario.
     */
    public User(int id, String name, String surname, String username, String email, String password,
                String gender, String description, String imageFile) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.description = description;
        this.imageFile = imageFile;
    }

    /**
     * Getter del identificador numérico del usuario.
     * @return Identificador numérico del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter del nombre del usuario.
     * @return Nombre del usuario.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter del nombre del usuario.
     * @param name Nombre del usuario
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter del apellido del usuario.
     * @return Apellido del usuario.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setter del apellido del usuario.
     * @param surname Apellido del usuario.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Getter del nombre de usuario.
     * @return Nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter de la dirección de correo electrónico del usuario.
     * @return Dirección de correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter de la contraseña del usuario.
     * @return Contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter del género del usuario.
     * @return Género del usuario.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Setter del género del usuario.
     * @param gender Género del usuario.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Getter de la descripción del usuario.
     * @return Descripción del usuario.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter de la descripción del usuario.
     * @param description Descripción del usuario.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter del nombre del fichero de la imagen de perfil del usuario.
     * @return Nombre del fichero de la imagen de perfil del usuario.
     */
    public String getImageFile() {
        return imageFile;
    }

    /**
     * Setter del nombre del fichero de la imagen de perfil del usuario.
     * @param imageFile Nombre del fichero de la imagen de perfil del usuario.
     */
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Método encargado de generar una String con toda la información del usuario.
     * @return String con toda la información del usuario.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("{ ID: ").append(id).append("; Name: "). append(name).append("; Surname: ")
                .append(surname).append("; Username: ").append(username).append("; E-mail: ")
                .append(email).append("; Password: ").append(password).append("; Gender: ")
                .append(gender).append("; Description: ").append(description)
                .append("; Image File: ").append(imageFile).append(" }");

        return builder.toString();
    }
}
