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

import club.minnced.discord.rpc.DiscordEventHandlers;
import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class EventHandlers {
    private final DiscordEventHandlers handlers;

    public EventHandlers(DiscordEventHandlers handlers) {
        this.handlers = handlers;
    }

    public MemoryAddress allocate(NativeScope scope) throws NoSuchMethodException, IllegalAccessException {
        MemorySegment memory = scope.allocate(Structs.EVENT_HANDLERS);
        Access access = new Access(memory).withScope(scope);

        MethodType ptr = MethodType.methodType(Void.TYPE, MemoryAddress.class);
        FunctionDescriptor ptrDesc = FunctionDescriptor.ofVoid(CLinker.C_POINTER);
        MethodType error = MethodType.methodType(Void.TYPE, int.class, MemoryAddress.class);
        FunctionDescriptor errorDesc = FunctionDescriptor.ofVoid(CLinker.C_INT, CLinker.C_POINTER);

        MethodHandle handle;

        handle = getHandle("ready", ptr);
        access.put(CLinker.getInstance().upcallStub(handle, ptrDesc).address());

        handle = getHandle("disconnected", error);
        access.put(CLinker.getInstance().upcallStub(handle, errorDesc).address());

        handle = getHandle("errored", error);
        access.put(CLinker.getInstance().upcallStub(handle, errorDesc).address());

        handle = getHandle("joinGame", ptr);
        access.put(CLinker.getInstance().upcallStub(handle, ptrDesc).address());

        handle = getHandle("spectateGame", ptr);
        access.put(CLinker.getInstance().upcallStub(handle, ptrDesc).address());

        handle = getHandle("joinRequest", ptr);
        access.put(CLinker.getInstance().upcallStub(handle, ptrDesc).address());

        return memory.address();
    }

    private MethodHandle getHandle(String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup()
                .findVirtual(EventHandlers.class, name, type)
                .bindTo(this);
    }

    public void ready(MemoryAddress address) {
        if (handlers.ready == null)
            return;
        handlers.ready.accept(Structs.toUser(address));
    }

    public void disconnected(int code, MemoryAddress message) {
        if (handlers.disconnected == null)
            return;
        handlers.disconnected.accept(code, Structs.toString(message));
    }

    public void errored(int code, MemoryAddress message) {
        if (handlers.errored == null)
            return;
        handlers.errored.accept(code, Structs.toString(message));
    }

    public void joinGame(MemoryAddress secret) {
        if (handlers.joinGame == null)
            return;
        handlers.joinGame.accept(Structs.toString(secret));
    }

    public void spectateGame(MemoryAddress secret) {
        if (handlers.spectateGame == null)
            return;
        handlers.spectateGame.accept(Structs.toString(secret));
    }

    public void joinRequest(MemoryAddress address) {
        if (handlers.joinRequest == null)
            return;
        handlers.joinRequest.accept(Structs.toUser(address));
    }
}
