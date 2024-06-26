package dev.zenhao.melon.module.modules.combat

import dev.zenhao.melon.manager.HotbarManager.spoofHotbar
import dev.zenhao.melon.manager.HotbarManager.spoofHotbarBypass
import dev.zenhao.melon.manager.RotationManager
import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.Module
import dev.zenhao.melon.module.modules.combat.HolePush.doHolePush
import dev.zenhao.melon.module.modules.player.AntiMinePlace
import dev.zenhao.melon.module.modules.player.PacketMine
import dev.zenhao.melon.utils.TimerUtils
import melon.system.event.SafeClientEvent
import melon.utils.block.BlockUtil.getNeighbor
import melon.utils.chat.ChatUtil
import melon.utils.combat.getPredictedTarget
import melon.utils.combat.getTarget
import melon.utils.concurrent.threads.onMainThread
import melon.utils.concurrent.threads.runSafe
import melon.utils.entity.EntityUtils.spoofSneak
import melon.utils.extension.fastPos
import melon.utils.hole.SurroundUtils
import melon.utils.hole.SurroundUtils.checkHole
import melon.utils.inventory.slot.firstBlock
import melon.utils.inventory.slot.hotbarSlots
import melon.utils.player.getTargetSpeed
import net.minecraft.block.Blocks
import net.minecraft.block.CobwebBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import team.exception.melon.util.math.toBlockPos

object AutoWeb : Module(
    name = "AutoWeb",
    langName = "自动蜘蛛网",
    category = Category.COMBAT,
    description = "Auto Place Web to stick target."
) {
    private var spoofRotations = bsetting("Rotate", false)
    private var holeCheck = bsetting("HoleCheck", true)
    private var betterPush = bsetting("BetterPush", true)
    private var betterAnchor = bsetting("BetterAnchor", true)
    private var eatingPause = bsetting("EatingPause", true)
    private var facePlace = bsetting("FacePlace", false)
    private var multiPlace = bsetting("MultiPlace", true)
    private var spoofBypass = bsetting("SpoofBypass", false)
    private var ground = bsetting("OnlyGround", true)
    private var inside = bsetting("Inside", false)
    private var strictDirection = bsetting("StrictDirection", false)
    private var air = bsetting("AirPlace", false)
    private var range = isetting("Range", 5, 1, 6)
    private var predictTicks = isetting("PredictTicks", 8, 0, 20)
    private var smartDelay = bsetting("SmartDelay", false)
    private var delay = isetting("minDelay", 25, 0, 500)
    private var maxDelay = isetting("MaxDelay", 400, 0, 1000).isTrue(smartDelay)
    private var debug = bsetting("Debug", false)
    private var timerDelay = TimerUtils()

    var target: PlayerEntity? = null
    var onAnchorPlacing = false

    override fun onEnable() {
        runSafe {
            timerDelay.reset()
        }
    }

    override fun getHudInfo(): String {
        target?.let {
            return "${it.name.string} ${getTargetSpeed(it) > 20.0}"
        } ?: return "Waiting..."
    }

    init {
        onLoop {
            if (!player.isOnGround && ground.value) return@onLoop
            val webSlot = player.hotbarSlots.firstBlock(Blocks.COBWEB)
            if ((eatingPause.value && player.isUsingItem) || webSlot == null) {
                return@onLoop
            }
            target = getTarget(range.value.toDouble())
            if (onAnchorPlacing && betterAnchor.value) return@onLoop
            target?.let {
                val targetDistance = getPredictedTarget(it, predictTicks.value).blockPos
                if (doHolePush(
                        it.blockPos.up(),
                        true,
                        null,
                        null
                    ) != null && betterPush.value && HolePush.isEnabled
                ) return@onLoop
                fun SafeClientEvent.place(delay: Long) {
                    onPacket@ fun packet(pos: BlockPos, checkDown: Boolean = false) {
                        if (checkDown && world.getBlockState(pos.down()).block is CobwebBlock) return
                        PacketMine.blockData?.let { data ->
                            if (data.blockPos == pos) return@onPacket
                        }

                        AntiMinePlace.mineMap[pos]?.let { mine ->
                            if (System.currentTimeMillis() - mine.start >= mine.mine) return@onPacket
                        }

                        if (world.isAir(pos) && (getNeighbor(pos, strictDirection.value) != null || air.value)) {
                            if (timerDelay.tickAndReset(delay)) {
                                if (spoofRotations.value) {
                                    RotationManager.addRotations(pos)
                                }
                                player.spoofSneak {
                                    if (!spoofBypass.value) {
                                        spoofHotbar(webSlot) {
                                            connection.sendPacket(
                                                fastPos(pos, true)
                                            )
                                        }
                                    } else {
                                        spoofHotbarBypass(webSlot) {
                                            connection.sendPacket(
                                                fastPos(pos, true)
                                            )
                                        }
                                    }
                                }
                                if (debug.value) ChatUtil.sendMessage("Placing")
                            }
                        }
                    }

                    getNeighbor(targetDistance.up(), strictDirection.value)?.let {
                        if (facePlace.value) {
                            packet(targetDistance.up(), true)
                        }
                    }

                    getNeighbor(targetDistance, strictDirection.value)?.let {
                        if (inside.value) {
                            packet(targetDistance)
                        }
                    }

                    getNeighbor(targetDistance.down(), strictDirection.value)?.let {
                        packet(targetDistance.down())
                    }

                    if (multiPlace.value) {
                        packet(it.pos.add(0.3, 0.3, 0.3).toBlockPos())
                        packet(it.pos.add(-0.3, 0.3, -0.3).toBlockPos())
                        packet(it.pos.add(-0.3, 0.3, 0.3).toBlockPos())
                        packet(it.pos.add(0.3, 0.3, -0.3).toBlockPos())
                        if (facePlace.value) {
                            packet(it.pos.add(0.3, 0.3, 0.3).toBlockPos().up(), true)
                            packet(it.pos.add(-0.3, 0.3, -0.3).toBlockPos().up(), true)
                            packet(it.pos.add(-0.3, 0.3, 0.3).toBlockPos().up(), true)
                            packet(it.pos.add(0.3, 0.3, -0.3).toBlockPos().up(), true)
                        }
                    }
                }

                if (checkHole(it) != SurroundUtils.HoleType.NONE && it.onGround && holeCheck.value) return@onLoop

                val useDelay = if (smartDelay.value) {
                    if (getTargetSpeed(it) < 20.0) maxDelay.value else delay.value
                } else {
                    delay.value
                }


                onMainThread {
                    place(useDelay.toLong())
                }
            }
        }
    }
}