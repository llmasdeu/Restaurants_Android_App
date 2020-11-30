package com.example.lluismasdeu.pprog2_p_final.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.activities.DescriptionActivity;
import com.example.lluismasdeu.pprog2_p_final.adapters.RestaurantsAdapter;
import com.example.lluismasdeu.pprog2_p_final.model.Restaurant;
import com.example.lluismasdeu.pprog2_p_final.model.StaticValues;

import java.util.List;

/**
 * Fragment encargado de gestionar el listado de todos los restaurantes abiertos.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class OpenRestaurantsListFragment extends Fragment {
    private static final String TAG = "OpenRestaurantsListFragment";

    private ListView restaurantsListView;
    private RestaurantsAdapter adapter;
    private List<Restaurant> openRestaurantsList;

    /**
     * Constructor.
     * @param openRestaurantsList Listado de restaurantes abiertos.
     */
    public OpenRestaurantsListFragment(List<Restaurant> openRestaurantsList) {
        this.openRestaurantsList = openRestaurantsList;
    }

    /**
     * Método encargado de crear el Fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        // Recuperamos el componente gráfico para poder asignarle un adapter.
        restaurantsListView = (ListView) view.findViewById(R.id.restaurants_listView);

        adapter = new RestaurantsAdapter(getActivity(), openRestaurantsList);
        restaurantsListView.setAdapter(adapter);

        // Asignamos el listener a la ListView.
        restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant selectedRestaurant = openRestaurantsList.get(i);
                showInformation(selectedRestaurant);
            }
        });

        return view;
    }

    /**
     * Método encargado de actualizar la lista.
     * @param openRestaurantsList Lista actualizada de los restaurantes abiertos.
     */
    public void updateRestaurantsList(List<Restaurant> openRestaurantsList) {
        this.openRestaurantsList = openRestaurantsList;

        adapter = new RestaurantsAdapter(getActivity(), openRestaurantsList);
        adapter.notifyDataSetChanged();
        restaurantsListView.setAdapter(adapter);
    }

    /**
     * Método encargado de gestionar la transición de la actividad.
     * @param selectedRestaurant Restaurante seleccionado.
     */
    private void showInformation(Restaurant selectedRestaurant) {
        // Guardamos los datos del restaurante seleccionado.
        StaticValues.getInstance().setSelectedRestaurant(selectedRestaurant);

        // Accedemos a la actividad de la descripción del restaurante.
        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
        startActivity(intent);
    }
}
