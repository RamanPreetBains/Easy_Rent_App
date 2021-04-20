package com.example.easyrent.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.easyrent.R;
import com.example.easyrent.extra.SessionData;
import com.example.easyrent.extra.UserData;

public class SignUpActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 100;
    private static final int MY_CAMERA_PERMISSION_CODE = 200;

    private Button btn_sign_up;
    private EditText et_password, et_last_name, et_first_name;
    private ImageView profile_pic;
    private Bitmap photo;

    @Override

    protected int getLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initViews() {
        btn_sign_up = findViewById(R.id.btn_sign_up);
        et_password = findViewById(R.id.et_password);
        et_last_name = findViewById(R.id.et_last_name);
        et_first_name = findViewById(R.id.et_first_name);
        profile_pic = findViewById(R.id.profile_pic);
    }

    @Override
    protected void initViewsWithData() {
        profile_pic.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        btn_sign_up.setOnClickListener(v -> {

            if(photo == null) {
                photo = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.user);
            }
            UserData userData = new UserData();
            userData.setUserId(SessionData.I.uniqueId);
            userData.setFirstName(et_first_name.getText().toString().trim());
            userData.setLastName(et_last_name.getText().toString().trim());
            userData.setPassword(et_password.getText().toString().trim());
            userData.setProfilePic(SessionData.I.encodeToBase64(photo));
            SessionData.I.localData.userList.add(userData);
            SessionData.I.saveLocalData();
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            profile_pic.setImageBitmap(photo);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

