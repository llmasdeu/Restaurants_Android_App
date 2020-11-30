package com.example.lluismasdeu.pprog2_p_final.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.adapters.RecentSearchesAdapter;
import com.example.lluismasdeu.pprog2_p_final.repositories.DatabaseManagementInterface;
import com.example.lluismasdeu.pprog2_p_final.repositories.implementations.DatabaseManagement;
import com.example.lluismasdeu.pprog2_p_final.services.LocationService;
import com.example.lluismasdeu.pprog2_p_final.utils.HttpRequestHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad de búsqueda de la aplicación.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    public static final String SEARCH_RESULT_EXTRA = "search_result";

    private Context context;
    private DatabaseManagementInterface dbManagement;
    private RecentSearchesAdapter adapter;
    private EditText searchEditText;
    private SeekBar radiusSeekBar;
    private TextView radiusKmTextView;
    private LinearLayout nothingToShowLinearLayout;
    private ListView recentSearchesListView;
    private List<String> recentSearchesList;
    private int radiusKm;

    /**
     * Clase encargada de llevar a cabo las tareas asíncronas.
     */
    private class AsyncRequest extends AsyncTask<String, Void, JSONArray> {
        private Context context;
        private ProgressDialog progressDialog;

        /**
         * Constructor de la clase.
         * @param context
         */
        public AsyncRequest(Context context) {
            this.context = context;

            progressDialog = new ProgressDialog(context);
        }

        /**
         * Método encargado de llevar a cabo tareas antes de ejecutar las asíncronas.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
        }

        /**
         * Método encargado de llevar a cabo tareas asíncronas.
         * @param params
         * @return JSON de respuesta del Webservice.
         */
        @Override
        protected JSONArray doInBackground(String... params) {
            return HttpRequestHelper.getInstance().doHttpRequest(params[0]);
        }

        /**
         * Método encargado de llevar a cabo tareas después de ejecutar las asíncronas.
         * @param jsonArray JSON de respuesta del Webservice.
         */
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            manageActivityTransition(jsonArray.toString(), jsonArray.length());
        }
    }

    /**
     * Método encaragado de llevar a cabo las tareas cuando se crea la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos el Layout de la actividad.
        setContentView(R.layout.activity_search);

        // Escondemos la ActionBar en esta actividad.
        getSupportActionBar().setTitle("");

        // Escondemos el teclado por defecto.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Localizamos los componentes en el layout.
        searchEditText = (EditText) findViewById(R.id.search_editText);
        radiusSeekBar = (SeekBar) findViewById(R.id.radius_seekBar);
        radiusKmTextView = (TextView) findViewById(R.id.radius_textView);
        nothingToShowLinearLayout = (LinearLayout) findViewById(R.id.nothingToShow_linearLayout);
        recentSearchesListView = (ListView) findViewById(R.id.recentSearches_listView);

        // Añadimos el listener a la SeekBar.
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0 && progress <= 10) {
                    radiusKm = 1;
                } else if (progress > 10 && progress <= 20) {
                    radiusKm = 2;
                } else if (progress > 20 && progress <= 30) {
                    radiusKm = 3;
                } else if (progress > 30 && progress <= 40) {
                    radiusKm = 4;
                } else if (progress > 40 && progress <= 50) {
                    radiusKm = 5;
                } else if (progress > 50 && progress <= 60) {
                    radiusKm = 6;
                } else if (progress > 60 && progress <= 70) {
                    radiusKm = 7;
                } else if (progress > 70 && progress <= 80) {
                    radiusKm = 8;
                } else if (progress > 80 && progress <= 90) {
                    radiusKm = 9;
                } else {
                    radiusKm = 10;
                }

                radiusKmTextView.setText(radiusKm + getString(R.string.radius_km));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Guardamos el contexto.
        context = this;

        // Inicializamos el adapter, y lo añadimos el adapter a la ListView.
        adapter = new RecentSearchesAdapter(this, new ArrayList<String>());
        recentSearchesListView.setAdapter(adapter);

        // Listener para la ListView.
        recentSearchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AsyncRequest(context).execute(HttpRequestHelper.getInstance()
                        .generateHTTPSearchRequest(recentSearchesList.get(i)));
            }
        });

        // Inicializamos el gestor de la base de datos.
        dbManagement = new DatabaseManagement(this);
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando se inicia la actividad.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Iniciamos a 0 la barra de progreso de la SeekBar, y configuramos el mensaje del radio a
        // 1 Km.
        radiusKm = 1;
        radiusSeekBar.setProgress(0);
        radiusKmTextView.setText(radiusKm + getString(R.string.radius_km));

        // Actualizamos la lista de búsquedas recientes.
        updateRecentSearchesList();

        // Inicializamos la clase encargada de gestionar la geolocalización.
        LocationService locationService = LocationService.getInstance(getApplicationContext());
        locationService.registerListeners(this);
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando se reanuda la actividad.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Actualizamos la lista de búsquedas recientes.
        updateRecentSearchesList();
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando se para la actividad.
     */
    @Override
    protected void onStop() {
        super.onStop();

        // Cerramos la clase encargada de gestionar la geolocalización.
        LocationService locationService = LocationService.getInstance(getApplicationContext());
        locationService.unregisterListeners();
    }

    /**
     * Método encargado de guardar el estado actual de la actividad.
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * Método encargado de reestablecer el estado actual de la actividad.
     * @param savedInstanceState
     * @param persistentState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    /**
     * Método encargado de añadir el menú en la actividad.
     * @param menu Menú a añadir.
     * @return CIERTO.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        //Mostramos ActionBar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return true;
    }

    /**
     * Método encargado de gestionar cuando el usuario pulsa el botón de búsqueda mediante
     * geolocalización.
     * @param view
     */
    public void onLocationSearchButtonClick(View view) {
        String[] messages = getResources().getStringArray(R.array.search_activity_messages);

        // Obtenemos la localización actual.
        Location location = LocationService.getInstance(getApplicationContext()).getLocation();

        if (location == null) {
            Toast.makeText(this, messages[2], Toast.LENGTH_SHORT).show();
        } else {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            new AsyncRequest(this).execute(HttpRequestHelper.getInstance()
                    .generateHTTPLocationRequest(latitude, longitude, radiusKm));
        }
    }

    /**
     * Método encargado de gestionar cuando el usuario pulsa el botón para borrar el campo de
     * búsqueda.
     * @param view
     */
    public void onClearButtonClick(View view) {
        // Limpiamos el campo de búsqueda.
        searchEditText.setText("");
    }

    /**
     * Método encargado de gestionar cuando el usuario pulsa el botón para realizar la búsqueda.
     * @param view
     */
    public void onSearchButtonClick(View view) {
        String[] messages = getResources().getStringArray(R.array.search_activity_messages);

        if (String.valueOf(searchEditText.getText()).equals("")) {
            Toast.makeText(this, messages[0], Toast.LENGTH_SHORT).show();
        } else {
            new AsyncRequest(this)
                    .execute(HttpRequestHelper.getInstance()
                            .generateHTTPSearchRequest(String.valueOf(searchEditText.getText())));
        }
    }

    /**
     * Método encargado de gestionar los ítems del menú.
     * @param item
     * @return CIERTO.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                // Intent para ingresar al perfil
                Intent intentPerfil = new Intent(this, ProfileActivity.class);
                startActivity(intentPerfil);
                break;

            case R.id.action_viewProfile_favorites:
                // Intent para ingresar a favoritos
                Intent intentFavorite = new Intent(this, FavoritesActivity.class);
                startActivity(intentFavorite);
                break;

            default:
                // Nada.
                break;
        }

        return true;
    }

    /**
     * Método encargado de gestionar la transición hacia la actividad de resultados.
     * @param response Respuesta del Webservice.
     * @param numResults Número de resultados.
     */
    private void manageActivityTransition(String response, int numResults) {
        String[] messages = getResources().getStringArray(R.array.search_activity_messages);

        if (numResults <= 0) {
            // Limpiamos el campo de búsqueda.
            searchEditText.setText("");

            Toast.makeText(this, messages[1], Toast.LENGTH_SHORT).show();
        } else {
            // Registramos la búsqueda reciente, si no estaba registrada previamente.
            if (!dbManagement.existsRecentSearch(String.valueOf(searchEditText.getText())))
                dbManagement.registerRecentSearch(String.valueOf(searchEditText.getText()));

            // Mostramos mensaje
            Toast.makeText(this, messages[3], Toast.LENGTH_SHORT).show();

            // Reiniciamos la interfaz gráfica.
            resetFields();

            // Accedemos a la actividad de resultados.
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra(SEARCH_RESULT_EXTRA, response);
            startActivity(intent);
        }
    }

    /**
     * Método encargado de actualizar el listado de las búsquedas recientes.
     */
    private void updateRecentSearchesList() {
        recentSearchesList = dbManagement.getAllRecentSearches();

        if (recentSearchesList.size() >= 1) {
            nothingToShowLinearLayout.setVisibility(View.GONE);
            recentSearchesListView.setVisibility(View.VISIBLE);

            adapter.setRecentSearchesList(recentSearchesList);
            adapter.notifyDataSetChanged();
        } else {
            nothingToShowLinearLayout.setVisibility(View.VISIBLE);
            recentSearchesListView.setVisibility(View.GONE);
        }
    }

    /**
     * Método encargado de limpiar los campos.
     */
    private void resetFields() {
        searchEditText.setText("");
        radiusKm = 1;
        radiusSeekBar.setProgress(0);
        radiusKmTextView.setText(radiusKm + getString(R.string.radius_km));
    }
}
