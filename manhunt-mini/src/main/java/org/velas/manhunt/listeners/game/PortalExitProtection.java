package org.velas.manhunt.listeners.game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.velas.manhunt.customItems.items.KeyToConvergence;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PortalExitProtection implements Listener {

    private static final long PROTECTION_DURATION_MS = 5_000L;

    private final Map<UUID, Long> protectedRunners =
            new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {

        Player player = event.getPlayer();
        KeyToConvergence.setCooldown(player.getUniqueId());

        ManhuntGame game = ManhuntGame.getCurrentGame();

        ManhuntPlayer manhuntPlayer = game.getGameManager()
                .getPlayerManager()
                .getManhuntPlayer(player);

        // Only protect the Runner, not Hunters.
        // This assumes non-Hunter game players are Runners.
        if (manhuntPlayer == null || manhuntPlayer.isHunter()) {
            return;
        }

        // Start the protection timer when portal travel is triggered.
        protectedRunners.put(
                player.getUniqueId(),
                System.currentTimeMillis() + PROTECTION_DURATION_MS
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        Player attacker = getAttackingPlayer(event.getDamager());
        Entity victimEntity = event.getEntity();

        if (attacker == null || !(victimEntity instanceof Player victim)) {
            return;
        }

        // Check whether either participant is a protected Runner.
        boolean attackerProtected = isProtected(attacker);
        boolean victimProtected = isProtected(victim);

        if (!attackerProtected && !victimProtected) {
            return;
        }

        ManhuntGame game = ManhuntGame.getCurrentGame();

        ManhuntPlayer attackerData = game.getGameManager()
                .getPlayerManager()
                .getManhuntPlayer(attacker);

        ManhuntPlayer victimData = game.getGameManager()
                .getPlayerManager()
                .getManhuntPlayer(victim);

        if (attackerData == null || victimData == null) {
            return;
        }

        // Block combat between a protected Runner and a Hunter.
        boolean attackerIsRunner = !attackerData.isHunter();
        boolean victimIsRunner = !victimData.isHunter();

        boolean runnerAttackingHunter =
                attackerProtected
                        && attackerIsRunner
                        && victimData.isHunter();

        boolean hunterAttackingRunner =
                victimProtected
                        && victimIsRunner
                        && attackerData.isHunter();

        if (runnerAttackingHunter || hunterAttackingRunner) {
            event.setCancelled(true);
        }
    }

    private boolean isProtected(Player player) {

        UUID uuid = player.getUniqueId();
        Long expiry = protectedRunners.get(uuid);

        if (expiry == null) {
            return false;
        }

        if (System.currentTimeMillis() >= expiry) {
            protectedRunners.remove(uuid, expiry);
            return false;
        }

        return true;
    }

    private Player getAttackingPlayer(Entity damager) {

        if (damager instanceof Player player) {
            return player;
        }

        // Also handle player-fired arrows, tridents, and other projectiles.
        if (damager instanceof Projectile projectile
                && projectile.getShooter() instanceof Player player) {
            return player;
        }

        return null;
    }
}