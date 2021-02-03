package com.merpyzf.lib

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build

/**
 * Description: 支持加密功能的配置文件
 * Date: 2/2/21
 * @author wangke
 *
 */
class SecuritySharedPreference(
    private var context: Context,
    private var name: String,
    private var mode: Int
) : SharedPreferences {
    private var aesKey: String = "fafjkajfewfuewhfurhu"
    private var sharedPreferences = context.getSharedPreferences(name, mode)

    constructor(context: Context, name: String, mode: Int, aesKey: String) : this(
        context,
        name,
        mode
    ) {
        this.aesKey = aesKey
    }

    /**
     * 获取所有的配置项
     */
    override fun getAll(): MutableMap<String, String> {
        val decryptMap = HashMap<String, String>()
        for (entry in sharedPreferences.all) {
            entry.value?.let {
                decryptMap.put(entry.key, entry.value.toString())
            }
        }
        return decryptMap
    }

    override fun getString(key: String?, defValue: String?): String? {
        if (key !== null && defValue != null) {
            return decrypt(
                sharedPreferences.getString(
                    encrypt(key),
                    encrypt(defValue.toString())
                )!!
            )
        }
        return null
    }

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? {
        val encryptSet = sharedPreferences.getStringSet(encrypt(key), null) ?: return defValues
        val decryptSet = HashSet<String>()
        encryptSet.forEach {
            decryptSet.add(decrypt(it))
        }
        return decryptSet
    }

    override fun getInt(key: String, defValue: Int): Int {
        return decrypt(
            sharedPreferences.getString(
                encrypt(key),
                encrypt(defValue.toString())
            )!!
        ).toInt()
    }


    override fun getLong(key: String, defValue: Long): Long {
        return decrypt(
            sharedPreferences.getString(
                encrypt(key),
                encrypt(defValue.toString())
            )!!
        ).toLong()
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return decrypt(
            sharedPreferences.getString(
                encrypt(key),
                encrypt(defValue.toString())
            )!!
        ).toFloat()
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return decrypt(
            sharedPreferences.getString(
                encrypt(key),
                encrypt(defValue.toString())
            )!!
        ).toBoolean()
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(encrypt(key))
    }

    override fun edit(): SharedPreferences.Editor {
        return SecurityEditor()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }


    private fun encrypt(plainText: String): String {
        return EncryptHelper.getInstance(aesKey).encrypt(plainText)
    }

    private fun decrypt(cipherText: String): String {
        return EncryptHelper.getInstance(aesKey).decrypt(cipherText)
    }

    inner class SecurityEditor : SharedPreferences.Editor {
        private val editor = sharedPreferences.edit()

        override fun putString(key: String, value: String?): SharedPreferences.Editor {
            value?.let {
                editor.putString(encrypt(key), encrypt(it))
            }
            return this
        }

        override fun putStringSet(
            key: String,
            values: MutableSet<String>?
        ): SharedPreferences.Editor {
            val encryptSet = HashSet<String>()
            values?.let {
                for (value in it) {
                    encryptSet.add(encrypt(value))
                }
            }
            editor.putStringSet(key, encryptSet)
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            editor.putString(encrypt(key), encrypt(value.toString()))
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            editor.putString(encrypt(key), encrypt(value.toString()))
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            editor.putString(encrypt(key), encrypt(value.toString()))
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            editor.putString(encrypt(key), encrypt(value.toString()))
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            editor.remove(encrypt(key))
            return this
        }

        override fun clear(): SharedPreferences.Editor {
            editor.clear()
            return this
        }

        override fun commit(): Boolean {
            return editor.commit()
        }

        override fun apply() {
            editor.apply();
        }
    }
}