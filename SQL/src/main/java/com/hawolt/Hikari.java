package com.hawolt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Hikari {

    private final HikariDataSource source;

    public Hikari(HikariDataSource source) {
        this.source = source;
    }

    public static Hikari setup(JsonSource source) {
        return setup("default", source);
    }

    public static Hikari setup(String name, JsonSource source) {
        HikariConfig config = new HikariConfig();
        String jdbc = String.format("jdbc:%s://%s:%s/%s",
                source.get("sql.driver"),
                source.get("sql.ip"),
                source.get("sql.port"),
                source.get("sql.database")
        );
        config.setJdbcUrl(jdbc);
        config.setUsername(source.get("sql.username"));
        config.setPassword(source.get("sql.password"));
        int size = source.containsKey("sql.max") ? Integer.parseInt(source.get("sql.max")) : Runtime.getRuntime().availableProcessors();
        if (source.containsKey("sql.max")) config.setMaximumPoolSize(size);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        Hikari hikari = new Hikari(new HikariDataSource(config));
        CONNECTION_MANAGERS.put(name, hikari);
        return hikari;
    }

    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    private static final Map<String, Hikari> CONNECTION_MANAGERS = new HashMap<>();

    public static Hikari getManager() {
        return CONNECTION_MANAGERS.get("default");
    }

    public static Hikari getManager(String name) {
        return CONNECTION_MANAGERS.get(name);
    }
}
