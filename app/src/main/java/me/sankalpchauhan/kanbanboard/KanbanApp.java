package me.sankalpchauhan.kanbanboard;

import android.app.Application;
import android.content.Context;

public class KanbanApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        KanbanApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return KanbanApp.context;
    }
}
