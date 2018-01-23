@file:JvmName("Main")
package club.minnced.discord.rpc.example

import club.minnced.discord.rpc.*
import club.minnced.discord.rpc.DiscordEventHandlers.*
import java.io.File
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    println("Initializing...")
    // if .application-id doesn't exist we hard fail here
    val applicationId = File(".application-id").readText()
    val steamId: String? = with (File(".steam-id")) {
        if (exists())
            readText()
        else
            null
    }

    println("Starting RPC...")
    discord {
        val handlers = handlers {
            ready = OnReady { println("Ready!") }
            errored = OnStatus { code, message -> println("$code: $message") }
        }

        Discord_Initialize(applicationId, handlers, true, steamId)

        presence {
            details = "Undisclosed"
            state = "Free 4 All"
            startTimestamp = System.currentTimeMillis() / 1000

            largeImageKey = "map-desert"
            largeImageText = "Desert"
            smallImageKey = "char-robert"
            smallImageText = "Robert"
        }

        thread(name="RPC-Callback-Handler") {
            while (!Thread.currentThread().isInterrupted) {
                Discord_RunCallbacks()
                Thread.sleep(2000)
            }
        }
    }

}

fun discord(block: DiscordRPC.() -> Unit) = DiscordRPC.INSTANCE.apply(block)
fun handlers(block: DiscordEventHandlers.() -> Unit) = DiscordEventHandlers().apply(block)
fun DiscordRPC.presence(block: DiscordRichPresence.() -> Unit) {
    Discord_UpdatePresence(DiscordRichPresence().apply(block))
}
