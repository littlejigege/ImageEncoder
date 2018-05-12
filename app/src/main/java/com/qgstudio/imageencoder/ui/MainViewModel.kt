package com.qgstudio.imageencoder.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.codekidlabs.storagechooser.Content
import com.codekidlabs.storagechooser.StorageChooser
import com.codekidlabs.storagechooser.utils.DiskUtil

import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.mobile.utils.inUiThread
import com.qgstudio.imageencoder.R
import com.qgstudio.imageencoder.image.GlideEngine
import com.qgstudio.imageencoder.image.strategy.EncodeStrategy
import com.qgstudio.imageencoder.image.strategy.StrategyA
import com.qgstudio.imageencoder.image.strategy.StrategyB
import com.qgstudio.imageencoder.image.strategy.StrategyC
import kotlinx.android.synthetic.main.activity_main.*

class MainViewModel : ViewModel() {
    val strategy: MutableLiveData<EncodeStrategy> = MutableLiveData()
    val path: MutableLiveData<String> = MutableLiveData()
    val slot: MutableLiveData<String> = MutableLiveData()
    var photo: Photo? = null
    fun openStrategySelector(ctx: Context) {
        MaterialDialog.Builder(ctx)
                .title("选择加密算法")
                .items(R.array.Strategy)
                .itemsCallback { _, _, _, text ->
                    val temp: EncodeStrategy = when (text) {
                        StrategyA.TAG -> {
                            StrategyA
                        }
                        StrategyB.TAG -> {
                            StrategyB
                        }
                        else -> {
                            StrategyC
                        }
                    }
                    strategy.postValue(temp)
                }
                .show()
    }

    fun openPathSelector(activity: MainActivity) {
        val content = Content()
        content.selectLabel = "确定"
        content.overviewHeading = "选择存储器"
        content.internalStorageText = "内部存储器"

        val chooser = StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(activity.fragmentManager)
                .withMemoryBar(true)
                .allowAddFolder(false)//不允许新建文件夹
                .setType(StorageChooser.DIRECTORY_CHOOSER)//设置为路径选择
                .actionSave(true)//自动保存已选路径
                .allowCustomPath(true)
                .withPreference(activity.getSharedPreferences(DiskUtil.SC_PREFERENCE_KEY, Context.MODE_PRIVATE))
                .withContent(content)
                .shouldResumeSession(true)
                .skipOverview(true)
                .build()
        chooser.setOnSelectListener {
            path.postValue(it)
        }
        chooser.show()
    }

    fun openSlotEditor(ctx: Context) {
        MaterialDialog.Builder(ctx)
                .title("输入密钥")
                .content("加密解密需要密钥")
                .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(null, null, { _, input ->
                    slot.postValue(input.toString())
                }).show()
    }

    fun openPhotoSelector(activity: MainActivity) {
        EasyPhotos.createAlbum(activity, true, GlideEngine)
                .setFileProviderAuthority("com.qgstudio.imageencoder.fileprovider")
                .setPuzzleMenu(false)
                .start(activity.REQUEST_CODE)
    }
}