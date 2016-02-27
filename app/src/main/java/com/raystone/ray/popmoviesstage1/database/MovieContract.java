package com.raystone.ray.popmoviesstage1.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ray on 2/10/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.raystone.ray.popmoviesstage1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RATING = "rating";
    public static final String PATH_POPULARITY = "popularity";
    public static final String PATH_FAVORITE = "favorite";

    //  "By Rating" table
    public static final class ByRatingEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATING).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATING;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATING;

        public static final String TABLE_NAME = "rating";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_TRAILER = "trailer";

        public static final String COLUMN_RATE = "rate";

        public static final String COLUMN_ID = "movie_id";

        public static final String COLUMN_REVIEW = "review";

        public static Uri buildRatingUri(long id)
        {return ContentUris.withAppendedId(CONTENT_URI,id);}

        public static Uri buildMovieItemURI(String movieID)
        {
            Uri uri = CONTENT_URI.buildUpon().appendPath(movieID).build();
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }
    }

    //  "By popularity" table
    public static final class ByPopularityEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULARITY).build();


        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULARITY;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULARITY;

        public static final String TABLE_NAME = "popularity";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_TRAILER = "trailer";

        public static final String COLUMN_RATE = "rate";

        public static final String COLUMN_ID = "movie_id";

        public static final String COLUMN_REVIEW = "review";

        public static Uri buildPopularityUri(long id)
        {return ContentUris.withAppendedId(CONTENT_URI,id);}

        public static Uri buildMovieItemURI(String movieID)
        {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }
    }

    //  "Favorite" movie table
    public static final class FavoriteEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;

        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_TRAILER = "trailer";

        public static final String COLUMN_RATE = "rate";

        public static final String COLUMN_ID = "movie_id";

        public static final String COLUMN_REVIEW = "review";

        public static Uri buildFavoriteUri(long id)
        {return ContentUris.withAppendedId(CONTENT_URI,id);}

        public static Uri buildMovieItemURI(String movieID)
        {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }
    }



}
