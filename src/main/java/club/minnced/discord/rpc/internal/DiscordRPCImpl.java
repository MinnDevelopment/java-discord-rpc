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
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import jdk.incubator.foreign.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_POINTER;

public class DiscordRPCImpl implements DiscordRPC {
    private static DiscordRPCImpl instance;

    private final LibraryLookup lib;
    private final CLinker linker = CLinker.getInstance();
    private final Map<String, MethodHandle> handles = new HashMap<>();

    public synchronized static DiscordRPCImpl getInstance() {
        if (instance != null)
            return instance;
        // TODO: Determine platform here
        try (InputStream resource = Main.class.getResourceAsStream("/win32-x86-64/discord-rpc.dll");
             FileOutputStream fout = new FileOutputStream("discord-rpc.dll")) { // TODO: Make this use /tmp instead
            resource.transferTo(fout);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        LibraryLookup DLL = LibraryLookup.ofLibrary("discord-rpc");
        return instance = new DiscordRPCImpl(DLL);
    }

    private DiscordRPCImpl(LibraryLookup lib) {
        this.lib = lib;
    }

    private MethodHandle simple(String name) {
        return handles.computeIfAbsent(name, k -> {
            LibraryLookup.Symbol symbol = lib.lookup(name).orElseThrow();
            return linker.downcallHandle(
                symbol,
                MethodType.methodType(Void.TYPE),
                FunctionDescriptor.ofVoid()
            );
        });
    }

    @Override
    public void Discord_Initialize(@Nonnull String applicationId, @Nullable DiscordEventHandlers handlers, boolean autoRegister, @Nullable String steamId) {
        Objects.requireNonNull(applicationId, "Application ID must not be null");
        MethodHandle handle = handles.computeIfAbsent("Discord_Initialize", (k) -> {
            LibraryLookup.Symbol symbol = lib.lookup(k).orElseThrow();
            return linker.downcallHandle(
                    symbol,
                    MethodType.methodType(Void.TYPE, MemoryAddress.class, MemoryAddress.class, int.class, MemoryAddress.class),
                    FunctionDescriptor.ofVoid(C_POINTER, C_POINTER, C_INT, C_POINTER)
            );
        });
        try (var scope = NativeScope.unboundedScope()) {
            MemoryAddress handlersAddr = MemoryAddress.NULL;
            if (handlers != null)
                handlersAddr = new EventHandlers(handlers).allocate(scope);
            handle.invokeExact(
                Structs.wrap(scope, applicationId),
                handlersAddr,
                autoRegister ? 1 : 0,
                Structs.wrap(scope, steamId)
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_Shutdown() {
        MethodHandle handle = simple("Discord_Shutdown");
        try {
            handle.invokeExact();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_RunCallbacks() {
        MethodHandle handle = simple("Discord_RunCallbacks");
        try {
            handle.invokeExact();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_UpdateConnection() {
        MethodHandle handle = simple("Discord_UpdateConnection");
        try {
            handle.invokeExact();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_UpdatePresence(@Nullable DiscordRichPresence presence) {
        MethodHandle handle = handles.computeIfAbsent("Discord_UpdatePresence", (k) -> {
            LibraryLookup.Symbol symbol = lib.lookup(k).orElseThrow();
            return linker.downcallHandle(
                    symbol,
                    MethodType.methodType(Void.TYPE, MemoryAddress.class),
                    FunctionDescriptor.ofVoid(C_POINTER)
            );
        });
        try (var scope = NativeScope.unboundedScope()) {
            if (presence == null) {
                handle.invokeExact(MemoryAddress.NULL);
                return;
            }
            MemorySegment struct = scope.allocate(Structs.RICH_PRESENCE);
            Access access = new Access(struct).withScope(scope);
            access.put(presence.state);
            access.put(presence.details);
            access.put(presence.startTimestamp);
            access.put(presence.endTimestamp);
            access.put(presence.largeImageKey);
            access.put(presence.largeImageText);
            access.put(presence.smallImageKey);
            access.put(presence.smallImageText);
            access.put(presence.partyId);
            access.put(presence.partySize);
            access.put(presence.partyMax);
            access.put(presence.matchSecret);
            access.put(presence.joinSecret);
            access.put(presence.spectateSecret);
            access.put(presence.instance);
            handle.invokeExact(struct.address());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_ClearPresence() {
        MethodHandle handle = simple("Discord_ClearPresence");
        try {
            handle.invokeExact();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_Respond(@Nonnull String userid, int reply) {
        Objects.requireNonNull(userid, "User ID must not be null");
        MethodHandle handle = handles.computeIfAbsent("Discord_Respond", (k) -> {
            LibraryLookup.Symbol symbol = lib.lookup(k).orElseThrow();
            return linker.downcallHandle(
                symbol,
                MethodType.methodType(Void.TYPE, MemoryAddress.class, int.class),
                FunctionDescriptor.ofVoid(C_POINTER, C_INT)
            );
        });
        try (var scope = NativeScope.unboundedScope()) {
            handle.invokeExact(
                Structs.wrap(scope, userid),
                reply
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_UpdateHandlers(@Nullable DiscordEventHandlers handlers) {
        MethodHandle handle = handles.computeIfAbsent("Discord_UpdateHandlers", (k) -> {
            LibraryLookup.Symbol symbol = lib.lookup(k).orElseThrow();
            return linker.downcallHandle(
                symbol,
                MethodType.methodType(Void.TYPE, MemoryAddress.class),
                FunctionDescriptor.ofVoid(C_POINTER)
            );
        });
        try (var scope = NativeScope.unboundedScope()) {
            MemoryAddress address = MemoryAddress.NULL;
            if (handlers != null)
                address = new EventHandlers(handlers).allocate(scope);
            handle.invokeExact(address);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_Register(String applicationId, String command) {
        MethodHandle handle = handles.computeIfAbsent("Discord_Register", (k) -> {
            LibraryLookup.Symbol symbol = lib.lookup(k).orElseThrow();
            return linker.downcallHandle(
                    symbol,
                    MethodType.methodType(Void.TYPE, MemoryAddress.class, MemoryAddress.class),
                    FunctionDescriptor.ofVoid(C_POINTER, C_POINTER)
            );
        });
        try (var scope = NativeScope.unboundedScope()) {
            handle.invokeExact(
                    Structs.wrap(scope, applicationId),
                    Structs.wrap(scope, command)
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void Discord_RegisterSteamGame(String applicationId, String steamId) {
        MethodHandle handle = handles.computeIfAbsent("Discord_RegisterSteamGame", (k) -> {
            LibraryLookup.Symbol symbol = lib.lookup(k).orElseThrow();
            return linker.downcallHandle(
                    symbol,
                    MethodType.methodType(Void.TYPE, MemoryAddress.class, MemoryAddress.class),
                    FunctionDescriptor.ofVoid(C_POINTER, C_POINTER)
            );
        });
        try (var scope = NativeScope.unboundedScope()) {
            handle.invokeExact(
                    Structs.wrap(scope, applicationId),
                    Structs.wrap(scope, steamId)
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
