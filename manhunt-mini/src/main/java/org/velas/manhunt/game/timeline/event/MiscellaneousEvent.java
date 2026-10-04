package org.velas.manhunt.game.timeline.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.game.timeline.GameTimeLine;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.manhunt.player.Runner;

public final class MiscellaneousEvent extends BaseMiscellaneousEvent {

    private final GameTimeLine gameTimeLine;
    private final ManhuntManager gameManager;

    public MiscellaneousEvent(@NotNull ManhuntGame game) {
        super(game);
        this.gameManager=game.getGameManager();
        this.gameTimeLine =this.gameManager.getGameTimeLine();
    }

    @Override
    public void execute() {
        if(this.gameTimeLine.getStrongholdTime() == -1) return;
        if(!this.game.getGameState().equals(GameState.TRANSITION)) return;

        int timeElapsedAfterStrongholdRequest = this.gameTimeLine.getTimeElapsedAfterStrongholdRequest();

        if (timeElapsedAfterStrongholdRequest >= 10 && timeElapsedAfterStrongholdRequest <=60) {
            GameFlowManager.showTransitionTitles(timeElapsedAfterStrongholdRequest-1);
        }

        if (timeElapsedAfterStrongholdRequest == 10) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                GameFlowManager.tpPlayerToLobbyForTransition(player);
                GameFlowManager.playIntenseVoidEffect(player);
            });
        }
        /*else if(timeElapsedAfterStrongholdRequest == 50){

        }*/else if(timeElapsedAfterStrongholdRequest == 15){
            this.gameManager.getWorldManager().setSpawnToStronghold();
        }else if(timeElapsedAfterStrongholdRequest == 25){
            this.gameManager.getWorldManager().setWorldBorders();
            this.gameManager.getWorldManager().preloadAsync(1);
            this.gameManager.getWorldManager().preloadAsync(
                    1,
                    this.gameManager.getWorldManager()
                            .getOverworld().getSpawnLocation().clone().add(100, 0, 0)
            );
        } else if (timeElapsedAfterStrongholdRequest == 60){

            Bukkit.getOnlinePlayers().forEach(player ->{

                ManhuntPlayer manhuntPlayer = this.gameManager.getPlayerManager().getManhuntPlayer(player);

                if(manhuntPlayer instanceof Runner){
                    Location finalLoc;
                    finalLoc = this.gameManager.getWorldManager().getOverworld().getSpawnLocation().clone().add(100, 0, 0);
                    finalLoc.setY(this.gameManager.getWorldManager().getOverworld().getHighestBlockYAt(finalLoc));
                    finalLoc.add(0, 8, 0);
                    player.teleport(finalLoc);
                }else{
                    player.teleport(this.gameManager.getWorldManager().getOverworld()
                            .getSpawnLocation().clone().add(0, 8, 0));
                }

                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }

                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.SLOW_FALLING,
                        20*4, 0, false, false
                ));
            });
            Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () ->
                    this.game.setGameState(GameState.STRONGHOLD), 5L);
        }
    }
}
