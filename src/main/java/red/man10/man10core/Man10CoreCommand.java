package red.man10.man10core;

import org.bukkit.Bukkit;
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
    private final Man10Core plugin;

    public Man10CoreCommand(Man10Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //      引数がない場合
        if (args.length < 1) {
            showHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }

        if(args[0].equalsIgnoreCase("settp")) {
            //      引数がない場合
            if (args.length < 2) {
                showHelp(sender);
                return true;
            }


            Player p = (Player) sender;
            plugin.savePos(p, args[1]);
            return true;
        }

        if(args[0].equalsIgnoreCase("tp")) {

            //      引数がない場合
            if (args.length < 2) {
                showHelp(sender);
                return true;
            }

            Player p = (Player) sender;
            plugin.tp(p, args[1]);
            return true;
        }


        if(args[0].equalsIgnoreCase("tpuser")) {

            //      引数がない場合
            if (args.length < 3) {
                showHelp(sender);
                return true;
            }

            Player p = Bukkit.getPlayer(args[1]);
            plugin.tp(p, args[2]);
            return true;
        }
        return false;
    }
    void showHelp(CommandSender p){
        p.sendMessage("§e============== §d●§f●§a●§e Man10 Core §d●§f●§a● §e===============");
        p.sendMessage("§e  by takatronix http://man10.red");
        p.sendMessage("§c* red commands for Admin");
        p.sendMessage("§c/man10 chatlog");

    }
}
