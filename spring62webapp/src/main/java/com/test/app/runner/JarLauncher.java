package com.test.app.runner;

import org.springframework.boot.loader.ExecutableArchiveLauncher;
import org.springframework.boot.loader.Launcher;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.ExplodedArchive;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * {@link Launcher} for JAR based archives. This launcher assumes that dependency jars are
 * included in a {@code /WEB-INF/lib}, that application classes are included
 * in a {@code /WEB-INF/classes} directory and additional libraries are stored in a lib directory
 * placed in the same root directory like this application.
 */
public class JarLauncher extends ExecutableArchiveLauncher {

    private static final String WEB_INF_CLASSES = "WEB-INF/classes/";
    private static final String WEB_INF_LIB = "WEB-INF/lib/";
    private static final String JAR_SUFFIX = ".jar";
    private static final String ADDITIONAL_LIB = "lib/";

    public JarLauncher() {
    }

    protected JarLauncher(Archive archive) {
        super(archive);
    }

    public static void main(String[] args) throws Exception {
        JarLauncher jarLauncher = new JarLauncher();
        jarLauncher.launch(args);
    }

    @Override
    protected boolean isNestedArchive(Archive.Entry entry) {
        if (entry.isDirectory()) {
            return WEB_INF_CLASSES.equals(entry.getName());
        }
        return entry.getName().startsWith(WEB_INF_LIB);
    }

    @Override
    protected void postProcessClassPathArchives(List<Archive> archives) throws Exception {
        archives.add(0, getArchive());
        archives.addAll(getAdditionalArchives());
    }

    private static boolean createDirIfAbsent(File dir) {
        boolean created = true;
        if (!dir.exists()) {
            created = dir.mkdirs();
        }
        return created;
    }
    private List<Archive> getAdditionalArchives() throws IOException, URISyntaxException {
        File libDir = new File(this.getClass().getResource("/").toURI().getPath() + ADDITIONAL_LIB);
        List<Archive> archives = Collections.emptyList();
        if (createDirIfAbsent(libDir)) {
            Iterator<Archive> nestedArchives = new ExplodedArchive(libDir).getNestedArchives(entry -> entry.getName().endsWith(JAR_SUFFIX), null);
            archives = new ArrayList<>();
            nestedArchives.forEachRemaining(archives::add);
        }
        return archives;
    }
}
