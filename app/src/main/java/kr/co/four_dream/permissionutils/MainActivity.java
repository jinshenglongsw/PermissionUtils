package kr.co.four_dream.permissionutils;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;

    private static final int CAMERA_REQUEST_CODE = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.mainButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //再集合中添加你想要的权限,别忘了在Mani文件中声明啊.
                String[] permissions = new String[]{Manifest.permission.CAMERA};
                //然后一句话跳转到授权就可以了.
                PermissionsUtils.getInstance().checkPermissions(MainActivity.this, permissions, permissionsResult, CAMERA_REQUEST_CODE);
            }
        });
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //授权成功了,你想干什么都行老弟
            Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
        }


    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                PermissionsUtils.getInstance().onRequestPermissionsResult(this, permissions, grantResults);
                break;

        }
    }
}
