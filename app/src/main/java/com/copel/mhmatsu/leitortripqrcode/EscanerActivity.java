package com.copel.mhmatsu.leitortripqrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EscanerActivity extends AppCompatActivity {

    private CompoundBarcodeView qrcodeView;
    private CaptureManager capture;
    private TextView textArea;
    private String texto;
    private Button botaoApagar,botaoEnviar,botaoGerarQrcode;
    private ArrayList<String> frases;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner);

        qrcodeView = (CompoundBarcodeView)findViewById(R.id.escaner_view);
        qrcodeView.setStatusText("Posicione a câmera sobre o QrCode");

        textArea = (TextView) findViewById(R.id.bloco_notas);

        Intent intent = getIntent();
        texto = (String) intent.getSerializableExtra("textoParaEscrever");
        textArea.setText(texto);

        frases = (ArrayList<String>) intent.getSerializableExtra("arrayFrases");

        /*ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);*/

        botaoApagar = (Button) findViewById(R.id.botaoApagar);
        botaoApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(EscanerActivity.this)

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("APAGAR REGISTROS!")
                        .setMessage("Você tem certeza que deseja apagar todos os registros?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                textArea.setText("");
                                MainActivity.setTexto("");
                                MainActivity.setContador(0);
                                MainActivity.apagaFrases();
                                MainActivity.apagaPreferencia();
                            }
                        })

                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });

        botaoGerarQrcode = (Button)findViewById(R.id.botaoGerarQrcode);
        botaoGerarQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intentVaiPraQRcodeActivity = new Intent(EscanerActivity.this, QRcodeActivity.class);
                intentVaiPraQRcodeActivity.putExtra("arrayFrases",frases);
                startActivity(intentVaiPraQRcodeActivity);

            }
        });

        botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t="";

                for (String s:MainActivity.getFrases()){
                    t +="\n"+s;
                }

                new emailAssincrono().execute(sendEmail(t));

            }
        });

        capture = new CaptureManager( this, qrcodeView );
        capture.initializeFromIntent( getIntent(), savedInstanceState );

        capture.decode();

    }
    @Override
    protected void onResume() {
        Log.d("cheguei:","on resume");
        super.onResume();

        capture.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    private class emailAssincrono extends AsyncTask<Intent, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Intent... intents) {

            Intent ei = intents[0];
            startActivity(ei);
            return null;
        }
    }






    protected Intent sendEmail(String texto) {




        //String[] TO = {"mhmatsumura@yahoo.com.br"};

        //String email1 = serv.getExecutor1().substring(serv.getExecutor1().indexOf("-")+1,serv.getExecutor1().length());
        //String email2 = serv.getExecutor2().substring(serv.getExecutor2().indexOf("-")+1,serv.getExecutor2().length());
        //String[] TO = {email1+"@COPEL.COM",email2+"@COPEL.COM"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Relação de rquipamentos do Leitor de qrcode Tripsaver. ");

        emailIntent.putExtra(Intent.EXTRA_TEXT, texto);

        //emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml(tela));

        /*String  caminhoPdf = getExternalFilesDir(null) + "/Servico.pdf";
        File arquivoPdf = new File(caminhoPdf);
        Uri pdfURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", arquivoPdf);
        emailIntent.putExtra(Intent.EXTRA_STREAM, pdfURI);*/

        return emailIntent;



    }


}
