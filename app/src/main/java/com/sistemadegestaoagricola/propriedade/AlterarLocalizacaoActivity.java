package com.sistemadegestaoagricola.propriedade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.principal.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaAtualizarEndereco;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlterarLocalizacaoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private LinearLayout llVoltar;
    private EditText edtLogradouro;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtCidade;
    private EditText edtReferencia;
    private EditText edtCep;
    private Button btAtualizar;
    private Spinner spinnerEstado;
    private String estado;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    CarregarDialog carregarDialog = new CarregarDialog(AlterarLocalizacaoActivity.this);
    AlertDialog atualizando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_localizacao);

        llVoltar = findViewById(R.id.llVoltarAlterarLocalizacao);
        edtLogradouro = findViewById(R.id.edtLogradouroAlterarLocalizacao);
        edtNumero = findViewById(R.id.edtNumeroAlterarLocalizacao);
        edtBairro = findViewById(R.id.edtBairroAlterarLocalizacao);
        edtCidade = findViewById(R.id.edtCidadeAlterarLocalizacao);
        edtReferencia = findViewById(R.id.edtReferenciaAlterarLocalizacao);
        edtCep = findViewById(R.id.edtCepAlterarLocalizacao);
        btAtualizar = findViewById(R.id.btAtualizarAlterarLocalizacao);

        spinnerEstado = findViewById(R.id.spinnerAlterarLocalizacao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.estados, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setOnItemSelectedListener(this);

        preencherCampos();

        edtCidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String cidade = edtCidade.getText().toString();
                /** Apresenta mensagem de campo obrigatório */
                if(!hasFocus){
                    if(cidade.isEmpty()){
                        findViewById(R.id.tvCidadaInvalidaAlterarLocalizacao).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tvCidadaInvalidaAlterarLocalizacao).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.tvCidadaInvalidaAlterarLocalizacao).setVisibility(View.GONE);
                }
            }
        });

        edtCep.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String strCep = edtCep.getText().toString();
                if(i == 66){
                    if(!strCep.isEmpty()){
                        if(strCep.length() != 9){
                            Toast.makeText(AlterarLocalizacaoActivity.this, "O CEP deve possui 8 números ou não será salvo", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Util.mascaraCepOnKeyListener(edtCep,i);
                }
                return false;
            }
        });

        edtCep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    Toast.makeText(AlterarLocalizacaoActivity.this, "O CEP deve possui 8 números ou não será salvo", Toast.LENGTH_LONG).show();
                }
            }
        });

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar();
            }
        });

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizando = carregarDialog.criarAtualizando();

                int tamanhoCep = edtCep.getText().toString().length();
                if(tamanhoCep > 0 && tamanhoCep < 9){
                    AlertDialog alertaCep = carregarDialog.criarDialogCepInvalido();
                    alertaCep.setButton(DialogInterface.BUTTON_POSITIVE, "Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            atualizando.show();
                            btAtualizar.setClickable(false);
                            verificarCamposESalvar();
                        }
                    });
                    alertaCep.setButton(DialogInterface.BUTTON_NEGATIVE, "Corrigir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            edtCep.requestFocus();
                        }
                    });
                    alertaCep.show();
                } else {
                    atualizando.show();
                    btAtualizar.setClickable(false);
                    verificarCamposESalvar();
                }
            }
        });
    }

    private void voltar(){
        onBackPressed();
        finish();
    }

    private void preencherCampos(){
        edtLogradouro.setText(Propriedade.getLogradouro());
        edtNumero.setText(String.valueOf(Propriedade.getNumero()));
        edtBairro.setText(Propriedade.getBairro());
        edtCidade.setText(Propriedade.getCidade());
        edtReferencia.setText(Propriedade.getReferencia());
        edtCep.setText(Propriedade.getCep());

        estado = Propriedade.getEstado();

        if (estado.equals("AC")) {
            spinnerEstado.setSelection(1);
        } else if (estado.equals("AL")) {
            spinnerEstado.setSelection(2);
        } else if (estado.equals("AP")) {
            spinnerEstado.setSelection(3);
        } else if (estado.equals("AM")) {
            spinnerEstado.setSelection(4);
        } else if (estado.equals("BA")) {
            spinnerEstado.setSelection(5);
        } else if (estado.equals("CE")) {
            spinnerEstado.setSelection(6);
        } else if (estado.equals("DF")) {
            spinnerEstado.setSelection(7);
        } else if (estado.equals("ES")) {
            spinnerEstado.setSelection(8);
        } else if (estado.equals("GO")) {
            spinnerEstado.setSelection(9);
        } else if (estado.equals("MA")) {
            spinnerEstado.setSelection(10);
        } else if (estado.equals("MT")) {
            spinnerEstado.setSelection(11);
        } else if (estado.equals("MS")) {
            spinnerEstado.setSelection(12);
        } else if (estado.equals("MG")) {
            spinnerEstado.setSelection(13);
        } else if (estado.equals("PA")) {
            spinnerEstado.setSelection(14);
        } else if (estado.equals("PB")) {
            spinnerEstado.setSelection(15);
        } else if (estado.equals("PR")) {
            spinnerEstado.setSelection(16);
        } else if (estado.equals("PE")) {
            spinnerEstado.setSelection(0);
        } else if (estado.equals("PI")) {
            spinnerEstado.setSelection(17);
        } else if (estado.equals("RJ")) {
            spinnerEstado.setSelection(18);
        } else if (estado.equals("RN")) {
            spinnerEstado.setSelection(19);
        } else if (estado.equals("RS")) {
            spinnerEstado.setSelection(20);
        } else if (estado.equals("RO")) {
            spinnerEstado.setSelection(12);
        } else if (estado.equals("RR")) {
            spinnerEstado.setSelection(22);
        } else if (estado.equals("SC")) {
            spinnerEstado.setSelection(23);
        } else if (estado.equals("SP")) {
            spinnerEstado.setSelection(24);
        } else if (estado.equals("SE")) {
            spinnerEstado.setSelection(25);
        } else if (estado.equals("TO")) {
            spinnerEstado.setSelection(26);
        } else {
            spinnerEstado.setSelection(0);
        }
    }

    private void verificarCamposESalvar(){
        if(!edtCidade.getText().toString().isEmpty() && !estado.isEmpty()){
            salvarCadastroAPI();
        } else {
            findViewById(R.id.tvCidadaInvalidaAlterarLocalizacao).setVisibility(View.VISIBLE);
            Toast.makeText(AlterarLocalizacaoActivity.this, "Preencha o campo Cidade", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String nomeEstado = adapterView.getItemAtPosition(i).toString();
        estado = Propriedade.siglaEstado(nomeEstado);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void salvarCadastroAPI(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Atribuindo dados a Propriedade
                Propriedade.setLogradouro(edtLogradouro.getText().toString());
                Propriedade.setBairro(edtBairro.getText().toString());
                Propriedade.setCidade(edtCidade.getText().toString());
                Propriedade.setEstado(estado);
                Propriedade.setReferencia(edtReferencia.getText().toString());

                if(!edtNumero.getText().toString().isEmpty()){
                    Propriedade.setNumero(Integer.parseInt(edtNumero.getText().toString()));
                } else {
                    Propriedade.setNumero(0);
                }

                if(edtCep.getText().toString().length() == 9){
                    Propriedade.setCep(edtCep.getText().toString());
                } else {
                    Propriedade.setCep(null);
                }

                RotaAtualizarEndereco rotaAtualizarEndereco = new RotaAtualizarEndereco();
                Future<ConexaoAPI> future = threadpool.submit(rotaAtualizarEndereco);

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
                        if(status == 200){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Salvo com sucesso
                                    Toast.makeText(AlterarLocalizacaoActivity.this, "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Erro ao salvar
                                    Toast.makeText(AlterarLocalizacaoActivity.this, "Dados não puderam ser salvos", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        Erro(mensagensExceptions[0],mensagensExceptions[1]);
                    }

                } catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    Erro("Falha na conexão","Tente novamente em alguns minutos");
                } finally {
                    if(conexao != null){
                        conexao.fechar();
                    }
                    atualizando.dismiss();
                    Intent intent = new Intent(AlterarLocalizacaoActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
        thread.start();
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }
}