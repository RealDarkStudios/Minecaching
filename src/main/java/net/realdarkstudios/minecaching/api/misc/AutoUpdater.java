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

    public static void updateBranch(String branch) {
        readURL = "https://maven.digitalunderworlds.com/" + branch + "s/net/realdarkstudios/Minecaching/maven-metadata.xml";
    }

    public static void startCheck() {
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
    }

    public static boolean hasUpdate() {
        return doUpdate;
    }

    public static String getNewVer() {
        return newVer;
    }

    public static int startExperimentalCheck() {
        StringBuilder versionList = new StringBuilder();

        try {
            URL url = new URL(readURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = br.readLine()) != null) {
                String line = str;

                if (line.contains("<versions>")) {
                    while ((str = br.readLine()) != null) {
                        line = str;
                        if (!line.contains("</versions>")) {
                            if (!line.contains("SNAPSHOT") || Config.getInstance().getUpdateBranch().equals("snapshot")) versionList.append(line.trim().replace("</version>", ""));
                        } else break;
                    }

                    String[] versionsA = versionList.toString().split("<version>");
                    versionsA = Arrays.copyOfRange(versionsA, 1, versionsA.length);

                    List<ModuleDescriptor.Version> versions = Stream.of(versionsA).map(ModuleDescriptor.Version::parse).sorted().toList();
                    String laterV = versions.get(versions.size() - 1).toString();
                    MinecachingAPI.tInfo("plugin.update.latest", Config.getInstance().getUpdateBranch(), laterV);

                    switch (ModuleDescriptor.Version.parse(Minecaching.getInstance().getDescription().getVersion()).compareTo(ModuleDescriptor.Version.parse(laterV))) {
                        case -1 -> {
                            MinecachingAPI.tInfo("plugin.update.behind");
                            doUpdate = true;
                            newVer = laterV;
                            return -1;
                        }
                        case 0 -> {
                            MinecachingAPI.tInfo("plugin.update.up_to_date");
                            return 0;
                        }
                        case 1 -> {
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
