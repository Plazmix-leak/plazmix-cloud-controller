package net.plazmix.avocato.inventory;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.ext.bridge.player.ServicePlayer;
import net.plazmix.avocato.utility.CloudNetUtil;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.inventory.impl.BasePaginatedInventory;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayersListInventory extends BasePaginatedInventory {

    String serverName;

    public PlayersListInventory(String serverName) {
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
                        .addLore("§eНажмите, чтобы обновить список игроков")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg4N2NjMzg4YzhkY2ZjZjFiYThhYTVjM2MxMDJkY2U5Y2Y3YjFiNjNlNzg2YjM0ZDRmMWMzNzk2ZDNlOWQ2MSJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> updateInventory(player));

        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetUtil.getServiceByName((serverName));
        if (ServiceInfoSnapshotUtil.getTaskOnlineCount(serviceInfoSnapshot.getServiceId().getTaskName()) == 0) {
            setOriginalItem(23, ItemUtil.newBuilder(Material.GLASS_BOTTLE)
                    .setName("§cУпс, ничего не найдено :c")
                    .build());
            return;
        }

        serviceInfoSnapshot.getProperty(BridgeServiceProperty.PLAYERS).ifPresent(players -> {
            for (ServicePlayer servicePlayer : players) {

                PlazmixUser plazmixUser = PlazmixUser.of(servicePlayer.getName());
                addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§fНик: " + plazmixUser.getDisplayName())

                        .addLore("§fСтатус: " + plazmixUser.getGroup().getColouredName())
                        .addLore("")
                        .addLore("§fМонет: §a" + plazmixUser.getCoins())
                        .addLore("§fПлазмы: §d" + plazmixUser.getGolds())
                        .addLore("§fУровень: §e" + plazmixUser.getLevel())
                        .addLore("")
                        .addLore("§fСервер: §7" + serverName)
                        .addLore("")
                        .addLore("§eНажмите, чтобы телепортироваться к игроку.")

                        .setDurability(3)
                        .setPlayerSkull(servicePlayer.getName())

                        .build(),

                ((player1, inventoryClickEvent) -> PlazmixCoreApi.redirect(player, serverName)));
            }
        });
    }
}
