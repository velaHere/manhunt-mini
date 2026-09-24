package org.velas.manhunt.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class CustomCommand {

    protected String name;
    protected String permissionName;
    protected String alias;
    protected final List<CustomSubCommand> subCommands;

    public CustomCommand(String name, String alias, String permissionName){
        this.name = name;
        this.permissionName=permissionName;
        this.alias = alias;
        this.subCommands = new ArrayList<>();
    }

    protected abstract boolean runPlayerCommand(Player player, String [] args);
    public abstract void sendUsage(Player player);

    public boolean runCommand(CommandSender sender, String [] args){

        if (sender instanceof Player player){
            return this.runPlayerCommand(player, args);
        }else if (sender instanceof ConsoleCommandSender console){
            console.sendMessage("You cannot use this command on console");
            return true;
        }else {
            return false;
        }
    }

    public String getName(){
        return this.name;
    }

    public String getAlias(){
        return this.alias;
    }

    public boolean hasAlias(){
        return this.alias != null;
    }

    public String getPermissionName(){
        return this.permissionName;
    }

    @Nullable
    public CustomSubCommand getSubCommand(String name){

        if (subCommands.isEmpty()) return null;

        for (CustomSubCommand subCommand: subCommands){
            if (subCommand.getName().equals(name)) return subCommand;
        }
        return null;
    }

}
