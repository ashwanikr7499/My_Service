package com.example.win10.my_service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class Service1 extends Service implements TextToSpeech.OnInitListener
{
    private SpeechRecognizer speechRecognizer;
    private Notification notification;
    private Thread workerThread;
    private boolean canContinue = true;
    private Handler handler;
    private TextToSpeech tts;
    private FloatingButton floatingButton;
    private Button dateButton, timeButton;
    private WindowManager windowManager;
    private int curX, curY;
    private LinearLayout linearLayout,linearLayout2,l1,l2,l3,l4;
    private boolean isMainWindowOpen = false;
    private LinkedList<ReminderRecord> reminderList;
    private static final int ONGOING_NOTIFICATION = 1;
    private TextView tv1,tv2,tv3,tv4,tvrow3;
    private EditText et31,et32 ;
    private Button b[]=new Button[4];
    private Button speechButton, oxforddictionary;
    private  String word="A" ;

    private int previoushour;
    Calendar c = Calendar.getInstance();



    void showToast(String s)
    {
        handler.post(new ToastRunnable(s));
    }



    @Override
    public void onInit(int i)
    {
        tts.setLanguage(Locale.UK);
        tts.setSpeechRate(1.5f);
        tts.speak("Hello sir.Whats up?The service has begun!", TextToSpeech.QUEUE_ADD, null, null);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener()
        {
            @Override
            public void onReadyForSpeech(Bundle bundle)
            {

            }

            @Override
            public void onBeginningOfSpeech()
            {

            }

            @Override
            public void onRmsChanged(float v)
            {

            }

            @Override
            public void onBufferReceived(byte[] bytes)
            {

            }

            @Override
            public void onEndOfSpeech()
            {

            }

            @Override
            public void onError(int i)
            {

            }

            @Override
            public void onResults(Bundle bundle)
            {
                ArrayList data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String mpspeech=(String)data.get(0);
                Toast.makeText(getApplicationContext(),mpspeech,Toast.LENGTH_SHORT).show();
                if(mpspeech.contains("hello"))
                {
                    tts.speak("hi sir? Whats up",TextToSpeech.QUEUE_ADD,null,null);
                }
                else if(mpspeech.contains("introduction") || mpspeech.contains("developer"))
                {
                    tts.speak("Sir, I am developed by Ashwani in Android Studio version 3.1.3 for api level 25.He is a very good person.He helps everyone",TextToSpeech.QUEUE_ADD,null,null);
                }
                else if(mpspeech.contains("weather"))
                {
                    tts.speak("Sir its 36 degree C. Its hot and sunny.",TextToSpeech.QUEUE_ADD,null,null);
                }
                else if(mpspeech.contains("time"))
                {
                    Calendar c = Calendar.getInstance();
                    String msg = "The time is " + c.get(Calendar.HOUR) + " " + c.get(Calendar.MINUTE) + " " + c.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());
                    tts.speak(msg, TextToSpeech.QUEUE_ADD, null, null);

                }
                else if(mpspeech.contains("date"))
                {
                    Calendar c = Calendar.getInstance();
                    String msg = "The date is " + c.get(Calendar.DATE) + ":" + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + c.get(Calendar.YEAR);
                    tts.speak(msg, TextToSpeech.QUEUE_ADD, null, null);

                }
                else if(mpspeech.contains("battery"))
                {
                    BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
                    String s="The battery level is";
                    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    s=s+" "+batLevel+" percentage. ";
                    if(BatteryManager.BATTERY_PLUGGED_AC==1)
                        s=s+"Battery is plugged to AC charging";
                    else if(BatteryManager.BATTERY_PLUGGED_USB==2)
                        s=s+"Battery plugged to USB";
                    else
                        s=s+"Battery is dicharging";

                    tts.speak(s,TextToSpeech.QUEUE_ADD,null,null);
                }
                else if(mpspeech.contains("meaning"))
                {
                    word=mpspeech.substring(mpspeech.lastIndexOf(" "));
                    oxforddictionary.performClick();
                }
                else
                {

                    tts.speak("I don't know what you mean by "+mpspeech,TextToSpeech.QUEUE_ADD,null,null);
                }



            }

            @Override
            public void onPartialResults(Bundle bundle)
            {

            }

            @Override
            public void onEvent(int i, Bundle bundle)
            {

            }

        });
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                showToast("Service Started");
                while (canContinue)
                {

                    Calendar c = Calendar.getInstance();
                    Iterator<ReminderRecord> it = reminderList.iterator();
                    while (it.hasNext())
                    {
                        ReminderRecord rr = it.next();
                        int h = c.get(Calendar.HOUR_OF_DAY);
                        int m = (c.get(Calendar.MINUTE));
                        int curh = rr.time.getHours();
                        int curm = rr.time.getMinutes();
                        //if(BatteryManager.)
                        if(m==0 && previoushour+1==h )
                        {
                            tts.speak("It's "+ h +"o clock",TextToSpeech.QUEUE_ADD,null,null);

                            String name =  et31.getText().toString();
                            speakAndWait(name);
                            previoushour=h;
                        }
                        if ((h == curh && (curm - m) <= 1 && (curm - m) >= 0))
                        {
                            String msg = rr.text;
                            tts.speak(msg, TextToSpeech.QUEUE_ADD, null, null);
                            String name =  et31.getText().toString();
                            speakAndWait(name);
                            it.remove();
                        }
                    }
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        });

        workerThread.start();

    }

    private class ToastRunnable implements Runnable
    {
        private String s;

        public ToastRunnable(String s)
        {
            this.s = s;
        }

        @Override
        public void run()
        {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    private class TextChangerRunnable implements Runnable
    {
        @Override
        public void run() {
            tv.setText(txt);
        }

        private TextView tv;
        private String txt;
        TextChangerRunnable(TextView tv,String txt)
        {
            this.tv = tv;
            this.txt = txt;

        }


    }

    class MyMediaPlayer implements MediaPlayer.OnCompletionListener
    {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stopPlaying();
        }

        private boolean isPlaying;
        private MediaPlayer mp;
        MyMediaPlayer()
        {
            isPlaying = false;
        }
        void playURL(String url) {
            if (!isPlaying) {
                isPlaying = true;
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(url);
                    mp.prepare();
                    mp.start();
                    mp.setOnCompletionListener(this);
                } catch (IOException e) {


                }
            } else {
                isPlaying = false;
                stopPlaying();
            }
        }
        private void stopPlaying() {
            mp.release();
            mp = null;
        }

    }

    MyMediaPlayer mediaPlayer = new MyMediaPlayer();
    @Override
    public void onCreate()
    {
        super.onCreate();
        handler = new Handler();
        tts = new TextToSpeech(getApplicationContext(), this);
        reminderList = new LinkedList<>();
        reminderList.add(new ReminderRecord(new Date(2018, 7, 4, 23, 32, 30), "Its past 6 pm 10 min"));
        reminderList.add(new ReminderRecord(new Date(2018, 7, 4, 23, 33, 0), "Its past 12 pm 10 min"));
        reminderList.add(new ReminderRecord(new Date(2018, 7, 4, 23, 34, 30), "Its past 10am 10 min"));
        //reminderList.add(new ReminderRecord(new Date(2018,7,4,6,10,30),"Its past 6 am 10 min"));
        //reminderList.add(new ReminderRecord(new Date(2018,7,4,2,10,30),"Its past 2 am 10 min"));


        curX = 0;
        curY = 100;

        floatingButton = new FloatingButton(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        floatingButton.setBackgroundColor(Color.RED);

        params.x = curX;
        params.y = curY;
        params.height = 60;
        params.width = 100;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingButton, params);

        floatingButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                //String s = motionEvent.getX() + "," + motionEvent.getY();
                //curX = curX + (int)motionEvent.getX();
                if(isMainWindowOpen)
                    return false;
                if (Math.abs(motionEvent.getY()) < 20)
                    return false;
                curY = curY + (int) motionEvent.getY();

               // Toast.makeText(getApplicationContext(),"change more thn 100",Toast.LENGTH_SHORT).show();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.x = curX;
                params.y = curY;
                params.height = 60;
                params.width = 100;
                windowManager.updateViewLayout(floatingButton,params);
                return false;

            }
        });
        Calendar c = Calendar.getInstance();
        String msg = "The time is " + c.get(Calendar.HOUR) + " " + c.get(Calendar.MINUTE) + " " + c.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());
        tts.speak(msg, TextToSpeech.QUEUE_ADD, null, null);
        linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.RED);

        linearLayout2=new LinearLayout(getApplicationContext());


        LinearLayout row1 = new LinearLayout(getApplicationContext());
        row1.setOrientation(LinearLayout.HORIZONTAL);

        final LinearLayout row2 = new LinearLayout(getApplicationContext());
        row2.setOrientation(LinearLayout.VERTICAL);



        LinearLayout row3=new LinearLayout(getApplicationContext());

        tvrow3 =new TextView(getApplicationContext());
       // tvrow3.setText("Created and Developed by Ashwani");
        row3.addView(tvrow3);
        row3.setGravity(Gravity.BOTTOM);
        linearLayout2.addView(linearLayout);
        linearLayout.addView(row1);
        linearLayout.addView(row2);
        linearLayout.addView(row3);

        tv1=new TextView(getApplicationContext());
        tv2=new TextView(getApplicationContext());
        tv3=new TextView(getApplicationContext());
        tv4=new TextView(getApplicationContext());
        tv4.setText("The service is stopping");

        //tvrow3 =new TextView(getApplicationContext());
        //tvrow3.setText("Created and Developed by Ashwani");

         et31=new EditText(getApplicationContext());
         et31.setText("Messages");
         et31.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {

                 et31.requestFocus();
             }
         });

       //  et32=new EditText(getApplicationContext());
        // et32.setText("message");


        //Calendar c1= Calendar.getInstance();
       // String msg1 = "The time is " + c.get(Calendar.HOUR) + " " + c.get(Calendar.MINUTE) + " " + c.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());
        //tv1.setText(msg1);
         l1 = new LinearLayout(getApplicationContext());
         l1.addView(tv1);
         l2 = new LinearLayout(getApplicationContext());
         l2.addView(tv2);
         l3 = new LinearLayout(getApplicationContext());
         l3.setOrientation(LinearLayout.HORIZONTAL);
         l3.addView(et31);
         //l3.addView(et32);
         l4 = new LinearLayout(getApplicationContext());
         l4.addView(tv4);

         GradientDrawable gd=new GradientDrawable();
         gd.setCornerRadius(100);
         for(int i=0;i<4;i++)
         {
             b[i] = new Button(getApplicationContext());
            // b[i].setBackground(gd);
         }
            speechButton = new Button(getApplicationContext());
         speechButton.setBackgroundResource(R.drawable.speechbuttonimage1);
         speechButton.setPadding(0,0,0,0);
        speechButton.setLayoutParams (new WindowManager.LayoutParams(75,75,0,0,50,50,50));
       //  speechButton.setText("Speech Button recognizier");

         speechButton.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {
                 row2.removeAllViews();
                 Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animationset);
                 final TextView txt= new TextView(getApplicationContext());
                 txt.setHeight(200);
                 txt.setTextSize(32);
                 final String str[]={"Listening","Listening.","Listening..","Listening...","Listening....","Listening.....",""};

                 for( int i=0;i<7;i++)
                 {
                     handler.postDelayed(new TextChangerRunnable(txt, str[i]), (i + 1) * 400);
                 }

                 row2.addView(txt);
                 //txt.setAnimation(a);


                      speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));

                      //row2.removeAllViews();
             }
         });

         row1.addView(speechButton);

         oxforddictionary =new Button(getApplicationContext());
         oxforddictionary.setBackgroundResource(R.drawable.oxforddictionaryimage);
        oxforddictionary.setLayoutParams (new WindowManager.LayoutParams(75,75,0,0,50,50,50));
        oxforddictionary.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        if(l1.getParent() == null)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //TODO: replace with your own app id and app key
                    final String language = "en";

                    final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
                    String u = "https://od-api.oxforddictionaries.com/api/v1/entries/" + language + "/" + word_id;

                    final String app_id = "21421ceb";
                    final String app_key = "df298ba67a687821787a753353153db2";

                    try {
                        URL url = new URL(u);
                        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                        urlConnection.setRequestProperty("Accept","application/json");
                        urlConnection.setRequestProperty("app_id",app_id);
                        urlConnection.setRequestProperty("app_key",app_key);

                        speakAndWait("response code is: " + urlConnection.getResponseCode());
                        // read the output from the server
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                        String line = null;
                        String data="";
                        while ((line = reader.readLine()) != null) {
                            //speakAndWait(line);
                            data += line;
                        }
                        JSONObject rootNode = new JSONObject(data);
                        String providerName = (String)rootNode.getJSONObject("metadata").get("provider");
                        speakAndWait("Provider is: " + providerName);

                       // LinkedList<String> definitions = new LinkedList<>();
                        //LinkedList<String> domains=new LinkedList<>();
                        //LinkedList<String> examples=new LinkedList<>();

                        JSONArray results = rootNode.getJSONArray("results");

                        tts.setSpeechRate(1.0f);
                        tts.speak("Pronunciation is "+word,TextToSpeech.QUEUE_ADD,null,null);
                        tts.setSpeechRate(1.5f);
                         int c=0;
                        for(int i=0;i<results.length();i++)
                        {
                            JSONArray lexicalEntries = results.getJSONObject(i).getJSONArray("lexicalEntries");
                            JSONArray pronunciations= lexicalEntries.getJSONObject(i).getJSONArray("pronunciations");
                            if(pronunciations.getJSONObject(0).has("pronunciations"))
                            {
                                speakAndWait("Pronunciation ");
                                mediaPlayer.playURL(pronunciations.getJSONObject(0).getString("audioFile"));
                            }
                            for(int j =0;j<lexicalEntries.length();j++)
                            {
                                JSONArray entries = lexicalEntries.getJSONObject(j).getJSONArray("entries");

                                for(int k =0;k<entries.length();k++)
                                {
                                    JSONArray senses = entries.getJSONObject(k).getJSONArray("senses");
                                    for(int l =0;l<senses.length();l++)
                                    {

                                        if(senses.getJSONObject(l).has("definitions"))
                                        {
                                            //definitions.add(senses.getJSONObject(l).getString("definitions"));
                                            tts.speak("definition"+(++c)+senses.getJSONObject(l).getString("definitions"),TextToSpeech.QUEUE_ADD,null,null);
                                        }
                                        if(senses.getJSONObject(l).has("domains"))
                                        {
                                           // domains.add(senses.getJSONObject(l).getString("domains"));
                                            tts.speak("domain"+c+senses.getJSONObject(l).getString("domains"),TextToSpeech.QUEUE_ADD,null,null);

                                        }
                                        if(senses.getJSONObject(l).has("examples"))
                                        {
                                           // examples.add(senses.getJSONObject(l).getString("examples"));
                                            tts.speak("Example"+c+senses.getJSONObject(l).getString("examples"),TextToSpeech.QUEUE_ADD,null,null);
                                        }
                                    }

                                }
                            }
                        }
                       /* for(int i=0;i<definitions.size();i++)
                        {
                            speakAndWait("definition" + (i + 1) + definitions.get(i));
                            //speakAndWait("domain"+(i+1)+domains.get(i));
                            //speakAndWait("example"+(i+1)+examples.get(i));
                        }*/
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        speakAndWait("Exception");
                        showToast(e.toString());
                        //speakAndWait(e.toString());
                    }

                }
            }).start();


        }
        else
        {
            row2.removeView(l1);
            b[0].setBackgroundResource(android.R.drawable.btn_default);
        }

    }
});


        b[0].setText("Time");

        b[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                row2.removeAllViews();
                b[0].setBackgroundColor(Color.rgb(255,0,0));
                for(int i=0;i<3;i++)
                {
                    if (i != 0)
                        b[i].setBackgroundResource(android.R.drawable.btn_default);
                }
                Calendar c = Calendar.getInstance();
                String msg = "The time is " + c.get(Calendar.HOUR) + " " + c.get(Calendar.MINUTE) + " " + c.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());
                tts.speak(msg, TextToSpeech.QUEUE_ADD, null, null);
                tv1.setText(msg);
                row2.addView(l1);


            }
        });


        b[1].setText("Date");
        b[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (l2.getParent() == null)
                {
                    row2.removeAllViews();
                    b[1].setBackgroundColor(Color.RED);
                    Calendar c = Calendar.getInstance();
                    String msg = "The date is " + c.get(Calendar.DATE) + ":" + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + c.get(Calendar.YEAR);
                    tts.speak(msg, TextToSpeech.QUEUE_ADD, null, null);
                    tv2.setText(msg);
                    row2.addView(l2);
                    for(int i=0;i<3;i++)
                    {
                        if (i != 1)
                            b[i].setBackgroundResource(android.R.drawable.btn_default);
                    }
                }
                else
                {
                    row2.removeView(l2);
                    b[1].setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        });

        b[2].setText("Reminders");
        b[2].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (l3.getParent() == null)
                {
                    row2.removeAllViews();
                    b[2].setBackgroundColor(Color.RED);

                    row2.addView(l3);
                    tts.speak("Reminder button clicked", TextToSpeech.QUEUE_ADD,null,null);
                    String name =  et31.getText().toString();
                   // tts.speak(name,TextToSpeech.QUEUE_ADD,null,null);
                    for(int i=0;i<3;i++)
                    {
                        if (i != 2)
                            b[i].setBackgroundResource(android.R.drawable.btn_default);
                    }
                }
                else
                {
                    row2.removeView(l3);
                    b[2].setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        });


        b[3].setBackgroundResource(R.drawable.stopserviceimage);
        b[3].setLayoutParams (new WindowManager.LayoutParams(75,75,0,0,50,50,50));


        b[3].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                row2.removeAllViews();
                for(int i=0;i<3;i++)
                {
                    if (i != 3)
                        b[i].setBackgroundResource(android.R.drawable.btn_default);
                }
                b[3].setBackgroundColor(Color.RED);

                row2.addView(l4);
                stopService(new Intent(getApplicationContext(), Service1.class));
            }
        });
        row1.addView(oxforddictionary);
        for(int i=0;i<4;i++)
        row1.addView(b[i]);



        //  linearLayout = (LinearLayout)getSystemService(WINDOW_SERVICE);


        windowManager.updateViewLayout(floatingButton, params);
        this.notification = new Notification(R.drawable.ic_launcher_background, getText(R.string.app_name), System.currentTimeMillis());
        startForeground(ONGOING_NOTIFICATION, this.notification);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    void speakAndWait(String s)
    {
        tts.speak(s, TextToSpeech.QUEUE_ADD, null, null);
        try
        {
            while (tts.isSpeaking())
                Thread.sleep(1000);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onDestroy()
    {
        windowManager.removeView(floatingButton);
        windowManager.removeView(linearLayout2);
        speakAndWait("Shutting down.");
        tts.stop();
        tts.shutdown();
        canContinue = false;
        try
        {
            workerThread.join();
            Toast.makeText(getApplicationContext(), "Service Ended Successfully", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
        }

        super.onDestroy();
    }

    class FloatingButton extends ImageView implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {

            if (!isMainWindowOpen)
            {

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.TOP | Gravity.LEFT;

                params.x = 0;
                params.y = 0;
                params.width = 100;
                params.height = 60;
                floatingButton.setBackgroundColor(Color.RED);

                windowManager.updateViewLayout(floatingButton, params);

                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.TOP | Gravity.LEFT;

                params.x = 0;
                params.y = 64;
                params.width = 700;
                params.height = 400;
                //linearLayout2.setBackgroundResource(R.drawable.linearlayoutimage);
                linearLayout.setBackgroundResource(R.drawable.linearlayoutimage);

                Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animationset);
                //a.reset();
                windowManager.addView(linearLayout2, params);
                //linearLayout.clearAnimation();
                linearLayout.startAnimation(a);


                windowManager.removeView(floatingButton);
                WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
                params2.gravity = Gravity.TOP | Gravity.LEFT;
                floatingButton.setBackgroundColor(Color.RED);

                params2.x = 0;
                params2.y = 0;
                params2.height = 60;
                params2.width = 100;
                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                windowManager.addView(floatingButton, params2);


              //  tts.speak("You clicked on me", TextToSpeech.QUEUE_ADD, null, null);
                isMainWindowOpen = true;
            }
            else
            {
                showToast("");
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
                    params.gravity = Gravity.TOP | Gravity.LEFT;

                    params.x = curX;
                    params.y = curY;
                    params.width = 100;
                    params.height = 60;
                    windowManager.updateViewLayout(floatingButton, params);


                    windowManager.removeView(linearLayout2);
                  //  tts.speak("You unclicked on me", TextToSpeech.QUEUE_ADD, null, null);
                    isMainWindowOpen = false;


             }

        }
        public boolean onKeyDown(int keyCode, KeyEvent event)
        {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                speakAndWait("Overidden done");
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }



        public FloatingButton(Context c)
        {
            super(c);
            setOnClickListener(this);
            setImageResource(R.drawable.floatingbuttonimage);


        }

    }
}

