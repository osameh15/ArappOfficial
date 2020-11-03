package ir.arapp.arappofficial.AppService;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.util.Log;

public class SessionManager
{
    //logCa tag
    private static String TAG = SessionManager.class.getSimpleName();

    //shared preference ...
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context _context;

    //shared preference mode ...
    private int PRIVATE_MODE =0;

    //shared preference filename ...
    private static final String PREF_NAME = "arapp";
    private static final String KEY_IS_LOGEDIN = "isLoggedIn";
    private static final String KEY_SERVICE_PROVIDER = "serviceType";
    private static final String KEY_PHONE = "phoneNumber";
    private static final String KEY_VERSION_NAME = "versionName";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context _context)
    {
        this._context = _context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor =sharedPreferences.edit();
    }

    //Check version name
    public String getVersionName(Context _context, Class cls)
    {
        try
        {
            ComponentName componentName = new ComponentName(_context, cls);
            PackageInfo packageInfo = _context.getPackageManager().getPackageInfo(componentName.getPackageName(), 0);
            return "Version: " + packageInfo.versionName;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    //set login and session modified ...
    public void setLogin(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_IS_LOGEDIN, isLoggedIn);
        //commit change ...
        editor.commit();

        Log.d(TAG, "user login session modified!");
    }

    //Set service type of providers ...
    public void setServiceProvider(String serviceProvider)
    {
        editor.putString(KEY_SERVICE_PROVIDER, serviceProvider);
        //commit change ...
        editor.commit();

        Log.d(TAG, "Service type session modified!");
    }

    //set user phone Number  and session modified...
    public void setUserPhone(String userPhone)
    {
        editor.putString(KEY_PHONE, userPhone);
        //commit change ...
        editor.commit();

        Log.d(TAG, "user phone Number session modified!");
    }

    //set pop up dialog(recently changed)only once time after each app update...
    public void setStoredVersionName(String versionName)
    {
        editor.putString(KEY_VERSION_NAME, versionName);
        //commit change
        editor.commit();

        Log.d(TAG, "Version name session modified!");
    }

    // get login session modified ...
    public boolean isLoggedIn()
    {
        return sharedPreferences.getBoolean(KEY_IS_LOGEDIN, false);
    }

    //get service type of providers...
    public String getServiceProvider()
    {
        return sharedPreferences.getString(KEY_SERVICE_PROVIDER, "");
    }

    //get user phone number session modified ...
    public String getUserPhone()
    {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    //get stored version name ...
    public String getStoredVersionName()
    {
        return sharedPreferences.getString(KEY_VERSION_NAME, "");
    }
}
