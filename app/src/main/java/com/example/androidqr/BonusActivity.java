package com.example.androidqr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class BonusActivity extends AppCompatActivity {

    String[] list_produk = { "Food", "Non Food", "Peralatan Rumah", "Permainan", "Makanan Segar", "Stationary" };
    TextInputEditText input_produk;
    EditText in_printer_id;
    Button print_struk;
    RadioButton radioButtonYa,radioButtonTidak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);

        PrintBluetooth printBT = new PrintBluetooth();

        input_produk = findViewById(R.id.input_produk);
        radioButtonYa = findViewById(R.id.radioButtonYa);
        radioButtonTidak = findViewById(R.id.radioButtonTidak);
        in_printer_id = findViewById(R.id.printer_id);
        print_struk = findViewById(R.id.print_struk);

        final ArrayAdapter<String> spinner_agama = new  ArrayAdapter<String>(BonusActivity.this,android.R.layout.simple_spinner_dropdown_item, list_produk);
        input_produk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                new AlertDialog.Builder(BonusActivity.this)
                        .setTitle("Pilih Produk")
                        .setAdapter(spinner_agama, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                input_produk.setText(list_produk[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        print_struk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintBluetooth.printer_id = in_printer_id.getText().toString();
                java.util.Date today = new java.util.Date();
                SimpleDateFormat format_date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                String print_date = format_date.format(today);
                String status_barang = "";
                if (radioButtonYa.isChecked()){
                    status_barang = "Diantar";
                }
                else if (radioButtonTidak.isChecked()){
                    status_barang = "Ambil Sendiri";
                }
                try {
                    printBT.findBT();
                    printBT.openBT();
                    printBT.printStruk(input_produk.getText().toString(),status_barang,print_date);
                    printBT.closeBT();
                }catch(IOException ex){ex.printStackTrace();}
            }
        });
    }
}