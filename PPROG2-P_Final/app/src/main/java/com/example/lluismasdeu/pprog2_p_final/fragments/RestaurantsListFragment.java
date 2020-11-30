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
 * Fragment encargado de gestionar el listado de todos los restaurantes.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class RestaurantsListFragment extends Fragment {
    private static final String TAG = "RestaurantsListFragment";

    private ListView restaurantsListView;
    private RestaurantsAdapter restaurantsAdapter;
    private List<Restaurant> restaurantsList;

    /**
     * Constructor.
     * @param restaurantsList Listado de restaurantes.
     */
    public RestaurantsListFragment(List<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;
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

        // Recuperamos el componente gráfico para poder asignarle un restaurantsAdapter.
        restaurantsListView = (ListView) view.findViewById(R.id.restaurants_listView);

        restaurantsAdapter = new RestaurantsAdapter(getActivity(), restaurantsList);
        restaurantsListView.setAdapter(restaurantsAdapter);

        // Asignamos el listener a la ListView.
        restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant selectedRestaurant = restaurantsList.get(i);
                showInformation(selectedRestaurant);
            }
        });

        return view;
    }

    /**
     * Método encargado de actualizar la lista.
     * @param restaurantsList Lista actualizada de los restaurantes.
     */
    public void updateRestaurantsList(List<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;

        restaurantsAdapter = new RestaurantsAdapter(getActivity(), restaurantsList);
        restaurantsAdapter.notifyDataSetChanged();
        restaurantsListView.setAdapter(restaurantsAdapter);
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
