package kr.co.four_dream.permissionutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {

    private PermissionsUtils() {
    }

    private static PermissionsUtils permissionsUtils;
    private IPermissionsResult mPermissionsResult;

    public static PermissionsUtils getInstance() {
        if (permissionsUtils == null) {
            permissionsUtils = new PermissionsUtils();
        }
        return permissionsUtils;
    }

    public void checkPermissions(Activity activity, String[] permissions, @NonNull IPermissionsResult permissionsResult, int REQUEST_CODE) {
        mPermissionsResult = permissionsResult;

        //6.0 以上才需要动态权限
        if (Build.VERSION.SDK_INT < 23) {
            permissionsResult.permissionPass();
            return;
        }

        //创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到needPermissionList中
        List<String> needPermissionList = new ArrayList<>();
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                needPermissionList.add(permissions[i]);//添加还未授予的权限/통과되지 못한 권함을 추가
            }
        }

        //申请权限
        if (needPermissionList.size() > 0) {
            //有权限没有通过，需要申请
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
            permissionsResult.permissionPass();

        }


    }

    public void onRequestPermissionsResult(final Activity context, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean hasAllGranted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {

                    mPermissionsResult.permissionNoPassNaver();

                } else {
                    //权限请求失败，但未选中“不再提示”选项

                    mPermissionsResult.permissionNoPass();
                }
                break;
            }
        }
        if (hasAllGranted) {
            //全部权限通过，可以进行下一步操作。。。
            mPermissionsResult.permissionPass();
        }

    }


    public interface IPermissionsResult {
        /**
         * 权限通过
         */
        void permissionPass();

        /**
         * 权限未通过
         */
        void permissionNoPass();

        /**
         * 权限未通过且点击了不再显示
         */
        void permissionNoPassNaver();
    }
}
