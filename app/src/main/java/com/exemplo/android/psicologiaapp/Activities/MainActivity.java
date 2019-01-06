package com.exemplo.android.psicologiaapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplo.android.psicologiaapp.Activities.Paciente.BrowseActivity;
import com.exemplo.android.psicologiaapp.Activities.Psicologo.PsicologoActivity;
import com.exemplo.android.psicologiaapp.R;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/*
Checklist
- Fragments (FEITO)
- Descrição (FEITO)
- Video (FEITO)
- Foto de perfil (FEITO)
- Rating bar (FEITO)
- Spinner
- Agendamento (FEITO)
- Colocar data na mensagem (FEITO) -> consertar data
*/


public class MainActivity extends AppCompatActivity {

    EditText emailView, passwordView;
    User user;
    TextView emergenceView;
    static String LOG_TAG = MainActivity.class.getName();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailView = findViewById(R.id.emailId);
        passwordView = findViewById(R.id.passwordId);
        emergenceView = findViewById(R.id.emergenceId);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");

    }

    public void signIn(View view) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(LOG_TAG, "State has changed");
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Entrando");
        progressDialog.show();
        try {
            firebaseAuth.signInWithEmailAndPassword(emailView.getText().toString(), passwordView.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        final FirebaseUser firebaseUser = task.getResult().getUser();
                        databaseReference = databaseReference.child(firebaseUser.getUid());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String category = dataSnapshot.child("category").getValue().toString();
                                String name = dataSnapshot.child("name").getValue().toString();
                                user = new User(firebaseUser.getUid(), name, category, firebaseUser.getEmail(), passwordView.getText().toString());
                                Log.i(LOG_TAG, ("Categoria: " + user.getCategory()));
                                if (user.getCategory().equals("Paciente")) {
                                    Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
                                    intent.putExtra("user", user);
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                    Log.i(LOG_TAG, "Paciente logado");
                                } else {
                                    Log.i(LOG_TAG, "Psicólogo");
                                    Intent intent = new Intent(MainActivity.this, PsicologoActivity.class);
                                    intent.putExtra("user", user);
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                    Log.i(LOG_TAG, "Psicólogo logado");

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(MainActivity.this, "O usuário inserido não existe", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(MainActivity.this, "O usuário inserido não existe", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(MainActivity.this, "Verifique a conexão com a internet", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(LOG_TAG, task.getException().toString());
                        }
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }


    public void goToSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    void emergenceCall(View view) {
        Intent emergenceIntent = new Intent(Intent.ACTION_DIAL);
        emergenceIntent.setData(Uri.parse("tel:188"));
        startActivity(emergenceIntent);
    }
}


