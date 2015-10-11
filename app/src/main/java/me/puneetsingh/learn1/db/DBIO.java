package me.puneetsingh.learn1.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import me.puneetsingh.learn1.bean.*;
import me.puneetsingh.learn1.R;

/**
 * Created by puneetsingh on 7/6/15.
 */
public class DBIO {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;
    private static final int MAX_WORDS = 9701;

    public DBIO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = new DBHelper(context);
        Log.i("PSL",dbHelper.getDatabaseName());
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    @SuppressWarnings("ResourceType")
    public void insertFirstTime() {
        open();

        Cursor cursor = database.rawQuery("select * from words", null);
        Cursor shuffleCursor = database.rawQuery("select * from shuffletimes", null);
        String strLine = "";

        if (cursor.getCount() < 1 || shuffleCursor.getCount() < 1) {
            Log.i("PSL", String.valueOf(cursor.getCount()));
            Log.i(DBIO.class.getName(), "Am here for first time");
            try {
                InputStream is = context.getResources().openRawResource(R.drawable.words);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ArrayList<Word> words = new ArrayList<Word>();
                Word word = null;
                database.execSQL("insert into shuffletimes values(1,1)");
                while ((strLine = reader.readLine()) != null) {
                    if (strLine.startsWith("{")) {
                        word = new Word();
                    } else if (strLine.startsWith("}")) {
                        word.setLmScore(5.0d);
                        word.setDifScore(5.0d);
                        word.setScore(5.0 * 5.0);
                        words.add(word);
                    }
                    if (strLine.startsWith("Word")) {
                        word.setWord(strLine.split(":")[1]);
                    }
                    if (strLine.startsWith("POS")) {
                        word.setPOS(strLine.split(":")[1]);
                    }
                    if (strLine.startsWith("Meaning")) {
                        word.setMeaning(strLine.split(":")[1]);
                    }
                    if (strLine.startsWith("Example")) {
                        word.setExample(strLine.split(":", 2)[1]);
                    }
                }
                is.close();
                int i = 1;
                for (Word w : words) {
                   // System.out.println(w.getWord());
                    ContentValues cv = new ContentValues();
                    cv.put("_id", i++);
                    cv.put("word", w.getWord());
                    cv.put("meaning", w.getMeaning());
                    cv.put("POS", w.getPOS());
                    cv.put("example", w.getExample());
                    cv.put("difScore", w.getDifScore());
                    cv.put("lmScore", w.getLmScore());
                    cv.put("score", w.getScore());
                    database.insert("words", null, cv);
                }

            } catch (FileNotFoundException e) {
                Log.e(DBIO.class.getName(), e.getMessage());
            } catch (IOException e) {
                Log.e(DBIO.class.getName(), e.getMessage());
            } catch (Exception e) {
                Log.e(DBIO.class.getName(), e.getMessage());
            }
        }
    }

    public boolean incrementShuffleTimes() {
        boolean workedFine = true;
        try {
            database.execSQL("update shuffletimes " +
                    " set shuffle = shuffle+1" +
                    " where _id=1");

        } catch (Exception e) {
            workedFine = false;
            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return workedFine;
    }

    public boolean resetShuffleTimes() {
        boolean workedFine = true;
        try {
            database.execSQL("update shuffletimes " +
                    " set shuffle = 1" +
                    " where _id=1");
        } catch (Exception e) {
            workedFine = false;
            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return workedFine;
    }

    public boolean decrementLMById(int id) {
        boolean workedFine = true;
        try {
            database.execSQL("update words " +
                    " set lmScore = lmScore + 0.2" +
                    " where _id = " + id);

        } catch (SQLException e) {
            workedFine = false;
            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return workedFine;
    }
    public boolean updateScores() {
        boolean workedFine=true;
        try {
            database.execSQL("update words " +
                            " set score = lmScore * difScore"
            );

        } catch (SQLException e) {
            workedFine = false;
            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return workedFine;
    }
    public boolean decrementDifById(int id) {

        boolean workedFine=true;
        try {
            database.execSQL("update words " +
                            " set difScore = difScore - 0.25 where _id = " + id
            );
            database.execSQL("update words " +
                            " set score = lmScore * difScore where _id = " + id
            );

        } catch (SQLException e) {
            workedFine = false;
            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return workedFine;
    }
    public boolean incrementDifById(int id) {

        boolean workedFine=true;
        try {
            database.execSQL("update words " +
                            " set difScore = difScore + 0.25 where _id = " + id
            );
            database.execSQL("update words " +
                            " set score = lmScore * difScore where _id = " + id
            );

        } catch (SQLException e) {
            workedFine = false;
            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return workedFine;
    }
    public int getShuffleTimes() {

        int sTimes = 0;
        try {
            Cursor rs = database.rawQuery("select shuffle from shuffletimes",null);
            rs.moveToFirst();
            rs.moveToNext();
            sTimes = rs.getInt(0);
        } catch (Exception e) {
            sTimes = 0;
            try{
                database.execSQL("insert into shuffletimes values(0,1)");
            }
            catch (Exception e1)
            {
                Log.e(DBIO.class.getName(),"Error in inserting again");
            }


            Log.e(DBIO.class.getName(), e.getMessage());
        }
        return sTimes;
    }
    public Word[] getWords(int limit) {

        if (getShuffleTimes()<6 && getShuffleTimes()%3==0) {
            Log.i("DBIO","Random Shuffle:"+getShuffleTimes());
            incrementShuffleTimes();
            return getRandomWords(limit);
        } else {
            Log.i("DBIO","Ordered Shuffle:"+getShuffleTimes());
            incrementShuffleTimes();
            return getTopXWords(limit);
        }
    }
    private Word[] getTopXWords(int limit) {

        Word w;
        int olimit = 0;
        ArrayList<Word> warr = new ArrayList<Word>();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        if(limit>5)
        {
            olimit = limit - 5;
            limit=5;
        }

        try {
            Cursor rs = database.rawQuery("select  * from words order by score ASC limit " + limit+1,null);
            rs.moveToFirst();
            while (rs.moveToNext()) {
                w = new Word();
                w.setWord(rs.getString(1));
                w.setMeaning(rs.getString(2));
                w.setPOS(rs.getString(3));
                w.setExample(rs.getString(4));
                w.setDifScore(rs.getDouble(5));
                w.setLmScore(rs.getDouble(6));
                w.setScore(rs.getDouble(7));
                w.setId(rs.getInt(0));

                ids.add(rs.getInt(0));
                warr.add(w);
            }
            if(olimit+limit>5)
                warr.addAll(Arrays.asList(getRandomWords(olimit)));
            for (int i = 0; i < limit; i++) {
                Log.i("IDS", String.valueOf(ids.get(i)));
                decrementLMById(ids.get(i));
            }
            updateScores();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warr.toArray(new Word[warr.size()]);
    }
    private Word[] getRandomWords(int limit) {
        Random rand = new Random();
        ArrayList<Word> words = new ArrayList<Word>();
        int randomNum = 0;
        HashSet<Integer> randomNumbers = new HashSet<Integer>();
        ArrayList<Integer> wordIds = new ArrayList<Integer>();
        for (int i = 0; i < limit; i++) {
            randomNum = rand.nextInt(MAX_WORDS) + 1;
            if (randomNumbers.contains(randomNum)) {
                i--;
                continue;
            } else
                for (int j = randomNum - 5; j < randomNum + 5; j++) {
                    randomNumbers.add(j);
                }
            wordIds.add(randomNum);
            Word w = getWord(randomNum);
            words.add(w);
        }
        for (int i = 0; i < limit; i++) {
            decrementLMById(wordIds.get(i));
        }
        updateScores();
        return words.toArray(new Word[words.size()]);
    }
    private Word getWord(int randomNum) {
        Word w = new Word();
        try {

            Cursor rs = database.rawQuery("select * from words where _id=" + randomNum,null);
            Log.i("PSL",String.valueOf(rs.getCount()));
            rs.moveToFirst();
            w.setWord(rs.getString(1));
            w.setMeaning(rs.getString(2));
            w.setPOS(rs.getString(3));
            w.setExample(rs.getString(4));
            w.setDifScore(rs.getDouble(5));
            w.setLmScore(rs.getDouble(6));
            w.setScore(rs.getDouble(7));
            w.setId(rs.getInt(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return w;
    }
}
