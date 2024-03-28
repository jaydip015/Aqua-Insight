package com.example.aquainsight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivty extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    PreviewView previewView;
    ImageButton capture;
    Button recapture,ok;
    ImageView capturedImage;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_activty);
        //binding
        recapture=findViewById(R.id.recapture);
        ok=findViewById(R.id.ok);
        capture=findViewById(R.id.captureButton);
        previewView=findViewById(R.id.previewView);
        capturedImage=findViewById(R.id.capturedImageView);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        startCamera();
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok.setVisibility(View.GONE);
                recapture.setVisibility(View.GONE);
                capturedImage.setVisibility(View.GONE);
                capture.setVisibility(View.VISIBLE);
                previewView.setVisibility(View.VISIBLE);
                file.delete();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CameraActivty.this,NewRaiseActivty.class);
                i.putExtra("FilePath",file.getAbsolutePath());
                startActivity(i);

            }
        });
    }
    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                capture.setVisibility(View.VISIBLE);
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
                // Initialize imageCapture here

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        // Bind camera to the lifecycle
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(CameraActivty.this, cameraSelector, preview, imageCapture);
    }
    private void captureImage() {
        // Create a file to store the image
        file = new File(getOutputDirectory(), new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg");
//        File file=new File(getExternalFilesDir(null),System.currentTimeMillis()+".jpg");
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(file)
                        .build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                Bitmap bitmap = ImageUtil.decodeBitmapFromFile(file.getAbsolutePath(), 400, 400);
                capturedImage.setVisibility(View.VISIBLE);
                recapture.setVisibility(View.VISIBLE);
                ok.setVisibility(View.VISIBLE);
                capture.setVisibility(View.GONE);
                previewView.setVisibility(View.GONE);
                capturedImage.setImageBitmap(bitmap);
                Toast.makeText(CameraActivty.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ImageCaptureException exception) {
                Log.e("CameraX", "Error capturing image", exception);
                Toast.makeText(CameraActivty.this, "Error capturing image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private File getOutputDirectory() {
        File mediaDir = getExternalMediaDirs()[0];
        File outputDir = new File(mediaDir, "AquaInsights");
        outputDir.mkdirs();
        return outputDir;
    }


}