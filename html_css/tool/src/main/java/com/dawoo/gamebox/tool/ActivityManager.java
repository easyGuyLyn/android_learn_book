package com.dawoo.gamebox.tool;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    private static List<Activity> list = new ArrayList<Activity>();

    public static void OnCreateActivity(Activity activity) {
        for (Activity a : list) {
            if (a.equals(list)) {
                return;
            }
        }
        list.add(activity);
    }

    public static void OnDestroyActivity(Activity activity) {
        list.remove(activity);
    }

    public static List<Activity> GetActivityList() {
        return list;
    }
} 