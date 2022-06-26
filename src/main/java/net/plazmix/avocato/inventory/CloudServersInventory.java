package net.plazmix.avocato.inventory;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.plazmix.avocato.utility.CloudNetUtil;
import net.plazmix.inventory.impl.BasePaginatedInventory;
import net.plazmix.utility.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CloudServersInventory extends BasePaginatedInventory {

    String templateName;

    public CloudServersInventory(String template) {
        super("Servers by " + template, 5);
        this.templateName = template;
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

                (player1, inventoryClickEvent) -> player.performCommand("pcloud"));

        setClickItem(42, ItemUtil.newBuilder(Material.SKULL_ITEM)
                        .setName("§7Обновить сервера")

                        .addLore("")
                        .addLore("§eНажмите, чтобы обновить список серверов")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg4N2NjMzg4YzhkY2ZjZjFiYThhYTVjM2MxMDJkY2U5Y2Y3YjFiNjNlNzg2YjM0ZDRmMWMzNzk2ZDNlOWQ2MSJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> updateInventory(player));

        setClickItem(5, ItemUtil.newBuilder(Material.DIAMOND_SWORD)
                        .setName("§7Создать сервер с типом: §e" + templateName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы создать сервер")

                        .build(),

                (player1, inventoryClickEvent) -> {

                    player.sendMessage("§d§lCloud §8:: §fИдёт создание сервера...");
                    player.performCommand("pcloud");

                    CloudNetUtil.createCloudServiceByTask(templateName);

                    player.sendMessage("§d§lCloud §8:: §fСервер §aуспешно §fсоздан!");

                });

        Collection<ServiceInfoSnapshot> template = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServicesByGroup(templateName);
        for (ServiceInfoSnapshot serviceInfoSnapshot : template) {

            String serverName = serviceInfoSnapshot.getServiceId().getTaskName() + "-" + serviceInfoSnapshot.getServiceId().getTaskServiceId();

            if (ServiceInfoSnapshotUtil.isIngameService(serviceInfoSnapshot)) {

                MotdType motd = MotdType.getMotdStatus(serverName);
                addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)
                                .setName("§7Сервер: §a" + serverName)

                                .addLore("§fСтатус: " + motd.chatColor + motd.name)
                                .addLore("§fКласстер: §e" + serviceInfoSnapshot.getServiceId().getNodeUniqueId())
                                .addLore("§fОнлайн: §b" + ServiceInfoSnapshotUtil.getOnlineCount(serviceInfoSnapshot) + "/" + ServiceInfoSnapshotUtil.getMaxPlayers(serviceInfoSnapshot))
                                .addLore("")
                                .addLore("§eНажмите, чтобы управлять сервером!")

                                .setDurability(3)
                                .setTextureValue(motd.skull)

                                .build(),

                        (player1, inventoryClickEvent) -> new ServersInventory(serviceInfoSnapshot.getServiceId().getTaskName() + "-" + serviceInfoSnapshot.getServiceId().getTaskServiceId()).openInventory(player));
            }

            if (ServiceInfoSnapshotUtil.isFullService(serviceInfoSnapshot) && serviceInfoSnapshot.getServiceId().getEnvironment().isMinecraftProxy()) {

                MotdType motd = MotdType.getMotdStatus(serviceInfoSnapshot.getServiceId().getTaskName() + "-" + serviceInfoSnapshot.getServiceId().getTaskServiceId());
                addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)
                                .setName("§7Сервер: §a" + serviceInfoSnapshot.getServiceId().getTaskName() + "-" + serviceInfoSnapshot.getServiceId().getTaskServiceId())

                                .addLore("§fСтатус: " + motd.chatColor + motd.name)
                                .addLore("")
                                .addLore("§eНажмите, чтобы управлять сервером!")

                                .setDurability(3)
                                .setTextureValue(motd.skull)

                                .build(),

                        (player1, inventoryClickEvent) -> new ServersInventory(serviceInfoSnapshot.getServiceId().getTaskName() + "-" + serviceInfoSnapshot.getServiceId().getTaskServiceId()).openInventory(player));
            }

        }

        Collection<ServiceInfoSnapshot> templateServers = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(templateName);
        for (ServiceInfoSnapshot serviceInfoSnapshot : templateServers) {

            String serverName = serviceInfoSnapshot.getServiceId().getTaskName() + "-" + serviceInfoSnapshot.getServiceId().getTaskServiceId();
            String online = ServiceInfoSnapshotUtil.getOnlineCount(serviceInfoSnapshot) + "/" + ServiceInfoSnapshotUtil.getMaxPlayers(serviceInfoSnapshot);

            if (ServiceInfoSnapshotUtil.isOnline(serviceInfoSnapshot)) {

                MotdType motd = MotdType.getMotdStatus(serverName);
                addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)
                                .setName("§7Сервер: §a" + serverName)

                                .addLore("§fСтатус: " + motd.chatColor + motd.name)
                                .addLore("§fКласстер: §e" + serviceInfoSnapshot.getServiceId().getNodeUniqueId())
                                .addLore("§fОнлайн: §b" + online)
                                .addLore("")
                                .addLore("§eНажмите, чтобы управлять сервером!")

                                .setDurability(3)
                                .setTextureValue(motd.skull)

                                .build(),

                        (player1, inventoryClickEvent) -> new ServersInventory(serverName).openInventory(player));
            }

            if (ServiceInfoSnapshotUtil.isStartingService(serviceInfoSnapshot)) {

                MotdType motd = MotdType.getMotdStatus(serverName);
                addClickItemToMarkup(ItemUtil.newBuilder(Material.SKULL_ITEM)
                                .setName("§7Сервер: §a" + serverName)

                                .addLore("§fСтатус: " + motd.chatColor + motd.name)
                                .addLore("§fКласстер: §e" + serviceInfoSnapshot.getServiceId().getNodeUniqueId())
                                .addLore("§fОнлайн: §b" + online)
                                .addLore("")
                                .addLore("§eНажмите, чтобы управлять сервером!")

                                .setDurability(3)
                                .setTextureValue(motd.skull)

                                .build(),

                        (player1, inventoryClickEvent) -> new ServersInventory(serverName).openInventory(player));
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    private enum MotdType {

        STARTING(ChatColor.WHITE, "Запускается", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDYzYTRlOTg4MmYxODkyMzNmOWJmMTEyMWI5NmI3OTliYjNmZDM3NzA1NGYzOWRkMTlhNWQ4M2JlMDFmZjYifX19"),
        WAIT(ChatColor.GREEN, "Запущен", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODUyODQ5YjY0NWZjZTg2YWNiZDIzMTk5MWJhOTBkMjBkYTQyZTNhOWE4YjU4ZTc2ZGQ0ZTM4NmRiZThiIn19fQ=="),
        INGAME(ChatColor.GOLD, "В игре", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBmYjRlNmE2MTMyMmE0MTg1ZTdiNDFhNjQzZjZhNGNiNjlhMzE3ZWJlMzkzOTZmYjI3NjI3ZTM2MTRjNjkifX19"),
        FULL(ChatColor.DARK_PURPLE, "Заполнен", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiMDdlNzEyNThkYjhmZTI0MWJjNjU5ZTg3NDk0MWNjYjNkMTJjNzVlNDgyZWE5YjUwYTA0OGU2Njc3YTMifX19"),
        OFFLINE(ChatColor.RED, "Отключён", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFjNTAxM2IyMjdhNDk0ZWMxNmM0ZWFmYWIxZjg0ZGVhNTcyZWZiMmYzYzRiNWMzZmY4ODI1MmYzNTZhMDJiIn19fQ=="),
        PREPARED(ChatColor.GRAY, "Подготовлен", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTUyODhkZGM5MTFhNzVmNzdjM2E1ZDMzNjM2NWE4ZjhiMTM5ZmE1MzkzMGI0YjZlZTEzOTg3NWM4MGNlMzY2YyJ9fX0="),

        UNKNOWN(null, "§cНе найден", "");

        private final ChatColor chatColor;
        private final String name;
        private final String skull;

        public static MotdType getMotdStatus(@NonNull String serverName) {
            ServiceInfoSnapshot serviceInfoSnapshot = CloudNetUtil.getServiceByName(serverName);

            if (ServiceInfoSnapshotUtil.isFullService(serviceInfoSnapshot)) {
                return MotdType.FULL;
            }

            if (ServiceInfoSnapshotUtil.isIngameService(serviceInfoSnapshot)) {
                return MotdType.INGAME;
            }

            if (ServiceInfoSnapshotUtil.isStartingService(serviceInfoSnapshot)) {
                return MotdType.STARTING;
            }

            if (ServiceInfoSnapshotUtil.getState(serviceInfoSnapshot).startsWith("PREPARED")) {
                return MotdType.PREPARED;
            }

            if (ServiceInfoSnapshotUtil.isOnline(serviceInfoSnapshot)) {
                return MotdType.WAIT;

            } else {

                return MotdType.OFFLINE;
            }

        }
    }
}
