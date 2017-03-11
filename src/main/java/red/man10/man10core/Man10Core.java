package red.man10.man10core;

import eu.theindra.geoip.GeoIP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import red.man10.MySQLManager;
import red.man10.VaultManager;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


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
        p.sendMessage(ChatColor.YELLOW  + "OHAMAN!! Welcome to Man10 Server / おはまん！！ まんじゅうサーバへようこそ！");


        ////////////////////////////////
        //      GeoIPチェック
        ////////////////////////////////

        InetAddress ip = p.getAddress().getAddress();
        String userip = p.getAddress().getAddress().toString().substring(1);
        String country = "";
        String city = "";
        String timezone = "";

        if (getServer().getPluginManager().getPlugin("GeoipAPI") == null) {
            getLogger().warning("GeoipAPI plugin is not installed");
        }else{
            country = GeoIP.getCountry(ip);
            city = GeoIP.getCity(ip);
            timezone = GeoIP.getTimezone(ip);
        }

        double balance = vault.getBalance(p.getUniqueId());

        //          全ユーザに通知
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            int loginCount = getLoginCount(p.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + p.getDisplayName()+"さんがログインしました");

            if(loginCount != -1){
                if(loginCount == 0) {
                    player.sendMessage(ChatColor.GREEN +"はじめてのログインです！");
                }{
                    player.sendMessage(ChatColor.YELLOW +""+ loginCount+"回目のログインです");
                }
            }
            //      OPには通知する
            if (player.isOp()) {
                player.sendMessage(ChatColor.RED + "IP:"+userip);
                player.sendMessage(ChatColor.RED + "Country:"+country);
                player.sendMessage(ChatColor.RED + "City:"+city);
                player.sendMessage(ChatColor.RED + "TimeZone:"+timezone);
                player.sendMessage(ChatColor.RED + "Balance:"+balance);
            }

        }

        //      ログインログ保存
        insertLoginLog(Bukkit.getServerName(),p.getWorld().getName(),p.getName(),userip,country,city,timezone,balance,p.getUniqueId().toString());

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

            tpDefaultPoint(p);
          //  vaultManager.showBalance(e.getPlayer().getUniqueId());
            return;
        }

        //     チャットログ保存
        insertChatLog(Bukkit.getServerName(), p.getWorld().getName(),p.getName(),message);
      //  GlowAPI.setGlowing(e.getPlayer(), GlowAPI.Color.RED, Bukkit.getOnlinePlayers());

        //getLoginCount(p.getUniqueId());
     //   serverMessage("レコード数" + mysql.count("login_log"));

    }


    /////////////////////////////////////////
    //      チャットログテーブル
    /////////////////////////////////////////
    String sqlChatTable = "CREATE TABLE `chat_log` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `server` varchar(100) DEFAULT NULL,\n" +
            "  `world` varchar(100) DEFAULT NULL,\n" +
            "  `name` varchar(40) NOT NULL DEFAULT '',\n" +
            "  `message` varchar(400) DEFAULT NULL,\n" +
            "  `time` datetime DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=3304 DEFAULT CHARSET=utf8;";
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
            "  `time` datetime DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;";

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

        mysql.execute("insert into login_log values(0,'"+server+ "','"+world+ "','"+name
                        +"','" + ip
                        +"','" + country
                        +"','" + city
                        +"','" + timezone
                        +"'," + balance
                        +",'" + uuid
                        +"'," + currentTime()
                        +");");

    }
    //////////////////////////////////
    //      チャットログ保存
    //////////////////////////////////
    void  insertChatLog(String server,String world,String name,String message){



        mysql.execute("insert into chat_log values(0,'"+server+ "','"+world+ "','"+name+ "','" + message + "'," + currentTime() + ");");
    }
    public String currentTime(){

        //long timestamp = 1371271256;
        //Date date = new Date(timestamp * 1000);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss");
        Bukkit.getLogger().info("datetime ");
        String currentTime = "'"+sdf.format(date)+"'";
        Bukkit.getLogger().info(currentTime);
        return currentTime;
    }

    //    ログイン回数を取得する
    int  getLoginCount(UUID uuid){

        int count = -1;
        ResultSet rs  = mysql.query("select * from login_log where uuid ='" + uuid.toString()+"';");
       // serverMessage(rs.toString());
        try{
            rs.last();
            count = rs.getRow();
        }
        catch (SQLException e)
        {
            this.getLogger().info("Error executing a query: " + e.getErrorCode());
            return -1;
        }
        return count;
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

    void tpDefaultPoint(Player p){
        tp(p,"Lobby");

    }

    public void tp(Player p,String name){


        Object o =  getConfig().get("pos."+name);
        if(o != null){
            Location loc = (Location)o;
            p.teleport(loc);
            p.sendMessage("§a§lTPしました。");
        }
        return;
    }
    public void savePos(Player p,String name){

        getConfig().set("pos."+name , p.getLocation());
        saveConfig();
        p.sendMessage("§a§lTPロケーションを設定しました。");
    }

}
