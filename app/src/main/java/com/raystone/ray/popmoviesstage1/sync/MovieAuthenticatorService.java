package com.raystone.ray.popmoviesstage1.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Ray on 2/20/2016.
 */
public class MovieAuthenticatorService extends Service {

    private MovieAuthenticator mMovieAuthenticator;

    @Override
    public void onCreate()
    {mMovieAuthenticator = new MovieAuthenticator(this);}

    @Override
    public IBinder onBind(Intent intent)
    {return mMovieAuthenticator.getIBinder();}
}
