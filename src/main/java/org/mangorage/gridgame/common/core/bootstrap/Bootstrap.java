package org.mangorage.gridgame.common.core.bootstrap;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.mangorage.gridgame.client.Client;
import org.mangorage.gridgame.client.Renderers;
import org.mangorage.gridgame.common.Events;
import org.mangorage.gridgame.common.events.LoadEvent;
import org.mangorage.gridgame.common.events.RegisterEvent;
import org.mangorage.gridgame.common.events.RegisterRenderersEvent;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.server.Server;
import org.mangorage.mangonetwork.Packets;

public class Bootstrap {
    private static boolean loaded = false;

    public static void main(String[] args) throws ParseException, InterruptedException {

        if (loaded) {
            System.out.println("Already loaded everything...");
            return;
        }
        loaded = true;


        Options options = new Options();

        // Add options
        Option serverOption = new Option("s", "server", false, "Use Server");
        Option useIntegrated = new Option("i", "integrated", false, "Use Integrated");


        options.addOption(serverOption);
        options.addOption(useIntegrated);

        CommandLineParser parser = new DefaultParser();
        var result = parser.parse(options, args);

        Packets.init();
        TileRegistry.init();

        Events.LOAD_EVENT.trigger(new LoadEvent());
        Events.REGISTER_EVENT.trigger(new RegisterEvent());

        boolean server = result.hasOption(serverOption.getOpt());
        boolean integrated = result.hasOption(useIntegrated.getOpt());

        if (!server || integrated) {
            Events.REGISTER_RENDERERS.trigger(new RegisterRenderersEvent());
            Renderers.init();
        }

        if (integrated) {
            System.out.println("Starting Server/Client");
            Server.init();
            Thread.sleep(3000);
            Client.init();
        } else {
            if (server) {
                System.out.println("Starting Server");
                Server.init();
            } else {
                System.out.println("Starting Client");
                var IP = MenuUtils.showInputDialog("IP for Server");
                if (IP == null || IP.isBlank()) {
                    System.out.println("Closing Program...");
                    System.exit(0);
                } else {
                    Client.init(IP);
                }
            }
        }
    }
}
