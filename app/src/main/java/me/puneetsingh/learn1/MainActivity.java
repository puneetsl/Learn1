package me.puneetsingh.learn1;


import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.puneetsingh.learn1.bean.Word;
import me.puneetsingh.learn1.db.DBHelper;
import me.puneetsingh.learn1.db.MemodictionBE;


public class MainActivity extends ActionBarActivity {
    private static final int MAX_WORDS = 10;
    MemodictionBE mbe;
    int curr = 0;
    Word[] wrds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        int secondsDelayed = 1;
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
//                finish();
//            }
//        }, secondsDelayed * 1000);
        //setContentView(R.layout.activity_main);

        DBHelper dbh = new DBHelper(this);
        mbe = new MemodictionBE(this);
        //new MBELoad().execute(this,this,this);


        wrds = mbe.getWords(MAX_WORDS);
        TextView tv = (TextView) findViewById(R.id.textView);
        Log.i("PSL", wrds[0].getWord());
        tv.setText(wrds[curr].getWord());
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(wrds[curr].getMeaning());
        Button b1 = (Button) findViewById(R.id.button);
        Button b2 = (Button) findViewById(R.id.button2);
        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                swipeLeft();
            }
        });

        b2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                swipeRight();
            }
        });
        setRightColor(tv);



    }
    private void setRightColor(View v)
    {
        if(wrds[curr].getScore()>=24.9&&wrds[curr].getScore()<=25.1)
            v.getRootView().getRootView().setBackgroundColor(Color.parseColor("#444444"));
        else if(wrds[curr].getScore()<24.9)
        {
            float colorVal = (float) (25.0 - wrds[curr].getScore());
            colorVal = colorVal * 25;
            v.getRootView().getRootView().setBackgroundColor(Color.rgb(100+(int)colorVal,80,80));
        }
        else
        {
            float colorVal = (float) (wrds[curr].getScore()-25.0);
            colorVal = colorVal * 10;
            v.getRootView().getRootView().setBackgroundColor(Color.rgb(68,80+(int)colorVal,68));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void swipeLeft() {

        mbe.swipeLeft(wrds[curr].getId());
        curr++;
        if (curr == MAX_WORDS) {
            wrds = mbe.getWords(MAX_WORDS);
            curr = 0;
        }

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(wrds[curr].getWord());
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(wrds[curr].getMeaning());
        setRightColor(tv);
        Log.i("SL_Score", String.valueOf(wrds[curr].getScore()));
    }

    public void swipeRight() {

        mbe.swipeRight(wrds[curr].getId());
        curr++;
        if (curr == MAX_WORDS) {
            wrds = mbe.getWords(MAX_WORDS);
            curr = 0;
        }
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(wrds[curr].getWord());
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(wrds[curr].getMeaning());
        setRightColor(tv);
        Log.i("SR_Score", String.valueOf(wrds[curr].getScore()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //noinspection SimplifiableIfStatement

        //return super.onOptionsItemSelected(item);
    }

    class MBELoad extends AsyncTask<MainActivity, MainActivity, MainActivity> {

        Intent load = new Intent(MainActivity.this, LoadActivity.class);
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            startActivity(load);
        }
        @Override
        protected MainActivity doInBackground(MainActivity... arg0)
        {

            mbe = new MemodictionBE(arg0[0]);
            return null;
            //Record method
        }

        @Override
        protected void onPostExecute(MainActivity result)
        {
            super.onPostExecute(result);
            stopService(load);
        }
    }
}

