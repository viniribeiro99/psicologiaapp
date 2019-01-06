package com.exemplo.android.psicologiaapp.Activities.Psicologo;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.exemplo.android.psicologiaapp.Classes.DayHourAvailable;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.exemplo.android.psicologiaonline.TimePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PsicologoSignUpActivity2 extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    CheckBox segunda, terca, quarta, quinta, sexta, sabado, domingo;
    LinearLayout linearSegunda, linearTerca, linearQuarta, linearQuinta, linearSexta, linearSabado, linearDomingo;
    Button selectTime;
    TextView selectDaysTv;
    String tag;
    int count = 0;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    User user;
    Uri filepath;
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    ArrayList<DayHourAvailable> daysHours = new ArrayList<>();
    byte[] dataByte;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (tag) {
            case "segundaDe":
                TextView segundaTimeDe = findViewById(R.id.segundaTimeDe);
                setTime(segundaTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("segunda", hourOfDay, minute, true));
                break;
            case "segundaAte":
                TextView segundaTimeAte = findViewById(R.id.segundaTimeAte);
                setTime(segundaTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("segunda", hourOfDay, minute, false));
                break;
            case "tercaDe":
                TextView tercaTimeDe = findViewById(R.id.tercaTimeDe);
                setTime(tercaTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("terça", hourOfDay, minute, true));

                break;
            case "tercaAte":
                TextView tercaTimeAte = findViewById(R.id.tercaTimeAte);
                setTime(tercaTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("terca", hourOfDay, minute, false));
                break;
            case "quartaDe":
                TextView quartaTimeDe = findViewById(R.id.quartaTimeDe);
                setTime(quartaTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("quarta", hourOfDay, minute, true));
                break;
            case "quartaAte":
                TextView quartaTimeAte = findViewById(R.id.quartaTimeAte);
                setTime(quartaTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("quarta", hourOfDay, minute, false));
                break;
            case "quintaDe":
                TextView quintaTimeDe = findViewById(R.id.quintaTimeDe);
                setTime(quintaTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("quinta", hourOfDay, minute, true));
                break;
            case "quintaAte":
                TextView quintaTimeAte = findViewById(R.id.quintaTimeAte);
                setTime(quintaTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("quinta", hourOfDay, minute, false));
                break;
            case "sextaDe":
                TextView sextaTimeDe = findViewById(R.id.sextaTimeDe);
                setTime(sextaTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("sexta", hourOfDay, minute, true));
                break;
            case "sextaAte":
                TextView sextaTimeAte = findViewById(R.id.sextaTimeAte);
                setTime(sextaTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("sexta", hourOfDay, minute, true));
                break;
            case "sabadoDe":
                TextView sabadoTimeDe = findViewById(R.id.sabadoTimeDe);
                setTime(sabadoTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("sabado", hourOfDay, minute, true));
                break;
            case "sabadoAte":
                TextView sabadoTimeAte = findViewById(R.id.sabadoTimeAte);
                setTime(sabadoTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("sabado", hourOfDay, minute, false));
                break;
            case "domingoDe":
                TextView domingoTimeDe = findViewById(R.id.domingoTimeDe);
                setTime(domingoTimeDe, hourOfDay, minute, true);
                daysHours.add(new DayHourAvailable("domingo", hourOfDay, minute, true));
                break;
            case "domingoAte":
                TextView domingoTimeAte = findViewById(R.id.domingoTimeAte);
                setTime(domingoTimeAte, hourOfDay, minute, false);
                daysHours.add(new DayHourAvailable("domingo", hourOfDay, minute, false));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psicologo_sign_up2);
        segunda = findViewById(R.id.segunda);
        terca = findViewById(R.id.terca);
        quarta = findViewById(R.id.quarta);
        quinta = findViewById(R.id.quinta);
        sexta = findViewById(R.id.sexta);
        sabado = findViewById(R.id.sabado);
        domingo = findViewById(R.id.domingo);

        selectTime = findViewById(R.id.selectTime);

        linearSegunda = findViewById(R.id.linearSegunda);
        linearTerca = findViewById(R.id.linearTerca);
        linearQuarta = findViewById(R.id.linearQuarta);
        linearQuinta = findViewById(R.id.linearQuinta);
        linearSexta = findViewById(R.id.linearSexta);
        linearSabado = findViewById(R.id.linearSabado);
        linearDomingo = findViewById(R.id.linearDomingo);

        user = (User) getIntent().getSerializableExtra("user");
        filepath = getIntent().getParcelableExtra("filepath");
        dataByte = getIntent().getByteArrayExtra("dataByte");

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    selectDaysTv = findViewById(R.id.selectDaysTv);
                    selectDaysTv.setText("Selecione os horários disponíveis.");

                    if (segunda.isChecked()) {
                        View segundaView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearSegunda, true);
                        Button deButton = segundaView.findViewById(R.id.buttonDe);
                        Button ateButton = segundaView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "segundaDe");
                                tag = "segundaDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "segundaAte");
                                tag = "segundaAte";
                            }
                        });
                    }
                    if (terca.isChecked()) {
                        View tercaView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearTerca, true);
                        Button deButton = tercaView.findViewById(R.id.buttonDe);
                        Button ateButton = tercaView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "tercaDe");
                                tag = "tercaDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "tercaAte");
                                tag = "tercaAte";
                            }
                        });
                    }
                    if (quarta.isChecked()) {
                        View quartaView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearQuarta, true);
                        Button deButton = quartaView.findViewById(R.id.buttonDe);
                        Button ateButton = quartaView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "quartaDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "quartaAte";
                            }
                        });
                    }
                    if (quinta.isChecked()) {
                        View quintaView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearQuinta, true);
                        Button deButton = quintaView.findViewById(R.id.buttonDe);
                        Button ateButton = quintaView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "quintaDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "quintaAte";
                            }
                        });
                    }
                    if (sexta.isChecked()) {
                        View sextaView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearSexta, true);
                        Button deButton = sextaView.findViewById(R.id.buttonDe);
                        Button ateButton = sextaView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "sextaDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "sextaAte";
                            }
                        });
                    }
                    if (sabado.isChecked()) {
                        View sabadoView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearSabado, true);
                        Button deButton = sabadoView.findViewById(R.id.buttonDe);
                        Button ateButton = sabadoView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "sabadoDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "sabadoAte";
                            }
                        });
                    }
                    if (domingo.isChecked()) {
                        View domingoView = LayoutInflater.from(PsicologoSignUpActivity2.this).inflate(R.layout.time_picker, linearDomingo, true);
                        Button deButton = domingoView.findViewById(R.id.buttonDe);
                        final Button ateButton = domingoView.findViewById(R.id.buttonAte);
                        deButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "domingoDe";
                            }
                        });
                        ateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TimePickerFragment newFragment = new TimePickerFragment();
                                newFragment.show(getFragmentManager(), "datePicker");
                                tag = "domingoAte";
                            }
                        });
                    }
                    count++;
                    selectTime.setText("Concluir cadastro");
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user.setId(task.getResult().getUser().getUid());
                                user.setDaysAvailable(daysHours);
                                user.saveOnDatabase();
                                StorageReference storageReference = reference.child("images/" + user.getId());
                                storageReference.putBytes(dataByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(PsicologoSignUpActivity2.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PsicologoSignUpActivity2.this, "Não foi possível", Toast.LENGTH_SHORT).show();
                                        Log.i("teste", e.getMessage().toString());

                                    }
                                });
                                Toast.makeText(PsicologoSignUpActivity2.this, "Cadastro concluido", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PsicologoSignUpActivity2.this, PsicologoActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        }
                    });
                    count = 0;
                }
            }
        });
    }

    private void setTime(TextView textView, int hour, int minute, boolean deOrAte) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);
        if (deOrAte) {
            textView.setText("De " + dateFormat.format(date));
        } else {
            textView.setText("  Até " + dateFormat.format(date));
        }

    }
}
