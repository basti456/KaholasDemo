package com.example.kaholasdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText writeSomething;
    Button generateSpeech;
    TextToSpeech tt;
    TextView txtQues;
    String text, s1, s2;
    ImageView m;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tt = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tt.setLanguage(Locale.ENGLISH);
                }
            }
        });
        writeSomething = findViewById(R.id.etText);
        generateSpeech = findViewById(R.id.btGenerate);
        txtQues = findViewById(R.id.txtQuestion);
        m = findViewById(R.id.ivMic);
        text = txtQues.getText().toString();
        s1 = text.replace("=", "");
        s2 = s1.replace("?", "");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tt.speak(s2 + "  Please write the answer", TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 100);


        while (tt.isSpeaking()) {
            writeSomething.setEnabled(false);
            generateSpeech.setClickable(false);
        }
        writeSomething.setEnabled(true);
        generateSpeech.setClickable(true);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(MainActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        generateSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writeSomething.getText().toString().equals("")){
                    return;
                }
                char[] chars = text.toCharArray();
                int i = 0;
                StringBuilder temp = new StringBuilder();
                String texts = "";
                for (i = 0; i < chars.length; i++) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        temp.append(chars[i]);
                    } else {
                        break;
                    }
                }
                int num1 = Integer.parseInt(String.valueOf(temp));
                for (; i < chars.length; i++) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        temp.append(chars[i]);
                    } else {
                        break;
                    }
                }
                int num2 = Integer.parseInt(String.valueOf(temp));
                int req = num1 * num2;
                if (writeSomething.getText().toString().equals(String.valueOf(req))) {
                    texts = "Wow, You entered Correct answer";
                } else {
                    texts = "Incorrect,please enter correct answer";
                }
                writeSomething.setText("");
                tt.speak(texts, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                writeSomething.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }
    //    @Override
//    protected void onStart() {
//        super.onStart();
//        writeSomething=findViewById(R.id.etText);
//        txtQues=findViewById(R.id.txtQuestion);
//        tt=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status!=TextToSpeech.ERROR){
//                    tt.setLanguage(Locale.ENGLISH);
//                }
//            }
//        });
//        text=txtQues.getText().toString();
//        s1=text.replace("=","");
//        s2=s1.replace("?","");
//        tt.speak(s2,TextToSpeech.QUEUE_FLUSH,null);
//    }
}