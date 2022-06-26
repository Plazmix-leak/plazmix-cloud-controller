package net.plazmix.avocato.command;

import net.plazmix.avocato.inventory.PluginsInventory;
import net.plazmix.avocato.utility.CloudNetUtil;
import net.plazmix.command.BaseCommand;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.coreconnector.core.group.Group;
import org.bukkit.entity.Player;

public class PluginsCommand extends BaseCommand<Player> {

    public PluginsCommand() {
        super("pzlmplugin","pzlmplugins", "plzmp");

        setMinimalGroup(Group.DEVELOPER);
    }

    @Override
    protected void onExecute(Player player, String[] args) {

        if (args.length < 1) {
            new PluginsInventory(PlazmixCoreApi.getCurrentServerName()).openInventory(player);

        } else {

            if (CloudNetUtil.getServiceByName(args[0]) == null) {
                player.sendMessage("§d§lCloud §8:: §cОшибка, сервера с названием: §a" + args[0] + " §cне существует!");
                return;
            }

            new PluginsInventory(args[0]).openInventory(player);
        }
    }
}
