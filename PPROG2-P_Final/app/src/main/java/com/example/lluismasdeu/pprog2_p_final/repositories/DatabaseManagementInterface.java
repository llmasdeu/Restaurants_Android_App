package com.example.lluismasdeu.pprog2_p_final.repositories;

import com.example.lluismasdeu.pprog2_p_final.model.Commentary;
import com.example.lluismasdeu.pprog2_p_final.model.Restaurant;
import com.example.lluismasdeu.pprog2_p_final.model.User;

import java.util.List;

/**
 * Interfície de la clase gestora de la base de datos.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public interface DatabaseManagementInterface {
    void registerUser(User u);
    void updateUser(User u);
    boolean existsUser(User u, int mode);
    User getConnectedUser(User u);
    List<User> getAllUsers();
    void registerRecentSearch(String recentSearch);
    boolean existsRecentSearch(String recentSearch);
    List<String> getAllRecentSearches();
    void registerFavorite(User u, Restaurant r);
    void unregisterFavorite(User u, Restaurant r);
    boolean existsFavorite(User u, Restaurant r);
    List<Restaurant> getAllFavorites(User u);
    void registerCommentary(Commentary c, Restaurant r);
    List<Commentary> getAllComentaries(Restaurant r);
}
