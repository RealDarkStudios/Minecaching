package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class AutoUpdater {
    private static String readURL = "https://maven.digitalunderworlds.com/" + Config.getInstance().getUpdateBranch() + "s/net/realdarkstudios/Minecaching/maven-metadata.xml";
    private static boolean doUpdate = false;
    private static String newVer = "";

    /**
     * Updates the branch of the readURL
     * @param branch
     * @since 0.3.0.1
     */
    public static void updateBranch(String branch) {
        readURL = "https://maven.digitalunderworlds.com/" + branch + "s/net/realdarkstudios/Minecaching/maven-metadata.xml";
    }

    /**
     * Checks for the latest published version on the maven.
     * @since 0.3.0.0
     * @deprecated 0.3.0.1
     */
    @Deprecated(since = "0.3.0.1")
    public static void checkForUpdateOld() {
        if (!Config.getInstance().autoUpdate()) {
            MinecachingAPI.info("Auto Updates are disabled!");
            return;
        }
        try {
            URL url = new URL(readURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = br.readLine()) != null) {
                String line = str;
                if (line.contains("<release>")) {
                    String newVersion = line.replace("<release>", "").replace("</release>", "").trim();
                    MinecachingAPI.info("Latest version: " + newVersion);

                    List<ModuleDescriptor.Version> versions = List.of(Minecaching.getInstance().getDescription().getVersion(), newVersion).stream().map(ModuleDescriptor.Version::parse).sorted().toList();

                    String laterV = versions.get(1).toString();
                    if (laterV.equals(Minecaching.getInstance().getDescription().getVersion()) && !laterV.equals(newVersion)) MinecachingAPI.info("Ahead");
                    else if (laterV.equals(newVersion) && !laterV.equals(Minecaching.getInstance().getDescription().getVersion())) {
                        MinecachingAPI.info("Behind");
                        doUpdate = true;
                        newVer = newVersion;
                    }
                    else MinecachingAPI.info("Up to date");

                    br.close();
                    break;
                }
            }
        } catch (IOException e) {
            MinecachingAPI.warning("Unable to perform auto-update");
        }
        MinecachingAPI.warning("AutoUpdater#checkForUpdateOld() is deprecated! Consider using AutoUpdater#checkForUpdate() instead!");
    }

    /**
     * Check if there is an update that needs to be downloaded/installed
     * @return {@code true} if there is a newer version available for download, {@code false} if not
     */
    public static boolean hasUpdate() {
        return doUpdate;
    }

    /**
     * Gets the new version string
     * @return The newest version
     */
    public static String getNewestVersion() {
        return newVer;
    }

    /**
     * Checks for the latest version.
     * This version actually takes {@link Config#getUpdateBranch()} into account.
     * @return -1 if plugin version is BEHIND, 0 if it is UP-TO-DATE, 1 if it is AHEAD, 10000 for ERROR
     */
    public static int checkForUpdate() {
        StringBuilder versionList = new StringBuilder();

        try {
            // Grab the info from maven-metadata.xml on the maven
            URL url = new URL(readURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = br.readLine()) != null) {
                String line = str;

                // Check for start of <versions> block
                if (line.contains("<versions>")) {
                    while ((str = br.readLine()) != null) {
                        line = str;
                        // Check for end of <versions> block
                        if (!line.contains("</versions>")) {
                            if (!line.contains("SNAPSHOT") || Config.getInstance().getUpdateBranch().equals("snapshot")) versionList.append(line.trim().replace("</version>", ""));
                        } else break;
                    }

                    // Put all versions into an array
                    String[] versionsA = versionList.toString().split("<version>");
                    // The first version is an empty string, so we remove it
                    versionsA = Arrays.copyOfRange(versionsA, 1, versionsA.length);

                    // Sort accoring to ModuleDescriptor.Version::parse
                    List<ModuleDescriptor.Version> versions = Stream.of(versionsA).map(ModuleDescriptor.Version::parse).sorted().toList();
                    // Get the most recent version
                    String laterV = versions.get(versions.size() - 1).toString();
                    MinecachingAPI.tInfo("plugin.update.latest", Config.getInstance().getUpdateBranch(), laterV);

                    // Compare laterV to plugin version
                    switch (ModuleDescriptor.Version.parse(Minecaching.getInstance().getDescription().getVersion()).compareTo(ModuleDescriptor.Version.parse(laterV))) {
                        case -1 -> {
                            // BEHIND
                            MinecachingAPI.tInfo("plugin.update.behind");
                            doUpdate = true;
                            newVer = laterV;
                            return -1;
                        }
                        case 0 -> {
                            // UP-TO-DATE
                            MinecachingAPI.tInfo("plugin.update.up_to_date");
                            return 0;
                        }
                        case 1 -> {
                            // AHEAD
                            MinecachingAPI.tInfo("plugin.update.ahead");
                            return 1;
                        }
                    }
                }

            }
        } catch (Exception e) {
            MinecachingAPI.tWarning("plugin.update.failcheck");
            e.printStackTrace();
        }

        return 10000;
    }
}
