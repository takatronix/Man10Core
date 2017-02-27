package red.man10.man10core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Man10Core extends JavaPlugin implements Listener {

    /////////////////////////////////
    //        設定値
    /////////////////////////////////
    String  mysql_ip;
    String  mysql_port;
    String  mysql_id;
    String  mysql_pass;
    String  mysql_dbname;

    /////////////////////////////////
    //       設定ファイル読み込み
    /////////////////////////////////
    public void loadConfig(){
        this.reloadConfig();
        mysql_ip = this.getConfig().getString("server_config.mysql_ip");
        mysql_port = this.getConfig().getString("server_config.mysql_port");
        mysql_id = this.getConfig().getString("server_config.mysql_id");
        mysql_pass = this.getConfig().getString("server_config.mysql_pass");
        mysql_dbname = this.getConfig().getString("server_config.dbname");
        getLogger().info("Config loaded");
        return;
    }

    /////////////////////////////////
    //      起動
    /////////////////////////////////
    @Override
    public void onEnable() {
        getLogger().info("Enabled");
        this.saveDefaultConfig();
        loadConfig();
        getServer().getPluginManager().registerEvents (this,this);
    }

    /////////////////////////////////
    //      終了
    /////////////////////////////////
    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

    /////////////////////////////////
    //      コマンド処理
    /////////////////////////////////
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        return true;
    }

    /////////////////////////////////
    //     ジョインイベント
    /////////////////////////////////
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.YELLOW  + "Welcome to Man10 Server");
    }
    /////////////////////////////////
    //      チャットイベント
    /////////////////////////////////
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();
        p.sendMessage(ChatColor.RED + message);
    }
}
