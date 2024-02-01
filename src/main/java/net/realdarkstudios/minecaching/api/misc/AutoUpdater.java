package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.util.List;

public class AutoUpdater {
    private static String readURL = "https://maven.digitalunderworlds.com/" + Config.getInstance().getUpdateBranch() + "s/net/realdarkstudios/Minecaching/maven-metadata.xml";
    private static boolean doUpdate = false;
    private static String newVer = "";

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
}
