package org.mangorage.gridgame.core.classloading;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Init {
    private static final String MAIN_CLASS = "org.mangorage.gridgame.core.classloading.Start";

    public static void main(String[] args) throws IOException {
        var loader = Thread.currentThread().getContextClassLoader();

        Path mods = Path.of("mods");
        if (!Files.exists(mods))
            Files.createDirectory(mods);

        URL[] modsURL = Files.find(mods, 1, (p, a) -> p.toString().endsWith(".jar")).map(p -> {
            try {
                return p.toUri().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(URL[]::new);

        for (URL url : modsURL) {
            System.out.println("Found %s mod".formatted(url.getFile()));
        }

        try (var newLoader = new URLClassLoader(modsURL, loader)) {
            Thread.currentThread().setContextClassLoader(newLoader);
            newLoader.loadClass(MAIN_CLASS).getConstructor().newInstance();
            Thread.currentThread().setContextClassLoader(loader); // Restore to old loader...
        } catch (IOException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        System.out.println("UH OH!");
    }
}
