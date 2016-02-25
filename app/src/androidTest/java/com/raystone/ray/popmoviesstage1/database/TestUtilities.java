package com.raystone.ray.popmoviesstage1.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import com.raystone.ray.popmoviesstage1.database.MovieContract.ByRatingEntry;
import com.raystone.ray.popmoviesstage1.database.MovieContract.FavoriteEntry;
import com.raystone.ray.popmoviesstage1.database.MovieContract.ByPopularityEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ray on 2/10/2016.
 */
public class TestUtilities extends AndroidTestCase {

    static ContentValues createRatingValues()
    {
        ContentValues ratingValues = new ContentValues();
        ratingValues.put(ByRatingEntry.COLUMN_TITLE,"KONGFU");
        ratingValues.put(ByRatingEntry.COLUMN_DATE,"2016/01/01");
        ratingValues.put(ByRatingEntry.COLUMN_RATE,"10/10");
        ratingValues.put(ByRatingEntry.COLUMN_DESCRIPTION,"What a great movie");
        ratingValues.put(ByRatingEntry.COLUMN_POSTER,"http:www.");
        ratingValues.put(ByRatingEntry.COLUMN_TRAILER,"http:www.");

        return ratingValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues)
    {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String,Object> entry : valueSet)
        {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found" + error, index == -1);

            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match he expected value" + expectedValue + "'." + error, expectedValue,valueCursor.getString(index));
        }
    }
}
