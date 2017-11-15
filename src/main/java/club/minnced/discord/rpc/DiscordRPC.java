/*
 *     Copyright 2016 - 2017 Florian Spieß
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

package club.minnced.discord.rpc;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DiscordRPC extends Library
{
    DiscordRPC INSTANCE = Native.loadLibrary("discord-rpc", DiscordRPC.class);

    int DISCORD_REPLY_NO = 0;
    int DISCORD_REPLY_YES = 1;
    int DISCORD_REPLY_IGNORE = 2;

    void Discord_Initialize(String applicationId,
                            DiscordEventHandlers handlers,
                            boolean autoRegister,
                            String steamId);
    void Discord_Shutdown();

    void Discord_RunCallbacks();
    void Discord_UpdateConnection();

    void Discord_UpdatePresence(DiscordRichPresence struct);
    void Discord_Respond(String userid, int reply);
}
