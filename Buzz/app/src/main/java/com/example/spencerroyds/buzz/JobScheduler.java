package com.example.spencerroyds.buzz;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class JobScheduler extends JobService {

    private static final String TAG = "Job1";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job Started");
        doBackgroundWork(jobParameters);
        return true;
    }
    private void stuff()
    {


    }
    private void doBackgroundWork(JobParameters jobParameters){

       new Thread(new Runnable() {
           @Override
           public void run() {
              }
       }).start();
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
