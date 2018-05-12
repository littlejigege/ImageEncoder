package com.qgstudio.imageencoder.image.strategy

object StrategyC : EncodeStrategy {
    override val TAG: String
        get() = "StrategyC"

    override fun encode() {
        println("C encode")
    }

    override fun decode() {
        println("C decode")
    }
}