package com.example.lluismasdeu.pprog2_p_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.model.Restaurant;

import java.util.List;

/**
 * Adapter de los restaurantes.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class RestaurantsAdapter extends BaseAdapter {
    private static final String TAG = "RestaurantsAdapter";

    private Context context;
    private List<Restaurant> restaurantList;

    /**
     * Constructor del adapter.
     * @param context
     * @param restaurantList Lista de restaurantes.
     */
    public RestaurantsAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    /**
     * Getter del total de restaurantes.
     * @return Total de restaurantes.
     */
    @Override
    public int getCount() {
        return restaurantList.size();
    }

    /**
     * Getter del restaurante en la posición actual.
     * @param position Posición actual.
     * @return Restaurante en la posición actual.
     */
    @Override
    public Object getItem(int position) {
        return restaurantList.get(position);
    }

    /**
     * Getter del identificador numérico del restaurante en la posición actual.
     * @param i Posición actual.
     * @return Identificador numérico del restaurante en la posición actual.
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Getter de la vista del restaurante en la posición actual.
     * @param position Posición actual.
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_results, parent, false);

        Restaurant restaurant = restaurantList.get(position);

        ImageView restaurantImage = (ImageView) itemView.findViewById(R.id.listview_cell_image);
        restaurantImage.setImageBitmap(restaurant.getImage());

        TextView title = (TextView) itemView.findViewById(R.id.resultTitle_textView);
        title.setText((restaurant.getName()));

        TextView address = (TextView) itemView.findViewById(R.id.resultAddress_textView);
        address.setText(restaurant.getAddress());

        RatingBar rate = (RatingBar) itemView.findViewById(R.id.restaurantRating_ratingBar);
        rate.setRating((float) restaurant.getRating());

        return itemView;
    }
}
