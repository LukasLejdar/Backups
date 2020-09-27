package com.example.zempisslepmapy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Script;
import android.text.InputType;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public class ScannerClass extends Fragment {

    Context context;
    WindowManager windowManager;
    ScannerClass(Context context, WindowManager windowManager) {
        this.context = context;
        this.windowManager = windowManager;
    }

    private View view;
    private Button cameraButton;
    private TextureView textureView;
    private ImageView imageView;

    CameraX cameraX;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.scanner_layout, container, false);

        textureView = (TextureView) view.findViewById(R.id.textureView);
        cameraButton = (Button) view.findViewById(R.id.camerabutton);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        startCamera();

        return view;
    }

    private void startCamera() {
        CameraX.unbindAll();

        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());

        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);

                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
        });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(windowManager.getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCap.takePicture(new ImageCapture.OnImageCapturedListener() {
                    @Override
                    public void onCaptureSuccess(ImageProxy image, int rotationDegrees) {
                        Bitmap bitmap = imageProxyToBitmap(image);
                        detectTextFromImage(bitmap);
                        super.onCaptureSuccess(image, rotationDegrees);
                    }
                });
            }
        });

        CameraX.bindToLifecycle(this, preview, imgCap);
    }

    private void updateTransform(){

        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int)textureView.getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float)rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }

    private void detectTextFromImage(Bitmap bitmap) {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                List<FirebaseVisionText.Block> blockList;
                blockList = firebaseVisionText.getBlocks();
                if (blockList.size() == 0) {
                    Toast.makeText(context, "No text found", Toast.LENGTH_SHORT);
                } else {
                    final ArrayList<String> names = new ArrayList<>();

                    for (FirebaseVisionText.Block block: blockList) {
                        Log.d("FirebaseVisionText", block.getText());
                        for (String s: block.getText().split("\n")) {
                            names.add(s);
                        }
                    }

                    String message = "";
                    for (String s: names) {
                        message += s +"\n";
                    }

                    ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                    final View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, viewGroup, false);
                    final TextView title = dialogView.findViewById(R.id.title);
                    ((TextView) dialogView.findViewById(R.id.mesage)).setText(message);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();

                    dialogView.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences sharedpreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            Set<String> set = new HashSet<String>();
                            set.addAll(names);
                            editor.putStringSet((String) title.getText().toString(), set);

                            HashSet<String> nullHashset = new HashSet<>(Arrays.asList("text"));
                            Set<String> hashSet = sharedpreferences.getStringSet("names", nullHashset);
                            hashSet.add((String) title.getText().toString());
                            editor.putStringSet("names", hashSet);

                            editor.commit();

                            alertDialog.dismiss();
                        }
                    });

                    dialogView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error: " + e.getMessage(),Toast.LENGTH_SHORT);
            }
        });
    }
}