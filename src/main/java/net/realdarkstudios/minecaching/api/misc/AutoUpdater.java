package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public abstract class AutoUpdater {
    private final Plugin plugin;
    private final String dataURL, versionURL;
    private Function<URL, HashMap<Version, String>> parser;
    private static String newVer = "", branch = "";
    private static boolean doUpdate = false;
    private static int lastUpdateCheck = 10000;

    /**
     * Creates an auto-updater for a given Plugin.
     * There is no need to create more than one auto updater, as this may have bad consequences.
     * @param plugin The {@link Plugin} that owns this auto updater.
     * @param dataURL The URL to check for updates from. Put '%b%' wherever the branch would go in the URL.
     * @param versionURL The URL to grab a version file from. Put '%b%' where the branch would go, and '%v%' where the version would go.
     */
    public AutoUpdater(Plugin plugin, String dataURL, String versionURL) {
        this.plugin = plugin;
        this.dataURL = dataURL;
        this.versionURL = versionURL;
        this.parser = this::parseMavenMetadata;
    }

    /**
     * Gets the parser type of the current {@link AutoUpdater}
     * @return The Parser
     */
    public Function<URL, HashMap<Version, String>> getParser() {
        return parser;
    }

    /**
     * Sets the parser.
     * @param parser The custom parsing method, a {@link Function} of {@link URL} that returns a {@link HashMap} of {@link Version} and {@link String}
     */
    public void setParser(Function<URL, HashMap<Version, String>> parser) {
        this.parser = parser;
    }

    /**
     * Updates the branch of the readURL
     * @param newBranch which branch to use
     * @since 0.3.0.1
     */
    public void updateBranch(String newBranch) {
        branch = newBranch;
    }

    /**
     * Check if there is an update that needs to be downloaded/installed
     * @return {@code true} if there is a newer version available for download, {@code false} if not
     * @since 0.3.0.0
     */
    public boolean hasUpdate() {
        return doUpdate;
    }

    /**
     * Gets the new version string
     * @return The newest version
     * @since 0.3.0.0
     */
    public String getNewestVersion() {
        return newVer;
    }

    /**
     * Returns the result of the last update check
     * @return -1 if plugin version is BEHIND, 0 if it is UP-TO-DATE, 1 if it is AHEAD, and 10000 for ERROR
     */
    public int getLastCheckResult() {
        return lastUpdateCheck;
    }

    /**
     * Checks for the latest Minecaching version using the maven.
     * Afterward, {@link #getLastCheckResult()} will be -1 if plugin version is BEHIND, 0 if it is UP-TO-DATE, 1 if it is AHEAD, and 10000 for ERROR
     * @since 0.3.0.1
     */
    public void checkForUpdate() {
        final int[] result = new int[1];
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<Version, String> versionMap;

                try {
                    // Grab the info from maven-metadata.xml on the maven
                    URL readURL = new URL(dataURL.replace("%b%", branch));

                    versionMap = getParser().apply(readURL);

                    // Put all versions into an array
                    List<Version> versions = new ArrayList<>(versionMap.keySet());

                    if (versions.isEmpty()) {
                        MinecachingAPI.tInfo(MessageKeys.Plugin.Update.NO_VERSIONS_AVAILABLE);
                        result[0] = 10000;
                        return;
                    }

                    // Sort accoring to Version.fromString()
                    versions.sort(Version::compareTo);

                    // Get the most recent version
                    Version laterV = versions.get(versions.size() - 1);
                    MinecachingAPI.tInfo(MessageKeys.Plugin.Update.LATEST, branch, plugin.getName(), versionMap.get(laterV));

                    // Compare laterV to plugin version
                    switch (Minecaching.getVersion().compareTo(laterV)) {
                        case -1 -> {
                            // BEHIND
                            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.STATUS_BEHIND, plugin.getName());
                            doUpdate = true;
                            newVer = versionMap.get(laterV);
                            result[0] = -1;
                            statusBehind(newVer);
                        }
                        case 0 -> {
                            // UP-TO-DATE
                            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.STATUS_UP_TO_DATE, plugin.getName());
                            result[0] = 0;
                            statusUpToDate();
                        }
                        case 1 -> {
                            // AHEAD
                            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.STATUS_AHEAD, plugin.getName());
                            result[0] = 1;
                            statusAhead();
                        }
                    }

                    lastUpdateCheck = result[0];
                    return;
                } catch (IOException e) {
                    MinecachingAPI.tWarning(MessageKeys.Plugin.Update.FAIL_TO_CHECK);
                    e.printStackTrace();
                }

                result[0] = 10000;
                lastUpdateCheck = result[0];
                errorChecking();
            }
        }.runTaskAsynchronously(Minecaching.getInstance());

        lastUpdateCheck = result[0];
    }

    public void applyUpdate() {
        String newVersion = getNewestVersion();
        MinecachingAPI.tInfo(MessageKeys.Plugin.Update.GETTING, newVersion);
        try {
            URL download = new URL(versionURL.replace("%b%", branch).replace("%v%", newVersion));
            ReadableByteChannel rbc = Channels.newChannel(download.openStream());
            File file = new File(Path.of(plugin.getDataFolder().toURI()).getParent().toString() + "/" + plugin.getName() + "-" + newVersion + ".jar");
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.DOWNLOADED);

            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File curfile = (File) getFileMethod.invoke(plugin);
            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.APPLIED);
            curfile.deleteOnExit();
        } catch (Exception e) {
            MinecachingAPI.tWarning(MessageKeys.Plugin.Update.FAIL);
            e.printStackTrace();
        }
    }

    /**
     * Converts a version to the representation used by the AutoUpdater
     * @param versionToParse The string to parse as a version
     * @return A string with the format of how the AutoUpdate compares versions
     */
    public static Version parseVersion(String versionToParse) {
        return Version.fromString(versionToParse.replace("<version>", "").replace("</version>", "").trim());
    }

    public abstract void ioExceptionHandler(IOException ioexc);
    public abstract void statusBehind(String newerVersion);
    public abstract void statusUpToDate();
    public abstract void statusAhead();
    public abstract void errorChecking();

    public HashMap<Version, String> parseMavenMetadata(URL readURL) {
        HashMap<Version, String> versionMap = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(readURL.openStream()));
            String str;

            while ((str = br.readLine()) != null) {
                // Check for start of <versions> block
                if (str.contains("<versions>")) {
                    while ((str = br.readLine()) != null) {
                        // Check for end of <versions> block
                        if (!str.contains("</versions>")) {
                            if (!str.toLowerCase().contains("snapshot") || branch.equals("snapshot")) {
                                // Will continue to next version if one fails to parse
                                try {
                                    versionMap.put(parseVersion(str), str.replace("</version>", "").replace("<version>", "").trim());
                                } catch (IllegalArgumentException ignored) {
                                }
                            }
                        } else break;
                    }
                }
            }

            br.close();
        } catch (IOException e) {
            ioExceptionHandler(e);
        }

        return versionMap;
    }

    /**
     * Assuming a list like
     * <br>{
     * <br> "0.3.1.0-pre1",
     * <br> "0.3.1.0",
     * <br> "snapshot-0.3.1.1-24w13a"
     * <br>}
     *
     * @param readURL The URL to read from
     */
    private HashMap<Version, String> parseJSON(URL readURL) {
        HashMap<Version, String> versionMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(readURL.openStream()));
            String str;

            while ((str = br.readLine()) != null) {
                String line = str.replace("{", "").replace("}", "").replace(",", "").replace("\"", "").trim();
                try {
                    versionMap.put(parseVersion(line), line);
                } catch (IllegalArgumentException ignored) {
                }
            }

            br.close();
        } catch (IOException e) {
            ioExceptionHandler(e);
        }

        return versionMap;
    }

    public static class Version implements Comparable<Version> {
        private final int majorapi, minorapi, build, patch, prereleaseVer;
        private final boolean snapshot, prerelease;
        private final String snapshotVer;

        public Version(int majorapi, int minorapi, int build, int patch, boolean snapshot, String snapshotVer, boolean prerelease, int prereleaseVer) {
            this.majorapi = majorapi;
            this.minorapi = minorapi;
            this.build = build;
            this.patch = patch;

            if (snapshot && prerelease) {
                MinecachingAPI.warning("Constructed version is both a snapshot and prerelease! Picking prerelease due to precedence.");
                snapshot = false;
            }

            this.snapshot = snapshot;
            this.snapshotVer = snapshotVer;
            this.prerelease = prerelease;
            this.prereleaseVer = prereleaseVer;
        }

        public int getMajorApi() {
            return majorapi;
        }

        public int getMinorApi() {
            return minorapi;
        }

        public int getBuild() {
            return build;
        }

        public int getPatch() {
            return patch;
        }

        public String getSnapshotVer() {
            return snapshotVer;
        }

        public int getPrereleaseVer() {
            return prereleaseVer;
        }

        public boolean isRelease() {
            return !snapshot && !prerelease;
        }

        public boolean isSnapshot() {
            return snapshot;
        }

        public boolean isPrerelease() {
            return prerelease;
        }

        @Override
        public String toString() {
            if (prerelease) {
                return String.format("%d.%d.%d.%d-pre%d", majorapi, minorapi, build, patch, prereleaseVer);
            } else if (snapshot) {
                return String.format("snapshot.%d.%d.%d.%d-%s", majorapi, minorapi, build, patch, snapshotVer);
            } else {
                return String.format("%d.%d.%d.%d", majorapi, minorapi, build, patch);
            }
        }

        public static Version fromString(String version) {
            if (version.contains("-pre")) {
                // prerelease
                int maj, min, build, patch, prn;
                // splits into X.X.X.X and X (prerelease number/prn)
                // ex 0.3.1.0-pre1 -> 0.3.1.0 and 1

                String[] parts = version.split("-pre");
                String[] release = parts[0].split("\\.");

                maj = Integer.parseInt(release[0]);
                min = Integer.parseInt(release[1]);
                build = Integer.parseInt(release[2]);
                patch = Integer.parseInt(release[3]);
                prn = Integer.parseInt(parts[1]);

                return new Version(maj, min, build, patch, false, "", true, prn);
            } else if (version.toLowerCase().contains("snapshot-")) {
                if (version.equals("snapshot-24w11b")) return new Version(0, 3, 1, 0, true, "24w11b", false, 0);

                //snapshot
                int maj, min, build, patch;
                String sv;

                String[] parts = version.split("-");
                String[] release;

                if (version.toLowerCase().startsWith("snapshot-")) {
                    // splits into ["snapshot", "X.X.X.X", "sv (snapshot version)"]
                    // ex snapshot-0.3.1.0-24w12a -> ["snapshot", "0.3.1.0", "24w12a"]
                    release = parts[1].split("\\.");
                } else {
                    // old: X.X.X.X-SNAPSHOT-X
                    release = parts[0].split("\\.");
                }

                maj = Integer.parseInt(release[0]);
                min = Integer.parseInt(release[1]);
                build = Integer.parseInt(release[2]);
                patch = Integer.parseInt(release[3]);
                sv = parts[2];

                return new Version(maj, min, build, patch, true, sv, false, 0);
            } else {
                if (version.equals("0.3.1.0-24w10a")) return new Version(0, 3, 1, 0, true, "24w10a", false, 0);
                if (version.equals("0.3.1.0-24w11a")) return new Version(0, 3, 1, 0, true, "24w11a", false, 0);

                //release
                int maj, min, build, patch;
                // splits into ["X.X.X.X", "extra (extra bits, will ignore)"]
                // ex 0.3.1.0-randomJunk -> ["0.3.1.0", "randomJunk"]
                // we only care about the first bit

                String[] parts = version.split("-");
                String[] release = (parts[0]).split("\\.");

                maj = Integer.parseInt(release[0]);
                min = Integer.parseInt(release[1]);
                build = Integer.parseInt(release[2]);
                patch = Integer.parseInt(release[3]);

                return new Version(maj, min, build, patch, false, "", false, 0);
            }
        }

        @Override
        public int compareTo(Version other) {
            // handles X.X.X.X comparing
            int maj = Integer.compare(this.getMajorApi(), other.getMajorApi());
            if (maj != 0) return maj;

            int min = Integer.compare(this.getMinorApi(), other.getMinorApi());
            if (min != 0) return min;

            int build = Integer.compare(this.getBuild(), other.getBuild());
            if (build != 0) return build;

            int patch = Integer.compare(this.getPatch(), other.getPatch());
            if (patch != 0) return patch;

            // release type check
            // release > prerelease > snapshot
            if (this.isRelease() && (other.isPrerelease() || other.isSnapshot()) || this.isPrerelease() && other.isSnapshot()) return 1;
            else if (other.isRelease() && (this.isPrerelease() || this.isSnapshot()) || other.isPrerelease() && this.isSnapshot()) return -1;

            // both are a release/prerelease/snapshot
            if (this.isRelease() && other.isRelease()) return 0;
            if (this.isPrerelease() && other.isPrerelease()) {
                return Integer.compare(this.getPrereleaseVer(), other.getPrereleaseVer());
            } else {
                //snapshot parsing woo
                //format: YYwWWX (YY is 2 digit year, WW is 2 digit week number, X is week snapshot ver)

                int year = compareSnapshotNumberPart(this.getSnapshotVer().substring(0, 2), other.getSnapshotVer().substring(0, 2));
                if (year != 0) return year;

                int week = compareSnapshotNumberPart(this.getSnapshotVer().substring(3, 5), other.getSnapshotVer().substring(3, 5));
                if (week != 0) return week;

                return compareSnapshotStringPart(this.getSnapshotVer().substring(5), other.getSnapshotVer().substring(5));
            }
        }

        private int compareSnapshotNumberPart(String partTs, String partOs) {
            int partT, partO;

            try {
                partT = Integer.parseInt(partTs);
            } catch (Exception e) {
                partT = 0;
            }

            try {
                partO = Integer.parseInt(partOs);
                if (partT == 0) return -1;
            } catch (Exception e) {
                if (partT == 0) return 0;
                else return 1;
            }

            int part = Integer.compare(partT, partO);

            if (part == 1) return 1;
            else if (part == -1) return -1;
            else return 0;
        }

        private int compareSnapshotStringPart(String partTs, String partOs) {
            // this will only be the very last part of the snapshot;

            if (partTs.length() > partOs.length()) return 1;
            if (partOs.length() > partTs.length()) return -1;
            if (partTs.isEmpty()) return 0;

            int lastCharCheck = 0;
            for (int i = 0; i < partTs.length(); i++) {
                int charV = Character.compare(partTs.toCharArray()[i], partOs.toCharArray()[i]);
                lastCharCheck = charV;
                if (charV != 0) break;
            }

            return lastCharCheck >= 1 ? 1 : lastCharCheck <= -1 ? -1 : 0;
        }
    }
}
