package com.qgstudio.imageencoder.image

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.huantansheng.easyphotos.engine.ImageEngine

object GlideEngine : ImageEngine {
    override fun getCacheBitmap(context: Context, path: String?, width: Int, height: Int): Bitmap {
        return Glide.with(context).asBitmap().load(path).submit(width, height).get()
    }

    override fun loadGif(context: Context, gifPath: String?, imageView: ImageView) {
        Glide.with(context).asGif().load(gifPath).transition(withCrossFade()).into(imageView)
    }

    override fun loadPhoto(context: Context, photoPath: String?, imageView: ImageView) {
        Glide.with(context).load(photoPath).transition(withCrossFade()).into(imageView)
    }

    override fun loadGifAsBitmap(context: Context, gifPath: String?, imageView: ImageView) {
        Glide.with(context).asBitmap().load(gifPath).into(imageView)
    }
}