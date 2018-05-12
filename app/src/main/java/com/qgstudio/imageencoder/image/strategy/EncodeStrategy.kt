package com.qgstudio.imageencoder.image.strategy

import android.support.design.widget.TabLayout

interface EncodeStrategy {
    val TAG: String
    fun encode()
    fun decode()
}