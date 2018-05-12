package com.qgstudio.imageencoder.image

import android.graphics.*
import com.qgstudio.imageencoder.image.strategy.EncodeStrategy
import com.qgstudio.imageencoder.image.strategy.StrategyA
import org.apache.sanselan.ImageInfo
import org.apache.sanselan.Sanselan
import org.apache.sanselan.common.RgbBufferedImageFactory
import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
import android.net.Uri
import android.os.Environment

import android.system.Os.mkdir
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object ImageHandler {
    var encodeStrategy: EncodeStrategy = StrategyA
    //获取rgb像素矩阵
//    fun getData(bitmap: Bitmap): ImageData {
//        val data = ImageData(bitmap.height, bitmap.width)
//        val array = Array(data.height) { Array(data.width) { IntArray(4) } }
//        for (i in 0 until data.height) {
//            for (j in 0 until data.width) {
//
//                array[i][j][0] = Color.red(bitmap.getPixel(j, i))
//                array[i][j][1] = Color.green(bitmap.getPixel(j, i))
//                array[i][j][2] = Color.blue(bitmap.getPixel(j, i))
//            }
//        }
//        data.data = array
//        return data
//    }

    fun getNewBitmap(array: Array<Array<IntArray>>): Bitmap {
        val list = mutableListOf<Int>()
        for (arrayOfIntArrays in array) {
            for (arrayOfIntArray in arrayOfIntArrays) {
                list.add(Color.rgb(arrayOfIntArray[0], arrayOfIntArray[1], arrayOfIntArray[2]))
            }
        }
        return Bitmap.createBitmap(list.toIntArray(), array.size, array[0].size, Bitmap.Config.RGB_565)
    }

    fun saveImageToGallery(context: Context, bmp: Bitmap, path: String, name: String) {
        // 首先保存图片
        val file = File(path, name)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver,
                    file.absolutePath, name, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // 最后通知图库更新
        context.sendBroadcast(Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
    }

}