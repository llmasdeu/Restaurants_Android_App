package com.example.lluismasdeu.pprog2_p_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lluismasdeu.pprog2_p_final.R;

import java.util.List;

/**
 * Adapter de la ListView de las búsquedas recientes.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class RecentSearchesAdapter extends BaseAdapter {
    private static final String TAG = "RecentSearchesAdapter";

    private Context context;
    private List<String> recentSearchesList;

    /**
     * Constructor del adapter.
     * @param context
     * @param recentSearchesList
     */
    public RecentSearchesAdapter(Context context, List<String> recentSearchesList) {
        this.context = context;
        this.recentSearchesList = recentSearchesList;
    }

    /**
     * Getter del número de entradas.
     * @return Número de entradas.
     */
    @Override
    public int getCount() {
        if (recentSearchesList == null)
            return 0;

        return recentSearchesList.size();
    }

    /**
     * Getter del objeto actual.
     * @param position Posición actual.
     * @return Objeto actual.
     */
    @Override
    public Object getItem(int position) {
        return recentSearchesList.get(position);
    }

    /**
     * Getter del identificador del objeto actual.
     * @param position Posición actual.
     * @return Identificador del objeto actual.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Getter de la vista del objeto actual.
     * @param position
     * @param convertView
     * @param parent
     * @return Vista del objeto actual.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recent_searches, parent, false);

        TextView recentSearchTextView = (TextView) view.findViewById(R.id.recentSearch_textView);
        recentSearchTextView.setText((String) getItem(position));

        return view;
    }

    /**
     * Setter de la lista de búsquedas recientes.
     * @param recentSearchesList Lista de búsquedas recientes.
     */
    public void setRecentSearchesList(List<String> recentSearchesList) {
        this.recentSearchesList = recentSearchesList;
    }
}
