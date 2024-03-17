package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoUpdater {
    private static String readURL = "https://maven.digitalunderworlds.com/" + Config.getInstance().getUpdateBranch() + "s/net/realdarkstudios/Minecaching/maven-metadata.xml";
    private static String newVer = "", branch = "";
    private static boolean doUpdate = false;
    private static int lastUpdateCheck = 10000;

    /**
     * Updates the branch of the readURL
     * @param newBranch which branch to use
     * @since 0.3.0.1
     */
    public static void updateBranch(String newBranch) {
        branch = newBranch;
        readURL = "https://maven.digitalunderworlds.com/" + newBranch + "s/net/realdarkstudios/Minecaching/maven-metadata.xml";
    }

    /**
     * Check if there is an update that needs to be downloaded/installed
     * @return {@code true} if there is a newer version available for download, {@code false} if not
     * @since 0.3.0.0
     */
    public static boolean hasUpdate() {
        return doUpdate;
    }

    /**
     * Gets the new version string
     * @return The newest version
     * @since 0.3.0.0
     */
    public static String getNewestVersion() {
        return newVer;
    }

    public static int getLastCheckResult() {
        return lastUpdateCheck;
    }

    /**
     * Checks for the latest Minecaching version using the maven.
     * @return -1 if plugin version is BEHIND, 0 if it is UP-TO-DATE, 1 if it is AHEAD, 10000 for ERROR
     * @since 0.3.0.1
     */
    public static void checkForUpdate() {
        final int[] result = new int[1];
        new BukkitRunnable() {
            @Override
            public void run() {
                updateBranch(Config.getInstance().getUpdateBranch());
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
                                    if (!line.toLowerCase().contains("snapshot") || branch.equals("snapshot")) {
                                        versionList.append(versionParse(line));
                                    }
                                } else break;
                            }

                            // Put all versions into an array
                            String[] versionsA = versionList.toString().split("<version>");
                            // The first version is an empty string, so we remove it
                            versionsA = Arrays.copyOfRange(versionsA, 1, versionsA.length);

                            // Sort accoring to ModuleDescriptor.Version::parse
                            List<ModuleDescriptor.Version> versions = new ArrayList<>();

                            // Will continue to next version if one fails to parse
                            for (String v: versionsA) {
                                try {
                                    versions.add(ModuleDescriptor.Version.parse(v));
                                } catch (IllegalArgumentException ignored) {
                                }
                            }

                            // Get the most recent version
                            String laterV = versions.get(versions.size() - 1).toString();
                            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.LATEST, branch, laterV);

                            // Compare laterV to plugin version
                            switch (ModuleDescriptor.Version.parse(versionParse(Minecaching.getVersion()).replace("<version>", "")).compareTo(ModuleDescriptor.Version.parse(laterV))) {
                                case -1 -> {
                                    // BEHIND
                                    MinecachingAPI.tInfo(MessageKeys.Plugin.Update.STATUS_BEHIND);
                                    doUpdate = true;
                                    newVer = laterV;
                                    lastUpdateCheck = -1;
                                    result[0] = -1;
                                }
                                case 0 -> {
                                    // UP-TO-DATE
                                    MinecachingAPI.tInfo(MessageKeys.Plugin.Update.STATUS_UP_TO_DATE);
                                    lastUpdateCheck = 0;
                                    result[0] = 0;
                                }
                                case 1 -> {
                                    // AHEAD
                                    MinecachingAPI.tInfo(MessageKeys.Plugin.Update.STATUS_AHEAD);
                                    lastUpdateCheck = 1;
                                    result[0] = 1;
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    MinecachingAPI.tWarning(MessageKeys.Plugin.Update.FAIL_TO_CHECK);
                    e.printStackTrace();
                }

                result[0] = 10000;
            }
        }.runTaskAsynchronously(Minecaching.getInstance());

        lastUpdateCheck = result[0];
    }

    private static String versionParse(String line) {
        String l = line.replace("</version>", "").replace("<version>", "").trim();

        // handles conversion to the older format (X.X.X.X-SNAPSHOT-X) so that im comparing apples to apples
        if (l.startsWith("snapshot-")) {
            // handle specific case (snapshot-24w11b)
            if (l.equals("snapshot-24w11b")) l = "0.3.1.0-snapshot-24w11b";
            else {
                // example: snapshot-0.3.1.0-24w11c
                String[] lparts = l.split("-");
                l = String.format("%s-%s-%s", lparts[1], lparts[0], lparts[2]);
            }
        } else if (l.equals("0.3.1.0-24w10a")) l = "0.3.1.0-snapshot-24w10a";
        else if (l.equals("0.3.1.0-24w11a")) l = "0.3.1.0-snapshot-24w11a";

        return "<version>" + l;
    }

    /**
     * Converts a version to the representation used by the AutoUpdater
     * @param versionToParse The string to parse as a version
     * @return A string with the format of how the AutoUpdate compares versions
     */
    public static String parseVersion(String versionToParse) {
        return versionParse(versionToParse).replace("<version>", "");
    }
}
