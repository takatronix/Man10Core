package red.man10.Man10Home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.man10.man10core.Man10Core;

import java.util.UUID;

/**
 * Created by sho-pc on 2017/04/07.
 */
public class Man10DeleteHome implements CommandExecutor {
    private final Man10Core plugin;

    public Man10DeleteHome(Man10Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length != 1){
            p.sendMessage(plugin.home_prefix + "コマンドの使い方が間違ってます/delhome <ホーム名>");
            return false;
        }
        UUID uuid = p.getUniqueId();
        String name = args[0];
        plugin.mysql.execute("DELETE FROM man10_home WHERE uuid = '" + uuid + "' and name = '" + name + "';");
        p.sendMessage(plugin.home_prefix + "ホームを削除しました");
        return false;
    }
}
