package dev.zenhao.melon.manager

import dev.zenhao.melon.module.modules.crystal.CrystalDamageCalculator
import melon.events.PacketEvents
import melon.events.RunGameLoopEvent
import melon.system.event.AlwaysListening
import melon.system.event.safeEventListener
import net.minecraft.entity.LivingEntity
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.math.Vec3d
import team.exception.melon.util.math.vector.Vec2f

object CrystalManager : AlwaysListening {
    var position: Vec3d = Vec3d.ZERO; private set
    var eyePosition: Vec3d = Vec3d.ZERO; private set
    var rotation = Vec2f.ZERO; private set

    fun onInit() {
        safeEventListener<RunGameLoopEvent.Tick> {
            for (entity in world.entities) {
                if (entity !is LivingEntity) continue
                CrystalDamageCalculator.reductionMap[entity] = CrystalDamageCalculator.DamageReduction(entity)
            }
        }

        safeEventListener<PacketEvents.PostSend> {
            if (it.packet !is PlayerMoveC2SPacket) return@safeEventListener
            if (it.packet.changePosition) {
                position = Vec3d(it.packet.x, it.packet.y, it.packet.z)
                eyePosition = Vec3d(it.packet.x, it.packet.y + player.getEyeHeight(player.pose), it.packet.z)
            }
            if (it.packet.changeLook) {
                rotation = Vec2f(it.packet.yaw, it.packet.pitch)
            }
        }
    }
}