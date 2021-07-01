package com.sistemadegestaoagricola.primeiroacesso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.ErroActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaCadastrarPropriedade;
import com.sistemadegestaoagricola.entidades.Propriedade;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CadastroCidadeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edtCidade;
    private Spinner spinnerEstado;
    private Button btProximo;
    private String estado = "";
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cidade);

        edtCidade = findViewById(R.id.edtCidadeCadastroCidade);
        btProximo = findViewById(R.id.btProximoCadastroCidade);

        spinnerEstado = findViewById(R.id.spinnerCadastroCidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.estados, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setOnItemSelectedListener(this);

        edtCidade.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!edtCidade.getText().toString().isEmpty() && !estado.isEmpty()){
                    btProximo.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                } else {
                    btProximo.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.cinza_escuro));
                }

                return false;
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cidade = edtCidade.getText().toString();
                if(!cidade.isEmpty() && !estado.isEmpty()){
//                    CarregarDialog dialog = new CarregarDialog(CadastroCidadeActivity.this);
//                    AlertDialog alertDialog = dialog.criarDialogContinuarCadastroLocalizacao();
//                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //Atribuir dados a Propriedade
//                            Propriedade.setCidade(cidade);
//                            Propriedade.setEstado(estado);
//
//                            Intent intent  = new Intent(getApplicationContext(), CadastroLocalizacaoActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            salvarCadastroAPI(cidade,estado);
//                            Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(),"Digite o nome da cidade",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String nomeEstado = adapterView.getItemAtPosition(i).toString();
        estado = Propriedade.siglaEstado(nomeEstado);
        Log.d("testeX",adapterView.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("testeX","nao selecionado");
    }

    public void salvarCadastroAPI(String cidade, String estado){
        //Atribuindo dados a Propriedade
        Propriedade.setCidade(cidade);
        Propriedade.setEstado(estado);

        RotaCadastrarPropriedade rotaCadastrarPropriedade = new RotaCadastrarPropriedade();
        Future<ConexaoAPI> future = threadpool.submit(rotaCadastrarPropriedade);

        while(!future.isDone()){
            //Aguardando
        }

        ConexaoAPI conexao = null;
        try{
            conexao = future.get();

            mensagensExceptions = conexao.getMensagensExceptions();

            if(mensagensExceptions == null){
                //Sem erro de conexão
                status = conexao.getCodigoStatus();
                Log.d("testeX","statusSalvar =" + status);
                if(status == 200){
                    //Salvo com sucesso
                    Toast.makeText(this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    //Erro ao salvar
                    Toast.makeText(this, "Dados não puderam ser salvos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Erro(mensagensExceptions[0],mensagensExceptions[1]);
            }

        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Erro("Falha na conexão","Tente novamente em alguns minutos");
        } finally {
            conexao.fechar();
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }
}
