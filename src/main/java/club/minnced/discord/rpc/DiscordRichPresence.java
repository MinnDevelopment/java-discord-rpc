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

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
typedef struct DiscordRichPresence {
    const char* state; // max 128 bytes
    const char* details; // max 128 bytes
    int64_t startTimestamp;
    int64_t endTimestamp;
    const char* largeImageKey; // max 32 bytes
    const char* largeImageText; // max 128 bytes
    const char* smallImageKey; // max 32 bytes
    const char* smallImageText; // max 128 bytes
    const char* partyId; // max 128 bytes
    int partySize;
    int partyMax;
    const char* matchSecret; // max 128 bytes
    const char* joinSecret; // max 128 bytes
    const char* spectateSecret; // max 128 bytes
    int8_t instance;
} DiscordRichPresence;
 */

public class DiscordRichPresence extends Structure
{
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "state",
            "details",
            "startTimestamp",
            "endTimestamp",
            "largeImageKey",
            "largeImageText",
            "smallImageKey",
            "smallImageText",
            "partyId",
            "partySize",
            "partyMax",
            "matchSecret",
            "joinSecret",
            "spectateSecret",
            "instance"
    ));

    public String state;
    public String details;
    public long startTimestamp;
    public long endTimestamp;
    public String largeImageKey;
    public String largeImageText;
    public String smallImageKey;
    public String smallImageText;
    public String partyId;
    public int partySize;
    public int partyMax;
    public String matchSecret;
    public String joinSecret;
    public String spectateSecret;
    public byte instance;

    @Override
    protected List<String> getFieldOrder()
    {
        return FIELD_ORDER;
    }
}
