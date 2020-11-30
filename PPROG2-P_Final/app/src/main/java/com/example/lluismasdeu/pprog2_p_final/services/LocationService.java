package com.example.lluismasdeu.pprog2_p_final.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Clase encargada de gestionar la geolocalización de la aplicación.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class LocationService {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 12345;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private static LocationService instance = null;
    private Context context;
    private Location location;
    private LocationManager locationManager;
    private LocationListener networkListener;
    private LocationListener gpsListener;

    /**
     * Constructor de la clase.
     * @param context
     */
    private LocationService(Context context) {
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Definimos los listeners.
        networkListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateCurrentLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        gpsListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateCurrentLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    /**
     * Método encargado de obtener la instancia de la clase.
     * @param context
     * @return Instancia de la clase.
     */
    public static synchronized LocationService getInstance(Context context) {
        if (instance == null)
            instance = new LocationService(context);

        return instance;
    }

    /**
     * Getter de la localización actual.
     * @return Localización actual.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Método encargado de actualizar la ubicación actual.
     * @param location
     */
    private void updateCurrentLocation(Location location) {
        if (isBetterLocation(location, this.location)) {
            this.location = location;
            Log.d(LocationService.class.getName(), "LOCATION UPDATED: " + location);
        }
    }

    /**
     * Método encargado de registrar los listeners.
     * @param activity
     */
    public void registerListeners(Activity activity) {
        // Comprovamos los permisos. En caso de haber sido dados, los pedimos en ejecución.
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Peticion de los permisos
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        // Registramos nuestros metodos de callback para la localizacion por GPS y Network
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);

        // Intentamos recuperar la primera localizacion conocida.
        updateCurrentLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        updateCurrentLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    }

    /**
     * Método encargado de borrar los listeners.
     */
    public void unregisterListeners() {
        locationManager.removeUpdates(networkListener);
        locationManager.removeUpdates(gpsListener);
    }

    /**
     * Método encargado de comprobar si la localización indicada es mejor.
     * @param location Nueva localización.
     * @param currentBestLocation Localización actual.
     * @return CIERTO si es mejor. FALSO en caso contrario.
     */
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (location == null)
            return false;

        if (currentBestLocation == null)
            return true;

        // Comprobaremos mediante el tiempo de actualización.
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Comprobamos el grado de fidelidad.
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }

        return false;
    }

    /**
     * Método encargado de comprobar si son los mismos proveedores.
     * @param provider1
     * @param provider2
     * @return CIERTO si son los mismos proveedores. FALSO en caso contrario.
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
