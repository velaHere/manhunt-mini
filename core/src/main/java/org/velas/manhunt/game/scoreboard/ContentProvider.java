package org.velas.manhunt.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.velas.manhunt.ability.Ability;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.avatar.Avatar;
import org.velas.manhunt.dropSystem.DropManager;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.ArrayList;
import java.util.List;

public final class ContentProvider {

    private static final int STARTING_PHASE_THRESHOLD = 50;
    private static final int PREPARATION_PHASE_THRESHOLD = 60;
    private static final int HEAD_START_OFFSET = 60;
    private static final String SPACER = "   ";

    private final ManhuntPlayer manhuntPlayer;

    public ContentProvider(ManhuntPlayer manhuntPlayer){
        this.manhuntPlayer=manhuntPlayer;
    }

    public List<String> getLines(int time){
        GameState gameState = manhuntPlayer.getManhuntGame().getGameState();
        List<String> textLines = new ArrayList<>();
        textLines.add(" ");
        textLines.add("§8──────────── ");
        switch (gameState){
            case STARTING -> addStartingLines(textLines, time);
            case HEAD_START -> addHeadStartLines(textLines, time);
            case PLAYING, VILLAGE, STRONGHOLD, TRANSITION -> addGameplayLines(textLines);
        }
        textLines.add("§8  ");
        return textLines;
    }

    private void addStartingLines(List<String> lines, int time) {
        if (time < STARTING_PHASE_THRESHOLD) {
            lines.addAll(List.of(
                    "§7Preparing World",
                    "§aStarting in §f" + (STARTING_PHASE_THRESHOLD - time) + "s",
                    "§8────────────",
                    "§7Players: " + getOnlinePlayerCount()
            ));
        } else {
            lines.addAll(List.of(
                    "§7Prepare Yourself",
                    "§aStarting in §f" + (PREPARATION_PHASE_THRESHOLD - time) + "s",
                    "§8────────────",
                    "§7Players: " + getOnlinePlayerCount()
            ));
        }
    }

    private void addHeadStartLines(List<String> lines, int time){
        int elapsed = time - HEAD_START_OFFSET;
        int headStartDurationSec = manhuntPlayer.getManhuntGame().getHeadStartTimeInSec();
        int remainingTime = headStartDurationSec - elapsed;

        lines.addAll(List.of(
                ChatColor.YELLOW + "Head Start",
                "§7➤ §f" + formatTime(remainingTime),
                "§8────────────",
                "§7Hunters Frozen"
        ));
    }

    private void addGameplayLines(List<String> lines){
        DropManager dropManager = manhuntPlayer.getManhuntGame().getGameManager().getDropManager();
        int remaining = dropManager.getNextDropSeconds();

        lines.add(ChatColor.AQUA + "AirDrop");

        if (!dropManager.canSummonDrop()) {
            lines.add("§7➤ §fOnHold");
        } else {
            lines.add("§7➤ §f" + formatTime(remaining));
        }

        addAbilitiesLines(lines);
        lines.add("§8────────────");
        addCoordinatesLines(lines, dropManager);
    }

    private void addAbilitiesLines(List<String> lines){
        Avatar avatar = manhuntPlayer.getAvatar();
        if(avatar==null) return;

        boolean firstAbility = true;
        for(Ability ability: avatar.getAbilities()){
            if(!(ability instanceof ActiveAbility activeAbility)) continue;

            if(firstAbility){
                lines.add(SPACER);
                firstAbility=false;
            }

            String abilityStatus = getAbilityStatus(activeAbility);
            lines.add(ChatColor.AQUA + activeAbility.getName().toUpperCase() + ": " + abilityStatus);
        }
    }

    private String getAbilityStatus(ActiveAbility ability){
        int timeElapsed = ability.getTimeElapsedAfterLastUse();
        int cooldownSec = ability.getCooldownSec();
        int remainingTime = ability.getRemainingTime();

        if(timeElapsed < cooldownSec){
            return remainingTime+"s";
        } else return ChatColor.GREEN + "Ready";
    }

    private void addCoordinatesLines(List<String> lines, DropManager dropManager){
        String x = formatCoordinate(dropManager.getCurrentDropX());
        String z = formatCoordinate(dropManager.getCurrentDropZ());

        lines.add(ChatColor.GREEN + "X: §f " + x);
        lines.add(ChatColor.GREEN + "Z: §f " + z);
    }

    private String formatCoordinate(int coord){
        return coord == 0 ? "-" : String.valueOf(coord);
    }

    private String formatTime(int seconds){
        return String.format("%02d:%02d", seconds/60, seconds%60);
    }

    private int getOnlinePlayerCount(){
        return Bukkit.getOnlinePlayers().size();
    }
}
