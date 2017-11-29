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
typedef struct DiscordJoinRequest {
    const char* userId;
    const char* username;
    const char* avatar;
} DiscordJoinRequest;
 */
/**
 * Struct binding for a discord join request.
 */
public class DiscordJoinRequest extends Structure
{
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "userId",
            "username",
            "avatar"
    ));

    /**
     * The userId for the user that requests to join
     */
    public String userId;
    /**
     * The username of the user that requests to join
     */
    public String username;
    /**
     * The avatar of the user that requests to join
     */
    public String avatar;

    @Override
    protected List<String> getFieldOrder()
    {
        return FIELD_ORDER;
    }
}
