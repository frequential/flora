package me.jasperedits;

import lombok.Getter;
import me.jasperedits.docs.BotValues;
import me.jasperedits.listeners.Ready;
import me.jasperedits.logging.LogPriority;
import me.jasperedits.logging.LogUtils;
import me.jasperedits.managers.DatabaseManager;
import me.jasperedits.managers.MongoDatabaseManager;
import me.jasperedits.managers.YAMLManager;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@Getter
public class FloraBot {

    private final boolean debug;
    private final BotValues botValues;
    private DefaultShardManagerBuilder builder;

    private DatabaseManager databaseManager;

    public FloraBot(boolean debug) throws IOException {
        this.debug = debug;
        this.botValues = new YAMLManager("botValues.yaml").buildObject(BotValues.class);
    }

    public void init() throws LoginException {
        this.databaseManager = new MongoDatabaseManager(
                this.botValues.getDatabaseHostname(),
                this.botValues.getDatabasePort(),
                this.botValues.getDatabaseUsername(),
                this.botValues.getDatabasePassword(),
                this.botValues.getDatabaseName()
        );

        this.builder = DefaultShardManagerBuilder.createDefault(this.botValues.getToken());

        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.watching("your community grow"));
        // Registers all the listeners
        registerListeners();
        builder.build();
    }

    public void registerListeners() {
        builder.addEventListeners(new Ready());
    }
}