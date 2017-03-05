package red.man10.man10core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * Created by takatronix on 2017/03/05.
 */
public class Man10CoreCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public Man10CoreCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        //      引数がない場合
        if (args.length < 1) {
            showHelp(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            showHelp(p);
            return true;
        }


        if(args[0].equalsIgnoreCase("settp")) {
            plugin.getConfig().set("tp.x", p.getLocation().getX());
            plugin.getConfig().set("tp.y", p.getLocation().getY());
            plugin.getConfig().set("tp.z", p.getLocation().getZ());

            Vector v = p.getLocation().getDirection();

            plugin.getConfig().set("tp.vx", v.getX());
            plugin.getConfig().set("tp.vy", v.getY());
            plugin.getConfig().set("tp.vz", v.getZ());
            plugin.saveConfig();
            p.sendMessage("§a§lTPロケーションを設定しました。");
            return true;
        }

        return false;
    }
    void showHelp(Player p){
        p.sendMessage("§e============== §d●§f●§a●§e Man10 Core §d●§f●§a● §e===============");
        p.sendMessage("§e  by takatronix http://man10.red");
        p.sendMessage("§c* red commands for Admin");
        p.sendMessage("§c/man10 chatlog");

    }
}
