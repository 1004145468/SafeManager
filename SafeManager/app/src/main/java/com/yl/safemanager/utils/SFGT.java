package com.yl.safemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yl.safemanager.AppLockActivity;
import com.yl.safemanager.FileLockActivity;
import com.yl.safemanager.InfoChangeActivity;
import com.yl.safemanager.MainActivity;
import com.yl.safemanager.R;
import com.yl.safemanager.RegisterActivity;

/**
 * 统跳协议
 * Created by YL on 2017/2/25.
 */


public class SFGT {

    public static int IMAGEPICK_REQUEST_CODE = 1; //打开图库请求码

    /**
     * 跳转到注册界面
     * @param context
     */
    public static void gotoRegisterActivity(Context context){
        if(context instanceof Activity){
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
           ((Activity) context).overridePendingTransition(R.anim.slide_toleft,R.anim.slide_toright);
        }
    }

    //跳转到密码找回界面
    public static void gotoForgetPswActivity(Context context){
        if(context instanceof Activity){
            Intent intent = new Intent(context, InfoChangeActivity.class);
            intent.putExtra(InfoChangeActivity.CONTENY_TYPE,InfoChangeActivity.FIND_PSW);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_toleft,R.anim.slide_toright);
        }
    }

    //打开图库
    public static void gotoImagePick(Context context){
        if(context instanceof Activity){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(intent,IMAGEPICK_REQUEST_CODE);
        }

    }

    // 打开功能界面
    public static void gotoFunctionActivity(Context context){
        if(context instanceof Activity){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    /**
     * 跳转到给文件加锁界面
     * @param context
     */
    public static void gotoFileLockActivity(Context context){
        if(context instanceof Activity){
            Intent intent = new Intent(context, FileLockActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 打开应用锁界面
     * @param context
     */
    public static void gotoAppLockActivity(Context context){
        if(context instanceof Activity){
            Intent intent = new Intent(context, AppLockActivity.class);
            context.startActivity(intent);
        }
    }

}
