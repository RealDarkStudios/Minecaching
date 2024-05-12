![Latest Release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.digitalunderworlds.com%2Freleases%2Fnet%2Frealdarkstudios%2Fminecaching%2Fmaven-metadata.xml&label=Latest%20Release)
![Latest Snapshot](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.digitalunderworlds.com%2Fsnapshots%2Fnet%2Frealdarkstudios%2Fminecaching%2Fmaven-metadata.xml&label=Latest%20Snapshot)

Bring [Geocaching](https://www.geocaching.com) to your Minecraft server!

This plugin allows players to hide and find Minecaches (or "Caches") around the server while allowing operators to manage it.

> DEVELOPERS: After v0.3.1.0, the artifact id is now `minecaching`, not `Minecaching`!

> DEVELOPERS: **Some API stuff has moved to a new plugin, RDSCommons!**
>
> RDSCommons is a new plugin for shared code across Minecaching and future plugins.
> See `For Developers / Transferring To RDSCommons` for more information!


## For Server Owners:

### Installation
You can find the latest version of Minecaching [here](https://modrinth.com/project/minecaching/).
Minecaching also requires a dependency plugin, RDSCommons, which you can find [here](https://modrinth.com/project/rdscommons).

Server owners can control many things, such as restrictions on where the caches are placed, the types of allowed caches, and more in the config!
Operators have access to certain commands, such as `/verify` and can (by default) bypass `/delete` and `/edit` checks.

For non-english servers, you should change the language in `plugins\Minecaching\config.yml`.
In there you can also find many other options, such as changing the `/mcstats` scoring, configuring auto-updates, and more.
You can also change some config values in-game by using `/mca conf`

### Permission Nodes
<table>
<tr>
<th>Permission Node</th>
<th>Who has it by default?</th>
</tr> 
<tr>
<td>minecaching.*</td>
<td>Nobody</td>
</tr>
<tr>
<td>minecaching.bypass.*</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.bypass.delete_others</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.bypass.edit_others</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.admin.*</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.admin.*</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.admin.config</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.admin.force_stat_update</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.admin.reload</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.admin.server_data</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.command.*</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.command.admin</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.command.archive</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.command.create</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.delete</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.disable</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.command.edit</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.hint</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.list</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.locate</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.log</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.logbook</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.maintainer</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.command.publish</td>
<td>OP</td>
</tr>
<tr>
<td>minecaching.command.stats</td>
<td>Everyone</td>
</tr>
<tr>
<td>minecaching.misc.*</td>
<td>OP</td>
</tr>
</table>

## For Developers:

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

<details><summary>Transferring to RDSCommons</summary>
With 0.4.0.0, a new plugin (RDSCommons) was created to hold shared code for Minecaching and future projects.

To start using RDSCommons, you will need to update to version Snapshot 24w13a or above.
Then, add this dependency, replacing (version) with the minimum version you require:
```xml
<dependency>
  <groupId>net.realdarkstudios</groupId>
  <artifactId>rdscommons</artifactId>
  <version>[(version), 1.0.99999.0)</version>
  <scope>provided</scope>
</dependency>
```
![Latest RDSCommons Snapshot](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.digitalunderworlds.com%2Fsnapshots%2Fnet%2Frealdarkstudios%2Frdscommons%2Fmaven-metadata.xml&label=Latest%20RDSCommons%20Snapshot)
![Latest RDSCommons Release](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.digitalunderworlds.com%2Freleases%2Fnet%2Frealdarkstudios%2Frdscommons%2Fmaven-metadata.xml&label=Latest%20RDSCommons%20Release)

#### Changes Summary:
- PACKAGE net.realdarkstudios.minecaching.api.menu.impl -> net.realdarkstudios.commons.menu
- minecaching.api.misc.AutoUpdater -> commons.util.AutoUpdater
- minecaching.api.misc.AutoUpdater.Version -> commons.util.Version
- minecaching.api.misc.Localization -> commons.util.Localization
- minecaching.api.misc.LocalizationProvider -> commons.util.LocalizationProvider
- minecaching.api.util.LocalizedMessages -> commons.util.LocalizedMessages
- minecaching.api.util.TextComponentBuilder -> commons.util.TextComponentBuilder
- minecaching.api.util.MessagesKeys -> minecaching.api.util.MCMessageKeys (some keys are instead in commons.util.MessageKeys)

**Registering A Localization**:
```java
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.misc.Config;

// OLD IMPORTS
import net.realdarkstudios.minecaching.api.misc.LocalizationProvider;
import net.realdarkstudios.minecaching.api.misc.Localization;

// NEW IMPORTS
import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.util.LocalizationProvider;
import net.realdarkstudios.commons.util.Localization;

@Override
public void onEnable() {
    Localization exampleLocalization;

    // OLD
    exampleLocalization = MinecachingAPI.getLocalizationProvider().load(Minecaching.getInstance());

    // NEW
    exampleLocalization = CommonsAPI.get().registerLocalization(Minecaching.getInstance(), Config.getInstance().getServerLocale());
}

```

`MCMessages` was also removed. Please switch over to `LocalizedMessages`.

You may also implement `IRDSPlugin` in your main plugin class to be compliant for any plugin requesting your `Version` information

You can see the full changelog message below:

<details><summary>Snapshot 24w13a Changelog</summary>

Added:
- MAVEN SOURCES + JAVADOCS to (hopefully) all future Minecaching releases
- MCMessageKeys.Command.Stats.NONE
- MinecachingAPI#getCommonsAPI

Removed:
- MinecachingAPI#getConfigDataVersion
- MinecachingAPI#getMinecacheDataVersion
- MinecachingAPI#getPlayerDataVersion
- MinecachingAPI#getLogbookDataVersion
- MinecachingAPI#tInfo(String, Object...)
- MinecachingAPI#tWarning(String, Object...)
- (ALL CLASSES BELOW ARE IN RDSCommons)
- AutoUpdater
- BaseEvent
- BooleanMenuItem
- CancellableBaseEvent
- CloseMenuItem
- ErrorMenuItem
- GoBackMenuItem
- Localization
- LocalizationProvider
- MCMenu
- MCMenuHolder
- MenuItem
- MenuItemClickEvent
- MenuItemState
- MultiStateMenuItem
- PaginationMenu
- PaginationMenuItem
- PaginationPageItem
- RefreshPaginationMenuItem
- SkullMenuItem
- TextComponentBuilder

Refactors:
- MessageKeys -> MCMessageKeys

Changes:
- (BUGFIX) Added checks if there are 0 caches (caused issues with adding caches and stats)
- Switched over to RDSCommons
- UUID check is now in PlayerDataObject#update
- RDSCommons dependency
- MinecachingAPI#getLogger now returns an RDSLogHelper
- And more
</details>
</details>