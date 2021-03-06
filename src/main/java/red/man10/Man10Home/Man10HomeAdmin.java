package red.man10.Man10Home;

import com.mysql.cj.api.mysqla.result.Resultset;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.man10.man10core.Man10Core;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by sho-pc on 2017/04/06.
 */
public class Man10HomeAdmin implements CommandExecutor {
    private final Man10Core plugin;

    public Man10HomeAdmin(Man10Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if(args.length < 2){
            help(p);
            return true;
        }
        if(args[0].equalsIgnoreCase("help")){
            help(p);
            return true;
        }
        if(args[0].equalsIgnoreCase("list")){
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                p.sendMessage(plugin.home_prefix + "プレイヤーは存在しません");
                return false;
            }
            ResultSet result = plugin.mysql.query("SELECT * FROM `man10_home` WHERE uuid = '" + player.getUniqueId() + "';");
            p.sendMessage("§c§l==========[" + player.getName() + "'s home]==========");
            try {
                while(result.next()){
                    p.sendMessage("§b§l" + result.getString("name") + " " + result.getString("world") + " " + Math.round(result.getDouble("x")*100/100) + " " + Math.round(result.getDouble("y")*100/100) + " "+ Math.round(result.getDouble("z")*100/100));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        if(args[0].equalsIgnoreCase("delete")) {
            if(args.length < 3){
                help(p);
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                p.sendMessage(plugin.home_prefix + "プレイヤーは存在しません");
                return false;
            }
            plugin.mysql.execute("DELETE FROM man10_home WHERE uuid = '" + player.getUniqueId() + "' and name = '" + args[2] + "';");
            p.sendMessage(plugin.home_prefix + "ホームを消去しました");
            return true;
        }
        if(args[0].equalsIgnoreCase("clear")){
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                p.sendMessage(plugin.home_prefix + "プレイヤーは存在しません");
                return false;
            }
            plugin.mysql.execute("DELETE FROM man10_home WHERE uuid = '" + player.getUniqueId() + "';");
            p.sendMessage(plugin.home_prefix + "ホームをクリアしました");
            return true;
        }
        if(args[0].equalsIgnoreCase("goto")){
            if(args.length < 3) {
                p.sendMessage(plugin.home_prefix + "コマンドの使い方が間違っています /mhome goto <player> <name>");
            }
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                p.sendMessage(plugin.home_prefix + "プレイヤーは存在しません");
                return false;
            }
            ResultSet result = plugin.mysql.query("SELECT * FROM `man10_home` WHERE uuid = '" + player.getUniqueId() + "' and name = '" + args[2] + "'");
            try {
                while(result.next()){
                    World world = Bukkit.getServer().getWorld(result.getString("world"));
                    float pich = (float) result.getDouble("pitch");
                    float yaw = (float) result.getDouble("yaw");
                    Location loc = new Location(world,result.getDouble("x"),result.getDouble("y"),result.getDouble("z"),pich,yaw);
                    p.teleport(loc);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            p.sendMessage(plugin.home_prefix + "テレポートしました");
            return true;

        }
        help(p);
        return true;
    }
    void help(Player p){
        p.sendMessage("§c====================[home admin]====================");
        p.sendMessage("§b/mhome list <player> プレイヤーのホームを見る");
        p.sendMessage("§b/mhome delete <player> <home name> プレイヤーのホームを削除");
        p.sendMessage("§b/mhome clear <player> プレイヤーのホームをクリア");
        p.sendMessage("§b/mhome goto <player> <name> プレイヤーのホームに飛ぶ");
    }
    void tp(Player p,String world,double x,double y,double z,double ya,double pi){
        World w = Bukkit.getWorld(world);
        float yaw = (float) ya;
        float pitch = (float) pi;
        Location l = new Location(w,x,y,z,yaw,pitch);
        p.teleport(l);
    }
}
