package com.example.lluismasdeu.pprog2_p_final.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.adapters.CommentariesAdapter;
import com.example.lluismasdeu.pprog2_p_final.model.Commentary;
import com.example.lluismasdeu.pprog2_p_final.model.StaticValues;
import com.example.lluismasdeu.pprog2_p_final.repositories.DatabaseManagementInterface;
import com.example.lluismasdeu.pprog2_p_final.repositories.implementations.DatabaseManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad de la descripción de restaurantes.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class DescriptionActivity extends AppCompatActivity {
    private static final String TAG = "DescriptionActivity";
    public static final String LATITUDE_EXTRA = "latitude";
    public static final String LONGITUDE_EXTRA = "longitude";

    private DatabaseManagementInterface dbManagement;
    private ColorStateList favoriteColor;
    private ColorStateList noFavoriteColor;
    private ImageView pictureImageView;
    private FloatingActionButton favoriteFloatingActionButton;
    private TextView nameTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView addressTextView;
    private TextView openingTextView;
    private TextView closingTextView;
    private RatingBar ratingRatingBar;
    private TextView descriptionTextView;
    private TextView nothingToShowTextView;
    private ListView commentariesListView;
    private EditText commentaryEditText;
    private List<Commentary> commentariesList;
    private CommentariesAdapter commentariesAdapter;

    /**
     * Método encaragado de llevar a cabo las tareas cuando se crea la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos el Layout de la actividad.
        setContentView(R.layout.activity_description);

        // Dejamos vacío el título de la actividad.
        getSupportActionBar().setTitle("");

        // Escondemos el teclado por defecto.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Localizamos los componentes en el Layout.
        pictureImageView = (ImageView) findViewById(R.id.restaurant_imageView);
        favoriteFloatingActionButton = (FloatingActionButton)
                findViewById(R.id.favorite_floatingActionButton);
        nameTextView = (TextView) findViewById(R.id.restaurantName_textView);
        latitudeTextView = (TextView) findViewById(R.id.latitude_textView);
        longitudeTextView = (TextView) findViewById(R.id.longitude_textView);
        addressTextView = (TextView) findViewById(R.id.address_textView);
        openingTextView = (TextView) findViewById(R.id.openingHour_textView);
        closingTextView = (TextView) findViewById(R.id.closingHour_textView);
        ratingRatingBar = (RatingBar) findViewById(R.id.restaurantRating_ratingBar);
        descriptionTextView = (TextView) findViewById(R.id.description_textView);
        nothingToShowTextView = (TextView) findViewById(R.id.nothingToShow_textView);
        commentariesListView = (ListView) findViewById(R.id.commentaries_listView);
        commentaryEditText = (EditText) findViewById(R.id.commentary_editText);

        commentariesListView.setNestedScrollingEnabled(true);

        // Iniciamos el conector con la base de datos.
        dbManagement = new DatabaseManagement(this);

        // Guardamos los colores de favorito y no favorito.
        favoriteColor = ColorStateList.valueOf(getResources().getColor(R.color
                .favoriteButtonColor));
        noFavoriteColor = ColorStateList.valueOf(getResources().getColor(R.color
                .noFavoriteButtonColor));

        // Creamos el Adapter de la lista de comentarios.
        commentariesAdapter = new CommentariesAdapter(this, new ArrayList<Commentary>());
        commentariesListView.setAdapter(commentariesAdapter);
    }

    /**
     * Método encaragado de llevar a cabo las tareas cuando se inicia la actividad.
     */
    @Override
    protected void onStart() {
        super.onStart();

        setRestaurantInfo();
        setFavoriteButtonColor();
        updateCommentariesList();
    }

    /**
     * Método encargado de guardar el estado actual de la actividad.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Método encargado de reestablecer el estado actual de la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Método encargado de añadir la ActionBar en la actividad.
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return true;
    }

    /**
     * Método encargado de controlar cuando el usuario pulsa los botones de la ActionBar.
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                resetFields();
                break;

            case R.id.action_viewProfile_favorites:
                intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                resetFields();
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * Método encargado de gestionar la transición cuando el usuario pulsa el botón de acceder al
     * mapa.
     * @param view
     */
    public void onMapClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra(LATITUDE_EXTRA,
                StaticValues.getInstance().getSelectedRestaurant().getLatitude());
        intent.putExtra(LONGITUDE_EXTRA,
                StaticValues.getInstance().getSelectedRestaurant().getLongitude());
        startActivity(intent);

        resetFields();
    }

    /**
     * Método encargado de añadir/borrar el restaurante de favoritos cuando el usuario pulsa el
     * botón correspondiente.
     * @param view
     */
    public void onAddFavoriteButtonClick(View view) {
        if (dbManagement.existsFavorite(StaticValues.getInstance().getConnectedUser(),
                StaticValues.getInstance().getSelectedRestaurant())) {
            dbManagement
                    .unregisterFavorite(StaticValues.getInstance().getConnectedUser(),
                            StaticValues.getInstance().getSelectedRestaurant());
            favoriteFloatingActionButton.setBackgroundTintList(noFavoriteColor);
        } else {
            dbManagement
                    .registerFavorite(StaticValues.getInstance().getConnectedUser(),
                            StaticValues.getInstance().getSelectedRestaurant());
            favoriteFloatingActionButton.setBackgroundTintList(favoriteColor);
        }
    }

    /**
     * Método encargado de añadir el comentario cuando el usuario pulsa el botón de enviar.
     * @param view
     */
    public void onClickSend(View view) {
        String[] messages = getResources().getStringArray(R.array.description_activity_messages);

        if (String.valueOf(commentaryEditText.getText()).equals("")) {
            Toast.makeText(this, messages[0], Toast.LENGTH_SHORT).show();
        } else {
            Commentary commentary = new Commentary(StaticValues.getInstance().getConnectedUser()
                    .getUsername(), String.valueOf(commentaryEditText.getText()));
            dbManagement.registerCommentary(commentary,
                    StaticValues.getInstance().getSelectedRestaurant());

            resetFields();
            Toast.makeText(this, messages[1], Toast.LENGTH_SHORT).show();

            updateCommentariesList();
        }
    }

    /**
     * Método encargado de mostrar la información del restaurante.
     */
    private void setRestaurantInfo() {
        pictureImageView.setImageBitmap(StaticValues.getInstance().getSelectedRestaurant()
                .getImage());
        nameTextView.setText(StaticValues.getInstance().getSelectedRestaurant().getName());
        latitudeTextView.setText(""
                + StaticValues.getInstance().getSelectedRestaurant().getLatitude());
        longitudeTextView.setText(""
                + StaticValues.getInstance().getSelectedRestaurant().getLongitude());
        addressTextView.setText(StaticValues.getInstance().getSelectedRestaurant().getAddress());
        openingTextView.setText(StaticValues.getInstance().getSelectedRestaurant().getOpening());
        closingTextView.setText(StaticValues.getInstance().getSelectedRestaurant().getClosing());
        ratingRatingBar.setRating((float) StaticValues.getInstance().getSelectedRestaurant()
                .getRating());

        if (StaticValues.getInstance().getSelectedRestaurant().getDescription() == null
                || StaticValues.getInstance().getSelectedRestaurant().getDescription().equals("")) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setText(StaticValues.getInstance().getSelectedRestaurant()
                    .getDescription());
            descriptionTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Método encargado de controlar el color del botón de favorito.
     */
    private void setFavoriteButtonColor() {
        if (dbManagement.existsFavorite(StaticValues.getInstance().getConnectedUser(),
                StaticValues.getInstance().getSelectedRestaurant())) {
            favoriteFloatingActionButton.setBackgroundTintList(favoriteColor);
        } else {
            favoriteFloatingActionButton.setBackgroundTintList(noFavoriteColor);
        }
    }

    /**
     * Método encargado de actualizar la lista de comentarios del restaurante.
     */
    private void updateCommentariesList() {
        commentariesList = dbManagement.getAllComentaries(StaticValues.getInstance()
                .getSelectedRestaurant());

        if (commentariesList.size() >= 1) {
            nothingToShowTextView.setVisibility(View.GONE);
            commentariesListView.setVisibility(View.VISIBLE);

            commentariesAdapter.setCommentariesList(commentariesList);
            commentariesAdapter.notifyDataSetChanged();
        } else {
            nothingToShowTextView.setVisibility(View.VISIBLE);
            commentariesListView.setVisibility(View.GONE);
        }
    }

    /**
     * Método encargado de limpiar los campos.
     */
    private void resetFields() {
        commentaryEditText.setText("");
    }
}
