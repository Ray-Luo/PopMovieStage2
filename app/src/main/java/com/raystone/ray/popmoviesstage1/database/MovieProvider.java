package com.raystone.ray.popmoviesstage1.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Ray on 2/11/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private  MovieDBHelper movieDBHelper;
    static final int RATING = 10;
    static final int POPULARITY = 20;
    static final int FAVORITE = 30;
    static final int RATING_MOVIE_WITH_ID = 40;
    static final int POPULARITY_MOVIE_WITH_ID = 50;
    static final int FAVORITE_MOVIE_WITH_ID = 60;



    static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority,MovieContract.PATH_RATING,RATING);
        uriMatcher.addURI(authority,MovieContract.PATH_POPULARITY,POPULARITY);
        uriMatcher.addURI(authority,MovieContract.PATH_FAVORITE,FAVORITE);
        uriMatcher.addURI(authority,MovieContract.PATH_RATING + "/#", RATING_MOVIE_WITH_ID);
        uriMatcher.addURI(authority,MovieContract.PATH_POPULARITY + "/#",POPULARITY_MOVIE_WITH_ID);
        uriMatcher.addURI(authority,MovieContract.PATH_FAVORITE + "/#",FAVORITE_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public String getType(Uri uri)
    {
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case RATING:
                return MovieContract.ByRatingEntry.CONTENT_TYPE;
            case POPULARITY:
                return MovieContract.ByPopularityEntry.CONTENT_TYPE;
            case FAVORITE:
                return MovieContract.FavoriteEntry.CONTENT_TYPE;
            case RATING_MOVIE_WITH_ID:
                return MovieContract.ByRatingEntry.CONTENT_ITEM_TYPE;
            case POPULARITY_MOVIE_WITH_ID:
                return MovieContract.ByPopularityEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_MOVIE_WITH_ID:
                return MovieContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+ uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor returnCursor;

        switch (sUriMatcher.match(uri))
        {
            case RATING:
            {
                returnCursor = movieDBHelper.getReadableDatabase().query(MovieContract.ByRatingEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case POPULARITY:
            {
                returnCursor = movieDBHelper.getReadableDatabase().query(MovieContract.ByPopularityEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case FAVORITE:
            {
                returnCursor = movieDBHelper.getReadableDatabase().query(MovieContract.FavoriteEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case RATING_MOVIE_WITH_ID:
            {
                String id = uri.getPathSegments().get(1).toString();
                returnCursor = movieDBHelper.getReadableDatabase().query(MovieContract.ByRatingEntry.TABLE_NAME,projection,"movie_id = ?" ,new String[] {id},null,null,sortOrder);
                break;
            }
            case POPULARITY_MOVIE_WITH_ID:
            {
                String id = uri.getPathSegments().get(1).toString();
                returnCursor = movieDBHelper.getReadableDatabase().query(MovieContract.ByPopularityEntry.TABLE_NAME,projection,"movie_id = ?" ,new String[] {id},null,null,sortOrder);
                break;
            }
            case FAVORITE_MOVIE_WITH_ID:
            {
                String id = uri.getPathSegments().get(1).toString();
                returnCursor = movieDBHelper.getReadableDatabase().query(MovieContract.FavoriteEntry.TABLE_NAME,projection,"movie_id = ?" ,new String[] {id},null,null,sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match)
        {
            case RATING:
            {
                long _id = db.insert(MovieContract.ByRatingEntry.TABLE_NAME,null,values);
                if(_id > 0)
                    returnUri = MovieContract.ByRatingEntry.buildRatingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case POPULARITY:
            {
                long _id = db.insert(MovieContract.ByPopularityEntry.TABLE_NAME,null,values);
                if(_id > 0)
                    returnUri = MovieContract.ByPopularityEntry.buildPopularityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case FAVORITE:
            {
                long _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME,null,values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deleteRowId;

        switch (match)
        {
            case RATING:
            {
                deleteRowId = db.delete(MovieContract.ByRatingEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case POPULARITY:
            {
                deleteRowId = db.delete(MovieContract.ByPopularityEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case FAVORITE:
            {
                deleteRowId = db.delete(MovieContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        if(deleteRowId > 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return deleteRowId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updateRowId;

        switch (match)
        {
            case RATING:
            {
                updateRowId = db.update(MovieContract.ByRatingEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case POPULARITY:
            {
                updateRowId = db.update(MovieContract.ByPopularityEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case FAVORITE:
            {
                updateRowId = db.update(MovieContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        if (updateRowId > 0 )
            getContext().getContentResolver().notifyChange(uri,null);
        return updateRowId;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch (match)
        {
            case RATING:
            {
                db.beginTransaction();
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insert(MovieContract.ByRatingEntry.TABLE_NAME,null,value);
                        if(_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            case POPULARITY:
            {
                db.beginTransaction();
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insert(MovieContract.ByPopularityEntry.TABLE_NAME,null,value);
                        if(_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            case FAVORITE:
            {
                db.beginTransaction();
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME,null,value);
                        if(_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public boolean onCreate()
    {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }
}
