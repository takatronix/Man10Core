package red.man10.man10core;


import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by takatronix on 2017/03/04.
 */
public class MySqlManager {
    private final JavaPlugin plugin;
    public MySqlManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /////////////////////////////////
    //        設定値
    /////////////////////////////////
    String  mysql_ip;
    String  mysql_port;
    String  mysql_user;
    String  mysql_pass;
    String  mysql_db;

    /////////////////////////////////
    //       設定ファイル読み込み
    /////////////////////////////////
    public void loadConfig(){
        plugin.reloadConfig();
        mysql_ip = plugin.getConfig().getString("server_config.mysql_ip");
        mysql_port = plugin.getConfig().getString("server_config.mysql_port");
        mysql_user = plugin.getConfig().getString("server_config.mysql_user");
        mysql_pass = plugin.getConfig().getString("server_config.mysql_pass");
        mysql_db = plugin.getConfig().getString("server_config.mysql_db");
        plugin.getLogger().info("Config loaded");
    }


    ////////////////////////////////
    //      SQL実行
    ////////////////////////////////
    Boolean executeSQL(String sql){
        // getLogger().info("executing SQL" + sql);
        Connection conn;
        try {
            //      データベース作成
            Class.forName("com.mysql.jdbc.Driver");
            String databaseURL =  "jdbc:mysql://" + mysql_ip + "/" + mysql_db ;
            //getLogger().info(databaseURL);

            conn = DriverManager.getConnection(databaseURL,mysql_user,mysql_pass);
            Statement st = conn.createStatement();
            st.execute(sql);

            st.close();
            conn.close();
            //getLogger().info("SQL performed");
            return true;
        } catch(ClassNotFoundException e){
        //    getLogger().warning("Could not read driver");
        } catch(SQLException e){
          ///  getLogger().warning("Database connection error");
        }
        return false;
    }


}
