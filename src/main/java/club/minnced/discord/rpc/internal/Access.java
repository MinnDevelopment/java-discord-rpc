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

import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.NativeScope;

import static jdk.incubator.foreign.CLinker.C_POINTER;

class Access {
    private final MemorySegment segment;
    private NativeScope scope;
    private int offset = 0;

    public Access(MemorySegment segment) {
        this.segment = segment;
    }

    public Access withScope(NativeScope scope) {
        this.scope = scope;
        return this;
    }

    public Access put(MemoryAddress address) {
        MemoryAccess.setAddressAtOffset(segment, offset, address);
        offset += C_POINTER.byteSize();
        return this;
    }

    public Access put(String string) {
        MemoryAddress address = Structs.wrap(scope, string);
        MemoryAccess.setAddressAtOffset(segment, offset, address);
        offset += C_POINTER.byteSize();
        return this;
    }

    public Access put(int i) {
        MemoryAccess.setIntAtOffset(segment, offset, i);
        offset += 4;
        return this;
    }

    public Access put(long l) {
        MemoryAccess.setLongAtOffset(segment, offset, l);
        offset += 8;
        return this;
    }

    public Access put(byte b) {
        MemoryAccess.setByteAtOffset(segment, offset, b);
        offset += 1;
        return this;
    }
}
