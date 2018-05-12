package com.qgstudio.imageencoder.image.strategy

object StrategyB : EncodeStrategy {
    override val TAG: String
        get() = "StrategyB"

    override fun encode() {
        println("B encode")
    }

    override fun decode() {
        println("B decode")
    }
}