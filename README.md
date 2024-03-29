![Latest Release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.digitalunderworlds.com%2Freleases%2Fnet%2Frealdarkstudios%2Fminecaching%2Fmaven-metadata.xml&label=Latest%20Release)
![Latest Snapshot](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.digitalunderworlds.com%2Fsnapshots%2Fnet%2Frealdarkstudios%2Fminecaching%2Fmaven-metadata.xml&label=Latest%20Snapshot)

Bring [Geocaching](https://www.geocaching.com) to your Minecraft server!

This plugin allows players to hide and find Minecaches (or "Caches") around the server while allowing operators to manage it.

> DEVELOPERS: After v0.3.1.0, the artifact id is now `minecaching`, not `Minecaching`!
> ! Some functions have also moved to RDSCommons !

## For Server Owners

Server owners can control many things, such as restrictions on where the caches are placed, the types of allowed caches, and more in the config!
Operators have access to certain commands to, such as `/verify` and can bypass `/delete` and `/edit` checks.

Setting up the plugin is pretty simple, a drag and drop install should work, however you need to make sure you install [RDSCommons](https://maven.digitalunderworlds.com/#/snapshots/net/realdarkstudios/rdscommons) too.
For non-english servers, you should change the language in `plugins\Minecaching\config.yml`.
In there you can also find many other options, such as changing the `/mcstats` scoring, configuring auto-updates, and more.

## For Developers

You can use the [Digitalunderworlds Maven](https://maven.digitalunderworlds.com) to add Minecaching to your project

<details><summary>Adding as a dependency</summary>

#### Snapshots:
```xml
<repository>
  <id>dumaven-snapshots</id>
  <name>Digitalunderworlds Maven Snapshots</name>
  <url>https://maven.digitalunderworlds.com/snapshots</url>
</repository>
```

#### Releases:
```xml
<repository>
  <id>dumaven-releases</id>
  <name>Digitalunderworlds Maven Releases</name>
  <url>https://maven.digitalunderworlds.com/releases</url>
</repository>
```

Then,
```xml
<dependency>
  <groupId>net.realdarkstudios</groupId>
  <artifactId>minecaching</artifactId>
  <version>0.4.0.0-snapshot-24w13a</version>
</dependency>
```
You can view versions [here](https://maven.digitalunderworlds.com/#/snapshots/net/realdarkstudios/minecaching)

> NOTE: Only full version releases (such as 0.2.0.7) are available in the Releases maven.
> If you want to be able to use ANY version, including releases, pick the Snapshot repository!
</details>

### Changes:
With 0.3.0.0, an `EXPERIMENTAL_FEATURES` config option was released. To check it, use `Config.getInstance().experimentalFeatures()`

With 0.3.0.1, The `GUI Menu` framework was added, which is a somewhat modified fork of [AmpMenus](https://github.com/Scarsz/AmpMenus) that is basically just refactors and some minor logic changes.

With 0.3.0.3, the Changelog was added, accessible at [`CHANGELOG.txt`](https://github.com/RealDarkStudios/Minecaching/blob/master/CHANGELOG.txt)

With 0.3.0.5, various `PlayerDataObject` methods were added, like `#getUsername()` and `#isOnline()`

With 0.3.1.0-24w10a, the `MCMessages V2` framework was introduced, mainly known as `LocalizationMessages`.

As of `0.3.1.0-24w10a`, the `MCMessages` class and `MinecachingAPI#tInfo(String, Object...)` and `MinecachingAPI#tWarning(String, Object...)`methods have been deprecated, set for removal in 0.3.2.0.

To update to the new system:
**MinecachingAPI#tInfo/tWarning**:

```java
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;

// Using "plugin.reloading" as an example

@Override
public void onEnable() {
    // OLD
    MinecachingAPI.tInfo("plugin.reloading");

    //NEW
    MinecachingAPI.tInfo(MCMessageKeys.Plugin.RELOADING);
}
```

**MCMessages**:

```java

// Using "error.cantfind" as an example

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;

@Override
public void onEnable() {
    // Assuming sender defined elsewhere

    //OLD
    MCMessages.sendErrorMsg(sender, "cantfind");
    // god forbid you did this
    MCMessages.send(sender, ChatColor.RED + "" + ChatColor.UNDERLINE + "cantfind");

    //NEW
    LocalizedMessages.send(sender, MCMessageKeys.Error.CANT_FIND_CACHE);
}
```

You can view the full issue [here](https://github.com/RealDarkStudios/Minecaching/issues/1)

As of 0.3.1.0, the artifact id moving forward will be `minecaching`. Please update your dependencies!

As of 0.4.0.0, many of the utils such as `AutoUpdater`, `Localization`, `LocalizationProvider`, `LocalizedMessages`, `TextComponentBuilder` and more are moving to a separate `RDSCommons` plugin.
<br>The packages of these things will be changing from `net.realdarkstudios.minecaching.api.xxx` -> `net.realdarkstudios.commons.xxx`