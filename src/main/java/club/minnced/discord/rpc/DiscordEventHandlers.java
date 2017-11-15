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

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
typedef struct DiscordEventHandlers {
    void (*ready)();
    void (*disconnected)(int errorCode, const char* message);
    void (*errored)(int errorCode, const char* message);
    void (*joinGame)(const char* joinSecret);
    void (*spectateGame)(const char* spectateSecret);
    void (*joinRequest)(const DiscordJoinRequest* request);
} DiscordEventHandlers;
 */

public class DiscordEventHandlers extends Structure
{
    public interface OnReady extends Callback
    {
        void accept();
    }

    public interface OnStatus extends Callback
    {
        void accept(int errorCode, String message);
    }

    public interface OnGameUpdate extends Callback
    {
        void accept(String secret);
    }

    public interface OnJoinRequest extends Callback
    {
        void accept(DiscordJoinRequest request);
    }

    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
       "ready",
       "disconnected",
       "errored",
       "joinGame",
       "spectateGame",
       "joinRequest"
    ));

    public OnReady ready;
    public OnStatus disconnected;
    public OnStatus errored;
    public OnGameUpdate joinGame;
    public OnGameUpdate spectateGame;
    public OnJoinRequest joinRequest;

    @Override
    protected List<String> getFieldOrder()
    {
        return FIELD_ORDER;
    }
}
