package com.puce.androidmed.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.puce.androidmed.R;

public class UserDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
    }

    // Maneja el clic en "Recordatorios"
    public void onRemindersClick(View view) {
        Intent intent = new Intent(this, RemindersActivity.class);
        startActivity(intent);
    }

    // Maneja el clic en "Medicinas"
    public void onMedicinesClick(View view) {
        Intent intent = new Intent(this, MedicinesActivity.class);
        startActivity(intent);
    }

    // Maneja el clic en "Recomendaciones"
    public void onRecommendationsClick(View view) {
        Intent intent = new Intent(this, RecommendationsActivity.class);
        startActivity(intent);
    }

    // Maneja el clic en "Acerca de Nosotros"
    public void onAboutUsClick(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    // Maneja el clic en "Cerrar Sesión"
    public void onLogoutClick(View view) {
        // Borrar SharedPreferences para limpiar las credenciales del usuario
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir a la pantalla de Login
        Intent intent = new Intent(UserDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finalizar la actividad actual
    }
}
