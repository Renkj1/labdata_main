package com.example.labdata_main;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.example.labdata_main.model.Equipment;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class QRCodeDisplayActivity extends AppCompatActivity {
    private static final String TAG = "QRCodeDisplay";
    private ViewPager2 viewPager;
    private ArrayList<Equipment> equipmentList;
    private QRCodeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "Starting QRCodeDisplayActivity");
            setContentView(R.layout.activity_qr_code_display);

            // 获取传递过来的设备列表
            equipmentList = getIntent().getParcelableArrayListExtra("equipment_list");
            Log.d(TAG, "Received equipment list: " + (equipmentList != null ? equipmentList.size() : "null"));
            
            if (equipmentList == null) {
                equipmentList = new ArrayList<>();
                Log.w(TAG, "Equipment list is null, creating empty list");
            }

            // 初始化 ViewPager2
            viewPager = findViewById(R.id.viewPager);
            if (viewPager == null) {
                throw new IllegalStateException("ViewPager2 not found in layout");
            }
            adapter = new QRCodeAdapter(this, equipmentList);
            viewPager.setAdapter(adapter);
            Log.d(TAG, "ViewPager2 initialized");

            // 设置 TabLayout
            TabLayout tabLayout = findViewById(R.id.tabLayout);
            if (tabLayout == null) {
                throw new IllegalStateException("TabLayout not found in layout");
            }
            new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("设备 " + (position + 1))
            ).attach();
            Log.d(TAG, "TabLayout initialized");

            // 保存按钮点击事件
            MaterialButton btnSave = findViewById(R.id.btnSave);
            if (btnSave == null) {
                throw new IllegalStateException("Save button not found in layout");
            }
            btnSave.setOnClickListener(v -> saveCurrentQRCode());

            // 完成按钮点击事件
            MaterialButton btnFinish = findViewById(R.id.btnFinish);
            if (btnFinish == null) {
                throw new IllegalStateException("Finish button not found in layout");
            }
            btnFinish.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
            Log.d(TAG, "Buttons initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void saveCurrentQRCode() {
        try {
            int currentPosition = viewPager.getCurrentItem();
            if (currentPosition >= 0 && currentPosition < equipmentList.size()) {
                Equipment equipment = equipmentList.get(currentPosition);
                
                // 生成二维码
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", equipment.getType());
                jsonObject.put("manufacturer", equipment.getManufacturer());
                jsonObject.put("model", equipment.getModel());
                jsonObject.put("purchaseYear", equipment.getPurchaseYear());

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(jsonObject.toString(),
                        BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                // 保存到相册
                String fileName = "QRCode_" + equipment.getType() + "_" + 
                        equipment.getModel() + ".png";
                
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, 
                        Environment.DIRECTORY_PICTURES + "/LabData");

                Uri imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (imageUri != null) {
                    try (OutputStream out = getContentResolver().openOutputStream(imageUri)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        Toast.makeText(this, "二维码已保存到相册", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saving QR code: " + e.getMessage(), e);
            Toast.makeText(this, "保存失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
