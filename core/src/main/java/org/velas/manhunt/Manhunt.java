package org.velas.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.velas.manhunt.commands.*;
import org.velas.manhunt.config.ConfigManager;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.menusystem.PlayerMenuUtility;
import org.velas.manhunt.runnables.BroadcastCredits;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public final class Manhunt extends JavaPlugin {

    private static Manhunt INSTANCE;
    private static ClassProvider PROVIDER;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {

        INSTANCE = this;
        BroadcastCredits.printConsoleBranding();
        ConfigManager.init(this);

        World lobby = Bukkit.createWorld(new WorldCreator(Settings.getConfigSettings().getLOBBY_WORLD_NAME()));
        if (lobby != null){
            lobby.setDifficulty(Difficulty.PEACEFUL);
        }

        try{
            Class<?> clazz = Class.forName("org.velas.manhunt.ClassProviderImpl");
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            PROVIDER = (ClassProvider) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ManhuntGame.createNewGame(null, null, null);

        ScoreManager.init();
        ScoreManager.setUpTeams();

        registerCommands();
        registerListeners();
        CustomItemType.registerRecipes(this);
        new BroadcastCredits().runTaskTimer(this, 500L, 4000L);
    }

    @Override
    public void onDisable() {
        getLogger().info("*****Team Disabled*****");
    }

    public static Manhunt getINSTANCE() {
        return INSTANCE;
    }

    public static ClassProvider getPROVIDER(){
        return PROVIDER;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return CommandCollection.runCommand(sender, label, args);
    }

    private void registerCommands(){

        CommandCollection.clearCommands();
        CommandCollection.addCommand(new ManhuntCmd("manhunt", "mh", null));
        CommandCollection.addCommand(new CrewMsgCmd("crewmsg", "cm", null));
        CommandCollection.addCommand(new Coordinates("cords", null, null));
        CommandCollection.addCommand(new TestCmd("test", null, Settings.SETUP_PERMISSION));

        PluginCommand manhunt = getCommand("manhunt");
        if (manhunt == null){
            Utils.warning("Manhunt Command was null");
            return;
        }
        manhunt.setTabCompleter(new TabCompleter());
    }

    private void registerListeners(){
        String packageName = getClass().getPackageName();
        for(Class<?> clazz: new Reflections(packageName+".listeners")
                .getSubTypesOf(Listener.class))
        {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player){

        PlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(player)){
            return playerMenuUtilityMap.get(player);
        }else {

            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);

            return playerMenuUtility;
        }
    }
}
