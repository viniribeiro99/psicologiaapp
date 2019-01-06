package com.exemplo.android.psicologiaapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.exemplo.android.psicologiaapp.Activities.Paciente.BrowseActivity;
import com.exemplo.android.psicologiaapp.Activities.Psicologo.PsicologoSignUpActivity;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    EditText nameView, emailView, passwordView;
    RadioGroup radioGroup;
    RadioButton chosenButton;
    FirebaseAuth firebaseAuth;
    Bundle savedInstance;
    static String LOG_TAG = SignUpActivity.class.getName();
    User user;
    String name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up);
        savedInstance = savedInstanceState;

        nameView = findViewById(R.id.nameId);
        emailView = findViewById(R.id.emailId);
        passwordView = findViewById(R.id.passwordId);
        radioGroup = findViewById(R.id.radioGroupId);

    }

    public void signUp(View view) {
        //obtem os dados das views
        name = nameView.getText().toString();
        email = emailView.getText().toString();
        password = passwordView.getText().toString();

        firebaseAuth = FirebaseAuth.getInstance(); //instancia a classe FirebaseAuth
        int chosenButtonId = radioGroup.getCheckedRadioButtonId(); //obtem o id do botao selecionado
        chosenButton = findViewById(chosenButtonId); //obtem a view a partir do id
        if (chosenButtonId == -1) {
            Toast.makeText(this, "Selecione uma opção", Toast.LENGTH_SHORT).show();
        }
        if (chosenButtonId > 0) {
            String category = chosenButton.getText().toString();
            user = new User(name, category, email, password);
            try {
                firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = task.getResult().getUser();
                                    user.setId(firebaseUser.getUid());
                                    if (chosenButton.getText().toString().equals("Paciente")) {
                                        user.saveOnDatabase();
                                        Log.i(LOG_TAG, "Usuário criado");
                                        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword());
                                        Log.i(LOG_TAG, "Usuário logado");
                                        Intent intent = new Intent(SignUpActivity.this, BrowseActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    } else {//deleta usuário
                                        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword());
                                        Log.i(LOG_TAG, "Usuário logado");
                                        Intent intent = new Intent(SignUpActivity.this, PsicologoSignUpActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        firebaseAuth.getCurrentUser().delete();
                                    }
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Toast.makeText(SignUpActivity.this, "A senha deve conter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(SignUpActivity.this, "Campo de email mal formatado", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Toast.makeText(SignUpActivity.this, "Endereço de email já existe", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();


                                    }
                                }
                            }
                        });
            } catch (NullPointerException e) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();

            }
        }
    }
}