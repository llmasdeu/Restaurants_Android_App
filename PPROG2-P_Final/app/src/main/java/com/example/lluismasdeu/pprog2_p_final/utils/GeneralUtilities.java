package com.example.lluismasdeu.pprog2_p_final.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Clase encargada de llevar a cabo tareas a lo largo de la aplicación.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class GeneralUtilities {
    private static final String TAG = "GeneralUtilities";
    private static final String PROFILE_PICTURES_FOLDER = "/profile_images/";
    private static final String RESTAUTANT_IMAGES_FOLDER = "restaurants";
    private static final String DEFAULT_PHOTO = "default_photo.jpg";

    private static GeneralUtilities instance = null;
    private static Context context;

    /**
     * Constructor de la clase.
     * @param context Context
     */
    private GeneralUtilities(Context context) {
        this.context = context;
    }

    /**
     * Método encargado de obtener la instancia de la clase.
     * @param context Context
     * @return Instancia de la clase.
     */
    public static GeneralUtilities getInstance(Context context) {
        if (instance == null)
            instance = new GeneralUtilities(context);

        instance.context = context;

        return instance;
    }

    /**
     * Método encargado de obtener el número de ficheros en la carpeta de imágenes.
     * @return Número de ficheros en la carpeta de imágenes.
     */
    public static int getNumberProfilePictures() {
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + PROFILE_PICTURES_FOLDER;
        File directory = new File(directoryPath);
        int numFiles = 0;

        if (!directory.exists())
            directory.mkdirs();

        File[] files = directory.listFiles();

        if (files != null)
            numFiles = files.length;

        return numFiles;
    }

    /**
     * Método encargado de guardar la imagen en el dispositivo.
     * @param image Imagen a guardar.
     * @param fileName Nombre del fichero.
     */
    public static void saveProfilePicture(Bitmap image, String fileName) {
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + PROFILE_PICTURES_FOLDER;
        File directory = new File(directoryPath);
        File file = new File(directory, fileName);
        FileOutputStream out = null;

        if (!directory.exists())
            directory.mkdirs();

        try {
            if (file.exists())
                file.delete();

            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método encargado de obtener la imagen de perfil por defecto decodificada.
     * @return Imagen de perfil por defecto decodificada.
     */
    public static Bitmap getDefaultProfilePhoto() {
        Bitmap defaultImage;

        try {
            // Cargamos el recurso.
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(DEFAULT_PHOTO);

            // Decodificamos la imagen, y la colocamos en el componente.
            defaultImage = BitmapFactory.decodeStream(inputStream);

            // Cerramos
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            defaultImage = null;
        }

        return defaultImage;
    }

    /**
     * Método encargado de obtener la imagen de perfil indicada.
     * @param imageFile Nombre de la imagen de perfil a buscar.
     * @return Imagen de perfil decodificada.
     */
    public static Bitmap getProfilePhoto(String imageFile) {
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + PROFILE_PICTURES_FOLDER;
        FileInputStream inputStream = null;
        Bitmap image = null;

        try {
            // Cargamos el recurso.
            File directory = new File(directoryPath);
            File file = new File(directory, imageFile);
            inputStream = new FileInputStream(file);

            // Decodificamos la imagen, y la colocamos en el componente.
            image = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return image;
    }

    /**
     * Método encargado de obtener el nombre de la imagen del restaurante.
     * @return Nombre de la imagen del restaurante.
     */
    public static String getRestaurantImageFileName() {
        String restaurantImageFileName = "";

        try {
            // Leemos la carpeta de Assets, y listamos los nombres de ficheros de la carpeta.
            AssetManager assetManager = context.getAssets();
            String[] files = assetManager.list(RESTAUTANT_IMAGES_FOLDER);

            if (files.length >= 1) {
                Random random = new Random();
                int imageId = random.nextInt(files.length);
                restaurantImageFileName = files[imageId];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return restaurantImageFileName;
    }

    /**
     * Método encargado de obtener la imagen del restaurante.
     * @param restaurantImageFileName Nombre de la imagen del restaurante.
     * @return Imagen del restaurante.
     */
    public static Bitmap getRestaurantImage(String restaurantImageFileName) {
        Bitmap restaurantImage = null;

        try {
            // Leemos la carpeta de Assets.
            AssetManager assetManager = context.getAssets();

            // Leemos el fichero indicado.
            InputStream inputStream = assetManager.open(RESTAUTANT_IMAGES_FOLDER + "/"
                    + restaurantImageFileName);

            // Decodificamos la imagen, y la colocamos en el componente.
            restaurantImage = BitmapFactory.decodeStream(inputStream);

            // Cerramos
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return restaurantImage;
    }
}
