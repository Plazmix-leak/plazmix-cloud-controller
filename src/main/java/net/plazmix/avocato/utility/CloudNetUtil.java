package net.plazmix.avocato.utility;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import lombok.experimental.UtilityClass;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.utility.JsonUtil;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Queue;

@UtilityClass
public class CloudNetUtil {

    /**
     * Создать сервер в CloudNet
     * по имени таска
     */
    public void createCloudServiceByTask(String serverName) {
        if (CloudNetDriver.getInstance().getServiceTaskProvider().isServiceTaskPresent(serverName)) {
            ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(serverName);

            ServiceInfoSnapshot serviceInfoSnapshot = ServiceConfiguration.builder(serviceTask).build()
                    .createNewService();

            if (serviceInfoSnapshot != null) {
                serviceInfoSnapshot.provider().start();
            }
        }
    }

    /**
     * Получить название таска
     * CloudNet по имени сервера
     */
    public ServiceInfoSnapshot getServiceByName(String name) {
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(name);
    }

    /**
     * Запустить сервер через
     * CloudNet по таску
     */
    public void startServiceByName(String name) {
        getServiceByName(name).provider().start();
    }

    /**
     * Остановить сервер через
     * CloudNet по таску
     */
    public void stopServiceByName(String name) {
        getServiceByName(name).provider().stop();
    }

    /**
     * Удалить сервер через
     * CloudNet по таску
     */
    public void deleteServiceByName(String name) {
        getServiceByName(name).provider().kill();
    }

    /**
     * Рестарт сервера через
     * CloudNet по таску
     */
    public void restartServiceByName(String name) {
        getServiceByName(name).provider().restart();
    }


    /**
     * Получить логи по имени
     * сервера по таску
     */
    public void getLogsByServiceName(Player player, String name) {
        HastebinUtil hasteBin = new HastebinUtil();
        String message = JsonUtil.toJson(getServiceByName(name).provider().getCachedLogMessages());

        try {
            String url = hasteBin.post(message.replaceAll("\",\"", "\n").replace("tat", ""), false);
            player.sendMessage("§d§lCloud §8:: §fСсылка на логи: " + url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
