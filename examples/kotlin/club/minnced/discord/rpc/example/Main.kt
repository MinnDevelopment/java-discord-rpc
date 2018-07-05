/*
 * Copyright 2016 - 2018 Florian Spieß
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
