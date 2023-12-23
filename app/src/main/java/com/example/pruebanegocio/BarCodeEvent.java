package com.example.pruebanegocio;

import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class BarCodeEvent implements View.OnClickListener {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher;

    public BarCodeEvent(AppCompatActivity activity, ActivityResultCallback<ScanIntentResult> function){
        barcodeLauncher = activity.registerForActivityResult(new ScanContract(), function);
    }
    @Override
    public void onClick(View view) {
        scan();
    }

    public void scan(){
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.PRODUCT_CODE_TYPES);//Si no funciona ponerl ALL_CODE_TYPES
        options.setPrompt("Escanear código");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        options.setCaptureActivity(CaptureActivityPortrait.class);
        options.setBarcodeImageEnabled(false);
        barcodeLauncher.launch(options);
    }
}
