package com.raystone.ray.popmoviesstage1.database;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Ray on 2/11/2016.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider()
    {
        mContext.getContentResolver().delete(MovieContract.ByRatingEntry.CONTENT_URI,null,null);
        mContext.getContentResolver().delete(MovieContract.ByPopularityEntry.CONTENT_URI,null,null);
        mContext.getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI,null,null);

        Cursor cursor = mContext.getContentResolver().query(MovieContract.ByRatingEntry.CONTENT_URI,null,null,null,null);
        assertEquals("Error: records not deleted from Rating table",0,cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(MovieContract.ByPopularityEntry.CONTENT_URI,null,null,null,null);
        assertEquals("Error: records not deleted from Popularity table",0,cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
        assertEquals("Error: records not deleted from Popularity table",0,cursor.getCount());
        cursor.close();
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    public void testProviderRegister()
    {
        PackageManager packageManager = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),MovieProvider.class.getName());

        try
        {
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName,0);

            assertEquals("Error: authority registered uncorrectly",providerInfo.authority,MovieContract.CONTENT_AUTHORITY);
        }catch (PackageManager.NameNotFoundException e)
        {
            assertTrue("Error: MovieProvider not registered at" + mContext.getPackageName(),false);
        }
    }

    public void testGetType()
    {
        String type = mContext.getContentResolver().getType(MovieContract.ByRatingEntry.CONTENT_URI);
        assertEquals("", MovieContract.ByRatingEntry.CONTENT_TYPE,type);


        type = mContext.getContentResolver().getType(MovieContract.ByPopularityEntry.CONTENT_URI);
        assertEquals("", MovieContract.ByPopularityEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(MovieContract.FavoriteEntry.CONTENT_URI);
        assertEquals("", MovieContract.FavoriteEntry.CONTENT_TYPE,type);
    }

    public void testQuery()
    {
        MovieDBHelper helper = new MovieDBHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = TestUtilities.createRatingValues();

        long rowId = db.insert(MovieContract.ByRatingEntry.TABLE_NAME,null,values);
        assertTrue("Unable to insert into Rating table",rowId != -1);

        db.close();

        Cursor cursor = mContext.getContentResolver().query(MovieContract.ByRatingEntry.CONTENT_URI,null,null,null,null);

        assertTrue("Empty cursor returned",cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Not equal",cursor,values);
    }

    public void testInsert()
    {
        ContentValues values = TestUtilities.createRatingValues();

        Uri insertRowUir = mContext.getContentResolver().insert(MovieContract.ByRatingEntry.CONTENT_URI,values);
        long rowId = ContentUris.parseId(insertRowUir);

        assertTrue(rowId != -1);

        Cursor cursor = mContext.getContentResolver().query(MovieContract.ByRatingEntry.CONTENT_URI,null,null,null,null);
        assertTrue("Empty cursor returned",cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Not equal",cursor,values);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertWeatherValues()
    {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++)
        {
            ContentValues ratingValue = new ContentValues();
            ratingValue.put(MovieContract.ByRatingEntry.COLUMN_TITLE,"Me"+ Integer.toString(i));
            ratingValue.put(MovieContract.ByRatingEntry.COLUMN_DATE,"2016/02/12" + Integer.toString(i));
            ratingValue.put(MovieContract.ByRatingEntry.COLUMN_RATE,"10/10" + Integer.toString(i));
            ratingValue.put(MovieContract.ByRatingEntry.COLUMN_DESCRIPTION,"What a great movie"+ Integer.toString(i));
            ratingValue.put(MovieContract.ByRatingEntry.COLUMN_POSTER,"www."+ Integer.toString(i));
            ratingValue.put(MovieContract.ByRatingEntry.COLUMN_TRAILER,"www."+ Integer.toString(i));
            returnContentValues[i] = ratingValue;
        }
        return returnContentValues;
    }

    public void testBulkInsert()
    {
        ContentValues[] bulkInsertContentValues = createBulkInsertWeatherValues();

        int insertRowNumber = mContext.getContentResolver().bulkInsert(MovieContract.ByRatingEntry.CONTENT_URI,bulkInsertContentValues);

        assertEquals(insertRowNumber,BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = mContext.getContentResolver().query(MovieContract.ByRatingEntry.CONTENT_URI,null,null,null,null);

        assertEquals(cursor.getCount(),BULK_INSERT_RECORDS_TO_INSERT);
        cursor.moveToFirst();

        for(int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++,cursor.moveToNext())
        {
            TestUtilities.validateCurrentRecord("Error validating ByRatingEntry at " + i, cursor,bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
