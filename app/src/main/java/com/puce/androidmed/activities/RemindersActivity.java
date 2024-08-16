package com.puce.androidmed.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.TimePickerDialog;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.puce.androidmed.R;
import com.puce.androidmed.ReminderBroadcastReceiver;
import com.puce.androidmed.models.Reminder;
import com.puce.androidmed.models.ReminderResponse;
import com.puce.androidmed.network.ApiService;
import com.puce.androidmed.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemindersActivity extends AppCompatActivity {

    private EditText editTextMedicament, editTextQuantity, editTextDate, editTextTime, editTextFrequency, editTextDescription;
    private Spinner spinnerStatus;
    private Button buttonAddNewReminder, buttonSaveReminder;
    private ListView listViewReminders;
    private ArrayList<Reminder> remindersList = new ArrayList<>();
    private Reminder currentReminder; // Para saber si estamos editando
    private ScrollView layoutAddEditReminder;
    private String userId; // Variable para almacenar el user_id
    private String originalDescription, originalMedicamentId, originalDate, originalTime, originalFrequency, originalQuantity, originalStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        // Recuperar el user_id desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = preferences.getString("user_id", null);

        if (userId == null) {
            // Si no hay user_id, redirigir al LoginActivity
            Intent intent = new Intent(RemindersActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Configurar los componentes de la interfaz
        layoutAddEditReminder = findViewById(R.id.layoutAddEditReminder);
        editTextMedicament = findViewById(R.id.editTextMedicament);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextFrequency = findViewById(R.id.editTextFrequency);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonAddNewReminder = findViewById(R.id.buttonAddNewReminder);
        buttonSaveReminder = findViewById(R.id.buttonSaveReminder);
        listViewReminders = findViewById(R.id.listViewReminders);

        // Configurar spinner de estado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Establecer la fecha actual automáticamente
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        editTextDate.setText(currentDate);

        // Configurar TimePicker para la hora
        editTextTime.setOnClickListener(v -> showTimePickerDialog());

        // Evento para agregar nuevo recordatorio
        buttonAddNewReminder.setOnClickListener(v -> showAddEditLayout(null));

        // Evento para guardar el recordatorio
        buttonSaveReminder.setOnClickListener(v -> saveReminder());

        // Obtener y mostrar recordatorios
        fetchReminders();
    }

    // Mostrar el diálogo para seleccionar la hora
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d:00", hourOfDay, minuteOfHour);
                    editTextTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    // Mostrar el layout de agregar o editar un recordatorio
    private void showAddEditLayout(@Nullable Reminder reminder) {
        layoutAddEditReminder.setVisibility(View.VISIBLE);
        currentReminder = reminder;

        if (reminder != null) {
            // Guardar los valores originales del recordatorio
            originalDescription = reminder.getDescription();
            originalMedicamentId = reminder.getMedicament_id();
            originalDate = reminder.getDate();
            originalTime = reminder.getTime();
            originalFrequency = reminder.getFrequency();
            originalQuantity = reminder.getQuantity();
            originalStatus = reminder.getStatus();

            // Rellenar campos con la información del recordatorio si estamos editando
            editTextMedicament.setText(reminder.getMedicament_id());
            editTextQuantity.setText(reminder.getQuantity());
            editTextDate.setText(reminder.getDate());
            editTextTime.setText(reminder.getTime());
            editTextFrequency.setText(reminder.getFrequency());
            editTextDescription.setText(reminder.getDescription());
            spinnerStatus.setSelection(((ArrayAdapter<String>) spinnerStatus.getAdapter()).getPosition(reminder.getStatus()));
            ((TextView) findViewById(R.id.modalTitle)).setText("Editar Recordatorio");
        } else {
            // Limpiar los campos para un nuevo recordatorio
            editTextMedicament.setText("");
            editTextQuantity.setText("");
            editTextDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())); // Fecha actual
            editTextTime.setText("");
            editTextFrequency.setText("");
            editTextDescription.setText("");
            spinnerStatus.setSelection(0);
            ((TextView) findViewById(R.id.modalTitle)).setText("Agregar Recordatorio");
        }
    }

    // Cerrar el layout de agregar o editar
    private void closeAddEditLayout() {
        layoutAddEditReminder.setVisibility(View.GONE);
        fetchReminders();
    }

    // Guardar o actualizar un recordatorio
    private void saveReminder() {
        // Obtener valores de los campos desde el layout
        String medicamentId = editTextMedicament.getText().toString().trim();
        String quantity = editTextQuantity.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String frequency = editTextFrequency.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString().trim();

        // Verificar si los campos obligatorios están vacíos
        if (TextUtils.isEmpty(medicamentId) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(frequency) || TextUtils.isEmpty(description) || TextUtils.isEmpty(status)) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si los valores no han cambiado
        if (currentReminder != null && medicamentId.equals(originalMedicamentId) && quantity.equals(originalQuantity)
                && date.equals(originalDate) && time.equals(originalTime) && frequency.equals(originalFrequency)
                && description.equals(originalDescription) && status.equals(originalStatus)) {
            Toast.makeText(this, "No se han realizado cambios.", Toast.LENGTH_SHORT).show();
            closeAddEditLayout();
            return;
        }

        // Recuperar el user_id desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = preferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo recordatorio o actualizar uno existente
        if (currentReminder == null) {
            // Nuevo recordatorio
            Reminder reminder = new Reminder(
                    null,  // ID es null para nuevo recordatorio
                    description,
                    userId,
                    medicamentId,
                    date,
                    time,
                    frequency,
                    quantity.isEmpty() ? "0" : quantity,
                    status
            );
            insertReminder(reminder);
        } else {
            // Actualizar recordatorio existente
            currentReminder.setDescription(description);
            currentReminder.setUser_id(userId);
            currentReminder.setMedicament_id(medicamentId);
            currentReminder.setDate(date);
            currentReminder.setTime(time);
            currentReminder.setFrequency(frequency);
            currentReminder.setQuantity(quantity.isEmpty() ? "0" : quantity);
            currentReminder.setStatus(status);
            updateReminder(currentReminder);
        }
    }

    // Inserta un nuevo recordatorio en la base de datos
    private void insertReminder(Reminder reminder) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Reminder> call = apiService.insertReminder(reminder);
        call.enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RemindersActivity.this, "Recordatorio añadido exitosamente", Toast.LENGTH_SHORT).show();
                    scheduleReminder(reminder);  // Programar la notificación
                    closeAddEditLayout();  // Cerrar el modal y actualizar la lista
                } else {
                    Toast.makeText(RemindersActivity.this, "Error al añadir recordatorio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                Toast.makeText(RemindersActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Actualiza un recordatorio existente en la base de datos
    private void updateReminder(Reminder reminder) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Reminder> call = apiService.updateReminder(reminder);
        call.enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RemindersActivity.this, "Recordatorio actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    scheduleReminder(reminder);  // Programar la notificación
                    closeAddEditLayout();  // Cerrar el modal y actualizar la lista
                } else {
                    Toast.makeText(RemindersActivity.this, "Error al actualizar recordatorio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                Toast.makeText(RemindersActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Programar la notificación del recordatorio
    private void scheduleReminder(Reminder reminder) {
        String[] timeParts = reminder.getTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("message", "Es hora de " + reminder.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    // Elimina un recordatorio con un diálogo de confirmación
    private void deleteReminder(Reminder reminder) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Recordatorio")
                .setMessage("¿Estás seguro de que deseas eliminar este recordatorio?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                    Call<Void> call = apiService.deleteReminder(reminder.getId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RemindersActivity.this, "Recordatorio eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                fetchReminders(); // Refrescar la lista
                            } else {
                                Toast.makeText(RemindersActivity.this, "Error al eliminar recordatorio", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(RemindersActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Obtiene los recordatorios del servidor y los muestra en la lista
    private void fetchReminders() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ReminderResponse> call = apiService.getReminders();
        call.enqueue(new Callback<ReminderResponse>() {
            @Override
            public void onResponse(Call<ReminderResponse> call, Response<ReminderResponse> response) {
                if (response.isSuccessful()) {
                    ReminderResponse reminderResponse = response.body();
                    if (reminderResponse != null) {
                        remindersList.clear();
                        remindersList.addAll(reminderResponse.getResult());
                        ReminderAdapter adapter = new ReminderAdapter(remindersList);
                        listViewReminders.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(RemindersActivity.this, "Error al obtener recordatorios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReminderResponse> call, Throwable t) {
                Toast.makeText(RemindersActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ReminderAdapter extends ArrayAdapter<Reminder> {

        public ReminderAdapter(ArrayList<Reminder> reminders) {
            super(RemindersActivity.this, 0, reminders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_item, parent, false);
            }

            Reminder reminder = getItem(position);

            TextView textViewMedicament = convertView.findViewById(R.id.textViewMedicament);
            TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
            TextView textViewDate = convertView.findViewById(R.id.textViewDate);
            TextView textViewTime = convertView.findViewById(R.id.textViewTime);
            TextView textViewFrequency = convertView.findViewById(R.id.textViewFrequency);
            TextView textViewStatus = convertView.findViewById(R.id.textViewStatus);
            TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
            ImageButton buttonDeleteReminder = convertView.findViewById(R.id.buttonDeleteReminder);
            ImageButton buttonEditReminder = convertView.findViewById(R.id.buttonEditReminder);

            textViewMedicament.setText("Medicamento: " + reminder.getMedicament_id());
            textViewQuantity.setText("Cantidad: " + reminder.getQuantity());
            textViewDate.setText("Fecha: " + reminder.getDate());
            textViewTime.setText("Hora: " + reminder.getTime());
            textViewFrequency.setText("Frecuencia: " + reminder.getFrequency());
            textViewStatus.setText("Estado: " + reminder.getStatus());
            textViewDescription.setText("Descripción: " + reminder.getDescription());

            buttonDeleteReminder.setOnClickListener(v -> deleteReminder(reminder));
            buttonEditReminder.setOnClickListener(v -> showAddEditLayout(reminder));

            return convertView;
        }
    }
}
