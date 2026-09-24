package org.velas.manhunt.commands;

import org.bukkit.command.CommandSender;
import org.velas.manhunt.Color;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandCollection {

    private static final List<CustomCommand> commands = new ArrayList<>();

    public static void addCommand(CustomCommand command){
        commands.add(command);
    }

    public static void clearCommands(){
        commands.clear();
    }

    @Nullable
    public static CustomCommand getCommand(String name){

        if (commands.isEmpty()) return null;

        for (CustomCommand command: commands){
            if (command.getName().equals(name) || (command.hasAlias() && command.getAlias().equals(name))) return command;
        }

        return null;

    }

    public static boolean runCommand(CommandSender sender, String label, String [] args){

        CustomCommand command = getCommand(label);

        if (command == null){
            sender.sendMessage("This command doesn't exists");
            return true;
        }

        if(command.getPermissionName() != null && !sender.hasPermission(command.getPermissionName())){
            sender.sendMessage(Color.RED + "You don't have permission to run this command");
            return true;
        }

        if (args.length == 0){
            return command.runCommand(sender, args);
        }

        String subCommandName = args[0];
        CustomSubCommand subCommand = command.getSubCommand(subCommandName);

        if (subCommand == null){
            return command.runCommand(sender,args);
        }

        if(subCommand.getPermissionName() != null && !sender.hasPermission(subCommand.getPermissionName())){
            sender.sendMessage(Color.RED + "You don't have permission to run this command");
            return true;
        }

        for(int i = 1; i<args.length; i++){
            CustomSubCommand temp = subCommand.getSubCommand(args[i]);

            if (temp == null){
                return subCommand.runCommand(sender, args);
            }

            if(temp.getPermissionName() != null && !sender.hasPermission(temp.getPermissionName())){
                sender.sendMessage(Color.RED + "You don't have permission to run this command");
                return true;
            }

            subCommand = temp;
        }
        return subCommand.runCommand(sender, args);
    }

}
