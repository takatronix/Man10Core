package red.man10.man10Home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import red.man10.man10core.Man10Core;

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

        return false;
    }
}
