package me.puneetsingh.learn1.db;

import android.content.Context;

import java.io.Closeable;

import me.puneetsingh.learn1.bean.Word;

/**
 * Created by puneetsingh on 7/6/15.
 */
public class MemodictionBE implements Closeable {
    DBIO dbio;

    public MemodictionBE(Context context) {
        dbio = new DBIO(context);
        dbio.open();
        dbio.insertFirstTime();

    }

    public Word[] getWords(int limit) {

        return dbio.getWords(limit);
    }

    public boolean swipeLeft(int id) {

        return dbio.decrementDifById(id);
    }

    public boolean swipeRight(int id) {

        return dbio.incrementDifById(id);
    }

    public void close() {
        dbio.close();
    }
}
