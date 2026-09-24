package org.velas.manhunt;

public enum Color {

    RED("§c"),
    DARK_RED("§4"),
    GOLD("§6"),
    YELLOW("§e"),
    DARK_GREEN("§2"),
    GREEN("§a"),
    AQUA("§b"),
    DARK_AQUA("§3"),
    DARK_BLUE("§1"),
    BLUE("§9"),
    PINK("§d"),
    DARK_PINK("§5"),
    WHITE("§f"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    BLACK("§0"),
    BOLD("§l");

    private final String code;

    Color(String code){
        this.code = code;
    }

    @Override
    public String toString(){
        return code;
    }

}
