package com.raystone.ray.popmoviesstage1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.raystone.ray.popmoviesstage1.database.MovieContract.ByRatingEntry;
import com.raystone.ray.popmoviesstage1.database.MovieContract.FavoriteEntry;
import com.raystone.ray.popmoviesstage1.database.MovieContract.ByPopularityEntry;
/**
 * Created by Ray on 2/10/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context)
    {super(context,DATABASE_NAME,null,DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_RATING_TABLE = "CREATE TABLE " + ByRatingEntry.TABLE_NAME + " (" +
                ByRatingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ByRatingEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ByRatingEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ByRatingEntry.COLUMN_RATE + " TEXT NOT NULL, " +
                ByRatingEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ByRatingEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                ByRatingEntry.COLUMN_ID+ " TEXT NOT NULL, " +
                ByRatingEntry.COLUMN_TRAILER +" TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_RATING_TABLE);

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_RATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_ID+ " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_TRAILER +" TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

        final String SQL_CREATE_POPULARITY_TABLE = "CREATE TABLE " + ByPopularityEntry.TABLE_NAME + " (" +
                ByPopularityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ByPopularityEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ByPopularityEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ByPopularityEntry.COLUMN_RATE + " TEXT NOT NULL, " +
                ByPopularityEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ByPopularityEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                ByPopularityEntry.COLUMN_ID+ " TEXT NOT NULL, " +
                ByPopularityEntry.COLUMN_TRAILER +" TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_POPULARITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ByRatingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ByPopularityEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
