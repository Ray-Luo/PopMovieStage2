package com.raystone.ray.popmoviesstage1.database;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Ray on 2/11/2016.
 */
public class TestMovieContract extends AndroidTestCase {

    public void testBuildUri()
    {
        Uri ratingUri = MovieContract.ByRatingEntry.buildRatingUri(0);
        assertEquals("Error",ratingUri.toString(),"content://com.raystone.ray.popmoviesstage1/rating/0");
    }
}
