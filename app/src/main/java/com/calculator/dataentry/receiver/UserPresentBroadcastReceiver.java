package com.calculator.dataentry.receiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.calculator.dataentry.R;
import com.calculator.dataentry.activity.MainActivity;
import com.calculator.dataentry.common.SessionManagment;

import java.util.List;

public class UserPresentBroadcastReceiver extends BroadcastReceiver {
 SessionManagment sd ;

 @Override
 public void onReceive(Context context, Intent intent) {
  sd = new SessionManagment(context);

  if (isAppForground(context)&& sd.getLOGIN_STATUS().equals("true")) {
   sd.setLOGIN_STATUS("false");
   Intent i = new Intent(context, MainActivity.class);
   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
   context.startActivity(i);
   int pid = android.os.Process.myPid();
   android.os.Process.killProcess(pid);
   System.exit(0);
  }

 }

 public boolean isAppForground(Context mContext) {

  ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
  List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
  if (!tasks.isEmpty()) {
   ComponentName topActivity = tasks.get(0).topActivity;
   if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
    return false;
   }
  }
  return true;
 }

}

    /*@Override
    public void onReceive(Context arg0, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            arg0.startActivity(i);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(0);
        }
        if(intent.getAction().equalsIgnoreCase("android.intent.category.HOME") )
        {
            Toast.makeText(arg0,"home",Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equalsIgnoreCase("android.intent.action.SCREEN_OFF") )
        {
            Toast.makeText(arg0,"screen off",Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equalsIgnoreCase("android.intent.action.DIAL") )
        {
            Toast.makeText(arg0,"dial",Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equalsIgnoreCase("android.intent.action.CALL")){
            Toast.makeText(arg0,"call",Toast.LENGTH_SHORT).show();

        }
     }*/


