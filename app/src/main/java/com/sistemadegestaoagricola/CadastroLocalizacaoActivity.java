package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaCadastrarPropriedade;
import com.sistemadegestaoagricola.entidades.Propriedade;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CadastroLocalizacaoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edtLogradouro;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtCidade;
    private EditText edtReferencia;
    private Button btConfirmar;
    private Spinner spinnerEstado;
    private String estado;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    AlertDialog salvando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_localizacao);

        edtLogradouro = findViewById(R.id.edtLogradouroLocalizacao);
        edtNumero = findViewById(R.id.edtNumeroLocalizacao);
        edtBairro = findViewById(R.id.edtBairroLocalizacao);
        edtCidade = findViewById(R.id.edtCidadeLocalizacao);
        edtReferencia = findViewById(R.id.edtReferenciaLocalizacao);
        btConfirmar = findViewById(R.id.btConfirmarLocalizacao);

        spinnerEstado = findViewById(R.id.spinnerLocalizacao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.estados, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setOnItemSelectedListener(this);

        //Setar valores da Propriedade nos campos
        preencherValores();

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtCidade.getText().toString().isEmpty() && !estado.isEmpty()){
                    CarregarDialog carregarDialog = new CarregarDialog(CadastroLocalizacaoActivity.this);
                    salvando = carregarDialog.criarDialogSalvarInformacoes();
                    salvando.show();
                    salvarCadastroAPI();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String nomeEstado = adapterView.getItemAtPosition(i).toString();
        estado = Propriedade.siglaEstado(nomeEstado);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void preencherValores(){
        edtLogradouro.setText( Propriedade.getLogradouro());
        edtNumero.setText(String.valueOf(Propriedade.getNumero()));
        edtBairro.setText(Propriedade.getBairro());
        edtCidade.setText(Propriedade.getCidade());
        estado = Propriedade.getEstado();

        if(estado.equals("AC")){
            spinnerEstado.setSelection(1);
        } else if(estado.equals("AL")){
            spinnerEstado.setSelection(2);
        } else if(estado.equals("AP")){
            spinnerEstado.setSelection(3);
        } else if(estado.equals("AM")){
            spinnerEstado.setSelection(4);
        } else if(estado.equals("BA")){
            spinnerEstado.setSelection(5);
        } else if(estado.equals("CE")){
            spinnerEstado.setSelection(6);
        } else if(estado.equals("DF")){
            spinnerEstado.setSelection(7);
        } else if(estado.equals("ES")){
            spinnerEstado.setSelection(8);
        } else if(estado.equals("GO")){
            spinnerEstado.setSelection(9);
        } else if(estado.equals("MA")){
            spinnerEstado.setSelection(10);
        } else if(estado.equals("MT")){
            spinnerEstado.setSelection(11);
        } else if(estado.equals("MS")){
            spinnerEstado.setSelection(12);
        } else if(estado.equals("MG")){
            spinnerEstado.setSelection(13);
        } else if(estado.equals("PA")){
            spinnerEstado.setSelection(14);
        } else if(estado.equals("PB")){
            spinnerEstado.setSelection(15);
        } else if(estado.equals("PR")){
            spinnerEstado.setSelection(16);
        } else if(estado.equals("PE")){
            spinnerEstado.setSelection(0);
        } else if(estado.equals("PI")){
            spinnerEstado.setSelection(17);
        } else if(estado.equals("RJ")){
            spinnerEstado.setSelection(18);
        } else if(estado.equals("RN")){
            spinnerEstado.setSelection(19);
        } else if(estado.equals("RS")){
            spinnerEstado.setSelection(20);
        } else if(estado.equals("RO")){
            spinnerEstado.setSelection(12);
        } else if(estado.equals("RR")){
            spinnerEstado.setSelection(22);
        } else if(estado.equals("SC")){
            spinnerEstado.setSelection(23);
        } else if(estado.equals("SP")){
            spinnerEstado.setSelection(24);
        } else if(estado.equals("SE")){
            spinnerEstado.setSelection(25);
        } else if(estado.equals("TO")){
            spinnerEstado.setSelection(26);
        } else {
            spinnerEstado.setSelection(0);
        }
    }

    private void salvarCadastroAPI(){
        //Atribuindo dados a Propriedade
        Propriedade.setLogradouro(edtLogradouro.getText().toString());
        Propriedade.setNumero(Integer.parseInt(edtNumero.getText().toString()));
        Propriedade.setBairro(edtBairro.getText().toString());
        Propriedade.setCidade(edtCidade.getText().toString());
        Propriedade.setEstado(estado);
        Propriedade.setReferencia(edtReferencia.getText().toString());

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
            salvando.dismiss();
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
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