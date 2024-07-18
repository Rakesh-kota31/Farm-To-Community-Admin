//Kushwanth23
package com.example.f2cadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.f2cadmin.databinding.ActivityVegetableBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VegetableActivity extends AppCompatActivity {


    ActivityVegetableBinding binding;

    String category;

    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseUser user;

    private Uri imageUri;

    Boolean inStock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVegetableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        category = getIntent().getStringExtra("category");


        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        storageReference = FirebaseStorage.getInstance().getReference().child("Products");
        user = FirebaseAuth.getInstance().getCurrentUser();


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.productImage.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 99);
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName = binding.inputProductName.getText().toString();
                String pDescription = binding.inputDescription.getText().toString();
                String pQuantity = binding.inputQuantity.getText().toString();
                String pPrice = binding.inputPrice.getText().toString();

                if (pName.isEmpty() || pDescription.isEmpty() || pQuantity.isEmpty() || pPrice.isEmpty()) {
                    Toast.makeText(VegetableActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(VegetableActivity.this, "Please select image!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage(pName, pDescription, pQuantity, pPrice);
                }
            }
        });


        binding.inputStock.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VegetableActivity.this);
            builder.setTitle("Select");
            builder.setCancelable(false);

            String[] items = {"Available", "Not"};
            builder.setItems(items, (dialog, which) -> {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        inStock = true;
                        binding.inputStock.setText("Available");
                        break;

                    case 1:
                        inStock = false;
                        binding.inputStock.setText("Not");
                        break;

                }
            });
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            builder.create().show();

        });

    }
    private void uploadImage(String pName, String pDescription, String pQuantity, String pPrice) {
        ProgressDialog progressDialog = new ProgressDialog(VegetableActivity.this);
        progressDialog.setMessage("Uploading....");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final StorageReference sRef = storageReference.child(System.currentTimeMillis() + ".jpg");

        sRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                sRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    String id = reference.push().getKey();

                    Products products = new Products();
                    products.setImage(imageUrl);
                    products.setName(pName);
                    products.setDescription(pDescription);
                    products.setCategory(category);
                    products.setId(id);
                    products.setStock(inStock);
                    products.setPrice(Integer.parseInt(pPrice));
                    products.setQuantity(pQuantity);
                    products.setPublisher(user.getUid());

                    reference.child(id).setValue(products).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(VegetableActivity.this, "Product uploaded!", Toast.LENGTH_SHORT).show();

                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(VegetableActivity.this, "Failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(VegetableActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.productImage.setImageURI(imageUri);

        } else {
            Toast.makeText(VegetableActivity.this, "Please select image..", Toast.LENGTH_SHORT).show();
        }
    }
}