package com.yl.safemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.AdviceAcitvity;
import com.yl.safemanager.AppLockActivity;
import com.yl.safemanager.ChangeInfoActivity;
import com.yl.safemanager.ChangePswActivity;
import com.yl.safemanager.ChangeSexActivity;
import com.yl.safemanager.DownLoadFileActivity;
import com.yl.safemanager.FileLockActivity;
import com.yl.safemanager.ForgetPswActivity;
import com.yl.safemanager.LockConfigActivity;
import com.yl.safemanager.LoginActivity;
import com.yl.safemanager.MainActivity;
import com.yl.safemanager.R;
import com.yl.safemanager.RegisterActivity;
import com.yl.safemanager.SMDataActivity;
import com.yl.safemanager.SMLockActivity;
import com.yl.safemanager.UploadFileActivity;
import com.yl.safemanager.UserInfoMotifyActivity;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.yl.safemanager.MainActivity.EXIT_CODE;
import static com.yl.safemanager.UserInfoMotifyActivity.CODE_CHANGE_SEX;

/**
 * 统跳协议
 * Created by YL on 2017/2/25.
 */


public class SFGT {

    public static final int IMAGEPICK_REQUEST_CODE = 6; //打开图库请求码
    public static final int REQUEST_FILE = 2;

    /**
     * 跳转到注册界面
     *
     * @param context
     */
    public static void gotoRegisterActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_toleft, R.anim.slide_toright);
        }
    }

    //跳转到密码找回界面
    public static void gotoForgetPswActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, ForgetPswActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_toleft, R.anim.slide_toright);
        }
    }

    //打开图库
    public static void gotoImagePick(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(intent, IMAGEPICK_REQUEST_CODE);
        }

    }

    // 打开功能界面
    public static void gotoFunctionActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    /**
     * 跳转到给文件加锁界面
     *
     * @param context
     */
    public static void gotoFileLockActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, FileLockActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 打开应用锁界面,如果没有配置过应用解密码，就打开配置界面
     *
     * @param context
     */
    public static void gotoAppLockActivity(Context context) {
        if (context instanceof Activity) {
            String shortCode = SpUtils.getString(context, LockConfigActivity.SHORT_CODE);
            if (shortCode == null) {
                gotoLockConfigActivity(context, true);
            } else {
                Intent intent = new Intent(context, AppLockActivity.class);
                context.startActivity(intent);
            }

        }
    }

    /**
     * 打开信息加解密模块
     *
     * @param context
     */
    public static void gotoSmsLockActivity(Context context) {
        gotoSMLockActivity(context, SMLockActivity.SMS_TYPE);
    }

    /**
     * 打开邮件加解密模块
     *
     * @param context
     */
    public static void gotoMailLockActivity(Context context) {
        gotoSMLockActivity(context, SMLockActivity.MAIL_TYPE);
    }


    private static void gotoSMLockActivity(Context context, int type) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, SMLockActivity.class);
            intent.putExtra("type", type);
            context.startActivity(intent);
        }
    }


    /**
     * 打开手机短信箱，并填入内容
     *
     * @param context
     * @param content
     */
    public static void openSmsBox(Context context, String content) {
        try {
            if (context instanceof Activity) {
                Uri uri = Uri.parse("smsto:");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", content);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.activitynofind), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开手机邮箱，并填入内容
     *
     * @param context
     * @param content
     */
    public static void openMailBox(Context context, String content) {
        try {
            if (context instanceof Activity) {
                Uri uri = Uri.parse("mailto:");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_TEXT, content); // 正文
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.systemmailnofind), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开提交建议界面
     *
     * @param context
     */
    public static void gotoAdviceActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, AdviceAcitvity.class);
            context.startActivity(intent);
        }
    }

    public static void gotoNoteRecordActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, SMDataActivity.class);
            context.startActivity(intent);
        }
    }

    public static void gotoBackupActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, UploadFileActivity.class);
            context.startActivity(intent);
        }
    }

    public static void openFileChoose(Context context) {
        try {
            if (context instanceof Activity) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                ((Activity) context).startActivityForResult(intent, REQUEST_FILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.activitynofind), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开文件下载模块
     *
     * @param context
     */
    public static void gotoDownLoadFileActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, DownLoadFileActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 打开个人中心面板
     *
     * @param context
     */
    public static void gotoUserInfoActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, UserInfoMotifyActivity.class);
            ((Activity) context).startActivityForResult(intent, EXIT_CODE);
        }
    }

    /**
     * 修改昵称界面
     *
     * @param context
     */
    public static void gotoChangeInfoActivity(Context context, int requestCode) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, ChangeInfoActivity.class);
            intent.putExtra("type", requestCode);
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 修改性别
     *
     * @param context
     */
    public static void gotoChangeSexActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, ChangeSexActivity.class);
            ((Activity) context).startActivityForResult(intent, CODE_CHANGE_SEX);
        }
    }

    /**
     * 修改密码
     *
     * @param context
     */
    public static void gotoChangePswActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, ChangePswActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 进入密码配置界面
     *
     * @param context
     */
    public static void gotoLockConfigActivity(Context context, boolean autoLockActivity) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, LockConfigActivity.class);
            intent.putExtra(LockConfigActivity.BACKTOLOCKACTIVITY, autoLockActivity);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_totop, 0);
        }
    }

    public static void gotoPermisstion(Context context) {
        try {
            if (context instanceof Activity) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                ((Activity) context).startActivityForResult(intent, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast((Activity) context, context.getString(R.string.functionisnavai), Effects.thumbSlider, R.id.id_root);
        }
    }

    /**
     * 打开登录界面
     *
     * @param context
     */
    public static void gotoLoginActivity(Context context) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 进入聊天列表
     *
     * @param context
     */
    public static void gotoConversationListActivity(Context context) {
        if (context instanceof Activity) {
            Map<String, Boolean> map = new HashMap<>();
            map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
            map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示
            RongIM.getInstance().startConversationList(context, map);
        }
    }

    /**
     * 进入聊天界面
     *
     * @param context
     * @param frientId
     */
    public static void gotoConversionActivity(Context context, String frientId, String title) {
        if (context instanceof Activity) {
            RongIM.getInstance().startPrivateChat(context, frientId, title);
        }
    }

    /**
     * 进入系统的设置界面
     */
    public static void gotoSetttingActivity(Context context) {
        if (context instanceof Activity) {
            try {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                ((Activity) context).startActivityForResult(intent, 1);
            } catch (Exception e) {
            }
        }
    }
}
