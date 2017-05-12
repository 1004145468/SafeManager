package com.yl.safemanager.utils;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.KeyChain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by YL on 2017/3/3.
 */

public class FileConcealUtils {

    /**
     * 大文件进行加密
     *
     * @param context
     * @param srcPath
     * @param desPath
     * @return
     */
    public static boolean EncryptionFile(Context context, String srcPath, String desPath) {
        KeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        if (!crypto.isAvailable()) {
            return false;
        }
        File srcFile = new File(srcPath);
        File outFile = new File(desPath);
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(srcFile);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            outputStream = crypto.getCipherOutputStream(fileOutputStream, Entity.create("entity_id"));
            byte[] data = new byte[2048];
            int len = 0;
            while ((len = fileInputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
            return false;
        } catch (KeyChainException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件解密
     */
    public static boolean DecryptionFile(Context context, String srcPath, String desPath) {
        KeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        if (!crypto.isAvailable()) {
            return false;
        }
        File srcFile = new File(srcPath);
        File outFile = new File(desPath);
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(srcFile);
            inputStream = crypto.getCipherInputStream(fileInputStream, Entity.create("entity_id"));
            fileOutputStream = new FileOutputStream(outFile);
            byte[] data = new byte[2048];
            int len = 0;
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
            return false;
        } catch (KeyChainException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
