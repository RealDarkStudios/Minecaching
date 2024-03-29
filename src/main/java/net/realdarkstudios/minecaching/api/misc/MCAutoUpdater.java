package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.Minecaching;

import java.io.IOException;

public class MCAutoUpdater extends AutoUpdater {
    public MCAutoUpdater() {
        super(Minecaching.getInstance(),
                "https://maven.digitalunderworlds.com/%b%s/net/realdarkstudios/minecaching/maven-metadata.xml",
                "https://maven.digitalunderworlds.com/%b%s/net/realdarkstudios/minecaching/%v%/minecaching-%v%.jar");
        setParser(this::parseMavenMetadata);
    }

    @Override
    public void ioExceptionHandler(IOException ioexc) {
        ioexc.printStackTrace();
    }

    @Override
    public void statusBehind(String newerVersion) {

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
