package com.example.lluismasdeu.pprog2_p_final.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lluismasdeu.pprog2_p_final.R;
import com.example.lluismasdeu.pprog2_p_final.model.StaticValues;
import com.example.lluismasdeu.pprog2_p_final.model.User;
import com.example.lluismasdeu.pprog2_p_final.repositories.DatabaseManagementInterface;
import com.example.lluismasdeu.pprog2_p_final.repositories.implementations.DatabaseManagement;
import com.example.lluismasdeu.pprog2_p_final.utils.GeneralUtilities;

/**
 * Actividad del perfil.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int TAKE_PICTURE = 1;

    private DatabaseManagementInterface dbManagement;
    private ImageView profileImageView;
    private EditText nameEditText;
    private EditText surnameEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private EditText descriptionEditText;
    private Button takePictureButton;
    private Menu activityMenu;
    private boolean editMode;

    /**
     * Método encaragado de llevar a cabo las tareas cuando se crea la actividad.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("");

        dbManagement = new DatabaseManagement(this);

        profileImageView = (ImageView) findViewById(R.id.profile_imageView);
        nameEditText = (EditText) findViewById(R.id.name_editText);
        surnameEditText = (EditText) findViewById(R.id.surname_editText);
        maleRadioButton = (RadioButton) findViewById(R.id.male_radioButton);
        femaleRadioButton = (RadioButton) findViewById(R.id.female_radioButton);
        descriptionEditText = (EditText) findViewById(R.id.description_editText);
        takePictureButton = (Button) findViewById(R.id.Button_picture);

        setInformation();
        editMode = false;
        enableFields();
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
     * Método encargado de añadir el menú a la actividad.
     * @param menu
     * @return TRUE
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        activityMenu = menu;
        updateActionBar();

        return true;
    }

    /**
     * Método encargado de controlar los botones del menú.
     * @param item
     * @return TRUE
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent favoritesIntent;

        switch (item.getItemId()) {
            case R.id.action_editProfile:
                editMode = true;
                updateActionBar();
                enableFields();
                break;

            case R.id.action_saveProfile:
                editMode = false;
                saveProfile();
                updateActionBar();
                enableFields();
                break;

            case R.id.action_viewProfile_favorites:
                editMode = false;
                setInformation();
                updateActionBar();
                enableFields();

                favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                break;

            case R.id.action_editProfile_favorites:
                editMode = false;
                setInformation();
                updateActionBar();
                enableFields();

                favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * Método encargado de mostrar la información del usuario por pantalla.
     */
    private void setInformation() {
        User connectedUser = StaticValues.getInstance().getConnectedUser();

        nameEditText.setText(connectedUser.getName());
        surnameEditText.setText(connectedUser.getSurname());
        descriptionEditText.setText(connectedUser.getDescription());

        if (connectedUser.getGender().equals(RegisterActivity.MALE_GENDER)) {
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        } else if (connectedUser.getGender().equals(RegisterActivity.FEMALE_GENDER)) {
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(true);
        } else {
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(false);
        }

        Bitmap image = GeneralUtilities.getInstance(this).getDefaultProfilePhoto();

        if (StaticValues.getInstance().getConnectedUser().getImageFile() != null)
            image = GeneralUtilities.getInstance(this)
                    .getProfilePhoto(StaticValues.getInstance().getConnectedUser().getImageFile());

        if (image != null)
            profileImageView.setImageBitmap(image);
    }

    /**
     * Método encargado de habilitar/inhabilitar los campos del formulario.
     */
    private void enableFields() {
        if (editMode) {
            takePictureButton.setVisibility(View.VISIBLE);
        } else {
            setInformation();
            takePictureButton.setVisibility(View.GONE);
        }

        nameEditText.setEnabled(editMode);
        surnameEditText.setEnabled(editMode);
        maleRadioButton.setEnabled(editMode);
        femaleRadioButton.setEnabled(editMode);
        descriptionEditText.setEnabled(editMode);
    }

    /**
     * Método encargado de actualizar la ActionBar.
     */
    private void updateActionBar() {
        MenuInflater inflater = getMenuInflater();
        activityMenu.clear();

        if (editMode) {
            inflater.inflate(R.menu.action_bar_profile_edit_mode, activityMenu);
        } else {
            inflater.inflate(R.menu.action_bar_profile_view_mode, activityMenu);
        }
    }

    /**
     * Método encargado de guardar el perfil.
     */
    private void saveProfile() {
        StaticValues.getInstance().getConnectedUser()
                .setName(String.valueOf(nameEditText.getText()));
        StaticValues.getInstance().getConnectedUser()
                .setSurname(String.valueOf(surnameEditText.getText()));

        if (maleRadioButton.isChecked()) {
            StaticValues.getInstance().getConnectedUser().setGender(RegisterActivity.MALE_GENDER);
        } else if (femaleRadioButton.isChecked()) {
            StaticValues.getInstance().getConnectedUser().setGender(RegisterActivity.FEMALE_GENDER);
        } else {
            StaticValues.getInstance().getConnectedUser().setGender(RegisterActivity.OTHER_GERNDER);
        }

        String imageFile = getImageFileName();

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

        StaticValues.getInstance().getConnectedUser()
                .setDescription(String.valueOf(descriptionEditText.getText()));
        StaticValues.getInstance().getConnectedUser().setImageFile(imageFile);

        dbManagement.updateUser(StaticValues.getInstance().getConnectedUser());

        Toast.makeText(this, R.string.profile_modified_correctly, Toast.LENGTH_SHORT).show();
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
}
