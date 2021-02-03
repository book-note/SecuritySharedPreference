package com.merpyzf.securitysharedpreference

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.merpyzf.lib.SecuritySharedPreference

class MainActivity : AppCompatActivity() {
    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val securitySharedPreference = SecuritySharedPreference(this, "security_preference", Context.MODE_PRIVATE)
        securitySharedPreference.edit().putString("key_string", "insert string value").commit()
        securitySharedPreference.edit().putBoolean("key_boolean", true).commit()
        securitySharedPreference.edit().putFloat("key_float", 3.1415926f).commit()
        securitySharedPreference.edit().putLong("key_long", 10000L).commit()

        Log.i("wk", "key_string: ${securitySharedPreference.getString("key_string", "没有获取到")}")
        Log.i("wk", "key_boolean: ${securitySharedPreference.getBoolean("key_boolean", false)}")
        Log.i("wk", "key_float: ${securitySharedPreference.getFloat("key_float", 0.0f)}")
        Log.i("wk", "key_long: ${securitySharedPreference.getLong("key_long", 0L)}")
    }
}