package com.qgstudio.imageencoder.image.strategy

object StrategyA : EncodeStrategy {
    override val TAG: String
        get() = "StrategyA"

    override fun encode() {
        println("A encode")
    }

    override fun decode() {
        println("A decode")
    }
}