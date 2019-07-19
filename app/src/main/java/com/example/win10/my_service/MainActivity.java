package com.example.win10.my_service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener
{



    private LinearLayout rootView;
    Handler handler=new Handler();
    ImageView imageView;
    public static final int CAMERA_REQUEST=9999;
    @Override
    public void onInit(int i)
    {
        tts.setLanguage(Locale.UK);
        tts.setSpeechRate(1.0f);
        //tts.speak("Hello sir.Whats up?The service has begun!",TextToSpeech.QUEUE_ADD,null,null);
    }

    private Button startButton, endButton,dateButton,timeButton;

    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(getApplicationContext(),this);

        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView);
        rootView = (LinearLayout) findViewById(R.id.rootview);
        startButton = new Button(getApplicationContext());

                /*Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animationset);
                a.reset();
                imageView.clearAnimation();
                 imageView.startAnimation(a);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2300);

*/



        finish();
        startService(new Intent(getApplicationContext(), Service1.class));

        startButton.setText("Start Service");
        //rootView.addView(startButton);
        endButton = new Button(getApplicationContext());
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(),Service1.class));
            }
        });
        endButton.setText("End Service");
        //rootView.addView(endButton);
        dateButton=new Button(getApplicationContext());
        //rootView.addView(dateButton);
        dateButton.setText("Date");
        timeButton=new Button(getApplicationContext());
        //rootView.addView(timeButton);
        timeButton.setText("Time");

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();

            }
        });
    }
    public void OpenCamera(View view)
    {
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST);
    }



    public final static int REQUEST_CODE = 10101;

    @Override
    protected void onDestroy() {
        tts.stop();
        tts.shutdown();
        super.onDestroy();
    }

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == CAMERA_REQUEST)
        {
              Bitmap bitmap =(Bitmap) data.getExtras().get("data");
              imageView.setImageBitmap(bitmap);
        }
    }
}
