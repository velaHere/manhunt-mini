package org.velas.manhunt.game.timeline.event;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.*;
import org.velas.manhunt.menusystem.menu.ClassSelection;
import org.velas.manhunt.player.ManhuntPlayer;

public final class BaseManhuntGameStartEvent extends TimelineEvent {

    public BaseManhuntGameStartEvent(@NotNull ManhuntGame game) {
        super(game, 0);
    }

    @Override
    public void execute() {
        this.game.setGameState(GameState.STARTING);
        ManhuntManager gameManager = game.getGameManager();
        gameManager.getGameFlowManager().setupPlayers();
        for (ManhuntPlayer player : gameManager.getPlayerManager().getPlayers()){
            Player p = player.getPlayer();
            if(p==null) continue;
            player.getSidebar().show();
            GameFlowManager.resetPlayer(p);

            if(player.isSpectator()) continue;
            if(!Settings.getConfigSettings().isALLOW_ABILITIES()) continue;

            p.getInventory().addItem(CustomItemType.create(CustomItemType.AVATAR_SIGIL));
            new ClassSelection(Manhunt.getPlayerMenuUtility(p)).open();
        }
        PreviousGameState.saveState(game);
    }
}
