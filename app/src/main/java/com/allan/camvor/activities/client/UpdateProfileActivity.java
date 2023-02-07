package com.allan.camvor.activities.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allan.camvor.R;
import com.allan.camvor.models.Client;
import com.allan.camvor.providers.AuthProvider;
import com.allan.camvor.providers.ClientProvider;
import com.allan.camvor.providers.ImagesProvider;
import com.allan.camvor.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    private ImageView mImageViewProfile;
    private Button mButtonUpdate;
    private TextView mTextViewName;

    private ClientProvider mClientProvider;
    private AuthProvider mAuthProvider;
    private ImagesProvider mImagesProvider;
    private CircleImageView mCircleImageBack;

    private File mImageFile;
    private String mImage;

    private final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgressDialog;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
     //   MyToolbar.show(this, "Actualizar Perfil", true);

        mImageViewProfile = findViewById(R.id.imageViewProfile);
        mButtonUpdate = findViewById(R.id.btnUpdateProfile);
        mTextViewName = findViewById(R.id.textInputName);
        mCircleImageBack = findViewById(R.id.circleImageBack);

        mClientProvider = new ClientProvider();
        mAuthProvider = new AuthProvider();
        mImagesProvider = new ImagesProvider("client_images");

        mProgressDialog = new ProgressDialog(this);

        getClientInfo();

        mImageViewProfile.setOnClickListener(v -> openGallery());
        mButtonUpdate.setOnClickListener(v -> updateProfile());
        mCircleImageBack.setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_REQUEST && resultCode == RESULT_OK){
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Mensaje: " +e.getMessage());
            }
        }
    }

    private void getClientInfo(){
        mClientProvider.getClient(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String image = "";
                    if (snapshot.hasChild("image")) {
                        image = snapshot.child("image").getValue().toString();
                        Picasso.with(UpdateProfileActivity.this).load(image).into(mImageViewProfile);
                        mTextViewName.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProfile() {
        mName = mTextViewName.getText().toString();
        if (!mName.equals("") && mImageFile != null) {
            mProgressDialog.setMessage("Espere Un Momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveImage();
        }
        else{
            Toast.makeText(this, "Ingresa La Imagen y El Nombre", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mImagesProvider.saveImage(UpdateProfileActivity.this, mImageFile, mAuthProvider.getId()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImagesProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            Client client = new Client();
                            client.setImage(image);
                            client.setName(mName);
                            client.setId(mAuthProvider.getId());
                            mClientProvider.update(client).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(UpdateProfileActivity.this, "Su Informacion ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(UpdateProfileActivity.this, "Hubo Un Error Al Subir La Imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}