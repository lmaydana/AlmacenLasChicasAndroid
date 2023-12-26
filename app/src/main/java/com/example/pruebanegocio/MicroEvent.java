package com.example.pruebanegocio;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MicroEvent implements View.OnClickListener, AdapterView.OnItemClickListener{

    private final ActivityResultLauncher<Intent> activityResult;

    public MicroEvent(AppCompatActivity activity, ActivityResultCallback<ActivityResult> function){
        activityResult = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),function);
    }

    @Override
    public void onClick(View view) {
        listen();
    }

    private void listen(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1000);
        activityResult.launch(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        listen();
    }
}
