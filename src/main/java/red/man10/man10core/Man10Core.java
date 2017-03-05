package red.man10.man10core;

import eu.theindra.geoip.GeoIP;
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

import java.net.InetAddress;
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
        //mysql.debugMode = true;
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
        p.sendMessage(ChatColor.YELLOW  + "OHAMAN!! Welcome to Man10 Server / おはまん！！ まんじゅうサーバへようこそ！");


        ////////////////////////////////
        //      GeoIPチェック
        ////////////////////////////////
        InetAddress ip = p.getAddress().getAddress();
        String userip = p.getAddress().getAddress().toString().substring(1);
        String country = GeoIP.getCountry(ip);
        String city = GeoIP.getCity(ip);
        String timezone = GeoIP.getTimezone(ip);
        double balance = vault.getBalance(p.getUniqueId());
        //      ログインログ保存
        insertLoginLog(Bukkit.getServerName(),p.getWorld().getName(),p.getName(),userip,country,city,timezone,balance,p.getUniqueId().toString());

        GlowAPI.setGlowing(e.getPlayer(), GlowAPI.Color.RED, Bukkit.getOnlinePlayers());



    }
    /////////////////////////////////
    //      チャットイベント
    /////////////////////////////////
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();
        if(message.contains("ohaman") || message.contains("おはまん")){
            p.sendMessage("あいさつしたので国王からお小遣いをもらった");
            vault.deposit(e.getPlayer().getUniqueId(),10000);
          //  vaultManager.showBalance(e.getPlayer().getUniqueId());
            return;
        }



       // createTables();
        insertChatLog(Bukkit.getServerName(), p.getWorld().getName(),p.getName(),message);
    }


    /////////////////////////////////////////
    //      チャットログテーブル
    /////////////////////////////////////////
    String sqlChatTable = "CREATE TABLE `chat_log` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `server` varchar(100) DEFAULT NULL,\n" +
            "  `world` varchar(100) DEFAULT NULL,\n" +
            "  `name` varchar(100) DEFAULT NULL,\n" +
            "  `message` varchar(400) DEFAULT NULL,\n" +
            "  `timestamp` int(50) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8;";
    ///////////////////////////////////////////
    //      ログインテーブル
    ///////////////////////////////////////////
    String sqlLoginTable = "CREATE TABLE `login_log` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `server` varchar(100) DEFAULT NULL,\n" +
            "  `world` varchar(100) DEFAULT NULL,\n" +
            "  `name` varchar(100) DEFAULT NULL,\n" +
            "  `ip` varchar(100) DEFAULT NULL,\n" +
            "  `country` varchar(100) DEFAULT NULL,\n" +
            "  `city` varchar(100) DEFAULT NULL,\n" +
            "  `timezone` varchar(100) DEFAULT NULL,\n" +
            "  `balance` double DEFAULT NULL,\n" +
            "  `uuid` varchar(100) DEFAULT NULL,\n" +
            "  `timestamp` int(50) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    void createTables(){;

        mysql.execute(sqlChatTable);
        mysql.execute(sqlLoginTable);

    }
    //      ログメッセージ
    void log(String text){
        getLogger().info(text);
    }
    //     サーバーメッセージ
    void serverMessage(String text){
        //command("say "+text);
        Bukkit.getServer().broadcastMessage(text);
    }

    /////////////////////////////////
    //      ログインログ保存
    /////////////////////////////////
    void  insertLoginLog(String server,String world,String name,String ip,String country,String city,String timezone,double balance,String uuid){
        long curTime = System.currentTimeMillis() / 1000L;
        mysql.execute("insert into login_log values(0,'"+server+ "','"+world+ "','"+name
                        +"','" + ip
                        +"','" + country
                        +"','" + city
                        +"','" + timezone
                        +"'," + balance
                        +",'" + uuid
                        +"'," + curTime
                        +");");

    }
    //////////////////////////////////
    //      チャットログ保存
    //////////////////////////////////
    void  insertChatLog(String server,String world,String name,String message){

        long curTime = System.currentTimeMillis() / 1000L;
        mysql.execute("insert into chat_log values(0,'"+server+ "','"+world+ "','"+name+ "','" + message + "'," + curTime + ");");
    }

    void showChatLog(){
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
