package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
                                    if (!line.contains("SNAPSHOT") || branch.equals("snapshot")) versionList.append(line.trim().replace("</version>", ""));
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
                            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.LATEST, branch, laterV);

                            // Compare laterV to plugin version
                            switch (ModuleDescriptor.Version.parse(Minecaching.getInstance().getDescription().getVersion()).compareTo(ModuleDescriptor.Version.parse(laterV))) {
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
                } catch (Exception e) {
                    MinecachingAPI.tWarning(MessageKeys.Plugin.Update.FAIL_TO_CHECK);
                    e.printStackTrace();
                }

                result[0] = 10000;
            }
        }.runTaskAsynchronously(Minecaching.getInstance());

        lastUpdateCheck = result[0];
    }
}
