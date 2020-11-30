package com.example.lluismasdeu.pprog2_p_final.repositories.implementations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lluismasdeu.pprog2_p_final.model.Commentary;
import com.example.lluismasdeu.pprog2_p_final.model.Restaurant;
import com.example.lluismasdeu.pprog2_p_final.model.StaticValues;
import com.example.lluismasdeu.pprog2_p_final.model.User;
import com.example.lluismasdeu.pprog2_p_final.repositories.DatabaseManagementInterface;
import com.example.lluismasdeu.pprog2_p_final.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de conectar con la base de datos.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class DatabaseManagement implements DatabaseManagementInterface {
    private static final String TAG = "DatabaseManagement";

    private static Context context;

    // Nombres de las tablas.
    private static final String COMMENTARIES_TABLE ="commentaries";
    private static final String FAVORITES_TABLE ="favorites";
    private static final String SEARCHES_TABLE = "searches";
    private static final String USERS_TABLE = "users";

    // Nombres de las columnas.
    private static final String ADDRESS_COLUMN = "address";
    private static final String CLOSING_COLUMN = "closing";
    private static final String COMMENTARY_COLUMN = "commentary";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String EMAIL_COLUMN = "email";
    private static final String GENDER_COLUMN = "gender";
    private static final String ID_COLUMN = "_id";
    private static final String IMAGE_FILE_COLUMN = "image";
    private static final String LATITUDE_COLUMN = "latitude";
    private static final String LONGITUDE_COLUMN = "longitude";
    private static final String NAME_COLUMN = "name";
    private static final String OPENING_COLUMN = "opening";
    private static final String PASSWORD_COLUMN = "password";
    private static final String RATING_COLUMN = "rating";
    private static final String SEARCH_COLUMN = "search";
    private static final String SURNAME_COLUMN = "surname";
    private static final String TYPE_COLUMN = "type";
    private static final String USERNAME_COLUMN = "username";

    /**
     * Constructor de la clase.
     * @param context Context.
     */
    public DatabaseManagement(Context context) {
        this.context = context;
    }

    /**
     * Método encargado de registrar un usuario en la base de datos.
     * @param user Información del usuario a registrar.
     */
    @Override
    public void registerUser(User user) {
        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos los valores de las distintas columnas.
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, user.getName());
        values.put(SURNAME_COLUMN, user.getSurname());
        values.put(USERNAME_COLUMN, user.getUsername());
        values.put(EMAIL_COLUMN, user.getEmail());
        values.put(PASSWORD_COLUMN, user.getPassword());
        values.put(GENDER_COLUMN, user.getGender());
        values.put(DESCRIPTION_COLUMN, user.getDescription());
        values.put(IMAGE_FILE_COLUMN, user.getImageFile());

        // Llevamos a cabo la inserción en la base de datos.
        helper.getWritableDatabase().insert(USERS_TABLE, null, values);
    }

    /**
     * Método encargado de actualizar un usuario en la base de datos.
     * @param u Información del usuario a actualizar.
     */
    @Override
    public void updateUser(User u) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN,u.getName());
        values.put(SURNAME_COLUMN,u.getSurname());
        values.put(GENDER_COLUMN,u.getGender());
        values.put(DESCRIPTION_COLUMN,u.getDescription());
        values.put(IMAGE_FILE_COLUMN,u.getImageFile());
        String whereClause = USERNAME_COLUMN + "=?";
        String[] whereArgs = {u.getUsername()};
        helper.getWritableDatabase().update(USERS_TABLE, values, whereClause, whereArgs);

    }

    /**
     * Método encargado de consultar si el usuario existe en la base de datos.
     * @param user Usuario a consultar.
     * @param mode Modo de búsqueda.
     *             1 - Consulta de login.
     *             2 - Consulta de registro.
     * @return
     */
    @Override
    public boolean existsUser(User user, int mode) {
        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos la petición.
        String whereClause = "";
        String[] whereArgs = new String[2];

        switch (mode) {
            case 1:
                whereClause = "(" + USERNAME_COLUMN + " =? OR "+ EMAIL_COLUMN + " =? ) AND "
                        + PASSWORD_COLUMN + " =?";
                whereArgs = new String[3];
                whereArgs[0] = user.getUsername();
                whereArgs[1] = user.getEmail();
                whereArgs[2] = user.getPassword();
                break;

            case 2:
                whereClause = USERNAME_COLUMN + " =? OR " + EMAIL_COLUMN + " =?";
                whereArgs[0] = user.getUsername();
                whereArgs[1] = user.getEmail();
                break;
        }

        // Llevamos a cabo la consulta en la base de datos.
        SQLiteDatabase db = helper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, USERS_TABLE, whereClause, whereArgs);

        // Comprovamos si existe alguno.
        return count > 0;
    }

    /**
     * Método encargado de obtener un usuario de la base de datos.
     * @param user Información del usuario a buscar.
     * @return Información completa del usuario.
     */
    @Override
    public User getConnectedUser(User user) {
        User retrievedUser = null;

        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos la petición.
        String[] selectColumns = null;
        String whereClause = "(" + USERNAME_COLUMN + " =? OR " + EMAIL_COLUMN + " =? ) AND "
                + PASSWORD_COLUMN + " =?";
        String[] whereArgs = {user.getUsername(), user.getEmail(), user.getPassword()};

        // Llevamos a cabo la consulta en la base de datos.
        Cursor cursor = helper.getReadableDatabase()
                .query(USERS_TABLE, selectColumns, whereClause, whereArgs, null, null, null);

        // Hacemos la lectura de los resultados.
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = 0;
                String name = "", surname = "", username = "", email = "", password = "",
                        gender = "", description = "", imageFile = "";

                do {
                    id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                    name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                    surname = cursor.getString(cursor.getColumnIndex(SURNAME_COLUMN));
                    username = cursor.getString(cursor.getColumnIndex(USERNAME_COLUMN));
                    email = cursor.getString(cursor.getColumnIndex(EMAIL_COLUMN));
                    password = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN));
                    gender = cursor.getString(cursor.getColumnIndex(GENDER_COLUMN));
                    description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN));
                    imageFile = cursor.getString(cursor.getColumnIndex(IMAGE_FILE_COLUMN));
                } while (cursor.moveToNext());

                retrievedUser = new User(id, name, surname, username, email, password, gender,
                        description, imageFile);
            }
        }

        return retrievedUser;
    }

    /**
     * Método encargado de obtener todos los usuarios registrados en la base de datos.
     * @return Usuarios registrados en la base de datos.
     */
    @Override
    public List<User> getAllUsers() {
        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Llevamos a cabo la consulta en la base de datos.
        Cursor cursor = helper.getReadableDatabase().query(USERS_TABLE, null, null, null, null,
                null, null);

        // Creamos la lista donde guardaremos los usuarios registrados.
        List<User> users = new ArrayList<User>();

        // Hacemos la lectura de los resultados.
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = 0;
                String name = "", surname = "", username = "", email = "", password = "", gender = "",
                        description = "", imageFile = "";

                do {
                    id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                    name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                    surname = cursor.getString(cursor.getColumnIndex(SURNAME_COLUMN));
                    username = cursor.getString(cursor.getColumnIndex(USERNAME_COLUMN));
                    email = cursor.getString(cursor.getColumnIndex(EMAIL_COLUMN));
                    password = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN));
                    gender = cursor.getString(cursor.getColumnIndex(GENDER_COLUMN));
                    description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN));
                    imageFile = cursor.getString(cursor.getColumnIndex(IMAGE_FILE_COLUMN));

                    // Añadimos el usuario en el ArrayList.
                    users.add(new User(id, name, surname, username, email, password, gender,
                            description, imageFile));
                } while (cursor.moveToNext());
            }
        }

        return users;
    }

    /**
     * Método encargado de registrar una búsqueda reciente en la base de datos.
     * @param recentSearch Búsqueda a registrar.
     */
    @Override
    public void registerRecentSearch(String recentSearch) {
        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos el valor de la columna.
        ContentValues values = new ContentValues();
        values.put(SEARCH_COLUMN, recentSearch);

        // Llevamos a cabo la inserción en la base de datos.
        helper.getWritableDatabase().insert(SEARCHES_TABLE, null, values);
    }

    /**
     * Método encargado de comprobar si una búsqueda está registrada en la base de datos.
     * @param recentSearch Búsqueda a comprobar.
     * @return CIERTO si está registrada. FALSO en caso contrario.
     */
    public boolean existsRecentSearch(String recentSearch) {
        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos la petición.
        String whereClause = SEARCH_COLUMN + " =? ";
        String[] whereArgs = {recentSearch};

        // Llevamos a cabo la consulta en la base de datos.
        SQLiteDatabase db = helper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, SEARCHES_TABLE, whereClause, whereArgs);

        // Comprovamos si existe alguno.
        return count > 0;
    }

    /**
     * Método encargado de obtener todas las búsquedas de la base de datos.
     * @return Búsquedas registradas en la base de datos.
     */
    @Override
    public List<String> getAllRecentSearches() {
        // Obtenemos la instancia de la base de datos.
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Llevamos a cabo la consulta en la base de datos.
        Cursor cursor = helper.getReadableDatabase().query(SEARCHES_TABLE, null, null, null, null,
                null, null);

        // Creamos la lista donde guardaremos las búsquedas recientes.
        List<String> recentSearches = new ArrayList<String>();

        // Hacemos la lectura de los resultados.
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String search = "";

                do {
                    search = cursor.getString(cursor.getColumnIndex(SEARCH_COLUMN));

                    // Añadimos la búsqueda en el ArrayList.
                    recentSearches.add(search);
                } while (cursor.moveToNext());
            }
        }

        return recentSearches;
    }

    /**
     * Método encargado de registrar un restaurante favorito.
     * @param u Usuario asociado.
     * @param r Restaurante a añadir.
     */
    @Override
    public void registerFavorite(User u, Restaurant r) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos los valores de las distintas columnas.
        ContentValues values = new ContentValues();
        values.put(USERNAME_COLUMN, u.getUsername());
        values.put(NAME_COLUMN, r.getName());
        values.put(TYPE_COLUMN, r.getType());
        values.put(ADDRESS_COLUMN, r.getAddress());
        values.put(OPENING_COLUMN, r.getOpening());
        values.put(CLOSING_COLUMN, r.getClosing());
        values.put(RATING_COLUMN, r.getRating());
        values.put(DESCRIPTION_COLUMN, r.getDescription());
        values.put(LATITUDE_COLUMN, r.getLatitude());
        values.put(LONGITUDE_COLUMN, r.getLongitude());
        values.put(IMAGE_FILE_COLUMN, r.getImageFile());

        // Llevamos a cabo la inserción en la base de datos.
        helper.getWritableDatabase().insert(FAVORITES_TABLE, null, values);
    }

    /**
     * Método encargado de eliminar un restaurante favorito
     * @param u Usuario asociado.
     * @param r Restaurante a eliminar.
     */
    @Override
    public void unregisterFavorite(User u, Restaurant r) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        String whereClause = USERNAME_COLUMN + " =? AND " + NAME_COLUMN + " =? AND "
                + ADDRESS_COLUMN + " =? AND " + TYPE_COLUMN + " =?";
        String[] whereArgs = {u.getUsername(), r.getName(), r.getAddress(), r.getType()};

        helper.getWritableDatabase().delete(FAVORITES_TABLE, whereClause, whereArgs);
    }

    /**
     * Método encargado de comprobar si el restaurante está marcado como favorito por el usuario.
     * @param u Usuario asociado.
     * @param r Restaurante a consultar.
     * @return CIERTO si está marcado como favorito. FALSO en caso contrario.
     */
    @Override
    public boolean existsFavorite(User u, Restaurant r) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        String whereClause = USERNAME_COLUMN + " =? AND " + NAME_COLUMN + " =? AND "
                + ADDRESS_COLUMN + " =? AND " + TYPE_COLUMN + " =?";
        String[] whereArgs = {u.getUsername(), r.getName(), r.getAddress(), r.getType()};

        SQLiteDatabase db = helper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, FAVORITES_TABLE, whereClause, whereArgs);

        return count > 0;
    }

    /**
     * Método encargado de obtener todos los restaurantes marcados como favoritos por el usuario.
     * @param u Usuario asociado.
     * @return Listado de restaurantes marcados como favoritos por el usuario.
     */
    @Override
    public List<Restaurant> getAllFavorites(User u) {
        Log.d(TAG, "getAllFavorites: " + u.getUsername());
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        List<Restaurant> favoritesList = new ArrayList<Restaurant>();
        String[] selectColumns = null;
        String whereClause = USERNAME_COLUMN + " =?";
        String[] whereArgs = {u.getUsername()};
        Cursor cursor = helper.getReadableDatabase()
                .query(FAVORITES_TABLE, selectColumns, whereClause, whereArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id;
                String name, type, address, opening, closing, description, imageFile;
                double review, latitude, longitude;

                do {
                    id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                    name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                    type = cursor.getString(cursor.getColumnIndex(TYPE_COLUMN));
                    address = cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN));
                    opening = cursor.getString(cursor.getColumnIndex(OPENING_COLUMN));
                    closing = cursor.getString(cursor.getColumnIndex(CLOSING_COLUMN));
                    review = cursor.getDouble(cursor.getColumnIndex(RATING_COLUMN));
                    description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN));
                    latitude = cursor.getDouble(cursor.getColumnIndex(LATITUDE_COLUMN));
                    longitude = cursor.getDouble(cursor.getColumnIndex(LONGITUDE_COLUMN));
                    imageFile = cursor.getString(cursor.getColumnIndex(IMAGE_FILE_COLUMN));

                    favoritesList.add(new Restaurant(id, name, type, address, opening, closing,
                            review, description, latitude, longitude, imageFile));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return favoritesList;
    }

    /**
     * Método encargado de registrar un comentario en un restaurante.
     * @param c Datos de los comentarios.
     * @param r Restaurante asociado.
     */
    @Override
    public void registerCommentary(Commentary c, Restaurant r) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        // Configuramos los valores de las distintas columnas.
        ContentValues values = new ContentValues();
        values.put(USERNAME_COLUMN, c.getUsername());
        values.put(NAME_COLUMN, r.getName());
        values.put(ADDRESS_COLUMN, r.getAddress());
        values.put(TYPE_COLUMN, r.getType());
        values.put(COMMENTARY_COLUMN, c.getCommentary());

        // Llevamos a cabo la inserción en la base de datos.
        helper.getWritableDatabase().insert(COMMENTARIES_TABLE, null, values);
    }

    /**
     * Método encargado de obtener todos los comentarios de un restaurante.
     * @param r Restaurante asociado.
     * @return Listado de comentarios del restaurante.
     */
    @Override
    public List<Commentary> getAllComentaries(Restaurant r) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        List<Commentary> commentariesList = new ArrayList<>();
        String[] selectColumns = null;
        String whereClause = NAME_COLUMN + " =? AND " + TYPE_COLUMN + " =? AND "
                + ADDRESS_COLUMN + " =?";
        String[] whereArgs = {r.getName(), r.getType(), r.getAddress()};
        Cursor cursor = helper.getReadableDatabase().
                query(COMMENTARIES_TABLE, selectColumns, whereClause, whereArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id;
                String username, commentary;

                do {
                    id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                    username = cursor.getString(cursor.getColumnIndex(USERNAME_COLUMN));
                    commentary = cursor.getString(cursor.getColumnIndex(COMMENTARY_COLUMN));

                    commentariesList.add(new Commentary(id, username, commentary));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return commentariesList;
    }
}
