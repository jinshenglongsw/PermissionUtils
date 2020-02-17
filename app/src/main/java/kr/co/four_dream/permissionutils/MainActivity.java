package kr.co.four_dream.permissionutils;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PermissionsUtils.IPermissionsResult {

    private Button button;

    private TextView textView;

    private static final int PERMISSION_REQUEST_CODE = 888;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        activity = this;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //再集合中添加你想要的权限,别忘了在Mani文件中声明啊.
                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //然后一句话跳转到授权就可以了.
                PermissionsUtils.getInstance().checkPermissions(activity, permissions, MainActivity.this, PERMISSION_REQUEST_CODE);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                PermissionsUtils.getInstance().onRequestPermissionsResult(this, permissions, grantResults);
                break;

        }
    }

    @Override
    public void permissionPass() {
        Toast.makeText(activity, "权限获得成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permissionNoPass() {
        Toast.makeText(activity, "因未获得权限导致无法使用此功能,请授权.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permissionNoPassNaver() {
        //解释原因，并且引导用户至设置页手动授权

        new AlertDialog.Builder(activity)
                .setMessage("无法使用此功能." +
                        "未获得相应权限:\r\n" +
                        "请手动在设置窗口内准许相应权限")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户至设置页手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击了取消，那么就结束这个页面吧。
                        finish();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //点击了back，那就结束吧
                finish();
            }

        }).show();
    }

}
