package com.merpyzf.lib

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Description:
 * Date: 2/2/21
 *
 * @author wangke
 */
internal class EncryptHelper {

    companion object {
        private var instance: EncryptHelper? = null
        private var aesKey: String? = null
        fun getInstance(aesKey: String): EncryptHelper {
            if (instance == null) {
                synchronized(Any::class.java) {
                    if (instance == null) {
                        instance = EncryptHelper()
                    }
                }
            }
            Companion.aesKey = aesKey.subSequence(0, 16).toString()
            return instance!!
        }
    }

    /**
     * SHA加密
     *
     * @param strText 明文
     * @return
     */
    private fun SHA(strText: String): String {
        // 返回值
        var strResult: String? = null
        // 是否是有效字符串
        if (strText.isNotEmpty()) {
            try {
                // SHA 加密开始
                val messageDigest = MessageDigest.getInstance("SHA-256")
                // 传入要加密的字符串
                messageDigest.update(strText.toByteArray())
                val byteBuffer = messageDigest.digest()
                val strHexString = StringBuffer()
                for (i in byteBuffer.indices) {
                    val hex = Integer.toHexString(
                        0xff and byteBuffer[i]
                            .toInt()
                    )
                    if (hex.length == 1) {
                        strHexString.append('0')
                    }
                    strHexString.append(hex)
                }
                strResult = strHexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return strResult!!
    }

    /**
     * AES128加密
     *
     * @param plainText 明文
     * @return
     */
    fun encrypt(plainText: String): String {
        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keySpec = SecretKeySpec(
                aesKey!!.toByteArray(), "AES"
            )
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encrypted = cipher.doFinal(plainText.toByteArray())
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }!!
    }

    /**
     * AES128解密
     *
     * @param cipherText 密文
     * @return
     */
    fun decrypt(cipherText: String): String {
        return try {
            val encrypted = Base64.decode(cipherText, Base64.NO_WRAP)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keySpec = SecretKeySpec(
                aesKey!!.toByteArray(), "AES"
            )
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val original = cipher.doFinal(encrypted)
            String(original)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }!!
    }
}