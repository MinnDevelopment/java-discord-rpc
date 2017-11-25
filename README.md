[ ![jitpack](https://jitpack.io/v/MinnDevelopment/Java-DiscordRPC.svg) ](https://jitpack.io/#MinnDevelopment/Java-DiscordRPC)
# Java-DiscordRPC

This library contains Java bindings for [Discord's official RPC SDK](https://github.com/discordapp/discord-rpc) using JNA. 

This project provides binaries for `linux-x86-64`, `win32-x86-64` and `darwin`.

If, on macOS, you get the following message:
```
Error in LSRegisterURL: -10811
``` 
it can safely be ignored.

## Examples

```java
import club.minnced.discord.rpc.*;

public class Main {

    public static void main(String[] args) {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = () -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Testing RPC";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

}
```

## License
Java-DiscordRPC is licensed under the Apache 2.0 License. The base DiscordRPC is licensed under the MIT license.

## Contributing
Find something that is lacking? Fork the project and pull request!
