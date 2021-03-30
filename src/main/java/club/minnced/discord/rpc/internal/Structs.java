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

package club.minnced.discord.rpc.internal;

import club.minnced.discord.rpc.DiscordUser;
import jdk.incubator.foreign.*;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_POINTER;

class Structs {
    public static final ValueLayout INT64 = MemoryLayout.ofValueBits(64, ByteOrder.nativeOrder());
    public static final ValueLayout INT8 = MemoryLayout.ofValueBits(8, ByteOrder.nativeOrder());
    public static final GroupLayout USER = MemoryLayout.ofStruct(C_POINTER, C_POINTER, C_POINTER, C_POINTER);
    public static final GroupLayout EVENT_HANDLERS = MemoryLayout.ofStruct(C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_POINTER);
    public static final GroupLayout RICH_PRESENCE = MemoryLayout.ofStruct(C_POINTER, C_POINTER, INT64, INT64, C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_POINTER, C_INT, C_INT, C_POINTER, C_POINTER, C_POINTER, INT8);

    public static MemoryAddress wrap(NativeScope scope, String string) {
        return string == null ? MemoryAddress.NULL : CLinker.toCString(string, StandardCharsets.UTF_8, scope).address();
    }

    public static String toString(MemoryAddress address) {
        return CLinker.toJavaStringRestricted(address, StandardCharsets.UTF_8);
    }

    public static DiscordUser toUser(MemoryAddress address) {
        DiscordUser user = new DiscordUser();

        MemorySegment segment = address.asSegmentRestricted(Structs.USER.byteSize());
        user.userId = Structs.toString(MemoryAccess.getAddressAtIndex(segment, 0));
        user.username = Structs.toString(MemoryAccess.getAddressAtIndex(segment, 1));
        user.discriminator = Structs.toString(MemoryAccess.getAddressAtIndex(segment, 2));
        user.avatar = Structs.toString(MemoryAccess.getAddressAtIndex(segment, 3));

        return user;
    }
}
