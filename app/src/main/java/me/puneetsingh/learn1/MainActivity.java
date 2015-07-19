package me.puneetsingh.learn1;

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
import me.puneetsingh.learn1.db.DBIO;
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
        DBHelper dbh = new DBHelper(this);
        mbe = new MemodictionBE(this);

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
        Log.i("Score",String.valueOf(wrds[curr].getScore()));
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
        Log.i("Score", String.valueOf(wrds[curr].getScore()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
