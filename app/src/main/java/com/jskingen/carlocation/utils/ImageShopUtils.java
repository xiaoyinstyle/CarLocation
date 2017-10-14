package com.jskingen.carlocation.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import net.bither.util.NativeUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ChneY on 2017/4/12.
 * <p>
 * 图片处理------裁剪-> 水印->压缩
 */

public class ImageShopUtils {
    private Activity context;
    private ProgressDialog dialog;

    public ImageShopUtils(Activity context) {
        this.context = context;

    }

    public void addText(final File odlfile, final String text, final OnFileListener listener) {
        showDialog();
//        尺寸压缩

        new Thread() {
            public void run() {
                addText(odlfile, text);

                if (Build.CPU_ABI.contains("armeabi") || Build.CPU_ABI2.contains("armeabi")) {
                    final File file = new File(odlfile.getParent(), "tempCompress.jpg");
                    //图片压缩
                    NativeUtil.compressBitmap(odlfile.getPath(), file.getPath());

                    if (odlfile.exists())
                        odlfile.delete();

                    file.renameTo(odlfile);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hidDialog();
                            listener.success(odlfile);
                        }
                    });
                }else {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hidDialog();
                            listener.success(odlfile);
                        }
                    });
                }



            }
        }.start();

    }

    /**
     * 尺寸压缩
     */
    private void addText(File file, String text) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);

        // 获取到这个图片的原始宽度和高度
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;

        // 获取屏的宽度和高度
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();//720
        int screenHeight = display.getHeight();//1280

        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        options.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                options.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)
                options.inSampleSize = picHeight / screenHeight;
        }

        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file.getPath(), options);

        //加文字
        bitmap = ImageUtil.drawText(context, bitmap, text);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制单个文件
     */
    private void moveFile(File oldPath, File newPath) {
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(oldPath.getPath());

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog     .setMessage("处理中..");
            dialog       .setCancelable(false);
        }
        dialog.show();
    }

    private void hidDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface OnFileListener {
        void success(File file);
    }
}
