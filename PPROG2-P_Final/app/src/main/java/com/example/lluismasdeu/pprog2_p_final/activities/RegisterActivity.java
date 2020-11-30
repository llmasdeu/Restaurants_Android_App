package com.example.lluismasdeu.pprog2_p_final.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.model.StaticValues;
import com.example.lluismasdeu.pprog2_p_final.model.User;
import com.example.lluismasdeu.pprog2_p_final.repositories.DatabaseManagementInterface;
import com.example.lluismasdeu.pprog2_p_final.repositories.implementations.DatabaseManagement;
import com.example.lluismasdeu.pprog2_p_final.utils.GeneralUtilities;

/**
 * Actividad encargada del registro de usuario.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final int TAKE_PICTURE = 1;
    public static final String FEMALE_GENDER = "female";
    public static final String MALE_GENDER = "male";
    public static final String OTHER_GERNDER = "other";

    private DatabaseManagementInterface dbManagement;
    private ImageView profileImageView;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private EditText descriptionEditText;
    private CheckBox termsCheckBox;
    private TextView errorTextView;

    /**
     * Método encargado de llevar a cabo las tareas cuando se crea la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos el Layout de la actividad.
        setContentView(R.layout.activity_register);

        // Escondemos la ActionBar en esta actividad.
        getSupportActionBar().hide();

        // Escondemos el teclado por defecto.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Creamos la comunicación con la base de datos.
        dbManagement = new DatabaseManagement(getApplicationContext());

        // Localizamos los componentes en el Layout.
        profileImageView = (ImageView) findViewById(R.id.profile_imageView);
        nameEditText = (EditText) findViewById(R.id.name_editText);
        surnameEditText = (EditText) findViewById(R.id.surname_editText);
        usernameEditText = (EditText) findViewById(R.id.usernameRegister_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        passwordEditText = (EditText) findViewById(R.id.passwordRegister_editText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordRegister_editText);
        maleRadioButton = (RadioButton) findViewById(R.id.male_radioButton);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_radioButton);
        descriptionEditText = (EditText) findViewById(R.id.description_editText);
        termsCheckBox = (CheckBox) findViewById(R.id.terms_checkBox);
        errorTextView = (TextView) findViewById(R.id.errorRegister_textView);

        // Definimos la imagen de perfil predefinida.
        setDefaultProfilePhoto();
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
     * Método encargado de llevar a cabo las tareas cuando el usuario pulsa el botón para hacer la
     * foto de perfil.
     * @param view
     */
    public void onTakePictureButtonClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /**
     * Método encargado de llevar a cabo las tareas cuando el usuario pulsa el botón de registro de
     * usuario.
     * @param view
     */
    public void onRegisterUserButtonClick(View view) {
        String[] messages = getResources().getStringArray(R.array.register_activity_messages);

        if (String.valueOf(nameEditText.getText()).equals("")) {
            errorTextView.setText(messages[0]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (String.valueOf(surnameEditText.getText()).equals("")) {
            errorTextView.setText(messages[1]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (String.valueOf(usernameEditText.getText()).equals("")) {
            errorTextView.setText(messages[2]);
            errorTextView.setVisibility(View.VISIBLE);
        }else if (String.valueOf(emailEditText.getText()).equals("")) {
            errorTextView.setText(messages[3]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (!checkEmailField()) {
            errorTextView.setText(messages[4]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (String.valueOf(passwordEditText.getText()).equals("")) {
            errorTextView.setText(messages[5]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (!String.valueOf(passwordEditText.getText())
                .equals(String.valueOf(confirmPasswordEditText.getText()))) {
            errorTextView.setText(messages[6]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (!termsCheckBox.isChecked()) {
            errorTextView.setText(messages[7]);
            errorTextView.setVisibility(View.VISIBLE);
        } else if (dbManagement.existsUser(new User(String.valueOf(usernameEditText.getText()),
                String.valueOf(emailEditText.getText())), 2)) {
            errorTextView.setText(messages[8]);
            errorTextView.setVisibility(View.VISIBLE);
        } else {
            String imageFile = getImageFileName(), gender = getGender();

            // Comprobamos si los permisos de lectura/escritura en el almacenamiento externo se
            // encuentran habilitados.
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                imageFile = null;
            } else {
                // Guardamos la imagen de perfil.
                GeneralUtilities.saveProfilePicture(((BitmapDrawable) profileImageView.getDrawable())
                        .getBitmap(), imageFile);
            }

            User newUser = new User(String.valueOf(nameEditText.getText()),
                    String.valueOf(surnameEditText.getText()),
                    String.valueOf(usernameEditText.getText()),
                    String.valueOf(emailEditText.getText()),
                    String.valueOf(passwordEditText.getText()),
                    gender, String.valueOf(descriptionEditText.getText()), imageFile);
            dbManagement.registerUser(newUser);

            // Obtenemos los parámetros completos del usuario conectado.
            User connectedUser = dbManagement.getConnectedUser(
                    new User(String.valueOf(usernameEditText.getText()),
                            String.valueOf(emailEditText.getText()),
                            String.valueOf(passwordEditText.getText())));

            // Guardamos el usuario actual.
            StaticValues.getInstance().setConnectedUser(connectedUser);

            // Mostramos mensaje por pantalla.
            Toast.makeText(this, messages[9], Toast.LENGTH_SHORT).show();

            // Accedemos a la actividad de búsqueda.
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            // Reseteamos los componentes.
            resetComponents();
        }
    }

    /**
     * Método encargado de controlar los resultados de los Intents.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap image = (Bitmap) bundle.get("data");
                    profileImageView.setImageBitmap(image);
                }
                break;
        }
    }

    /**
     * Método encargado de cargar la imagen de perfil por defecto.
     */
    private void setDefaultProfilePhoto() {
        Bitmap image = GeneralUtilities.getInstance(this).getDefaultProfilePhoto();

        if (image != null)
            profileImageView.setImageBitmap(image);
    }

    /**
     * Método encargado de comprobar que la dirección de correo electrónico es válida.
     * @return CIERTO si es válida. FALSO en caso contrario.
     */
    private boolean checkEmailField() {
        int emailCharacters = 0;

        // Comprobamos que exista sólo un carácter '@' en la dirección.
        for (int i = 0 ; i < String.valueOf(emailEditText.getText()).length(); i++) {
            if (String.valueOf(emailEditText.getText()).charAt(i) == '@')
                emailCharacters++;
        }

        return emailCharacters == 1;
    }

    /**
     * Método encargado de generar el nombre del fichero de la imagen de perfil.
     * @return Nombre del fichero de la imagen de perfil.
     */
    private String getImageFileName() {
        StringBuilder builder = new StringBuilder();
        builder.append("img_").append(GeneralUtilities.getNumberProfilePictures() + 1).append(".jpg");

        return builder.toString();
    }

    /**
     * Método encargado de obtener el género del usuario.
     * @return Género del usuario.
     */
    private String getGender() {
        String gender = "";

        if (femaleRadioButton.isChecked()) {
            gender = FEMALE_GENDER;
        } else if (maleRadioButton.isChecked()) {
            gender = MALE_GENDER;
        } else {
            gender = OTHER_GERNDER;
        }

        return gender;
    }

    /**
     * Método encargado de resetear la interfaz gráfica de la actividad.
     */
    private void resetComponents() {
        nameEditText.setText("");
        surnameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        maleRadioButton.setChecked(false);
        femaleRadioButton.setChecked(false);
        descriptionEditText.setText("");
        termsCheckBox.setChecked(false);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

        setDefaultProfilePhoto();
    }
}
