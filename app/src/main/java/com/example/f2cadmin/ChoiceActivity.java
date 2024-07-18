//Kushwanth23
package com.example.f2cadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.f2cadmin.Activities.OptionsActivity;
import com.example.f2cadmin.Activities.OrdersActivity;
import com.example.f2cadmin.databinding.ActivityChoiceBinding;

public class ChoiceActivity extends AppCompatActivity {


    ActivityChoiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnVegetables.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this,VegetableActivity.class);
            intent.putExtra("category","Vegetables");
            startActivity(intent);

        });

        binding.btnFruits.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this,VegetableActivity.class);
            intent.putExtra("category","Fruits");
            startActivity(intent);

        });

        binding.btnGrains.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this,VegetableActivity.class);
            intent.putExtra("category","Grains");
            startActivity(intent);

        });
        binding.btnUpdateStock.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this,UpdateActivity.class);
            startActivity(intent);

        });
        binding.btnOrders.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this, OrdersActivity.class);
            startActivity(intent);

        });

        binding.imgProfile.setOnClickListener(view -> startActivity(new Intent(ChoiceActivity.this, OptionsActivity.class)));

    }
}