package com.example.androidqr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRActivity extends AppCompatActivity {

    Button buttonScan;
    TextView qrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_activity);

        buttonScan = findViewById(R.id.button_scan);
        qrResult = findViewById(R.id.qr_text);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ScanQRActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.setCaptureActivity(OrientationCapture.class);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(ScanQRActivity.this, "Cancel Scan", Toast.LENGTH_LONG).show();
            }
            else {
                String nn = result.getContents();
                try {
                    String xx = Crypto.decrypt(nn);
                    Toast.makeText(ScanQRActivity.this, "The Result is " + xx, Toast.LENGTH_LONG).show();
                    qrResult.setText(xx);
                }catch(Exception e){e.printStackTrace();}
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}