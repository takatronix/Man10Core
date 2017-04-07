package red.man10.Man10Home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.man10.man10core.Man10Core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by sho-pc on 2017/04/06.
 */
public class Man10Home implements CommandExecutor {
    private final Man10Core plugin;

    public Man10Home(Man10Core plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if(args.length == 0){
            int count = 0;
            ResultSet result = plugin.mysql.query("SELECT * FROM `man10_home` WHERE uuid = '" + p.getUniqueId() + "' and name = '__default__';");
            try {
                while (result.next()) {
                    World w = Bukkit.getServer().getWorld(result.getString("world"));
                    double x = result.getDouble("x");
                    double y = result.getDouble("y");
                    double z = result.getDouble("z");
                    float pitch = (float) result.getDouble("pitch");
                    float yaw = (float) result.getDouble("yaw");
                    Location l = new Location(w,x,y,z,yaw,pitch);
                    p.teleport(l);
                    count++;
                }
                if(count == 0){
                    p.sendMessage(plugin.home_prefix + "ホームが存在しません");
                    return false;
                }
                p.sendMessage(plugin.home_prefix + "ホームへテレポートしました");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        int count = 0;
        ResultSet result = plugin.mysql.query("SELECT * FROM `man10_home` WHERE uuid = '" + p.getUniqueId() + "' and name = '" + args[0] + "';");
        try {
            while (result.next()) {
                World w = Bukkit.getServer().getWorld(result.getString("world"));
                double x = result.getDouble("x");
                double y = result.getDouble("y");
                double z = result.getDouble("z");
                float pitch = (float) result.getDouble("pitch");
                float yaw = (float) result.getDouble("yaw");
                Location l = new Location(w,x,y,z,yaw,pitch);
                p.teleport(l);
                count++;
            }
            if(count == 0){
                p.sendMessage(plugin.home_prefix + "ホームが存在しません");
                return false;
            }
            p.sendMessage(plugin.home_prefix + "ホームへテレポートしました");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}

