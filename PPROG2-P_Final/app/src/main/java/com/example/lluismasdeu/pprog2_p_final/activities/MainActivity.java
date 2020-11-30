package com.example.lluismasdeu.pprog2_p_final.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.model.StaticValues;
import com.example.lluismasdeu.pprog2_p_final.model.User;
import com.example.lluismasdeu.pprog2_p_final.repositories.implementations.DatabaseManagement;
import com.example.lluismasdeu.pprog2_p_final.repositories.DatabaseManagementInterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * Actividad principal de la aplicación.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String APP_LOGO = "app_logo.png";
    private static final int PERMISSIONS_ALL = 12345;

    private DatabaseManagementInterface dbManagement;
    private ImageView logoImageView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView errorTextView;

    /**
     * Método encargado de llevar a cabo las tareas cuando se crea la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos el Layout de la actividad.
        setContentView(R.layout.activity_main);

        // Escondemos la ActionBar en esta actividad.
        getSupportActionBar().hide();

        // Escondemos el teclado por defecto.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Creamos la comunicación con la base de datos.
        dbManagement = new DatabaseManagement(getApplicationContext());

        // Localizamos los componentes en el Layout.
        logoImageView = (ImageView) findViewById(R.id.logo_imageView);
        usernameEditText = (EditText) findViewById(R.id.usernameLogin_editText);
        passwordEditText = (EditText) findViewById(R.id.password_editText);
        errorTextView = (TextView) findViewById(R.id.errorLogin_textView);

        // Añadimos el logo de la aplicación.
        setAppLogo();
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando se inicia la actividad.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_ALL);
        }
    }

    /**
     * Método encargado de guardar el estado de la actividad.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Método encargado de reestablecer el estado de la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Método encargado de controlar los resultados de la petición de los permisos.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_ALL:
                if (grantResults.length >= 3) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        notifyWriteStoragePermissionsDenied();
                    }

                    if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                        notifyReadStoragePermissionsDenied();
                    }
                }
                break;
        }
    }

    /**
     * Método encargado de notificar que los permisos de lectura del almacenamiento externo no se
     * encuentran habilitados.
     */
    private void notifyReadStoragePermissionsDenied() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.write_storage_permissions_denied_title))
                .setMessage(getString(R.string.write_storage_permissions_denied_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * Método encargado de notificar que los permisos de escritura del almacenamiento externo no se
     * encuentran habilitados.
     */
    private void notifyWriteStoragePermissionsDenied() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.write_storage_permissions_denied_title))
                .setMessage(getString(R.string.write_storage_permissions_denied_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando el usuario pula el botón de inicio de
     * sesión.
     * @param view
     */
    public void onLoginButtonClick(View view) {
        String[] messages = getResources().getStringArray(R.array.main_activity_messages);

        if (String.valueOf(usernameEditText.getText()).equals("")) {
            errorTextView.setText(messages[0]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (String.valueOf(passwordEditText.getText()).equals("")) {
            errorTextView.setText(messages[1]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (!dbManagement.existsUser(
                new User(String.valueOf(usernameEditText.getText()),
                        String.valueOf(usernameEditText.getText()),
                        String.valueOf(passwordEditText.getText())), 1)) {
            errorTextView.setText(messages[2]);
            errorTextView.setVisibility(View.VISIBLE);
        } else {
            // Obtenemos los parámetros completos del usuario conectado.
            User connectedUser = dbManagement.getConnectedUser(
                    new User(String.valueOf(usernameEditText.getText()),
                            String.valueOf(usernameEditText.getText()),
                            String.valueOf(passwordEditText.getText())));

            // Guardamos el usuario actual.
            StaticValues.getInstance().setConnectedUser(connectedUser);

            // Mostramos mensaje por pantalla.
            Toast.makeText(this, messages[3], Toast.LENGTH_SHORT).show();

            // Accedemos a la actividad de búsqueda.
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            // Reseteamos los componentes.
            resetComponents();
        }
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando el usuario pulsa el botón de registro de
     * usuario.
     * @param view
     */
    public void onRegisterButtonClick(View view) {
        // Accedemos a la actividad de registro.
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

        // Reseteamos los componentes.
        resetComponents();
    }

    /**
     * Método encargada de posicionar el logo de la aplicación.
     */
    private void setAppLogo() {
        InputStream inputStream = null;

        try {
            // Cargamos el recurso.
            AssetManager assetManager = getAssets();
            inputStream = assetManager.open(APP_LOGO);

            // Decodificamos la imagen, y la colocamos en el componente.
            Bitmap defaultImage = BitmapFactory.decodeStream(inputStream);
            logoImageView.setImageBitmap(defaultImage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método encargado de resetear la interfaz gráfica de la actividad.
     */
    private void resetComponents() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
    }
}
