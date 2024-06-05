package melon.verify

import com.mojang.blaze3d.systems.RenderSystem
import dev.zenhao.melon.Melon.Companion.id
import dev.zenhao.melon.utils.math.RandomUtil
import melon.events.client.VerificationEvent
import melon.system.event.AlwaysListening
import melon.system.event.listener
import melon.verify.SocketConnection.taskID

object VerificationManager : AlwaysListening {
    init {/*
        listener<VerificationEvent.DrawTessellator>(true) {
            if (id != taskID) {
                RenderSystem.assertOnRenderThread()
                RenderSystem.getProjectionMatrix().scale(RandomUtil.nextFloat(0.01f, 0.8f))
                RenderSystem.getProjectionMatrix().rotate(
                    RandomUtil.nextFloat(0.01f, 0.8f),
                    RandomUtil.nextFloat(0.01f, 0.8f),
                    RandomUtil.nextFloat(0.01f, 0.8f),
                    RandomUtil.nextFloat(0.01f, 0.8f)
                )
            }
        }

        listener<VerificationEvent.DrawBuffer>(true) {
            if (id != taskID) {
                RenderSystem.assertOnRenderThread()
                RenderSystem.getProjectionMatrix().scale(RandomUtil.nextFloat(0.01f, 0.8f))
                RenderSystem.getProjectionMatrix().rotate(
                    RandomUtil.nextFloat(0.01f, 0.8f),
                    RandomUtil.nextFloat(0.01f, 0.8f),
                    RandomUtil.nextFloat(0.01f, 0.8f),
                    RandomUtil.nextFloat(0.01f, 0.8f)
                )
            }
        }*/
    }
}