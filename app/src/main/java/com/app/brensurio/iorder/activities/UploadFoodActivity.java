package com.app.brensurio.iorder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.brensurio.iorder.models.Food;
import com.app.brensurio.iorder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadFoodActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String STORE_NAME = "storeName";
    private static final int PICK_IMAGE_REQUEST = 0;
    private DatabaseReference mDatabase;
    private Bitmap imageBitmap;
    private EditText foodNameEditText;
    private EditText foodPriceEditText;
    private EditText foodDescEditText;
    private ImageView foodImageView;
    private String storeName;
    private String storeDBName;
    private StorageReference storeRef;
    StorageReference storageRef;
    FirebaseStorage storage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setTitle("Upload Food");

        Intent intent =  getIntent();
        storeName = intent.getStringExtra(STORE_NAME);
        storeDBName = storeName.concat("foodlist");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://iorder-72aca.appspot.com");

        foodNameEditText = (EditText) findViewById(R.id.food_name_edit_text);
        foodPriceEditText = (EditText) findViewById(R.id.food_price_edit_text);
        foodDescEditText = (EditText) findViewById(R.id.food_desc_edit_text);

        ImageButton openCamButton = (ImageButton) findViewById(R.id.open_cam_button);
        openCamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button galleryButton = (Button) findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryTakePictureIntent();
            }
        });

        Button uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

        foodImageView = (ImageView) findViewById(R.id.food_image_image_view);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            foodImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream image_stream = null;
            try {
                image_stream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageBitmap = BitmapFactory.decodeStream(image_stream );

            foodImageView.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void galleryTakePictureIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        if (pickPhotoIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);

    }

    private void upload() {
        storageRef = storage.getReferenceFromUrl("gs://iorder-72aca.appspot.com");
        storeRef = storageRef.child(storeName + "/");
        String imageRef = foodNameEditText.getText().toString().toLowerCase();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        storeRef = storeRef.child(imageRef);
        UploadTask uploadTask = storeRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(UploadFoodActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size,
                // content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Food food = new Food(foodNameEditText.getText().toString(),
                        Double.parseDouble(foodPriceEditText.getText().toString()),
                        foodDescEditText.getText().toString(),
                        downloadUrl.toString(),
                        storeName);
                mDatabase.child(String.valueOf(storeDBName)).push().setValue(food);
                finish();
            }
        });
    }
}