package org.velas.manhunt.dropSystem;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.drops.*;
import org.velas.manhunt.dropSystem.loot.LootItem;
import org.velas.manhunt.dropSystem.lootProvider.*;

import java.util.List;


public enum AirDropType {

    RESPAWN(RespawnDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new RespawnDrop(dropLoc));
        }

    }, DESERT(DesertDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new DesertDrop(dropLoc));
        }

    }, RUINED(RuinedDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new RuinedDrop(dropLoc));
        }

    }, MINING(MiningDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new MiningDrop(dropLoc));
        }

    }, COMBAT(CombatDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new CombatDrop(dropLoc));
        }

    }, END(EndDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new EndDrop(dropLoc));
        }

    }, NETHER(NetherDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new NetherDrop(dropLoc));
        }

    }, ANCIENT(AncientDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new AncientDrop(dropLoc));
        }

    }, GATEWAY(GatewayDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new GatewayDrop(dropLoc));
        }

    }, EARLY_COMBAT(EarlyCombatDLP.INSTANCE) {
        @Override
        public void create(Location dropLoc) {
            new CargoDragon(new EarlyCombatDrop(dropLoc));
        }

    };

    private final LootProvider lootProvider;

    AirDropType(LootProvider lootProvider){
        this.lootProvider = lootProvider;
    }

    public abstract void create(Location dropLoc);

    public List<LootItem> generateRandomLoot(Location location){

        if(location == null)
            throw new IllegalArgumentException("Location cannot be null for loot generation");

        return this.lootProvider.generateLoot(location);
    }
}
