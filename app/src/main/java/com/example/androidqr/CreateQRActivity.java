package com.example.androidqr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CreateQRActivity extends AppCompatActivity {

    EditText inQRCode,in_printer_id;
    TextView buttonCreateQR,button_print,button_create_encrypt_qr;
    TextInputEditText in_encrypt;
    Button button_encrypt;
    ImageView imageQR;
    PrintBluetooth printBT = new PrintBluetooth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_activity);

        inQRCode = findViewById(R.id.in_qr_code);
        buttonCreateQR = findViewById(R.id.button_create);
        imageQR = findViewById(R.id.image_qr);
        in_printer_id = findViewById(R.id.in_printer_id);
        button_print = findViewById(R.id.button_print);
        button_encrypt = findViewById(R.id.button_encrypt);
        button_create_encrypt_qr = findViewById(R.id.button_create_encrypt_qr);
        in_encrypt = findViewById(R.id.in_encrypt);

        buttonCreateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inQRCode.getText().toString().isEmpty()){
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(inQRCode.getText().toString(), BarcodeFormat.QR_CODE,300,300);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageQR.setImageBitmap(bitmap);
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        });

        button_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inQRCode.getText().toString().isEmpty()){
                    try {
                        in_encrypt.setText(Crypto.encrypt(inQRCode.getText().toString()));
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        });

        button_create_encrypt_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inQRCode.getText().toString().isEmpty()){
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(in_encrypt.getText().toString(), BarcodeFormat.QR_CODE,300,300);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageQR.setImageBitmap(bitmap);
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        });

        button_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintBluetooth.printer_id = in_printer_id.getText().toString();
                Bitmap qrBit = printQRCode(in_encrypt.getText().toString());
                java.util.Date today = new java.util.Date();
                SimpleDateFormat format_date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                String print_date = format_date.format(today);
                try {
                    printBT.findBT();
                    printBT.openBT();
                    printBT.printQrCode(qrBit);
                    printBT.printText("Test Print Encrypt QR Codes",in_encrypt.getText().toString(),print_date);
                    printBT.closeBT();
                }catch(IOException ex){ex.printStackTrace();}
            }
        });
    }

    private Bitmap printQRCode(String textToQR){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToQR, BarcodeFormat.QR_CODE, 300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        }catch(WriterException e){
            e.printStackTrace();
            return null;
        }
    }
}