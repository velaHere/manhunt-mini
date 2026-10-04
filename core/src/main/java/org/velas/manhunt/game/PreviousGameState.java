package org.velas.manhunt.game;

import org.bukkit.generator.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PreviousGameState {

    private static final Map<UUID, Team> PLAYERS_IN_TEAM = new HashMap<>();
    private static long SEED;
    private static int HEAD_START_TIME;
    private static @Nullable Structure VILLAGE;

    private PreviousGameState() {}

    public static void saveState(ManhuntGame game) {
        PLAYERS_IN_TEAM.clear();

        HEAD_START_TIME = game.getHeadStartTimeInSec();
        SEED = game.getGameManager().getWorldManager().getSeed();
        VILLAGE = game.getGameManager().getWorldManager().getVillage();

        game.getGameManager().getPlayerManager().getPlayers().forEach(player -> {
            if(player.isHunter()) PLAYERS_IN_TEAM.put(player.getUniqueId(), Team.HUNTER);
            else if(player.isRunner()) PLAYERS_IN_TEAM.put(player.getUniqueId(), Team.RUNNER);
            else PLAYERS_IN_TEAM.put(player.getUniqueId(), Team.SPECTATOR);
        });
    }

    public static long seed() {
        return SEED;
    }

    public static int headStart() {
        return HEAD_START_TIME;
    }

    @Nullable
    public static Structure village() {
        return VILLAGE;
    }

    public static Map<UUID, Team> getPlayerTeams() {
        return new HashMap<>(PLAYERS_IN_TEAM);
    }
}
