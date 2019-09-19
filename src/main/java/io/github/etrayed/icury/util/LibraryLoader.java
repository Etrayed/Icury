package io.github.etrayed.icury.util;

import io.github.etrayed.icury.Icury;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Etrayed
 */
public class LibraryLoader {

    public static final Dependency[] DEPENDENCIES = {
            new Dependency("org.jcodec", "jcodec", "0.2.5"),
            new Dependency("org.jcodec", "jcodec-javase", "0.2.5"),
            new Dependency("org.mongodb", "mongo-java-driver", "3.11.0"),
            new Dependency("com.j256.ormlite", "ormlite-core", "5.1"),
            new Dependency("com.j256.ormlite", "ormlite-jdbc", "5.1"),
            new Dependency("com.sun.xml.fastinfoset", "FastInfoset", "1.2.16")
    };

    private static final Method ADD_URL_METHOD;

    static {
        try {
            Path libPath = Paths.get("plugins/Icury/lib/");

            if(Files.notExists(libPath)) Files.createDirectory(libPath);

            ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

            ADD_URL_METHOD.setAccessible(true);
        } catch (NoSuchMethodException | IOException e) {
            throw new InternalError(e);
        }
    }

    public static void loadAll() {
        Icury.getLogger().info("Loading dependencies...");

        long fullTimestamp = System.currentTimeMillis();

        for (Dependency dependency : DEPENDENCIES) {
            try {
                if(Files.notExists(dependency.path)) {
                    Icury.getLogger().info("Downloading " + dependency.mavenRepositoryURL);

                    long timestamp = System.currentTimeMillis();

                    InputStream inputStream = dependency.mavenRepositoryURL.openStream();

                    Files.copy(inputStream, dependency.path);

                    inputStream.close();

                    Icury.getLogger().info("Finished download. Took " + (System.currentTimeMillis() - timestamp) + "ms.");
                }

                ADD_URL_METHOD.invoke(ClassLoader.getSystemClassLoader(), dependency.path.toUri().toURL());

                Icury.getLogger().info("Loaded library " + dependency.toString() + " into runtime.");
            } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                e.printStackTrace();
            }
        }

        Icury.getLogger().info(DEPENDENCIES.length + " dependencies loaded successfully. Took "
                + (System.currentTimeMillis() - fullTimestamp) + "ms.");
    }

    private static final class Dependency {

        private final String string;

        private final URL mavenRepositoryURL;

        private final Path path;

        private Dependency(String groupId, String artifactId, String version) {
            try {
                this.string = groupId + ":" + artifactId + ":" + version;

                this.mavenRepositoryURL =  new URL("http://repo.maven.apache.org/maven2/" + groupId.replaceAll("\\.",
                        "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar");

                this.path = Paths.get("plugins/Icury/lib/" + artifactId + "-" + version + ".jar");
            } catch (MalformedURLException e) {
                throw new InternalError(e);
            }
        }

        @Override
        public String toString() {
            return string;
        }
    }
}
