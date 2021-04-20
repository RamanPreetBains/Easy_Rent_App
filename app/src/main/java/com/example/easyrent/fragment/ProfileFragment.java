package com.example.easyrent.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.easyrent.R;
import com.example.easyrent.extra.SessionData;
import com.example.easyrent.extra.UserData;


public class ProfileFragment extends Fragment {

    private static final int MY_CAMERA_PERMISSION_CODE = 200;
    private static final int CAMERA_REQUEST = 150;
    private ImageView profilePic;
    private Button btnEdit;
    private EditText etFirstName, etLastName;
    private Bitmap photo;
    private boolean isEdit = false;
    private OnProfileUpdateListener onProfileUpdateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(rootView);
        initViewWithData();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onProfileUpdateListener = (OnProfileUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    private void initViews(View view) {
        profilePic = view.findViewById(R.id.profile_pic);
        btnEdit = view.findViewById(R.id.btn_edit);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
    }

    private void initViewWithData() {
        photo = SessionData.I.decodeBase64(SessionData.I.localData.currentUserData.getProfilePic());
        profilePic.setImageBitmap(photo);
        etFirstName.setText(SessionData.I.localData.currentUserData.getFirstName());
        etLastName.setText(SessionData.I.localData.currentUserData.getLastName());
        isComponentsClickable(false);
        profilePic.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }


        });
        btnEdit.setOnClickListener(v -> {
            if (isEdit) {
                if (isValidate()) {

                    for (int i = 0; i < SessionData.I.localData.userList.size(); i++) {
                        if (SessionData.I.localData.currentUserData.getUserId().equals(SessionData.I.localData.userList.get(i).getUserId())) {
                            SessionData.I.localData.userList.get(i).setFirstName(etFirstName.getText().toString().trim());
                            SessionData.I.localData.userList.get(i).setLastName(etLastName.getText().toString().trim());
                            SessionData.I.localData.userList.get(i).setProfilePic(SessionData.I.encodeToBase64(photo));
                            SessionData.I.localData.currentUserData = SessionData.I.localData.userList.get(i);
                            onProfileUpdateListener.onProfileUpdate(SessionData.I.localData.currentUserData);
                        }
                    }
                    SessionData.I.saveLocalData();
                }
            } else {
                isEdit = true;
                isComponentsClickable(true);
                btnEdit.setText(getResources().getString(R.string.save));
            }
        });
    }

    private void isComponentsClickable(boolean isClickable) {
        profilePic.setEnabled(isClickable);
        etFirstName.setEnabled(isClickable);
        etLastName.setEnabled(isClickable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(photo);

        }
    }

    private boolean isValidate() {
        boolean isValid = true;
        if (photo == null) {
            Toast.makeText(getActivity(), "Please select Profile Pic", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError("Field should not be empty");
            isValid = false;
        }
        if (etLastName.getText().toString().trim().isEmpty()) {
            etLastName.setError("Field should not be empty");
            isValid = false;
        }
        return isValid;
    }

    public interface OnProfileUpdateListener {
        void onProfileUpdate(UserData userData);
    }
}