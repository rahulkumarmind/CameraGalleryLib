package com.rahul.main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CameraGalleryHelper {
    private static CameraGalleryHelper instance;
    private final Context context;
    private Uri cameraUri;
    private ActivityResultLauncher<Intent> startCamera;
    private ActivityResultLauncher<String> startGallery;

    public interface CameraResultCallback {
        void onCameraResult(File file, Uri uri);
    }

    public interface GalleryResultCallback {
        void onGalleryResult(File file, Uri uri);
    }

    private CameraResultCallback cameraResultCallback;
    private GalleryResultCallback galleryResultCallback;

    private CameraGalleryHelper(Context context) {
        this.context = context;
    }

    private void initLauncher(AppCompatActivity context) {

        this.startCamera =
                context.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode()==Activity.RESULT_OK && cameraUri!=null) {
                                if (cameraResultCallback!=null) {
                                    cameraResultCallback.onCameraResult(createFileFromUri(cameraUri), cameraUri);
                                }
                            }
                        });

        this.startGallery =
                context.registerForActivityResult(new ActivityResultContracts.GetContent(),
                        uri -> {
                            if (galleryResultCallback!=null) {
                                galleryResultCallback.onGalleryResult(createFileFromUri(uri), uri);
                            }
                        });
    }


    public static synchronized CameraGalleryHelper getInstance(Context context) {
        if (instance==null) {
            instance = new CameraGalleryHelper(context);
        }
        instance.initLauncher((AppCompatActivity) context);
        return instance;
    }

    public void openCamera(CameraResultCallback callback) {
        this.cameraResultCallback = callback;
        pickCamera();
    }

    public void openGallery(GalleryResultCallback callback) {
        this.galleryResultCallback = callback;
        startGallery.launch("image/*");
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");


        cameraUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        );

        if (cameraUri!=null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);


            startCamera.launch(cameraIntent);
        }
    }


    private File createFileFromUri(Uri uri) {
        File file = null;

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);


            file = File.createTempFile("image", ".jpeg", context.getCacheDir());

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                if (inputStream!=null) {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = inputStream.read(buffer))!=-1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();


                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream!=null) {
                    inputStream.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
