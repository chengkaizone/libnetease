package org.libnetease.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

public class DebugUtil {

    @TargetApi(11)//启用严格检查模式
    public static void enableStrictMode(Object ...clazzes) {
        if (hasGingerbread()) {//版本9 2.3开始的代码质量检测功能
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (hasHoneycomb()) {//提醒策略为屏幕闪烁
                threadPolicyBuilder.penaltyFlashScreen();
                for(Object clazz:clazzes){//限制类的实例个数
                	vmPolicyBuilder.setClassInstanceLimit(clazz.getClass(), 1);
                }
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }
    //冻酸奶--8   2.2
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    //姜饼--9   2.3
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
    //蜂巢--11   3.1
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    //蜂窝--12   3.2
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    //果冻豆--16   4.3
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
}
