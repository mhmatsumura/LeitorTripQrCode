
package com.copel.mhmatsu.leitortripqrcode;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {



    private Button botaoEscanearCompleto;
    private Button botaoEscanear;
    private static String frasesString,frase,texto,opcao;
    private static int contador;
    private static SharedPreferences preferencesTripsaver;
    private static SharedPreferences.Editor editorPreferencesTripsaver;
    private static ArrayList<String> frases;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencesTripsaver = getSharedPreferences("tripsCadastradas", MODE_PRIVATE);
        editorPreferencesTripsaver = preferencesTripsaver.edit();

        frasesString = preferencesTripsaver.getString("trips","");

        contador=0;
        texto = "";
        frase = " ";
        opcao = "";


        if (!frasesString.isEmpty()){
            frases = new ArrayList<>(Arrays.asList(frasesString.split(";")));
            for (String s:frases){

                contador+=1;
                texto +="\n"+contador+" "+s;

            }
        }else{
            frases = new ArrayList<String>();
        }

        botaoEscanearCompleto = (Button)findViewById(R.id.button);
        botaoEscanear = (Button)findViewById(R.id.btnScan);


        botaoEscanearCompleto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opcao = "";

                Intent intentVaiPraEscanerActivity = new Intent(MainActivity.this, EscanerActivity.class);
                intentVaiPraEscanerActivity.putExtra("textoParaEscrever",texto);
                intentVaiPraEscanerActivity.putExtra("arrayFrases",frases);
                startActivityForResult(intentVaiPraEscanerActivity,0);

            }
        });


        botaoEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);

                integrator.initiateScan();*/

                opcao = "filtrado";

                Intent intentVaiPraEscanerActivity = new Intent(MainActivity.this, EscanerActivity.class);
                intentVaiPraEscanerActivity.putExtra("textoParaEscrever",texto);
                intentVaiPraEscanerActivity.putExtra("arrayFrases",frases);
                startActivityForResult(intentVaiPraEscanerActivity,0);



            }
        });
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {

                if (opcao.equals("filtrado")) {
                    frase = filtraTexto(data.getStringExtra("SCAN_RESULT"));
                }else{
                    frase = data.getStringExtra("SCAN_RESULT");
                }

                if (frase==""){
                    Toast.makeText(MainActivity.this, "QrCode não contém TripSaver!!" , Toast.LENGTH_SHORT).show();
                }else if (!frases.contains(frase)){

                    contador+=1;

                    if (opcao.equals("filtrado")) {
                        texto += contador+" "+frase+"\n";
                    }else{
                        texto += frase+"\n\n";
                    }

                    frases.add(frase);
                    geraArquivo(converteFrasesTexto(frases));

                    editorPreferencesTripsaver.clear();
                    frasesString = TextUtils.join(";", frases);
                    editorPreferencesTripsaver.putString("trips", frasesString);
                    editorPreferencesTripsaver.commit();


                }else{
                    Toast.makeText(MainActivity.this, "Entrada repetida!!" , Toast.LENGTH_SHORT).show();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intentVaiPraEscanerActivity = new Intent(MainActivity.this, EscanerActivity.class);
                intentVaiPraEscanerActivity.putExtra("textoParaEscrever",texto);
                intentVaiPraEscanerActivity.putExtra("arrayFrases",frases);
                startActivityForResult(intentVaiPraEscanerActivity,0);

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }




    }
    public static void apagaFrases() {
        frases.removeAll(frases);
    }

    private String converteFrasesTexto(ArrayList<String> frases) {

        String aux="";
        for (String s:frases){
             aux +="\n"+s;
        }
       return aux;
    }

    public static void apagaPreferencia() {

        editorPreferencesTripsaver.clear();
        editorPreferencesTripsaver.commit();
    }

    private String filtraTexto(String texto) {

        String resultado = "";

        if (texto.contains("SERIAL")){

            String[] textoSeparado = texto.split("\\*");
            resultado = textoSeparado[2].substring(textoSeparado[2].length()-7, textoSeparado[2].length()) + " " + textoSeparado[0].substring(textoSeparado[0].length()-35, textoSeparado[0].length());
        }


        return resultado;

    }

    public String getTexto() {
        return texto;
    }

    public static void setTexto(String t) {
        texto = t;
    }

    public int getContador() {
        return contador;
    }

    public static void setContador(int c) {
        contador = c;
    }

    public static ArrayList<String> getFrases() {
        return frases;
    }

    public static void setFrases(ArrayList<String> frases) {
        MainActivity.frases = frases;
    }

    public void geraArquivo(String conteudoArquivo){
        try

        {

            File arquivoTxt = new File(getExternalFilesDir(null) + "/relacaoTripQr.txt");
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(arquivoTxt), "ISO-8859-1"));

            out.append(conteudoArquivo);
            out.flush();
            out.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }
}

