package red.man10.Man10Home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.man10.man10core.Man10Core;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Created by sho-pc on 2017/04/07.
 */
public class Man10HomeList implements CommandExecutor {
    private final Man10Core plugin;

    public Man10HomeList(Man10Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        ResultSet result = plugin.mysql.query("SELECT * FROM `man10_home` WHERE uuid = '" + p.getUniqueId() + "';");
        p.sendMessage("§c§l==========[" + p.getName() + "'s home]==========");
        try {
            while(result.next()){
                p.sendMessage("§b§l" + result.getString("name") + " " + result.getString("world") + " " + Math.round(result.getDouble("x")*100/100) + " " + Math.round(result.getDouble("y")*100/100) + " "+ Math.round(result.getDouble("z")*100/100));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}

/*
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
 */