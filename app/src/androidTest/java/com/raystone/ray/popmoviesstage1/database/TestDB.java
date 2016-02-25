package com.raystone.ray.popmoviesstage1.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.raystone.ray.popmoviesstage1.database.MovieContract.ByRatingEntry;
import com.raystone.ray.popmoviesstage1.database.MovieDBHelper;
import com.raystone.ray.popmoviesstage1.database.MovieContract.ByPopularityEntry;
import com.raystone.ray.popmoviesstage1.database.MovieContract.FavoriteEntry;

import java.util.HashSet;

/**
 * Created by Ray on 2/10/2016.
 */
public class TestDB extends AndroidTestCase{

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    //  start with a clean state
    void deleteTheDatabase()
    {mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);}

    //  This runs before the test
    public void setUp()
    {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable
    {
        //  test "ByRating" table
        final HashSet<String> tableNameHashSet = new HashSet<>();

        tableNameHashSet.add(ByRatingEntry.TABLE_NAME);
        tableNameHashSet.add(ByPopularityEntry.TABLE_NAME);
        tableNameHashSet.add(FavoriteEntry.TABLE_NAME);

        SQLiteDatabase db = new MovieDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        //  Have created the tables?
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not created correctly", cursor.moveToFirst());

        //  verify the content of the tables been created
        do {
            tableNameHashSet.remove(cursor.getString(0));
        }while (cursor.moveToNext());
        assertTrue("Error: your database was created without ByRating entry, ByPopularity entry, and Favorite entry",tableNameHashSet.isEmpty());

        //  verify the columns for "ByRating" table
        cursor = db.rawQuery("PRAGMA table_info(" + ByRatingEntry.TABLE_NAME + ")",null);
        assertTrue("This means we are unable to query the database for table information", cursor.moveToFirst());

        //  Build a HashSet of all the column names we are looking for
        final HashSet<String> byRatingColumnHashSet = new HashSet<>();
        byRatingColumnHashSet.add(ByRatingEntry._ID);
        byRatingColumnHashSet.add(ByRatingEntry.COLUMN_TITLE);
        byRatingColumnHashSet.add(ByRatingEntry.COLUMN_DATE);
        byRatingColumnHashSet.add(ByRatingEntry.COLUMN_DESCRIPTION);
        byRatingColumnHashSet.add(ByRatingEntry.COLUMN_RATE);
        byRatingColumnHashSet.add(ByRatingEntry.COLUMN_POSTER);
        byRatingColumnHashSet.add(ByRatingEntry.COLUMN_TRAILER);

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            byRatingColumnHashSet.remove(columnName);
        }while (cursor.moveToNext());

        assertTrue("Error; The database doesn't contain all of the required entry columns", byRatingColumnHashSet.isEmpty());


        //  verify the columns for "ByPopularity" table
        cursor = db.rawQuery("PRAGMA table_info(" + ByPopularityEntry.TABLE_NAME + ")",null);
        assertTrue("This means we are unable to query the database for table information", cursor.moveToFirst());

        //  Build a HashSet of all the column names we are looking for
        final HashSet<String> byPopularityColumnHashSet = new HashSet<>();
        byPopularityColumnHashSet.add(ByPopularityEntry._ID);
        byPopularityColumnHashSet.add(ByPopularityEntry.COLUMN_TITLE);
        byPopularityColumnHashSet.add(ByPopularityEntry.COLUMN_DATE);
        byPopularityColumnHashSet.add(ByPopularityEntry.COLUMN_DESCRIPTION);
        byPopularityColumnHashSet.add(ByPopularityEntry.COLUMN_RATE);
        byPopularityColumnHashSet.add(ByPopularityEntry.COLUMN_POSTER);
        byPopularityColumnHashSet.add(ByPopularityEntry.COLUMN_TRAILER);

        columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            byPopularityColumnHashSet.remove(columnName);
        }while (cursor.moveToNext());

        assertTrue("Error; The database doesn't contain all of the required entry columns", byPopularityColumnHashSet.isEmpty());


        //  verify the columns for "Favorite" table
        cursor = db.rawQuery("PRAGMA table_info(" + FavoriteEntry.TABLE_NAME + ")",null);
        assertTrue("This means we are unable to query the database for table information", cursor.moveToFirst());

        //  Build a HashSet of all the column names we are looking for
        final HashSet<String> byFavoriteColumnHashSet = new HashSet<>();
        byFavoriteColumnHashSet.add(FavoriteEntry._ID);
        byFavoriteColumnHashSet.add(FavoriteEntry.COLUMN_TITLE);
        byFavoriteColumnHashSet.add(FavoriteEntry.COLUMN_DATE);
        byFavoriteColumnHashSet.add(FavoriteEntry.COLUMN_DESCRIPTION);
        byFavoriteColumnHashSet.add(FavoriteEntry.COLUMN_RATE);
        byFavoriteColumnHashSet.add(FavoriteEntry.COLUMN_POSTER);
        byFavoriteColumnHashSet.add(FavoriteEntry.COLUMN_TRAILER);

        columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            byFavoriteColumnHashSet.remove(columnName);
        }while (cursor.moveToNext());

        assertTrue("Error; The database doesn't contain all of the required entry columns", byFavoriteColumnHashSet.isEmpty());
    }

    public void testRatingTable()
    {
        MovieDBHelper helper = new MovieDBHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = TestUtilities.createRatingValues();

        long newRowId = db.insert(ByRatingEntry.TABLE_NAME,null,values);
        assertTrue("insert error", newRowId != -1);

        Cursor cursor = db.query(ByRatingEntry.TABLE_NAME, null,null,null,null,null,null);
        assertTrue("No records", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Validation Error: ContentValues not match", cursor,values);
        assertFalse("more than one record", cursor.moveToNext());

        cursor.close();
        db.close();
    }
}
