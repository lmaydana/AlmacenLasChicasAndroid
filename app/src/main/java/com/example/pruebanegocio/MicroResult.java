package com.example.pruebanegocio;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import java.util.ArrayList;

public class MicroResult implements ActivityResultCallback<ActivityResult> {

    private Context sourceActivity;

    public MicroResult(Context sourceActivity){
        this.sourceActivity = sourceActivity;
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        ArrayList<String> listenedWordsArray = result.getData() != null ? result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) : new ArrayList<>();
        if( listenedWordsArray.size() > 0 ) {
            InstructionsParser instructionsParser = new InstructionsParser(listenedWordsArray.get(0), sourceActivity);
            Instruction instruction = instructionsParser.getInstruction();
            instruction.execute();
        }
    }

    private void launchProductSelectIntent(String listenedWords) {
        Intent changeProductIntent = new Intent(sourceActivity, ChangeSelectedActivity.class);
        changeProductIntent.putExtra("listenedWords", listenedWords);
        sourceActivity.startActivity(changeProductIntent);
    }
}
