package com.raystone.ray.popmoviesstage1.database;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Ray on 2/13/2016.
 */
public class testUriMatcher extends AndroidTestCase{

    private static final String TITLE = "Jurassic%20World";

    private static final Uri TEST_MOVIE_RATING = MovieContract.ByRatingEntry.CONTENT_URI;

    Uri titleUri = Uri.parse("content://com.raystone.ray.popmoviesstage1/rating/293660");

    public void testUriMatcher()
    {
        UriMatcher matcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: Rating table was matched incorrectly.",matcher.match(TEST_MOVIE_RATING),MovieProvider.RATING);

        assertEquals("Item in Rating table was matched incorrectly",matcher.match(titleUri),MovieProvider.RATING_MOVIE_WITH_ID);
    }
}
