package com.example.lluismasdeu.pprog2_p_final.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Adapter de las pestañas.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class TabsAdapter extends FragmentPagerAdapter {
    private static final String TAG = "TabsAdapter";

    private List<TabEntry> entries;

    /**
     * Clase encargada de gestionar una pestaña.
     */
    public static class TabEntry {
        private Fragment fragment;
        private String name;

        /**
         * Constructor de la clase.
         * @param fragment Fragment de la pestaña.
         * @param name Nombre de la pestaña.
         */
        public TabEntry(Fragment fragment, String name) {
            this.fragment = fragment;
            this.name = name;
        }

        /**
         * Getter del Fragment de la pestaña.
         * @return Fragment de la pestaña.
         */
        public Fragment getFragment() {
            return fragment;
        }

        /**
         * Getter del nombre de la pestaña.
         * @return Nombre de la pestaña.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Constructor del adapter.
     * @param fm FragmentManager.
     * @param entries Listado con las pestañas.
     */
    public TabsAdapter(FragmentManager fm, List<TabEntry> entries) {
        super(fm);
        this.entries = entries;
    }

    /**
     * Getter de la pestaña en la posición actual.
     * @param position Posición actual.
     * @return Objeto en la posición actual.
     */
    @Override
    public Fragment getItem(int position) {
        return entries.get(position).getFragment();
    }

    /**
     * Getter del total de pestañas.
     * @return Total de pestañas.
     */
    @Override
    public int getCount() {
        return entries.size();
    }

    /**
     * Getter del nombre de la pestaña.
     * @param position Posición actual.
     * @return Nombre de la pestaña.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return entries.get(position).getName();
    }
}
