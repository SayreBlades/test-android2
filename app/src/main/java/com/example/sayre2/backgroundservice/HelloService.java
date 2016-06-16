package com.example.sayre2.backgroundservice;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class HelloService extends Service {
    private static String TAG = HelloService.class.getSimpleName();
    private Looper mServiceLooper;

    class CheckRunningActivity extends Thread{
        ActivityManager am = null;
        Context context = null;

        public CheckRunningActivity(Context con){
            context = con;
            am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }

        public void run(){

            while(true){
                Log.i(TAG, "inside run loop");
                // Return a list of the tasks that are currently running,
                // with the most recent being first and older ones after in order.
                // Taken 1 inside getRunningTasks method means want to take only
                // top activity from stack and forgot the olders.
                List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);

                String currentRunningActivityName = taskInfo.get(0).topActivity.getClassName();
                Log.i(TAG, currentRunningActivityName);


                if (!currentRunningActivityName.equals("com.example.sayre2.backgroundservice.FullscreenActivity")) {
                    Log.i(TAG, "starting full screen activity");
                    Intent intent = new Intent(context, FullscreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                    // show your activity here on top of PACKAGE_NAME.ACTIVITY_NAME
                }
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "inside onCreate");

        Thread thread = new Thread(new CheckRunningActivity(getApplication()));
        thread.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
