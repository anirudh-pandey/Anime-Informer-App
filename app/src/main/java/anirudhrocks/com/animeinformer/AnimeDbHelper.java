package anirudhrocks.com.animeinformer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AnimeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "animeDataDb.db";
    private static final String TABLE_NAME = "anime_data";
    private static final String KEY_ID = "id";
    private static final String ANIME_TITLE = "anime_title";
    private static final String LATEST_EPISODE = "latest_episode";
    private static final String LANGUAGE_TYPE = "language_type";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;


    public AnimeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_ANIME_DATA_TABLE = "CREATE TABLE " +
                this.TABLE_NAME + " ( " + this.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                this.ANIME_TITLE + " TEXT," +
                this.LATEST_EPISODE + " TEXT," +
                this.LANGUAGE_TYPE + " TEXT ) ";

        db.execSQL(SQL_CREATE_ANIME_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + this.TABLE_NAME);
        onCreate(db);
    }


    public boolean addAnimeInfo(Anime anime) {
        if (getCount(anime) == 0) {
            System.out.println(getCount(anime));
            db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            // see if the anime title is present or not
            cv.put(this.ANIME_TITLE, anime.getAnimeTitle());
            cv.put(this.LATEST_EPISODE, anime.getLatestEpisode());
            cv.put(this.LANGUAGE_TYPE, anime.getLanguageType());
            db.insert(this.TABLE_NAME, null, cv);
            db.close();
            return true;
        } else {
            return false;
        }
    }


    public void deleteAimeInfo(Anime anime) {
        db = getWritableDatabase();
        db.delete(this.TABLE_NAME, this.ANIME_TITLE + "=? and " + this.LATEST_EPISODE + "=? and " +
                this.LANGUAGE_TYPE + "=?", new String[]{anime.getAnimeTitle(), anime.getLatestEpisode(), anime.getLanguageType()});
        db.close();
    }


    public void updateLatestEpisode(Anime anime) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(this.LATEST_EPISODE, anime.getLatestEpisode());
        db.update(this.TABLE_NAME, cv, this.ANIME_TITLE + "=? and " + this.LANGUAGE_TYPE + "=?",
                new String[]{anime.getAnimeTitle(), anime.getLanguageType()});
        db.close();
    }


    public ArrayList<Anime> getAnimeInfoList() {
        ArrayList<Anime> animeInfoList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + this.ANIME_TITLE + ", " + this.LATEST_EPISODE + ", " + this.LANGUAGE_TYPE
                + " FROM " + this.TABLE_NAME, null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String animeTitle = c.getString(c.getColumnIndex(this.ANIME_TITLE));
                String latestEpisode = c.getString(c.getColumnIndex(this.LATEST_EPISODE));
                String languageType = c.getString(c.getColumnIndex(this.LANGUAGE_TYPE));

                animeInfoList.add(new Anime(animeTitle, latestEpisode, languageType));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return animeInfoList;

    }


    public int getCount(Anime anime) {
        Cursor c = null;
        try {
            db = getReadableDatabase();
            String query = "SELECT COUNT(*) FROM " + this.TABLE_NAME + " WHERE " + this.ANIME_TITLE + "=? and "
                    + this.LANGUAGE_TYPE + "=?";
            c = db.rawQuery(query, new String[]{anime.getAnimeTitle(), anime.getLanguageType()});
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
            return 0;
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }


}
