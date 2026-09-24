package org.velas.manhunt.commands;

public abstract class CustomSubCommand extends CustomCommand {

    public CustomSubCommand(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }
}
