package com.puce.androidmed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.puce.androidmed.R;
import com.puce.androidmed.models.Medicine;

import java.util.List;

/**
 * Adaptador personalizado para mostrar la información de las medicinas en un ListView.
 */
public class MedicineAdapter extends ArrayAdapter<Medicine> {

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        super(context, 0, medicines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medicine medicine = getItem(position);

        // Inflar el layout solo si es necesario (optimización de recursos)
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_medicine, parent, false);
        }

        // Obtener referencias a los elementos de la vista
        TextView medicineName = convertView.findViewById(R.id.medicineName);
        TextView medicineFirstEffects = convertView.findViewById(R.id.medicineFirstEffects);
        TextView medicineDescription = convertView.findViewById(R.id.medicineDescription);
        TextView medicineRecommendedDose = convertView.findViewById(R.id.medicineRecommendedDose);
        TextView medicineSideEffects = convertView.findViewById(R.id.medicineSideEffects);
        ImageView medicineImage = convertView.findViewById(R.id.medicineImage);

        // Configurar los textos en los elementos de la vista
        medicineName.setText(medicine.getTitle());  // Asigna el nombre de la medicina
        medicineFirstEffects.setText("Efectos Primarios: " + medicine.getFirstEffects());  // Asigna los efectos primarios
        medicineDescription.setText("Descripción: " + medicine.getDescription());  // Asigna la descripción
        medicineRecommendedDose.setText("Dosis Recomendada: " + medicine.getRecommendedDose());  // Asigna la dosis recomendada
        medicineSideEffects.setText("Efectos Secundarios: " + medicine.getSideEffects());  // Asigna los efectos secundarios

        // Cargar la imagen usando Glide (o cualquier otra librería)
        Glide.with(getContext()).load(medicine.getImage()).into(medicineImage);

        return convertView;
    }
}
