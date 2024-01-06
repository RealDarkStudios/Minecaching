Bring [Geocaching](https://www.geocaching.com) to your Minecraft server!

This plugin allows players to hide and find Minecaches (or "Caches") around the server while allowing operators to manage it.

## For Server Owners

Server owners can control many things, such as restrictions on where the caches are placed, the types of allowed caches, and more in the config!
Operators have access to certain commands to, such as `/verify` and can bypass `/delete` and `/edit` checks.

<style>
td {
  text-align: center;
}
</style>

<table>
  <tr>
    <th>Permission Node</th>
    <th>Default Access</th>
    <th>What it affects</th>
  </tr>
  <tr>
    <td>minecaching.addcache</td>
    <td>All players</td>
    <td>/addcache, Allows a player to create a cache</td>
  </tr>
  <tr>
    <td>minecaching.admin</td>
    <td>Operators</td>
    <td>/mcadmin, Allows access to admin utilities</td>
  </tr>
  <tr>
    <td>minecaching.admin.reload</td>
    <td>Operators</td>
    <td>/mcadmin reload, Allows reloading the plugin and the data</td>
  </tr>
  <tr>
    <td>minecaching.admin.verifycache</td>
    <td>Operators</td>
    <td>/verifycache, Verifies a cache, so it can be logged by players</td>
  </tr>
  <tr>
    <td>minecaching.deletecache</td>
    <td>All players</td>
    <td>/deletecache, Allows deleting only their caches. Operators bypass this restriction</td>
  </tr>
  <tr>
    <td>minecaching.editcache</td>
    <td>All players</td>
    <td>/editcache, Allows editing only their caches. Operators bypass this restriction</td>
  </tr>
  <tr>
    <td>minecaching.locatecache</td>
    <td>All players</td>
    <td>Allows a player to locate a cache</td>
  </tr>
  <tr>
    <td>minecaching.logcache</td>
    <td>All players</td>
    <td>Allows a player to log a cache</td>
  </tr>
  <tr>
    <td>minecaching.listcaches</td>
    <td>All players</td>
    <td>Allows players to see the list of caches</td>
  </tr>
</table>

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
  <artifactId>Minecaching</artifactId>
  <version>2.0.0.7-SNAPSHOT-1</version>
</dependency>
```


> NOTE: Only full version releases (such as 2.0.0.7) are available in the Releases maven.
> If you want to be able to use ANY version, including releases, pick the snapshot repository!
</details>

### API & Events:

Since v2.0.0, Minecaching has a dedicated API class, `MinecachingAPI`

It contains a bunch of methods you can call for various things.

Alongside this update, there are now events!

#### Events:
- MinecacheCreatedEvent
- MinecacheDeletedEvent
- MinecacheEditedEvent
- MinecacheFoundEvent
- MinecacheVerifiedEvent
- StartLocatingMinecacheEvent
- StopLocatingMinecacheEvent

You can read the javadocs for each of these classes to see the parameters they have.