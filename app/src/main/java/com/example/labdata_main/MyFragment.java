package com.example.labdata_main;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.example.labdata_main.utils.SharedPrefsManager;

import java.io.File;

/**
 * "我的"界面Fragment
 * 显示用户个人信息和提供退出登录功能
 */
public class MyFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CROP_IMAGE = 2;

    private ImageView ivAvatar;
    private TextView tvCompany;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvPosition;
    private Button btnModifyInfo;
    private Button btnLogout;

    private SharedPrefsManager sharedPrefsManager;
    private Uri tempImageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cropImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 SharedPrefsManager
        sharedPrefsManager = new SharedPrefsManager(requireContext());

        // 初始化图片选择器
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("MyFragment", "收到图片选择结果: " + result.getResultCode());
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    Log.d("MyFragment", "选择的图片URI: " + imageUri);
                    if (imageUri != null) {
                        cropImage(imageUri);
                    } else {
                        Log.e("MyFragment", "选择的图片URI为空");
                    }
                } else {
                    Log.d("MyFragment", "用户取消选择图片或选择失败");
                }
            }
        );

        // 初始化裁剪器
        cropImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("MyFragment", "收到裁剪结果: " + result.getResultCode());
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (tempImageUri != null) {
                        Log.d("MyFragment", "裁剪完成，临时文件URI: " + tempImageUri);
                        saveAvatarUri(tempImageUri.toString());
                    } else {
                        Log.e("MyFragment", "裁剪完成但临时文件URI为空");
                    }
                } else {
                    Log.d("MyFragment", "用户取消裁剪或裁剪失败");
                }
            }
        );

        // 初始化权限请求处理
        requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                Log.d("MyFragment", "存储权限请求结果: " + (isGranted ? "已授权" : "已拒绝"));
                if (isGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(requireContext(), "需要存储权限才能选择头像", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // 初始化界面控件
        initViews(view);
        // 设置点击事件监听
        setClickListeners();
        // 显示用户信息
        displayUserInfo();

        return view;
    }

    /**
     * 初始化界面控件
     */
    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvCompany = view.findViewById(R.id.tvCompany);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPosition = view.findViewById(R.id.tvPosition);
        btnModifyInfo = view.findViewById(R.id.btnModifyInfo);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    /**
     * 设置点击事件监听
     */
    private void setClickListeners() {
        // 头像点击事件
        ivAvatar.setOnClickListener(v -> checkPermissionAndPickImage());

        // 更改信息按钮点击事件
        btnModifyInfo.setOnClickListener(v -> {
            // 处理编辑信息的点击事件
            Intent intent = new Intent(getActivity(), EditInfoActivity.class);
            startActivity(intent);
        });

        // 退出登录按钮点击事件
        btnLogout.setOnClickListener(v -> logout());
    }

    /**
     * 显示用户信息
     */
    private void displayUserInfo() {
        String company = sharedPrefsManager.getUserCompany();
        String name = sharedPrefsManager.getUserName();
        String email = sharedPrefsManager.getUserEmail();
        String phone = sharedPrefsManager.getUserPhone();

        Log.d("MyFragment", "显示用户信息:");
        Log.d("MyFragment", "公司: " + company);
        Log.d("MyFragment", "姓名: " + name);
        Log.d("MyFragment", "邮箱: " + email);
        Log.d("MyFragment", "电话: " + phone);

        tvName.setText(name);
        tvEmail.setText(email);
        tvCompany.setText(company);
        tvPhone.setText(phone != null && !phone.isEmpty() ? phone : "未设置");
    }

    /**
     * 检查权限并打开图片选择器
     */
    private void checkPermissionAndPickImage() {
        Log.d("MyFragment", "检查存储权限");
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyFragment", "请求存储权限");
            ActivityCompat.requestPermissions(requireActivity(),
                new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
        } else {
            Log.d("MyFragment", "已有权限，打开图片选择器");
            openImagePicker();
        }
    }

    /**
     * 打开图片选择器
     */
    private void openImagePicker() {
        Log.d("MyFragment", "启动图片选择器");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            imagePickerLauncher.launch(intent);
            Log.d("MyFragment", "图片选择器已启动");
        } catch (Exception e) {
            Log.e("MyFragment", "启动图片选择器失败: " + e.getMessage());
            Toast.makeText(requireContext(), "无法打开图片选择器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        // 清除登录状态
        sharedPrefsManager.clearUserLoginSession();
        // 跳转到登录界面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUserInfo();
        loadAvatar();
    }

    private void loadAvatar() {
        String avatarUri = sharedPrefsManager.getAvatarUri();
        Log.d("MyFragment", "Loading avatar URI: " + avatarUri);
        
        if (avatarUri != null && !avatarUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(avatarUri);
                Log.d("MyFragment", "Loading avatar from URI: " + uri);
                
                Picasso.get()
                       .load(uri)
                       .placeholder(R.drawable.circle_avatar_background)
                       .error(R.drawable.circle_avatar_background)
                       .into(ivAvatar);
                     
            } catch (Exception e) {
                Log.e("MyFragment", "Error loading avatar: " + e.getMessage());
                e.printStackTrace();
                ivAvatar.setImageResource(R.drawable.circle_avatar_background);
            }
        } else {
            Log.d("MyFragment", "No avatar URI found, using default background");
            ivAvatar.setImageResource(R.drawable.circle_avatar_background);
        }
    }

    private void saveAvatarUri(String uri) {
        Log.d("MyFragment", "Saving avatar URI: " + uri);
        sharedPrefsManager.saveAvatarUri(uri);
        loadAvatar(); // 立即重新加载头像
    }



    private void cropImage(Uri sourceUri) {
        Log.d("MyFragment", "开始裁剪图片，源URI: " + sourceUri);
        
        try {
            // 创建临时文件用于保存裁剪后的图片
            File outputDir = new File(requireContext().getCacheDir(), "images");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, "cropped_" + System.currentTimeMillis() + ".jpg");
            tempImageUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                outputFile
            );

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(sourceUri, "image/*");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);

            cropImageLauncher.launch(cropIntent);
            Log.d("MyFragment", "裁剪器已启动");
        } catch (Exception e) {
            Log.e("MyFragment", "裁剪图片时出错: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(requireContext(), "图片裁剪失败", Toast.LENGTH_SHORT).show();
        }
    }
}
