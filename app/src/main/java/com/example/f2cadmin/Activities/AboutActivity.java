package com.example.f2cadmin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.f2cadmin.databinding.ActivityAboutBinding;
import com.google.firebase.database.core.Context;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnPhone.setOnClickListener(v ->{
            askPermissions();
        });

        binding.txtEmail.setOnClickListener(v -> {
            try{
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"kushwanthchandra1@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL,recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Problem in app!");
                intent.putExtra(Intent.EXTRA_TEXT,"Write issues you faced in this application");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }catch (Exception e){
                Toast.makeText(AboutActivity.this,"Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.txtWhatsapp.setOnClickListener(v -> sendMessage("6303574706"));
    }

    private void askPermissions(){
        if(ContextCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.CALL_PHONE)
            !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AboutActivity.this, new String[]{Manifest.permission.CALL_PHONE},
            100);
        }
        else{
            String phone1 = binding.txtPhone.getText().toString();
            try {
                String phone = "+91"+phone1;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String phoneNumber){
        PackageManager pm=getPackageManager();

        String number;

        if (phoneNumber.startsWith("+91")){
            number = phoneNumber;
        }else {
            number = "+91"+phoneNumber;
        }

        String message = "Hello i have seen your post at Stumart";

        startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                        number, message)
                        )
                )
        );
    }

}