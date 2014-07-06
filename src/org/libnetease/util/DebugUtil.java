package org.libnetease.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

public class DebugUtil {

    @TargetApi(11)//�����ϸ���ģʽ
    public static void enableStrictMode(Object ...clazzes) {
        if (hasGingerbread()) {//�汾9 2.3��ʼ�Ĵ���������⹦��
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (hasHoneycomb()) {//���Ѳ���Ϊ��Ļ��˸
                threadPolicyBuilder.penaltyFlashScreen();
                for(Object clazz:clazzes){//�������ʵ������
                	vmPolicyBuilder.setClassInstanceLimit(clazz.getClass(), 1);
                }
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }
    //������--8   2.2
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    //����--9   2.3
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
    //�䳲--11   3.1
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    //����--12   3.2
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    //������--16   4.3
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
}
