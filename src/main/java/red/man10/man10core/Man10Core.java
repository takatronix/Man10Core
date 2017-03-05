package red.man10.man10core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.glow.GlowAPI;

import java.sql.ResultSet;
import java.sql.SQLException;


public final class Man10Core extends JavaPlugin implements Listener {


    VaultManager vault = null;
    MySQLManager mysql = null;

    /////////////////////////////////
    //      起動
    /////////////////////////////////
    @Override
    public void onEnable() {
        getLogger().info("Enabled");
        this.saveDefaultConfig();
        //loadConfig();
        getServer().getPluginManager().registerEvents (this,this);

        vault = new VaultManager(this);
        mysql = new MySQLManager(this,"Man10Core");
        mysql.debugMode = true;
        //   テーブル作成
        createTables();

        getCommand("man10").setExecutor(new Man10CoreCommand(this));
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
        if(message.contains("ohaman")){
            p.sendMessage("あいさつしたので国王からお小遣いをもらった");
            vault.deposit(e.getPlayer().getUniqueId(),10000);
          //  vaultManager.showBalance(e.getPlayer().getUniqueId());
            return;
        }

        GlowAPI.setGlowing(e.getPlayer(), GlowAPI.Color.RED, Bukkit.getOnlinePlayers());
       // createTables();
        insertMessage(Bukkit.getServerName(), p.getWorld().getName(),p.getName(),message);
    }

    String sqlMessageTable = "CREATE TABLE `chat_log` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `server` varchar(100) DEFAULT NULL,\n" +
            "  `world` varchar(100) DEFAULT NULL,\n" +
            "  `name` varchar(100) DEFAULT NULL,\n" +
            "  `message` varchar(400) DEFAULT NULL,\n" +
            "  `timestamp` varchar(50) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    void createTables(){;
        //executeSQL(sqlCrateChatLogTable);

        mysql.execute(sqlMessageTable);

    }


    void  insertMessage(String server,String world,String name,String message){
        String time = "1234";
        long currTime = System.currentTimeMillis() / 1000L;

        mysql.execute("insert into chat_log values(0,'"+server+ "','"+world+ "','"+name+ "','" + message + "','" + currTime + "');");

        return;
                /*
        final ResultSet rs = mysql.query("SELECT * FROM `messages` LIMIT 500");
        try
        {
            while(rs.next())
            {

            }
        }
        catch (SQLException e)
        {
            this.getLogger().info("Error executing a query: " + e.getErrorCode());
        }
*/

    }
    //      基本表示系


}
