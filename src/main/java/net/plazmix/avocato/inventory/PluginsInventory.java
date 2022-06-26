package net.plazmix.avocato.inventory;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.PluginInfo;
import net.plazmix.inventory.impl.BasePaginatedInventory;
import net.plazmix.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PluginsInventory extends BasePaginatedInventory {

    String serverName;

    public PluginsInventory(String serverName) {
        super(serverName, 5);
        this.serverName = serverName;
    }

    @Override
    public void drawInventory(Player player) {
        addRowToMarkup(2, 1);
        addRowToMarkup(3, 1);
        addRowToMarkup(4, 1);

        setClickItem(41, ItemUtil.newBuilder(Material.SKULL_ITEM)
                        .setName("§cНазад")

                        .addLore("")
                        .addLore("§c▶ Нажмите, чтобы вернуться в предыдущее меню.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYyNTkwMmIzODllZDZjMTQ3NTc0ZTQyMmRhOGY4ZjM2MWM4ZWI1N2U3NjMxNjc2YTcyNzc3ZTdiMWQifX19")

                        .build(),

                (player1, inventoryClickEvent) -> new ServersInventory(serverName).openInventory(player));

        setClickItem(42, ItemUtil.newBuilder(Material.SKULL_ITEM)
                        .setName("§7Обновить список")

                        .addLore("")
                        .addLore("§eНажмите, чтобы обновить список плагинов")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg4N2NjMzg4YzhkY2ZjZjFiYThhYTVjM2MxMDJkY2U5Y2Y3YjFiNjNlNzg2YjM0ZDRmMWMzNzk2ZDNlOWQ2MSJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> updateInventory(player));

        CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(serverName).getProperty(BridgeServiceProperty.PLUGINS).ifPresent(pluginInfos -> {
            for (PluginInfo pluginInfo : pluginInfos) {

                if (pluginInfo.getName().startsWith("Plazmix")) {
                    addOriginalItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)

                            .setName("§7Плагин §a" + pluginInfo.getName())

                            .addLore("§fНазвание: §e" + pluginInfo.getName())
                            .addLore("§fВерсия: §e" + pluginInfo.getVersion())

                            .setDurability(3)
                            .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBjNTUzYWU2ZGZjZGYxMzhkZGY4MjA0MmQyMTEzZmIyZjI4ZjlmYWJhMWU4OGIyZDhjMGM5OGQ2YzFmOWYifX19")

                            .build());
                } else {

                    addOriginalItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)

                            .setName("§7Плагин §a" + pluginInfo.getName())

                            .addLore("§fНазвание: §e" + pluginInfo.getName() + " §7(§a" + pluginInfo.getVersion() + "§7)")

                            .setDurability(3)
                            .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk4NWNkMjJlNjI4OTc4MzQ5NDNlZDVkNGQxZmEwZDlhOGIxM2I5ZWVmZWY1NmY1MzE0MTllM2IxMTc0NmMifX19")

                            .build());
                }
            }
        });
    }
}
