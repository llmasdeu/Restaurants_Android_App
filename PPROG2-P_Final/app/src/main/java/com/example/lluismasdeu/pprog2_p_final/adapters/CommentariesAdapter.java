package com.example.lluismasdeu.pprog2_p_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.model.Commentary;

import java.util.List;

/**
 * Adapter de la ListView de los comentarios de los restaurantes.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class CommentariesAdapter extends BaseAdapter {
    private static final String TAG = "CommentariesAdapter";

    private Context context;
    private List<Commentary> commentariesList;

    /**
     * Constructor del Adapter.
     * @param context
     * @param commentariesList
     */
    public CommentariesAdapter(Context context, List<Commentary> commentariesList) {
        this.context = context;
        this.commentariesList = commentariesList;
    }

    /**
     * Getter del número de entradas.
     * @return Número de entradas.
     */
    @Override
    public int getCount() {
        return commentariesList.size();
    }

    /**
     * Getter del objeto actual.
     * @param i Posición actual.
     * @return Objeto actual.
     */
    @Override
    public Object getItem(int i) {
        return commentariesList.get(i);
    }

    /**
     * Getter del identificador del objeto actual.
     * @param i Posición actual.
     * @return Identificador del objeto actual.
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Getter de la vista del objeto actual.
     * @param i
     * @param view
     * @param viewGroup
     * @return Vista del objeto actual.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_commentaries,viewGroup,false);

        Commentary commentary = commentariesList.get(i);

        TextView usernameTextView = (TextView) view.findViewById(R.id.username_textView);
        usernameTextView.setText(commentary.getUsername());

        TextView commentaryTextView = (TextView) view.findViewById(R.id.commentary_textView);
        commentaryTextView.setText(commentary.getCommentary());

        return view;
    }

    /**
     * Setter de la lista de comentarios.
     * @param commentariesList Lista de comentarios.
     */
    public void setCommentariesList(List<Commentary> commentariesList) {
        this.commentariesList = commentariesList;
    }
}
