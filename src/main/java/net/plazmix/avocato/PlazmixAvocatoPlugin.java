package net.plazmix.avocato;

import net.plazmix.PlazmixApi;
import net.plazmix.avocato.command.*;
import org.bukkit.plugin.java.JavaPlugin;

/*  Leaked by https://t.me/leak_mine
    - Все слитые материалы вы используете на свой страх и риск.

    - Мы настоятельно рекомендуем проверять код плагинов на хаки!
    - Список софта для декопиляции плагинов:
    1. Luyten (последнюю версию можно скачать можно тут https://github.com/deathmarine/Luyten/releases);
    2. Bytecode-Viewer (последнюю версию можно скачать можно тут https://github.com/Konloch/bytecode-viewer/releases);
    3. Онлайн декомпиляторы https://jdec.app или http://www.javadecompilers.com/

    - Предложить свой слив вы можете по ссылке @leakmine_send_bot или https://t.me/leakmine_send_bot
*/

public final class PlazmixAvocatoPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PlazmixApi.registerCommand(new CloudCommand());
        PlazmixApi.registerCommand(new LogCommand());
        PlazmixApi.registerCommand(new ServerCommand());
        PlazmixApi.registerCommand(new PluginsCommand());
    }

}
