package com.bestoffers.enjoeepharmacy.helper;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.core.content.FileProvider;

import com.bestoffers.enjoeepharmacy.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImagePicker extends Activity{

    public static Picker picker;
    private static final int PICK_FROM_CAMERA = 4;
    private static final int PICK_FROM_FILE = 6;
    private Uri mImageCaptureUri;
    private String strMyImagePath="null";
    private AlertDialog dialog;
    private String TAG = this.getClass().getName();
    private File file;
    private String [] itemsProfile	= new String [] {"Camera","Gallery","Cancel"};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        dialogPic();
    }


    public interface Picker{
        void onImagePicked(Bitmap bitmap, String imagePath);
    }



    public void dialogPic() {

        ArrayAdapter<String> adapter	= new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,itemsProfile);
        AlertDialog.Builder builder		= new AlertDialog.Builder(this);

        // Defining Alert Dialog box to pop up options Either Take from Camera or Select from Gallery

        builder.setTitle("Select Image From");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener()

        {
            public void onClick(DialogInterface dialog, int item)

            { //pick from camera
                if (item == 0)
                {
                    dialog.dismiss();
                    startCameraCaptureActivity();
                } else if(item ==1) {
                    // Gallery Pick Intent
                    dialog.dismiss();
                    startGalleryPickActivity();
                }
                else
                    finish();
            }
        });

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }





    private void startGalleryPickActivity() {
        file = new File(getExternalFilesDir(null),
                "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //startActivityForResult(i, RESULT_LOAD_IMAGE);
        startActivityForResult(i, PICK_FROM_FILE);
    }

    private void startCameraCaptureActivity() {

        file = new File(getExternalFilesDir(null),
                "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mImageCaptureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider"
                , file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            intent.putExtra("return-data", true);

            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)

    {
        Log.e(TAG, "CHECK: " + requestCode + ")))" + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            finish();
        } else
        {
            switch (requestCode) {
                //If Pick intent is fired then start camera and calling cropping method
                case PICK_FROM_CAMERA:
//                    if (getIntent().hasExtra("dontCrop")){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                            String imagePath = saveFileFromBitmap(bitmap, file);

                            new ImageCompression(this, bitmap).execute(imagePath);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                    }
//                    else
//                        doCrop(Uri.fromFile(file));
                    break;

                //If Pick from gallery intent is started then image is selected and cropping method is called
                case PICK_FROM_FILE:
                    mImageCaptureUri = data.getData();

//                    if (getIntent().hasExtra("dontCrop")){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                            String imagePath = saveFileFromBitmap(bitmap, file);

                            new ImageCompression(this, bitmap).execute(imagePath);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                    }
//                    else
//                        doCrop(mImageCaptureUri);

                    break;

//                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                    Log.e(TAG, "onActivityResult: cropped");
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                    Uri resultUri = result.getUri();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//                        String imagePath = saveFileFromBitmap(bitmap, file);
//
//                        new ImageCompression(this, bitmap).execute(imagePath);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
                case -1:
                    finish();
            }
        }
    }


//    public void doCrop(Uri uri)
//    {
//        CropImage.activity(uri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setFixAspectRatio(false)
//                .start(this);
//    }

    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        strMyImagePath = "null";
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = getExternalFilesDir(null).toString();

            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.jpg";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            Log.e("iamgep",strMyImagePath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;
    }



    public  Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight,
                                  ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId, options);

        return unscaledBitmap;
    }

    public  Bitmap decodeFile(String path, int dstWidth, int dstHeight,
                              ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(path, options);

        return unscaledBitmap;
    }

    /**
     * Utility function for creating shape_selected_edit_text scaled version of an existing bitmap
     *
     * @param unscaledBitmap Bitmap to scale
     * @param dstWidth Wanted width of destination bitmap
     * @param dstHeight Wanted height of destination bitmap
     * @param scalingLogic Logic to use to avoid image stretching
     * @return New scaled bitmap object
     */
    public  Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
                                      ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     *
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     *
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to shape_selected_edit_text smaller size than
     * requested.
     */
    enum ScalingLogic {
        CROP, FIT
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of shape_selected_edit_text source
     * image, the dimensions of shape_selected_edit_text destination area and shape_selected_edit_text scaling logic.
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal down scaling sample size for decoding
     */
    public  int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                    ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    /**
     * Calculates source rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal source rectangle
     */
    public  Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                  ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * Calculates destination rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal destination rectangle
     */
    public  Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                  ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    //    Saving file to the mobile internal memory
    public static String saveFileFromBitmap(Bitmap sourceUri, File destination) {
        try {
            FileOutputStream out = new FileOutputStream(destination);
            sourceUri.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destination.toString();
    }




    public class ImageCompression extends AsyncTask<String, Void, String> {

        private Context context;
        private static final float maxHeight = 1280.0f;
        private static final float maxWidth = 1280.0f;
        private Bitmap bitmap;

        public ImageCompression(Context context, Bitmap bitmap){
            this.context=context;
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(strings.length == 0 || strings[0] == null)
                return null;

            return compressImage(strings[0], null);
        }

        protected void onPostExecute(String imagePath){
            // imagePath is path of new compressed image.
            progressDialog.dismiss();
            if (!TextUtils.isEmpty(imagePath)){
                picker.onImagePicked(bitmap, imagePath);
                finish();
            }
            else
                finish();

        }


        public String compressImage(String imagePath, Bitmap bitmap) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap scaledBitmap = null;
            Bitmap bmp=null;

            if (bitmap == null)
                bmp = BitmapFactory.decodeFile(imagePath, options);
            else if (imagePath == null)
                bmp = bitmap;

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            if(bmp!=null)
            {
                bmp.recycle();
            }

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filepath = getFilename();
            try {
                out = new FileOutputStream(filepath);

                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filepath;
        }

        public  int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }

        public String getFilename() {
            File mediaStorageDir = new File(getExternalFilesDir(null), "Img");
//            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "SLNidhi");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists())
                mediaStorageDir.mkdir();


            String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
            String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
            return uriString;

        }

    }

}