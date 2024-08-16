package com.puce.androidmed.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.puce.androidmed.R;
import com.puce.androidmed.adapters.MedicineAdapter;
import com.puce.androidmed.models.Medicine;
import com.puce.androidmed.models.MedicineResponse;
import com.puce.androidmed.network.ApiService;
import com.puce.androidmed.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicinesActivity extends AppCompatActivity {

    private ListView medicinesListView;

    private MedicineAdapter adapter;
    private List<Medicine> medicineList = new ArrayList<>();
    private ApiService apiService;
    private static final String TAG = "MedicinesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);

        medicinesListView = findViewById(R.id.medicinesListView);

        adapter = new MedicineAdapter(this, medicineList);
        medicinesListView.setAdapter(adapter);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        fetchMedicines();

    }

    private void fetchMedicines() {
        apiService.getMedicines().enqueue(new Callback<MedicineResponse>() {
            @Override
            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicine> medicines = response.body().getResult();

                    if (medicines != null && !medicines.isEmpty()) {
                        medicineList.clear();
                        medicineList.addAll(medicines);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MedicinesActivity.this, "No se encontraron medicinas", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "La lista de medicinas está vacía o es null");
                    }
                } else {
                    Toast.makeText(MedicinesActivity.this, "Fallo al obtener las medicinas", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicineResponse> call, Throwable t) {
                Toast.makeText(MedicinesActivity.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error en la conexión: " + t.getMessage());
            }
        });
    }
}