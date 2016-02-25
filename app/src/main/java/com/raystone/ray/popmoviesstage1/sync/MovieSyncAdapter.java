package com.raystone.ray.popmoviesstage1.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.raystone.ray.popmoviesstage1.BuildConfig;
import com.raystone.ray.popmoviesstage1.Movie;
import com.raystone.ray.popmoviesstage1.R;
import com.raystone.ray.popmoviesstage1.database.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2/20/2016.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter{

    // 3 seconds;
    public static final int SYNC_INTERVAL = 60*60*24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    String appId = BuildConfig.MOVIE_API_KEY;
    final String APIId = "api_key";
    List<String> trailers = new ArrayList<>();

    public MovieSyncAdapter(Context context, boolean autoInitialize)
    {super(context,autoInitialize);}

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.d(LOG_TAG,"Starting sync");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        //String appId = BuildConfig.MOVIE_API_KEY;

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String QUERY_PARAM = "sort_by";

            String sortBy = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sort","popularity.desc");

            Uri buildUrl = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, sortBy).appendQueryParameter(APIId, appId).build();
            URL url = new URL(buildUrl.toString());
            Log.v(LOG_TAG, "Built URL" + url);

            //  Create the request to MovieApi, and open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //  Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) return;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) return;
            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie string" + movieJsonStr);

            //  Get trailers
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");
            String id;

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieItem = movieArray.getJSONObject(i);
                id = movieItem.getString("id");

                final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/";
                Uri trailerUrl;

                trailerUrl = Uri.parse(TRAILER_BASE_URL).buildUpon().appendPath(id).appendPath("videos").appendQueryParameter(APIId, appId).build();

                url = new URL(trailerUrl.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) return;
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) return;
                trailers.add(buffer.toString());
                Log.v(LOG_TAG, "Trailer: " + trailers.get(i).toString());
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            return ;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return;
    }

    private List<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {

        //  Names of JSON objects need to be extracted
        final String RESULT = "results";
        final String TITLE = "original_title";
        final String RELEASE = "release_date";
        final String POSTER = "poster_path";
        final String VOTE = "vote_average";
        final String DESCRIPTION = "overview";
        final String ID = "id";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(RESULT);

        List<ContentValues> tempMovieValue = new ArrayList<>();
        List<Movie> movie = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            //  Info needs to be extracted
            String title;
            String releaseDate;
            String moviePoster;
            String voteAverage;
            String plotSynopsis;
            String id;

            //  get info from JSON
            JSONObject movieItem = movieArray.getJSONObject(i);
            title = movieItem.getString(TITLE);
            releaseDate = movieItem.getString(RELEASE);
            moviePoster = movieItem.getString(POSTER);
            voteAverage = movieItem.getString(VOTE);
            plotSynopsis = movieItem.getString(DESCRIPTION);
            id = movieItem.getString(ID);

            // insert into database
            ContentValues movieValue = new ContentValues();

            //  TO DO: Add switch
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_TITLE, title);
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_DATE, releaseDate);
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_RATE, voteAverage);
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_DESCRIPTION, plotSynopsis);
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_POSTER, moviePoster);
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_DATE, releaseDate);
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_TRAILER,trailers.get(i));
            movieValue.put(MovieContract.ByRatingEntry.COLUMN_ID,id);

            tempMovieValue.add(movieValue);

            movie.add(new Movie(title, releaseDate, moviePoster, voteAverage, plotSynopsis,id,trailers.get(i)));
        }
        Log.v(LOG_TAG, "Movie Sample" + movie.get(0));

        ContentValues[] allMovieValues = new ContentValues[tempMovieValue.size()];
        for(int i = 0;i<allMovieValues.length;i++)
        {
            allMovieValues[i] = tempMovieValue.get(i);
        }

        String sortBy = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sort","");
        int insertedByRating;
        if (sortBy.equals("vote_average.desc")){
            getContext().getContentResolver().delete(MovieContract.ByRatingEntry.CONTENT_URI,null,null);
            insertedByRating = getContext().getContentResolver().bulkInsert(MovieContract.ByRatingEntry.CONTENT_URI,allMovieValues);}
        else {
            getContext().getContentResolver().delete(MovieContract.ByPopularityEntry.CONTENT_URI,null,null);
            insertedByRating = getContext().getContentResolver().bulkInsert(MovieContract.ByPopularityEntry.CONTENT_URI, allMovieValues);
        }

        trailers.clear();
        Log.v(LOG_TAG,"FetchMovieTask complete." + insertedByRating + "inserted");
        return movie;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime)
    {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));


        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        else
        {
            String authority = context.getString(R.string.content_authority);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // we can enable inexact timers in our periodic sync
                SyncRequest request = new SyncRequest.Builder().
                        syncPeriodic(SYNC_INTERVAL, SYNC_FLEXTIME).
                        setSyncAdapter(newAccount, authority).
                        setExtras(new Bundle()).build();
                ContentResolver.requestSync(request);
            } else {
                ContentResolver.addPeriodicSync(newAccount,
                        authority, new Bundle(), SYNC_INTERVAL);
            }
            ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
