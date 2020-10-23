package com.ftninformatika.termin21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.net.IpSecManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //prvi korak
    private TextView textView;
    private Switch swSmer;
    private Button bStart;
    //da znamo smer zbog toga sto smo dodali Switch
    private boolean smer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deo prvog koraka
        textView = findViewById(R.id.tvText);
        swSmer=findViewById(R.id.swSmer);
        bStart=findViewById(R.id.bStart);
        //drugi korak
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pravimo metodu
                //startThread();
                //14. Kada neko klikne da dugme pravimo
                startAsynctask();

            }
        });
        // ovo je treci korak, koji kaze true ili false
        swSmer.setText(smer? "Pozitivan" : "Negativan");
        //deveti korak
        swSmer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //negacija
                smer =! smer;
                //da text bude up to date
                swSmer.setText(smer? "Pozitivan" : "Negativan");
            }
        });

    }
    //13. metoda 
    
    private void startAsynctask(){
        MojAsynctask mojAsynctask = new MojAsynctask();
        mojAsynctask.execute(10);
    }
    private void startThread(){
        //deseti korak - pokrecemo app pritiskom na dugme
        bStart.setEnabled(true);
        //druga verzija cetvrtog koraka
        Thread mojThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //peti korak
                int sekunde = 10;
                while (sekunde>1){
                    //ako je pozitivan uvecavamo sek, ako je negativan smer smanjujemo
                    sekunde = smer? sekunde + 1 : sekunde - 1;
                    //sada idemo da osvezimo TextView, samo da ispise kao string (veoma jednostavan kod). Ovo je pozadinska nit i to ne smemo da radimo
                    //zato ddodajemo post!

                    //ovo je sada sedmi korak.
                   updateTextViewAsync(sekunde + "");
                   try{
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

                }
                //osmi korak
                updateTextViewAsync("Boom!!!");
                //11. korak - postujemo, jer je u pitanju pozadinska nit
                bStart.post(new Runnable() {
                    @Override
                    public void run() {
                        bStart.setEnabled(true);
                    }
                });

            }
        });
        mojThread.start();
    }
//        //sada idemo na cetvrti korak. Pravimo Thread i zelimo da ga startujemo. Nismo dodelili referencu niti jednu promenjljivu
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//    }
    //sesti korak - nova metoda.
    private void updateTextViewAsync(final String text){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }
    // 12. korak - prosledimo odakle da krene da broji, da updateuje svoje rezultate i da nista ne vrati (void)

    private class MojAsynctask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bStart.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            bStart.setEnabled(true);
            updateTextViewAsync("Boom!!!");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //setovali smo text koji smo vec dobili. Samo smo jednu vrednost prosledili preko publishProgress!
            textView.setText(values[0] + "");
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int sekunde = integers[0];
            do {
                sekunde = smer? sekunde + 1 : sekunde -1;
                publishProgress(sekunde);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (sekunde>1);
            
            return null;
        }
// Boom ide na kraju i to je onPostExecute

    }
}