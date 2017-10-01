package com.example.kkjsu.textrecognitionwithphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText txtResultP1, txtResultP2, txtResultP3;
    boolean first=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = (Button)findViewById(R.id.btnCameraAndProcess);
        imageView = (ImageView)findViewById(R.id.ImageView);
        txtResultP1 = (EditText)findViewById(R.id.txtResultP1);
        txtResultP2 = (EditText)findViewById(R.id.txtResultP2);
        txtResultP3 = (EditText)findViewById(R.id.txtResultP3);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational())
            Log.e("ERROR","Detector dependencies are not yet available");
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<items.size();++i)
            {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append(" ");//\n
            }
            //txtResult.setText(stringBuilder.toString());
            showString(stringBuilder.toString());
        }
    }

    void showString(String s)
    {
        String split[] = s.split(" ");
        String p2="";
        if(split.length>2)
            for (int i=1; i<split.length-1; i++) p2+=split[i];
        txtResultP1.setText(split[0]);
        txtResultP2.setText(p2);
        txtResultP3.setText(split[split.length-1]);
    }
}
