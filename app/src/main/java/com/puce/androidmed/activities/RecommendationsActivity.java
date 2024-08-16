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
import com.puce.androidmed.models.Recommendation;
import com.puce.androidmed.network.ApiService;
import com.puce.androidmed.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationsActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private EditText problemTitleEditText;
    private EditText emailEditText;
    private Button sendRecommendationButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        // Enlazando vistas desde el XML
        descriptionEditText = findViewById(R.id.description);
        problemTitleEditText = findViewById(R.id.problemTitle);
        emailEditText = findViewById(R.id.email);
        sendRecommendationButton = findViewById(R.id.sendRecommendationButton);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        sendRecommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRecommendation();
            }
        });
    }

    // Método para enviar la recomendación
    private void sendRecommendation() {
        String description = descriptionEditText.getText().toString();
        String problemTitle = problemTitleEditText.getText().toString();
        String email = emailEditText.getText().toString();

        // Validación simple de campos
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(problemTitle) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de correo electrónico
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Recommendation con los campos necesarios
        Recommendation recommendation = new Recommendation(description, problemTitle, email);

        // Enviar la recomendación a través de la API
        apiService.sendRecommendation(recommendation).enqueue(new Callback<Recommendation>() {
            @Override
            public void onResponse(Call<Recommendation> call, Response<Recommendation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RecommendationsActivity.this, "Recomendación enviada con éxito", Toast.LENGTH_SHORT).show();
                    // Opcional: limpiar campos después de enviar
                    descriptionEditText.setText("");
                    problemTitleEditText.setText("");
                    emailEditText.setText("");
                } else {
                    Toast.makeText(RecommendationsActivity.this, "Error al enviar la recomendación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recommendation> call, Throwable t) {
                Toast.makeText(RecommendationsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para validar el correo electrónico
    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
