package com.example.lluismasdeu.pprog2_p_final.model;

/**
 * Clase encargada de almacenar valores estáticos a lo largo de la aplicación.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class StaticValues {
    private static StaticValues instance = null;
    private User connectedUser;
    private Restaurant selectedRestaurant;

    /**
     * Constructor de la clase.
     */
    private StaticValues() {}

    /**
     * Getter de la instancia.
     * @return Instancia de la clase.
     */
    public static StaticValues getInstance() {
        if (instance == null)
            instance = new StaticValues();

        return instance;
    }

    /**
     * Getter del usuario conectado.
     * @return Usuario conectado
     */
    public User getConnectedUser() {
        return connectedUser;
    }

    /**
     * Setter del usuario conectado.
     * @param connectedUser Usuario conectado.
     */
    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    /**
     * Getter del restaurante seleccionado.
     * @return Restaurante seleccionado.
     */
    public Restaurant getSelectedRestaurant() {
        return selectedRestaurant;
    }

    /**
     * Setter del restaurante seleccionado.
     * @param selectedRestaurant Restaurante seleccionado.
     */
    public void setSelectedRestaurant(Restaurant selectedRestaurant) {
        this.selectedRestaurant = selectedRestaurant;
    }
}
