package org.velas.manhunt.ability;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.event.OnDamageAbility;
import org.velas.manhunt.ability.event.OnInteractAbility;
import org.velas.manhunt.avatar.Avatar;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.event.ManhuntMatchStopEvent;
import org.velas.manhunt.event.ManhuntMatchStartEvent;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.List;

public final class AbilityDispatcher implements Listener {

    private final ManhuntManager manager;
    private final Resolver resolver;
    private boolean isInit = false;

    public AbilityDispatcher(@NotNull ManhuntManager manager){
        this.manager=manager;
        this.resolver=this.generateResolver();
    }

    public interface Resolver {
        @Nullable ManhuntPlayer getManhuntPlayer(Player player);
        @Nullable Avatar getAvatar(ManhuntPlayer mp);
    }

    public void init(){
        if(this.isInit) return;
        Bukkit.getPluginManager().registerEvents(this, Manhunt.getINSTANCE());
        this.isInit=true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        ManhuntPlayer mp = resolver.getManhuntPlayer(event.getPlayer());
        if (mp == null) return;

        ManhuntGame game = mp.getManhuntGame();
        if(!game.isRunning()) return;
        if(mp.getManhuntGame().getGameState().equals(GameState.TRANSITION)) return;

        Avatar avatar = resolver.getAvatar(mp);
        if (avatar == null) return;

        ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) return;

        for (Ability a : avatar.getAbilities()) {
            if (a instanceof OnInteractAbility ia) {
                if(CustomItemType.matches(itemInMainHand, CustomItemType.PRIMARY_ABILITY_ACTIVATOR)) {
                    if(a.isPrimaryActive()) ia.onInteract(event);
                    event.setCancelled(true);
                }
                if(CustomItemType.matches(itemInMainHand, CustomItemType.SECONDARY_ABILITY_ACTIVATOR)) {
                    if(a.isSecondaryActive()) ia.onInteract(event);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;

        ManhuntPlayer mp = resolver.getManhuntPlayer(damager);
        if (mp == null) return;

        Avatar avatar = resolver.getAvatar(mp);
        if (avatar == null) return;

        for (Ability a : avatar.getAbilities()) {
            if (a instanceof OnDamageAbility da) {
                da.onDamage(event);
            }
        }
    }

    @EventHandler
    public void onGameStart(ManhuntMatchStartEvent event) {
        List<ManhuntPlayer> players = event.getGame().getGameManager().getPlayerManager().getPlayers();
        players.forEach(player -> {
            Avatar avatar = resolver.getAvatar(player);
            if(avatar==null) return;
            for(Ability ability: avatar.getAbilities()) {
                if(ability instanceof PassiveAbility pa)
                    pa.onGrant();
                if(ability instanceof Grantable g)
                    g.onGrant();
            }
        });
    }

    @EventHandler
    public void onMatchStop(ManhuntMatchStopEvent event) {
        List<ManhuntPlayer> players = event.getGame().getGameManager().getPlayerManager().getPlayers();
        players.forEach(player -> {
            Avatar avatar = resolver.getAvatar(player);
            if(avatar==null) return;
            for(Ability ability: avatar.getAbilities()) {
                if(ability instanceof PassiveAbility pa)
                    pa.onGrant();
                if(ability instanceof Grantable g)
                    g.onRevoke();
            }
        });
    }

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent event) {
        grantPassiveAbilities(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        grantPassiveAbilities(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        grantPassiveAbilities(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event){
        ManhuntPlayer mp = resolver.getManhuntPlayer(event.getEntity());
        if (mp == null) return;

        if(!mp.isRunner()) return;

        Avatar avatar = resolver.getAvatar(mp);
        if (avatar == null) return;

        for(Ability ability: avatar.getAbilities()) {
            if(ability instanceof Grantable g)
                g.onRevoke();
        }
    }

    private void grantPassiveAbilities(@NotNull Player player) {
        ManhuntPlayer mp = resolver.getManhuntPlayer(player);
        if (mp == null) return;

        Avatar avatar = resolver.getAvatar(mp);
        if (avatar == null) return;

        for (Ability a : avatar.getAbilities()) {
            if (a instanceof PassiveAbility pa) {
                pa.onGrant();
            }
        }
    }

    private Resolver generateResolver() {
        return new Resolver() {
            @Override
            public @Nullable ManhuntPlayer getManhuntPlayer(Player player) {
                return manager.getPlayerManager().getManhuntPlayer(player);
            }

            @Override
            public @Nullable Avatar getAvatar(@NotNull ManhuntPlayer mp) {
                return mp.getAvatar();
            }
        };
    }
}
