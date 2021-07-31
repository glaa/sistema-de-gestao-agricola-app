package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaAtualizarPropriedade;
import com.sistemadegestaoagricola.conexao.RotaCadastrarPropriedade;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.primeiroacesso.CadastroLocalizacaoActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InformacoesActivity extends AppCompatActivity {

    private LinearLayout llVoltar;
    private EditText edtTamanho;
    private Button btAtualizar;
    private Button btPoco;
    private Button btCisterna;
    private Button btCompesa;
    private Button btOutro;
    private boolean pocoMarcado = false;
    private boolean cisternaMarcado = false;
    private boolean compesaMarcado = false;
    private boolean outroMarcado = false;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    CarregarDialog carregarDialog = new CarregarDialog(InformacoesActivity.this);
    AlertDialog atualizando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        llVoltar = findViewById(R.id.llVoltarInformacoes);
        edtTamanho = findViewById(R.id.edtTamanhoInformacoes);
        btAtualizar = findViewById(R.id.btAtualizarInformacoes);
        btPoco = findViewById(R.id.btPocoInformacoes);
        btCisterna = findViewById(R.id.btCisternaInformacoes);
        btCompesa = findViewById(R.id.btCompesaInformacoes);
        btOutro = findViewById(R.id.btOutroInformacoes);

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { voltar(); }
        });

        preencherInformacoes();

        btPoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pocoMarcado){
                    marcar(btPoco);
                    pocoMarcado = true;
                } else {
                    desmarcar(btPoco);
                    pocoMarcado = false;
                }
            }
        });

        btCisterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cisternaMarcado){
                    marcar(btCisterna);
                    cisternaMarcado = true;
                } else {
                    desmarcar(btCisterna);
                    cisternaMarcado = false;
                }
            }
        });

        btCompesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!compesaMarcado){
                    marcar(btCompesa);
                    compesaMarcado = true;
                } else {
                    desmarcar(btCompesa);
                    compesaMarcado = false;
                }
            }
        });

        btOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!outroMarcado){
                    marcar(btOutro);
                    outroMarcado = true;
                } else {
                    desmarcar(btOutro);
                    outroMarcado = false;
                }
            }
        });

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> fonte = new ArrayList<>();
                if(pocoMarcado){
                    fonte.add("Poço");
                }
                if(cisternaMarcado){
                    fonte.add("Cisterna");
                }
                if(compesaMarcado){
                    fonte.add("Compesa");
                }
                if(outroMarcado){
                    fonte.add("Outro");
                }

                String strTamanho = edtTamanho.getText().toString();
                if(!strTamanho.isEmpty()){
                    int tamanho = Integer.parseInt(edtTamanho.getText().toString());
                    if(tamanho > 0){
                        if(fonte.isEmpty()){
                            Toast.makeText(InformacoesActivity.this, "Selecione a(s) fonte(s) de água", Toast.LENGTH_SHORT).show();
                        } else {
                            atualizando = carregarDialog.criarAtualizando();
                            atualizando.show();
                            atualizarPropriedade(fonte);
                        }
                    } else {
                        Toast.makeText(InformacoesActivity.this, "Digite o tamanho da propriedade", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InformacoesActivity.this, "Digite o tamanho da propriedade", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void voltar(){
        onBackPressed();
        finish();
    }

    private void preencherInformacoes(){
        //Preencher Tamanho
        edtTamanho.setText(String.valueOf(Propriedade.getTamanho()));
        //Preencher Fonte
        Log.d("testeX", "fonte = " + Propriedade.getFonteAgua());
        String[] fontes = Propriedade.getFonteAgua().split(",");
        for(int i=0; i < fontes.length; i++){
            if(fontes[i].equals("Poço")){
                marcar(btPoco);
                pocoMarcado = true;
            } else if(fontes[i].equals("Cisterna")){
                marcar(btCisterna);
                cisternaMarcado = true;
            } else if(fontes[i].equals("Compesa")){
                marcar(btCompesa);
                compesaMarcado = true;
            } else if(fontes[i].equals("Outro")){
                marcar(btOutro);
                outroMarcado = true;
            }
        }
    }

    private void marcar(Button botao){
        botao.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        botao.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.verde_escuro));
        botao.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_check_24,0);
    }

    private void desmarcar(Button botao){
        botao.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.verde_escuro));
        botao.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.white));
        botao.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    private void atualizarPropriedade(ArrayList<String> fonte){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Atribuindo dados a Propriedade
                Propriedade.setTamanho(Integer.parseInt(edtTamanho.getText().toString()));

                String fonteAgua = "";
                for(String f: fonte){
                    fonteAgua += f + ",";
                }
                fonteAgua = fonteAgua.substring(0,fonteAgua.length()-1);

                Propriedade.setFonteAgua(fonteAgua);

                RotaAtualizarPropriedade rotaAtualizarPropriedade = new RotaAtualizarPropriedade();
                Future<ConexaoAPI> future = threadpool.submit(rotaAtualizarPropriedade);

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
                                    Toast.makeText(InformacoesActivity.this, "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Erro ao salvar
                                    Toast.makeText(InformacoesActivity.this, "Dados não puderam ser salvos", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(InformacoesActivity.this, HomeActivity.class);
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