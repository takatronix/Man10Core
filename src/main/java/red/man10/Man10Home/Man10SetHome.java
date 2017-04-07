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
public class Man10SetHome implements CommandExecutor {
    private final Man10Core plugin;

    public Man10SetHome(Man10Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if(args.length != 1){
            p.sendMessage(plugin.home_prefix + "コマンドの使い方が間違ってます /sethome <ホーム名>");
            return false;
        }
        insertHome(p.getUniqueId(), p.getName(), args[0], p.getLocation().getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
        p.sendMessage(plugin.home_prefix + "ホームをセットしました");
        return false;
    }

    void insertHome(UUID uuid, String username, String name, String world, double x, double y, double z, double pitch, double yaw) {
        plugin.mysql.execute("DELETE FROM man10_home WHERE uuid = '" + uuid + "' and name = '" + name + "';");
        plugin.mysql.execute("insert into man10_home values(0,'" + uuid + "','" + username + "','" + name + "','" + world + "','" + x + "','" + y + "','" + z + "','" + pitch + "','" + yaw + "'," + plugin.currentTime() + ");");
    }
}