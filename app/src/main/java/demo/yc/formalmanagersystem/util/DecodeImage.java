package demo.yc.formalmanagersystem.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class DecodeImage {
    /**
     * 计算图片的宽和高，得出 inSampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取地址与ImageView宽高获取Bitmap
     *
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * 文件夹列表快速排序
     * @param a
     * @param low
     * @param high
     */
//    public static void qSort(ArrayList<FileItem> a, int low, int high) {
//        if (low < high) {
//            int pos = getPar(a, low, high);
//            qSort(a, low, pos - 1);
//            qSort(a, pos + 1, high);
//        }
//    }
//
//    public static int getPar(ArrayList<FileItem> a, int low, int high) {
//        FileItem partition = a.get(low);
//        FileItem temp;
//        while (low < high) {
//            while (low < high && (a.get(high).getPath().toLowerCase().compareTo(partition.getPath().toLowerCase()) >= 0)) {
//                high--;
//            }
//            temp = a.get(low);
//            a.set(low, a.get(high));
//            a.set(high, temp);
//
//            while (low < high && (a.get(low).getPath().toLowerCase().compareTo(partition.getPath().toLowerCase()) <= 0)) {
//                low++;
//            }
//            temp = a.get(high);
//            a.set(high, a.get(low));
//            a.set(low, temp);
//        }
//        return low;
//    }
}
