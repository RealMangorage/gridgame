package org.mangorage.gridgame.core.classloading;

import org.mangorage.gridgame.core.events.Events;
import org.mangorage.gridgame.core.events.LoadCompleteEvent;
import org.mangorage.gridgame.core.events.RegistryEvent;
import org.mangorage.gridgame.core.events.TileRenderRegisterEvent;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.core.registry.Registries;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Start {

    public static Set<Class<?>> findExternalAnnotatedClasses(Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(
                        ClasspathHelper.forClassLoader(
                            Thread.currentThread().getContextClassLoader(),
                            Thread.currentThread().getContextClassLoader().getParent()
                        )
                )
                .addClassLoader(
                        Thread.currentThread().getContextClassLoader()
                )

        );
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static Set<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections("");
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public Start() throws InterruptedException {
        findAnnotatedClasses(EntryPoint.class).forEach(clz -> {
            try {
                clz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        findExternalAnnotatedClasses(EntryPoint.class).forEach(clz -> {
            try {
                clz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        Events.REGISTRY_EVENT.trigger(new RegistryEvent());
        Registries.init();

        Events.REGISTER_TILE_RENDERER_EVENT.trigger(new TileRenderRegisterEvent());
        Events.LOAD_COMPLETE_EVENT.trigger(new LoadCompleteEvent());
        Game.init();

        Thread.sleep(100000);
    }
}
