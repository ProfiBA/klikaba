package ba.klika.vanja.employee;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import ba.klika.vanja.employee.data.DBHelper;

/**
 * Created by vanja on 03.02.2016..
 */
public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;
    public SQLiteDatabase db;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}