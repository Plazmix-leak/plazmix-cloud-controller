package net.plazmix.avocato.command;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import net.plazmix.avocato.inventory.CloudServersInventory;
import net.plazmix.avocato.utility.AlphabetUtil;
import net.plazmix.command.BaseCommand;
import net.plazmix.coreconnector.core.group.Group;
import net.plazmix.inventory.impl.BasePaginatedInventory;
import net.plazmix.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CloudCommand extends BaseCommand<Player> {

    public CloudCommand() {
        super("pcloud", "plazmixcloud", "pcl", "plzm", "cl");

        setMinimalGroup(Group.DEVELOPER);
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        new CloudInventory().openInventory(player);
    }

    public static class CloudInventory extends BasePaginatedInventory {

        public CloudInventory() {
            super("Выбор типов серверов", 5);
        }

        @Override
        public void drawInventory(Player player) {
            addRowToMarkup(2, 1);
            addRowToMarkup(3, 1);
            addRowToMarkup(4, 1);

            setClickItem(41, ItemUtil.newBuilder(Material.SKULL_ITEM)
                            .setName("§7Обновить список")

                            .addLore("")
                            .addLore("§eНажмите, чтобы обновить список шаблонов")

                            .setDurability(3)
                            .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg4N2NjMzg4YzhkY2ZjZjFiYThhYTVjM2MxMDJkY2U5Y2Y3YjFiNjNlNzg2YjM0ZDRmMWMzNzk2ZDNlOWQ2MSJ9fX0=")

                            .build(),

                    (player1, inventoryClickEvent) -> updateInventory(player));

            for (ServiceTask serviceTask : CloudNetDriver.getInstance().getServiceTaskProvider().getPermanentServiceTasks()) {
                addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)
                                .setDurability(3)
                                .setTextureValue(AlphabetUtil.getTextureByString(serviceTask.getName()))

                                .setName("§7Сервера по префиксу: §a" + serviceTask.getName())

                                .addLore("§fСерверов запущено: §a" + CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(serviceTask.getName()).size())
                                .addLore("§fНачальный порт: §e" + serviceTask.getStartPort())
                                .addLore("")
                                .addLore("§fСтатичный: " + (serviceTask.isStaticServices() ? "§aДа" : "§cНет"))
                                .addLore("§fУдалятеся при остановке: " + (serviceTask.isAutoDeleteOnStop() ? "§aДа" : "§cНет"))
                                .addLore("§fПерезапись IP: " + (serviceTask.isDisableIpRewrite() ? "§aДа" : "§cНет"))
                                .addLore("")
                                .addLore("§eНажмите, чтобы перейти к списку серверов!")

                                .build(),
                        (player1, inventoryClickEvent) -> new CloudServersInventory(serviceTask.getName()).openInventory(player));
            }
        }
    }
}
