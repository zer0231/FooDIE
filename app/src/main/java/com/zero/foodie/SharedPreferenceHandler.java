package com.zero.foodie;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHandler {
    private Context context;

    SharedPreferenceHandler(Context context)
    {
        this.context = context;
    }

    public void destroyUser()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID","");
        editor.apply();
    }

    public void saveUser(String userID)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID",userID);
        editor.apply();
    }

    public String getUser()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("ID","");
        return userID;
    }

}
