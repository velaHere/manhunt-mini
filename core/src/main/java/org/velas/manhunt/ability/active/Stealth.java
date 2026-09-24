package org.velas.manhunt.ability.active;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.ability.event.OnDamageAbility;
import org.velas.manhunt.ability.event.OnInteractAbility;
import org.velas.manhunt.avatar.Avatar;

public final class Stealth extends ActiveAbility implements OnInteractAbility, OnDamageAbility {

    private final int durationTicks;
    private boolean active = false;
    private boolean breakOnGetDamage = false;
    private BukkitTask informer;

    public Stealth(Avatar avatar, AbilityType type) {
        super("stealth", avatar, type, 30);
        durationTicks =7*20;
    }

    public Stealth setBreakOnGetDamage(boolean should) {
        this.breakOnGetDamage=should;
        return this;
    }

    @Override
    protected boolean onActivate(Event event) {

        if (!(event instanceof PlayerInteractEvent event1)) return false;
        if (!event1.getAction().equals(Action.RIGHT_CLICK_AIR) && !event1.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return false;
        Player player = event1.getPlayer();
        if(!player.isSneaking()) return false;

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.INVISIBILITY,
                durationTicks,
                0,
                false,
                false
        ));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.3f);
        player.spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 20, 0.3, 0.1, 0.3, 0);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aStealth Activated!"));

        active=true;

        this.informer = Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {
            active=false;
            if (player.isOnline()) {
                this.inform(player);
            }
        }, durationTicks);
        return true;
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!active) return;
        Player owner = avatar.getManhuntPlayer().getPlayer();
        if (owner == null) return;
        if (!event.getDamager().equals(owner)) return;
        else if (event.getEntity().equals(owner)) {
            if(!breakOnGetDamage) return;
        }

        if(this.informer!= null && !this.informer.isCancelled()) {
            this.informer.cancel();
            this.inform(owner);
        }
        owner.removePotionEffect(PotionEffectType.INVISIBILITY);
        owner.playSound(owner.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.6f, 1.2f);
        owner.spawnParticle(Particle.ASH, owner.getLocation().add(0, 1.0, 0), 10, 0.2, 0.2, 0.2, 0.01);
        active = false;
    }

    private void inform(@NotNull Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 0.5f, 0.7f);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Stealth Ended"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        tryActivate(event);
    }
}
