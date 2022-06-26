package net.plazmix.avocato.inventory;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import net.plazmix.avocato.utility.CloudNetUtil;
import net.plazmix.avocato.utility.HastebinUtil;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.inventory.impl.BaseSimpleInventory;
import net.plazmix.utility.DateUtil;
import net.plazmix.utility.ItemUtil;
import net.plazmix.utility.JsonUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;

public class ServersInventory extends BaseSimpleInventory {

    String serverName;

    public ServersInventory(String serverName) {
        super(serverName, 6);
        this.serverName = serverName;
    }

    @Override
    public void drawInventory(Player player) {

        ServiceInfoSnapshot service = CloudNetUtil.getServiceByName(serverName);

        double percentUsageDouble = service.getProcessSnapshot().getCpuUsage();
        int percentUsage = (int) percentUsageDouble;

        String online = ServiceInfoSnapshotUtil.getOnlineCount(service) + "/" + ServiceInfoSnapshotUtil.getMaxPlayers(service);
        String usageMemory = service.getProcessSnapshot().getHeapUsageMemory() / 1048576 + "§7/§c" + service.getProcessSnapshot().getMaxHeapMemory() / 1048576 + " §fМБ";

        setOriginalItem(5, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Информация о сервере §a" + serverName)

                        .addLore("§fЗадача: §d" + service.getServiceId().getTaskName())
                        .addLore("§fГруппы: §2" + Arrays.toString(service.getConfiguration().getGroups()))
                        .addLore("§fОнлайн: §b" + online)
                        .addLore("§fПорт: §9" + service.getAddress().getPort())
                        .addLore("")
                        .addLore("§fКласстер: §e" + service.getServiceId().getNodeUniqueId())
                        .addLore("§fИспользовано CPU: §a" + percentUsage + "§7/§c100§7%")
                        .addLore("§fИспользуется памяти: §a" + usageMemory)
                        .addLore("")
                        .addLore("§fВсего классов: §a" + service.getProcessSnapshot().getTotalLoadedClassCount())
                        .addLore("§fЗагружено классов: §a" + service.getProcessSnapshot().getCurrentLoadedClassCount())
                        .addLore("§fОтгруженно классов: §c" + service.getProcessSnapshot().getUnloadedClassCount())
                        .addLore("")
                        .addLore("§fБыл создан: ")
                        .addLore(" §7" + DateUtil.formatTime(service.getCreationTime(), DateUtil.DEFAULT_DATETIME_PATTERN))
                        .addLore("§fБыл подключен: ")
                        .addLore(" §7" + DateUtil.formatTime(service.getConnectedTime(), DateUtil.DEFAULT_DATETIME_PATTERN))

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5ZjE1ODdjN2YyMjc1NDU5MDdmZTliNzU5MmJmMTI2MmMzNjk4Y2RiNmM4YzQ0MDExMTM3YTEyYzIxYjliIn19fQ==")

                        .build());

        String groupName = service.getServiceId().getTaskName();
        setClickItem(50, ItemUtil.newBuilder(Material.SKULL_ITEM)
                        .setName("§cНазад")

                        .addLore("")
                        .addLore("§c▶ Нажмите, чтобы вернуться в предыдущее меню.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYyNTkwMmIzODllZDZjMTQ3NTc0ZTQyMmRhOGY4ZjM2MWM4ZWI1N2U3NjMxNjc2YTcyNzc3ZTdiMWQifX19")

                        .build(),

                (player1, inventoryClickEvent) -> new CloudServersInventory(groupName).openInventory(player));

        setClickItem(21, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Перезагрузить сервер §a" + serverName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы перезагрузить.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg4ZmI2YjJiM2VmYTZhYjI5OWY5ZjRlN2Q4YzFhMWQ3MzM4NzUzYmRmOGZlZjgxMDc1ZjIxNTU5NDNiYzY5In19fQ==")

                        .build(),

                (player1, inventoryClickEvent) -> {

                    player.sendMessage("§d§lCloud §8:: §fИдёт перезагрузка сервера...");

                    new CloudServersInventory(groupName).openInventory(player);

                    CloudNetUtil.restartServiceByName(serverName);

                    player.sendMessage("§d§lCloud §8:: §fСервер §aуспешно §fперезагружен!");

                });

        setClickItem(22, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Запустить сервер §a" + serverName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы запустить сервер.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQyMGNkZjJhZjU3Y2M3NTc3NzNhY2UzZjE5MTVjYjcyYjU0MzJhMmZkYTMzNzNiMTY3OGY5OGJlYTdhYzcifX19")

                        .build(),

                (player1, inventoryClickEvent) -> {

                    player.sendMessage("§d§lCloud §8:: §fИдёт запуск сервера...");
                    new CloudServersInventory(groupName).openInventory(player);

                    CloudNetUtil.startServiceByName(serverName);

                    player.sendMessage("§d§lCloud §8:: §fСервер §aуспешно §fзапущен!");

                });

        setClickItem(23, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Остановить сервер §a" + serverName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы остановить сервер.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjNiYTAyNGMwNzRiNWQ5YWRmNTJlY2VjZGVkNmI0YTlkMTRmMWEyZjQxMzg2MzVkYjc5YTk1NmZiYWIxNDM1MCJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> {

                    player.sendMessage("§d§lCloud §8:: §fИдёт остановка сервера...");
                    new CloudServersInventory(groupName).openInventory(player);

                    CloudNetUtil.stopServiceByName(serverName);

                    player.sendMessage("§d§lCloud §8:: §fСервер §aуспешно §fостановлен!");

                });

        setClickItem(24, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Удалить сервер §a" + serverName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы удалить сервер.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTljZGI5YWYzOGNmNDFkYWE1M2JjOGNkYTc2NjVjNTA5NjMyZDE0ZTY3OGYwZjE5ZjI2M2Y0NmU1NDFkOGEzMCJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> {

                    player.sendMessage("§d§lCloud §8:: §fИдёт удаление сервера...");
                    new CloudServersInventory(groupName).openInventory(player);

                    CloudNetUtil.deleteServiceByName(serverName);

                    player.sendMessage("§d§lCloud §8:: §fСервер §aуспешно §fудалён!");

                });

        setClickItem(25, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Телепортироваться на сервер §a" + serverName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы телепортироваться на сервер.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNiN2MyMWNjNDNkYzE3Njc4ZWU2ZjE2NTkxZmZhYWIxZjYzN2MzN2Y0ZjZiYmQ4Y2VhNDk3NDUxZDc2ZGI2ZCJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> PlazmixCoreApi.redirect(player, serverName));

        setClickItem(31, ItemUtil.newBuilder(Material.SKULL_ITEM)

                .setName("§7Список игроков сервера §a" + serverName)

                .addLore("")
                .addLore("§eНажмите, чтобы открыть список игроков сервера.")

                .setDurability(3)
                .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYzMjMyZjc5ZTkwYTNhZGU3OGFiYjQwMDc1Y2E0MmM3OWNhMDUxOWM3NzQ0ZjI4MDg2YzkyZmE3MGQxZWE0NyJ9fX0=")

                .build(),
                ((player1, inventoryClickEvent) -> new PlayersListInventory(serverName).openInventory(player)));

        setClickItem(32, ItemUtil.newBuilder(Material.SKULL_ITEM)

                        .setName("§7Получить логи с сервера §a" + serverName)

                        .addLore("")
                        .addLore("§eНажмите, чтобы получить логи с сервера.")

                        .setDurability(3)
                        .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQxOWM2ODQ2MTY2NmFhY2Q3NjI4ZTM0YTFlMmFkMzlmZTRmMmJkZTMyZTIzMTk2M2VmM2IzNTUzMyJ9fX0=")

                        .build(),

                (player1, inventoryClickEvent) -> {
                    player.closeInventory();

                    HastebinUtil haste = new HastebinUtil();
                    Queue<String> logs = service.provider().getCachedLogMessages();
                    String message = JsonUtil.toJson(logs);

                    try {
                        String url = haste.post(message.replaceAll("\",\"", "\n").replace("tat", "   "), false);
                        player.sendMessage("§d§lCloud §8:: §fСсылка на логи: " + url);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        setClickItem(33, ItemUtil.newBuilder(Material.SKULL_ITEM)

                .setName("§7Список плагинов сервера §a" + serverName)

                .addLore("")
                .addLore("§eНажмите, чтобы открыть список плагинов сервера.")

                .setDurability(3)
                .setTextureValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTJiMTcxMmI5MDdjZTZiMTQwMmVhYWMyOGVjMjRhNGQ5NTU2OGY0YWI4N2U1OTc5ODBjMTViMjJiYmJkN2E1In19fQ==")

                .build(),

                (player1, inventoryClickEvent) -> {

                    new PluginsInventory(service.getServiceId().getTaskName() + "-" + service.getServiceId().getTaskServiceId()).openInventory(player);

                });
    }
}
