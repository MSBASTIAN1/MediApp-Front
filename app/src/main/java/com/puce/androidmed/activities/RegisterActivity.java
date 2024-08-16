package com.puce.androidmed.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.puce.androidmed.R;
import com.puce.androidmed.models.User;
import com.puce.androidmed.models.UserResponse;
import com.puce.androidmed.network.ApiService;
import com.puce.androidmed.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, phoneNumberEditText;
    private Button registerButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar componentes de la interfaz
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        registerButton = findViewById(R.id.registerButton);

        // Configuración de Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Configurar acción del botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();

        // Validación de los campos de entrada
        if (TextUtils.isEmpty(firstName) || !firstName.matches("[a-zA-Z]+")) {
            firstNameEditText.setError("Nombre no válido (solo letras)");
            return;
        }

        if (TextUtils.isEmpty(lastName) || !lastName.matches("[a-zA-Z]+")) {
            lastNameEditText.setError("Apellido no válido (solo letras)");
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo no válido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Contraseña no válida");
            return;
        }

        if (TextUtils.isEmpty(phoneNumber) || !Pattern.compile("^\\d{10}$").matcher(phoneNumber).matches()) {
            phoneNumberEditText.setError("Número no válido (debe tener 10 dígitos)");
            return;
        }

        // Encriptar la contraseña antes de enviarla al servidor
        String encryptedPassword = encryptPassword(password);

        // Crear un objeto User para la solicitud
        User newUser = new User();
        newUser.setFirst_name(firstName);
        newUser.setLast_name(lastName);
        newUser.setEmail(email);
        newUser.setPassword(encryptedPassword);  // Establecer la contraseña encriptada
        newUser.setPhone_number(phoneNumber);

        // Enviar la solicitud al servidor
        apiService.registerUser(newUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    finish();  // Cierra la actividad de registro
                } else {
                    Toast.makeText(RegisterActivity.this, "Fallo en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error en el registro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para encriptar la contraseña usando SHA-256
    private String encryptPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] array = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
