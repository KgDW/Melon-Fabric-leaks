package dev.zenhao.melon.module.modules.render

import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.Module
import dev.zenhao.melon.utils.animations.Easing
import melon.utils.concurrent.threads.runSafe

object Zoom : Module(
    name = "Zoom",
    langName = "推进镜头",
    description = "change u fov.",
    category = Category.RENDER
) {
    private val fadeLength by fsetting("FadeLength", 200f, 0f, 1000f)

    private var startTime = System.currentTimeMillis()
    var fov = 120.0

    override fun onEnable() {
        runSafe { startTime = System.currentTimeMillis() }
    }

    init {
        onLoop {
            val normal = if (CustomFov.isEnabled) CustomFov.fov.value else mc.options.fov.value.toDouble()
            if (Easing.IN_BACK.dec(
                    Easing.toDelta(
                        startTime,
                        fadeLength
                    )
                ) * normal >= 50.0
            ) fov = normal * Easing.IN_CUBIC.dec(Easing.toDelta(startTime, fadeLength))
        }
    }
}