package com.qgstudio.imageencoder.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.os.Bundle

import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.mobile.utils.permission.PermissionCompatActivity
import com.qgstudio.imageencoder.R

import com.qgstudio.imageencoder.image.ImageHandler
import kotlinx.android.synthetic.main.activity_main.*

import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.mobile.utils.showToast
import com.mobile.utils.toBitmap
import com.qgstudio.imageencoder.App
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.coroutines.experimental.async
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream


class MainActivity : PermissionCompatActivity() {
    val REQUEST_CODE = 1
    private val model by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
        observe()
    }

    private fun observe() {
        model.path.observe(this, Observer {
            pathTextView.text = it
        })
        model.slot.observe(this, Observer {
            slotTextView.text = it
        })
        model.strategy.observe(this, Observer {
            it?.let {
                ImageHandler.encodeStrategy = it
                strategyTextView.text = it.TAG
            }
        })
    }

    //设置所有控件的事件
    private fun setListener() {
        imageView.setOnClickListener {
            //无需处理权限问题，内部处理
            model.openPhotoSelector(this)
        }
        strategyCardView.setOnClickListener {
            model.openStrategySelector(this)
        }
        slotCardView.setOnClickListener {
            model.openSlotEditor(this)
        }
        pathCardView.setOnClickListener {
            //必须先获取权限
            Permission.STORAGE.doAfterGet(this) {
                inUiThread { model.openPathSelector(this) }
            }
        }
        encodeButton.setOnClickListener {

            println("start===========================")
            async {
                val file = RequestBody.create(MediaType.parse("multipart/form-data"), File(model.photo!!.path))
                val body = MultipartBody.Part.createFormData("file", model.photo!!.name, file)
                App.api.uploadEncrypt(body).subscribe(object : io.reactivex.Observer<Array<Array<IntArray>>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(value: Array<Array<IntArray>>) {
                        val bitmap = ImageHandler.getNewBitmap(value)
                        ImageHandler.saveImageToGallery(this@MainActivity, bitmap, model.path.value!!, "encode.jpg")
                        showToast("图片加密完成")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
            }

        }
        decodeButton.setOnClickListener {

            println("start===========================")
            async {
                val file = RequestBody.create(MediaType.parse("multipart/form-data"), File(model.photo!!.path))
                val body = MultipartBody.Part.createFormData("file", model.photo!!.name, file)
                App.api.uploadDecrypt(body).subscribe(object : io.reactivex.Observer<Array<Array<IntArray>>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(value: Array<Array<IntArray>>) {
                        val bitmap = ImageHandler.getNewBitmap(value)
                        ImageHandler.saveImageToGallery(this@MainActivity, bitmap, model.path.value!!, "decode.jpg")
                        showToast("图片解密完成")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println(resultCode)
        if (requestCode == REQUEST_CODE && resultCode == -1 && data != null) {
            val list: List<Photo> = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)
            list.forEach {
                model.photo = it
                imageView.setImageBitmap(BitmapFactory.decodeFile(it.path))
            }
        }
    }
}
