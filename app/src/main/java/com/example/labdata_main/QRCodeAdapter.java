package com.example.labdata_main;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.example.labdata_main.model.Equipment;
import org.json.JSONObject;
import java.util.ArrayList;

public class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.QRCodeViewHolder> {
    private static final String TAG = "QRCodeAdapter";
    private final Context context;
    private final ArrayList<Equipment> equipmentList;

    public QRCodeAdapter(Context context, ArrayList<Equipment> equipmentList) {
        this.context = context;
        this.equipmentList = equipmentList;
        Log.d(TAG, "QRCodeAdapter created with " + (equipmentList != null ? equipmentList.size() : 0) + " items");
    }

    @NonNull
    @Override
    public QRCodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_qr_code, parent, false);
        return new QRCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRCodeViewHolder holder, int position) {
        try {
            Equipment equipment = equipmentList.get(position);
            Log.d(TAG, "Binding equipment at position " + position);

            // 清除之前的图片
            holder.ivQRCode.setImageDrawable(null);
            
            // 设置默认背景色
            holder.ivQRCode.setBackgroundColor(android.graphics.Color.WHITE);

            // 生成并设置二维码
            generateQRCode(holder.ivQRCode, equipment);

        } catch (Exception e) {
            Log.e(TAG, "Error in onBindViewHolder: " + e.getMessage(), e);
            Toast.makeText(context, "显示设备信息时出错", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateQRCode(ImageView imageView, Equipment equipment) {
        try {
            if (equipment == null) {
                Log.e(TAG, "Equipment is null");
                throw new IllegalArgumentException("Equipment cannot be null");
            }

            // 创建包含所有设备信息的JSON对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", equipment.getType());
            jsonObject.put("manufacturer", equipment.getManufacturer());
            jsonObject.put("model", equipment.getModel());
            jsonObject.put("purchaseYear", equipment.getPurchaseYear());
            
            Log.d(TAG, "Creating QR code with data: " + jsonObject.toString());

            // 计算合适的二维码大小
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int qrSize = Math.min(screenWidth - 32, 800); // 考虑屏幕宽度，最大800

            // 生成二维码
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(
                jsonObject.toString(),
                BarcodeFormat.QR_CODE,
                qrSize,  // 宽度
                qrSize   // 高度
            );

            // 转换为位图
            BarcodeEncoder encoder = new BarcodeEncoder();
            final Bitmap bitmap = encoder.createBitmap(bitMatrix);
            
            if (bitmap == null) {
                Log.e(TAG, "Generated bitmap is null");
                throw new IllegalStateException("Failed to generate QR code bitmap");
            }

            // 在主线程中设置图片
            imageView.post(() -> {
                try {
                    // 确保 ImageView 可见
                    imageView.setVisibility(View.VISIBLE);
                    // 设置缩放类型
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    // 设置图片
                    imageView.setImageBitmap(bitmap);
                    // 强制重绘
                    imageView.invalidate();
                    Log.d(TAG, "QR code set to ImageView successfully");
                } catch (Exception e) {
                    Log.e(TAG, "Error setting bitmap to ImageView: " + e.getMessage(), e);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error generating QR code: " + e.getMessage(), e);
            imageView.post(() -> {
                imageView.setImageResource(android.R.drawable.ic_dialog_alert);
                Toast.makeText(context, "生成二维码失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return equipmentList != null ? equipmentList.size() : 0;
    }

    static class QRCodeViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivQRCode;

        QRCodeViewHolder(View itemView) {
            super(itemView);
            ivQRCode = itemView.findViewById(R.id.ivQRCode);
        }
    }
}
