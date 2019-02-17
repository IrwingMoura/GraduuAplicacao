package graduuaplicacao.graduuaplicacao.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class RegistroDeEventoActivity extends AppCompatActivity {

    private TextInputEditText titulo;
    private TextInputEditText horaInicio;
    private TextInputEditText horaFim;
    private TextInputEditText dataiptText;
    private TextInputEditText descricao;
    private TextInputEditText apresentador;
    private TextInputEditText frequencia;
    private TextInputEditText local;
    private TextInputEditText setor;
    private ImageView fotoCard;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;
    private Spinner spinnerLocais;

    private Usuario usuario;
    private Evento eventos;
    private DatabaseReference firebase, firebase2;

    private Button criarEvento;

    private int hora;
    private int minuto;
    private String amPm;

    private int hora2;
    private int minuto2;
    private String amPm2;

    private int ano, mes, dia;

    FirebaseUser firebaseUser;
    String uid;
    private String urlImagem;

    String localSpinner;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_de_evento2);


        titulo = (TextInputEditText) findViewById(R.id.edtTitulo);
        horaInicio = (TextInputEditText) findViewById(R.id.edtHoraInicio);
//        horaFim = (TextInputEditText) findViewById(R.id.edtHoraFim);
        dataiptText = (TextInputEditText) findViewById(R.id.edtData);
        descricao = (TextInputEditText) findViewById(R.id.edtDescricao);
        apresentador = (TextInputEditText) findViewById(R.id.edtApresentador);
//        frequencia = (EditText) findViewById(R.id.edtFrequencia);
//        local = (TextInputEditText) findViewById(R.id.edtLocal);
        setor = (TextInputEditText) findViewById(R.id.edtCategoria);
        fotoCard = (ImageView) findViewById(R.id.imgCardView);
        spinnerLocais = (Spinner) findViewById(R.id.spinnerLocais);

        titulo.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.locais, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocais.setAdapter(adapterSpinner);
        spinnerLocais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                localSpinner = parent.getItemAtPosition(position).toString();

//                if(localSpinner.equals("Local")) {
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dataiptText.setInputType(InputType.TYPE_NULL);
        horaInicio.setInputType(InputType.TYPE_NULL);
//        horaFim.setInputType(InputType.TYPE_NULL);


        onClickHoraInicio();
        onClickData();
//        onClickHoraFim();


        criarEvento = (Button) findViewById(R.id.btnCriarEvento);

        formatarInputs();

        criarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventos = new Evento();
                usuario = new Usuario();



//                eventos.setIdDoCriador(usuario.getId());
                eventos.setNome(titulo.getText().toString());
                eventos.setHoraInicio(horaInicio.getText().toString());
//                eventos.setHoraFim(horaFim.getText().toString());
                eventos.setData(dataiptText.getText().toString());
                eventos.setDescricao(descricao.getText().toString());
                eventos.setApresentador(apresentador.getText().toString());
//                eventos.setFrequencia(frequencia.getText().toString());
                eventos.setLocal(localSpinner);
                eventos.setCategoria(setor.getText().toString());
                eventos.setIdUsuarioLogado(uid);
                eventos.setUrlFotoUsuarioCard(urlImagem);
                eventos.setDeepLink(ConfiguracaoFirebase.generateDeepLink(titulo.getText().toString()));
                eventos.setNumShares(0);

//                Validador validador = new Validador();
//                boolean opt = validador.validarData(eventos.getData());

                Calendar cal = Calendar.getInstance();
                System.out.println(cal);
                int horaAtual = cal.get(Calendar.HOUR_OF_DAY);
                int diaAtual = cal.get(Calendar.DAY_OF_MONTH);


                // TODO: PEGAR AS VARIAVEIS SETADAS NA CLASSE EVENTO E TRANSFORMAR PARA INTEGER, O QUE FOR PRECISO, PARA PODER FAZER A VERIFICAÇÃO DE DATA E HORA;

                if(TextUtils.isEmpty(titulo.getText())) {
                    titulo.setError("Insira um titulo");
                }else if(eventos.getLocal().equals("Escolha um local")) {
                    Toast.makeText(RegistroDeEventoActivity.this, "Escolha um local", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(dataiptText.getText())) {
                    dataiptText.setError("Insira uma data");
                }else if(TextUtils.isEmpty(horaInicio.getText())) {
                    horaInicio.setError("Insira o horário de inicio do evento");
                }else if(TextUtils.isEmpty(setor.getText())) {
                    setor.setError("Insira o setor do evento");
                }else if(TextUtils.isEmpty(apresentador.getText())) {
                    apresentador.setError("Insira o nome do apresentador do evento");
                }else if(TextUtils.isEmpty(descricao.getText())) {
                    descricao.setError("Insira uma descrição do evento");
                }

                else {

                    salvarEventos(eventos);
                    listarEventos();
                    }
                }
        });

    }

    private void onClickHoraInicio() {
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                hora = cal.get(Calendar.HOUR_OF_DAY);
                minuto = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(RegistroDeEventoActivity.this,
                        mTimeSetListener,
                        hora, minuto, false);

                dialog.show();

            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                if (hora >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                horaInicio.setText(hora + ":" + minuto + "h");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        StorageReference imagemRef = storageReference.child("Users").child(uid).child("imagem");
        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                urlImagem = String.valueOf(uri);
            }
        });
    }

    private void onClickHoraFim() {
        horaFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                hora2 = cal.get(Calendar.HOUR_OF_DAY);
                minuto2 = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(RegistroDeEventoActivity.this,
                        mTimeSetListener2,
                        hora2, minuto2, false);

                dialog.show();

            }
        });

        mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                if (hora >= 12) {
                    amPm2 = "PM";
                } else {
                    amPm2 = "AM";
                }
                horaFim.setText(hora + ":" + minuto + "h");
            }
        };
    }

    private void onClickData() {
        dataiptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                 ano = cal.get(Calendar.YEAR);
                 mes = cal.get(Calendar.MONTH);
                 dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegistroDeEventoActivity.this,
                        mDateSetListener,
                        ano,mes,dia);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                mes = mes + 1;
                String date = dia + "/" + mes + "/" + ano;
                dataiptText.setText(date);
            }
        };
    }

    private void formatarInputs(){
//        horaInicio.addTextChangedListener(Formatador.mask(horaInicio, Formatador.FORMAT_HOUR));
//        horaFim.addTextChangedListener(Formatador.mask(horaFim, Formatador.FORMAT_HOUR));
//        data.addTextChangedListener(Formatador.mask(data,Formatador.FORMAT_DATE));
    }


    private boolean salvarEventos(Evento eventos) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("Usuarios").child(uid).child("eventosCriados ");
            firebase2 = ConfiguracaoFirebase.getFirebase().child("eventosCriados");
//            firebase = ConfiguracaoFirebase.getFirebase().child("eventosCriados");
            firebase.child(eventos.getNome()).setValue(eventos);
            firebase2.child(eventos.getNome()).setValue(eventos);
            Toast.makeText(RegistroDeEventoActivity.this, "Evento criado com sucesso!", Toast.LENGTH_LONG).show();

            return true ;

        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    private void listarEventos() {
        Intent intent = new Intent(RegistroDeEventoActivity.this, EventosActivity.class);
        startActivity(intent);
        finish();
    }


    public static void formatarData(String str, Date data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            data = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(data);
    }

}
