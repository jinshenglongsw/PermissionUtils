package kr.co.four_dream.permissionutils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限工具类
 */
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
            permissionsResult.passPermissons();
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
            permissionsResult.passPermissons();
            return;
        }


    }

    public void onRequestPermissionsResult(final Activity context, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean hasAllGranted = true;
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {
                    //解释原因，并且引导用户至设置页手动授权
                    new android.support.v7.app.AlertDialog.Builder(context)
                            .setMessage("无法使用此功能." +
                                    "未获得相应权限:\r\n" +
                                    "请手动在设置窗口内准许相应权限")
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                                    intent.setData(uri);
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户手动授权，权限请求失败
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //引导用户手动授权，权限请求失败
                        }
                    }).show();

                } else {
                    //权限请求失败，但未选中“不再提示”选项
                    Toast.makeText(context, "因未获得权限导致无法使用此功能,请授权.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        if (hasAllGranted) {
            //全部权限通过，可以进行下一步操作。。。
            mPermissionsResult.passPermissons();
        }

    }


    public interface IPermissionsResult {
        void passPermissons();
    }

}
