package org.velas.manhunt.dropSystem;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.dropSystem.drops.AirDrop;

public class CargoDragon extends BukkitRunnable {

    private final EnderDragon dragon;
    private final World world;
    private final Location current;
    private final Location end;
    private final Location dropLoc;
    private BossBar bar;
    private final AirDrop drop;

    public CargoDragon(AirDrop drop){

        Location start = drop.getInitialDropLoc().clone().add(0, 0, 120);
        this.end = drop.getInitialDropLoc().clone().add(0, 0, -120);
        this.current = start.clone();
        this.world = drop.getInitialDropLoc().getWorld();
        this.dropLoc = drop.getInitialDropLoc();
        this.drop = drop;
        if(world==null)
            throw new RuntimeException("World was null at the time of cargo spawned");
        this.dragon = world.spawn(start, EnderDragon.class);
        this.dragon.setPhase(EnderDragon.Phase.HOVER);
        this.dragon.setInvulnerable(true);

        announceDragonAppearance();

        this.runTaskTimer(Manhunt.getINSTANCE(), 0L, 1L);
    }

    @Override
    public void run() {

        // If reached exit → remove dragon
        if (current.distance(end) < 1) {
            playDragonPortalEffect(end, world);
            bar.removeAll();
            bar.setVisible(false);
            dragon.remove();
            this.cancel();
            return;
        }

        // Move a small step each tick
        double speed = 0.5;
        double newZ = current.getZ() - speed;
        current.setZ(newZ);

        dragon.teleport(current);

        if (Math.abs(current.getZ() - dropLoc.getZ()) < speed) {
            this.drop.spawnDrop();
        }
    }

    private void announceDragonAppearance() {

        String playerName = this.drop.getPlayerName();
        String dropName = this.drop.getName();
        Location loc = this.drop.getInitialDropLoc();

        if (dropName.equals("Flare")) {

            this.bar = Bukkit.createBossBar(
                    "§d§l⚡ " + dropName + " has ARRIVED! ⚡", // Pink & bold
                    BarColor.PURPLE,
                    BarStyle.SOLID
            );

            bar.setProgress(1.0);
            bar.setVisible(true);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(" ");
                p.sendMessage("§d§l[⚡ FLARE ALERT ⚡]");
                p.sendMessage("§bPlayer §e§l" + playerName + " §bhas summoned §d§l" + dropName + "!");
                p.sendMessage("§aLocation: §6X: §e" + loc.getBlockX() + " §6Z: §e" + loc.getBlockZ());
                p.sendMessage("§7Prepare yourself for an epic encounter!");
            }

        } else {
            this.bar = Bukkit.createBossBar(
                    "§6§l⚡ " + dropName + " Drop Arrived! ⚡", // Gold & bold
                    BarColor.YELLOW,
                    BarStyle.SOLID
            );
        }

        bar.setProgress(1.0);
        bar.setVisible(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            bar.addPlayer(p);
            p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 100f, 1f);
        }
    }

    private void playDragonPortalEffect(Location loc, World world) {

        // Portal + end magic + dragon breath swirl
        world.spawnParticle(Particle.PORTAL, loc, 300, 2, 2, 2, 0.2);
        world.spawnParticle(Particle.DRAGON_BREATH, loc, 150, 1.5, 1.5, 1.5, 0.1);
        world.spawnParticle(Particle.SPELL_WITCH, loc, 80, 1.2, 2, 1.2, 0.01);
        world.spawnParticle(Particle.END_ROD, loc, 40, 1, 1.5, 1, 0.0);

        // Sounds
        world.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 2f, 0.3f);
        world.playSound(loc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5f, 1.8f);
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1f, 0.6f);
    }
}
