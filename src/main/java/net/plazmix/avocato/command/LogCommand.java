package net.plazmix.avocato.command;

import net.plazmix.avocato.utility.CloudNetUtil;
import net.plazmix.command.BaseCommand;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.coreconnector.core.group.Group;
import org.bukkit.entity.Player;

public class LogCommand extends BaseCommand<Player> {

    public LogCommand() {
        super("log", "logs", "лог", "getlog", "getlogs", "plzmlog", "plog", "serlog", "plg");

        setMinimalGroup(Group.QA);
    }

    @Override
    protected void onExecute(Player player, String[] args) {
        if (args.length < 1) {
            CloudNetUtil.getLogsByServiceName(player, PlazmixCoreApi.getCurrentServerName());

        } else {

            if (CloudNetUtil.getServiceByName(args[0]) == null) {
                player.sendMessage("§d§lCloud §8:: §cОшибка, сервера с названием: §a" + args[0] + " §cне существует!");
                return;
            }

            CloudNetUtil.getLogsByServiceName(player, args[0]);
        }
    }
}
