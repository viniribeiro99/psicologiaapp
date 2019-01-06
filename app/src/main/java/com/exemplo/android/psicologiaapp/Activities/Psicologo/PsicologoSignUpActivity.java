package com.exemplo.android.psicologiaapp.Activities.Psicologo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PsicologoSignUpActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 2;
    private final int CAMERA_REQUEST = 1;
    private final int PERMISSION_REQUEST_CODE = 3;
    ImageView imageView;
    FirebaseStorage storage;
    EditText description;
    User user;
    FirebaseAuth firebaseAuth;
    static String LOG_TAG = PsicologoSignUpActivity.class.getName();
    Bitmap bitmap;
    private File destination = null;
    private String imgPath = null;
    Spinner spinner;
    File photoFile;
    String photoFileName = "profile.jpg";
    StorageReference storageReference;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    byte[] dataByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psicologo_sign_up);

        imageView = findViewById(R.id.imageViewId);
        storage = FirebaseStorage.getInstance();
        description = findViewById(R.id.descriptionId);
        spinner = findViewById(R.id.spinner);
        user = (User) getIntent().getSerializableExtra("user");
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + user.getId());

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.methodologies, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.setMethodology(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void goToNextIntent(View view) {
        if (dataByte != null && !user.getMethodology().equals("Selecione uma metodologia") && !description.getText().toString().equals("")) {
            String descriptionText = description.getText().toString();
            user.setDescription(descriptionText);
            Intent intent = new Intent(PsicologoSignUpActivity.this, PsicologoSignUpActivity2.class);
            intent.putExtra("user", user);
            intent.putExtra("dataByte", dataByte);
            startActivity(intent);
        } else if (dataByte == null) {
            Toast.makeText(PsicologoSignUpActivity.this, "Insira uma foto de perfil", Toast.LENGTH_SHORT).show();
        } else if (user.getMethodology().equals("Selecione uma metodologia")) {
            Toast.makeText(PsicologoSignUpActivity.this, "Selecione uma metodologia", Toast.LENGTH_SHORT).show();
        } else if (description.getText().toString().equals("")) {
            Toast.makeText(PsicologoSignUpActivity.this, "Escreva na descrição um breve texto falando sobre você e suas especialidades", Toast.LENGTH_LONG).show();
        }

            /*firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                progressDialog.dismiss();
                                String descriptionText = description.getText().toString();
                                user.setDescription(descriptionText);
                                FirebaseUser firebaseUser = task.getResult().getUser();
                                user.setId(firebaseUser.getUid());
                                user.saveOnDatabase();
                                StorageReference ref = storageReference.child("images/" + user.getId());
                                ref.putFile(filePath);
                                Toast.makeText(PsicologoSignUpActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                final Intent intent = new Intent(PsicologoSignUpActivity.this, PsicologoSignUpActivity2.class);
                                intent.putExtra("user", user);
                                onPressed = true;
                                startActivity(intent);

                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(PsicologoSignUpActivity.this, "A senha deve conter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                                    Log.e(LOG_TAG, task.getException().toString());
                                    Intent intent = new Intent(PsicologoSignUpActivity.this, SignUpActivity.class);
                                    startActivity(intent);

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(PsicologoSignUpActivity.this, "O campo email está mal formatado", Toast.LENGTH_SHORT).show();
                                    Log.e(LOG_TAG, task.getException().toString());
                                }
                                catch (Exception e) {
                                    Toast.makeText(PsicologoSignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    Log.e(LOG_TAG, task.getException().toString());
                                    progressDialog.dismiss();
                                }

                            }


                        }
                    });*/
        }

        void chooseImage (View view){
            selectImage();
        }

        private void selectImage () {
            try {
                PackageManager pm = getPackageManager();
                final CharSequence[] options = {"Capturar imagem", "Escolher uma imagem da galeria", "Cancelar"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(PsicologoSignUpActivity.this);
                builder.setTitle("Selecione uma opção");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Capturar imagem")) {
                            dialog.dismiss();
                            if (checkPermission()) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 1);
                            } else {
                                requestPermission();
                            }

                        } else if (options[item].equals("Escolher uma imagem da galeria")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 2);
                        } else if (options[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            } catch (Exception e) {
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    imageView.setImageBitmap(bitmap);
                    dataByte = bytes.toByteArray();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
                try {
                    Uri filePath = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    dataByte = bytes.toByteArray();
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(LOG_TAG, e.getMessage());
                }
            }
        }

        private boolean checkPermission () {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return false;
            }
            return true;
        }

        private void requestPermission () {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);

                        // main logic
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                showMessageOKCancel("You need to allow access permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermission();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                    break;
            }
        }

        private void showMessageOKCancel (String message, DialogInterface.OnClickListener okListener)
        {
            new AlertDialog.Builder(PsicologoSignUpActivity.this)
                    .setMessage(message)
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }
        public Uri getImageUri (Context inContext, Bitmap inImage){
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
    }

