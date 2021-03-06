package com.example.administrator.helloandroid;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button button;
    private URL _url;
    private String _textColor;
    private int _mode = 0;
    private String _textURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);

        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }

        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.send);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final EditText editText1 = (EditText) findViewById(R.id.editText2);
//        initSpinner();

        //setContentView();
        final Light light = new Light("192.168.4.1", 23333);
        Button.OnClickListener listener = new Button.OnClickListener(){
            public void onClick(View view){

                Thread thread1 = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            light.setStatic(new long[]{0x100});
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread1.start();

                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            light.setFlow(2000, new long[]{2000, 0x10000, 0xf30000});
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread2.start();

                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            light.setTimer(1, new long[]{200}, 3000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread3.start();
            }
        };
        button.setOnClickListener(listener);
    }

    private void initSpinner(){
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.mode,android.R.layout.simple_spinner_dropdown_item);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Mode");
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id){
                String[] strMode = getResources().getStringArray(R.array.mode);
                Toast.makeText(MainActivity.this, "Mode:"+strMode[pos],Toast.LENGTH_SHORT).show();
                 _mode = pos;
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0){
               arg0.setVisibility(View.VISIBLE);
            }

        });
    }

}
