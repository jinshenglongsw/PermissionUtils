package kr.co.four_dream.permissionutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    private PermissionUtil() {
    }

    private static PermissionUtil permissionUtil;
    private IPermissionsResult iPermissionsResult;

    private List<String> needPermissionList;

    public static PermissionUtil getInstance() {
        if (permissionUtil == null) {
            permissionUtil = new PermissionUtil();
        }
        return permissionUtil;
    }

    /**
     * 선택 권한 예제.
     */
    private final String UNNECESSARY_FOR_ACCESS_BACKGROUND_LOCATION = "ACCESS_BACKGROUND_LOCATION";

    public void checkPermissions(Activity activity, String[] permissions, @NonNull IPermissionsResult permissionsResult, int REQUEST_CODE) {
        iPermissionsResult = permissionsResult;

        if (Build.VERSION.SDK_INT < 23) {
            permissionsResult.permissionPass();
            return;
        }

        needPermissionList = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                // 선택 권한
                if (permissions[i].contains(UNNECESSARY_FOR_ACCESS_BACKGROUND_LOCATION)) {

                } else {
                    // 필수 권한을 리스트에 추가
                    needPermissionList.add(permissions[i]);
                }
            }
        }

        // start request
        if (needPermissionList.size() > 0) {

            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);

        } else {
            // permission all pass.
            permissionsResult.permissionPass();

        }
    }

    public void onRequestPermissionsResult(final Activity context, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean hasAllGranted = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                // 선택 권한
                if (permissions[i].contains(UNNECESSARY_FOR_ACCESS_BACKGROUND_LOCATION)) {
                    break;
                }

                hasAllGranted = false;
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {

                    iPermissionsResult.permissionNoPassNever();

                } else {
                    //权限请求失败，但未选中“不再提示”选项

                    iPermissionsResult.permissionNoPass();
                }

//                }
                break;
            }
        }
        if (hasAllGranted) {
            // permission all pass.
            iPermissionsResult.permissionPass();
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
        void permissionNoPassNever();
    }
}
