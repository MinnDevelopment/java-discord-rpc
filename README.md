# Java-DiscordRPC

This library contains Java bindings for Discord's Rich Presence API (https://github.com/discordapp/discord-rpc) using JNA. 

It is currently specific to the Windows 64 bit platform.

## Examples
Here is an example on how to use the API.

```java
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
while (!Thread.currentThread().isInterrupted())
    lib.Discord_RunCallbacks();
```

## License
Java-DiscordRPC is licensed under the Apache 2.0 License. The base DiscordRPC is licensed under the MIT license.

## Contributing
Find something that is lacking? Fork the project and pull request!
