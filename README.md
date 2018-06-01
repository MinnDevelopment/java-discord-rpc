[bintray-version]: https://api.bintray.com/packages/minndevelopment/maven/java-discord-rpc/images/download.svg
[bintray-download]: https://bintray.com/minndevelopment/maven/java-discord-rpc/_latestVersion
[jitpack-version]: https://jitpack.io/v/MinnDevelopment/java-discord-rpc.svg
[jitpack-setup]: https://jitpack.io/#MinnDevelopment/java-discord-rpc

# java-discord-rpc

[ ![bintray-version][bintray-version] ][bintray-download] [ ![jitpack-version][jitpack-version] ][jitpack-setup]

This library contains Java bindings for [Discord's official RPC SDK](https://github.com/discordapp/discord-rpc) using JNA.

This project provides binaries for `linux-x86-64`, `win32-x86-64`, `win32-x86`, and `darwin`.
The binaries can be found at [MinnDevelopment/discord-rpc-release](https://github.com/MinnDevelopment/discord-rpc-release)

## Documentation

You can see the official discord documentation in the [API Documentation](https://discordapp.com/developers/docs/rich-presence/how-to).
<br>Alternatively you may visist the javadoc at [jitpack](https://jitpack.io/com/github/MinnDevelopment/java-discord-rpc/master-SNAPSHOT/javadoc/index.html).

## Setup

In the follwing please replace `%VERSION%` with the version listed above.

Note: Version **2.0.0** and above will be listed on **jcenter**, if you are interested in older builds use **jitpack**.

### Gradle

**Repository**

```gradle
repositories {
    jcenter()
}
```

**Artifact**

```gradle
dependencies {
    compile 'club.minnced:java-discord-rpc:%VERSION%'
}
```

### Maven

**Repository**

```xml
<repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com</url>
    <name>jcenter-bintray</name>
</repository>
```

**Artifact**

```xml
<dependency>
    <groupId>club.minnced</groupId>
    <artifactId>java-discord-rpc</artifactId>
    <version>%VERSION%</version>
</dependency>
```

### Compile Yourself

1. Install git and JDK 8+
2. `git clone https://github.com/minndevelopment/java-discord-rpc`
3. `cd java-discord-rpc`
4. `./gradlew build` or on windows `gradlew build`
5. Get the jar from `build/libs` with the name `java-discord-rpc-%VERSION%-all.jar`

## Examples

### Basics

The library can be used just like the SDK. This means you can almost copy the exact code used in the official documentation.

```java
import club.minnced.discord.rpc.*;

public class Main {
    public static void main(String[] args) {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");
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

### Community Examples

If you want to add an example to the [example directory](https://github.com/MinnDevelopment/java-discord-rpc/tree/master/examples)
you can do so by making a PR with your package and source.
All I ask from you is that the example does not abuse the API in any way and that your example can compile.
If you would like to add examples in other JVM languages you must first add support via the `build.gradle` file.

For example, `examples/java/club/minnced/rpc/examples/MyGame.java` would be your example file relative to the project root directory.

## License

java-discord-rpc is licensed under the Apache 2.0 License. The base DiscordRPC is licensed under the MIT license.

## Contributing

Find something that is lacking? Fork the project and pull request!
