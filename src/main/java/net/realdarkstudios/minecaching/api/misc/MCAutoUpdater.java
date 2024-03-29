package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.commons.util.AutoUpdater;
import net.realdarkstudios.commons.util.Version;
import net.realdarkstudios.minecaching.api.Minecaching;

import java.util.HashMap;
import java.util.Map;

public class MCAutoUpdater extends AutoUpdater {
    public MCAutoUpdater() {
        super(Minecaching.getInstance(),
                "https://maven.digitalunderworlds.com/%b%s/net/realdarkstudios/minecaching/maven-metadata.xml",
                "https://maven.digitalunderworlds.com/%b%s/net/realdarkstudios/minecaching/%v%/minecaching-%v%.jar",
                Parser.MAVEN_METADATA);

        Map<String, String> replacements = new HashMap<>();

        replacements.put("0.3.1.0-24w10a", "0.3.1.0-snapshot-24w10a");
        replacements.put("snapshot-24w11a", "0.3.1.0-snapshot-24w11a");
        replacements.put("snapshot-24w11b", "0.3.1.0-snapshot-24w11b");

        setReplacements(replacements);
    }

    @Override
    protected void statusBehind(Version version, String s) {

    }

    @Override
    public void statusUpToDate() {

    }

    @Override
    public void statusAhead() {

    }

    @Override
    public void errorChecking() {

    }
}
