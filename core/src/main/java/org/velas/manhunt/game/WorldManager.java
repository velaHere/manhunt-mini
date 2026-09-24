package org.velas.manhunt.game;

import org.bukkit.*;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;

import java.io.File;
import java.util.Random;

public class WorldManager {

    private World overworld;
    private World netherWorld;
    private Location stronghold;
    private @Nullable final Structure village;

    public WorldManager(@Nullable Structure village) {
        this.village=village;
    }

    public World getOverworld(){return this.overworld;}
    public World getNetherWorld(){return this.netherWorld;}

    public void generateManhuntWorlds(){

        String worldName = Settings.OVERWORLD_NAME;
        String world_netherName = Settings.NETHER_WORLD_NAME;
        String world_the_endName = Settings.END_WORLD_NAME;

        String [] worldNames = {worldName, world_netherName, world_the_endName};

        for (String worldN: worldNames){

            Bukkit.unloadWorld(worldN, false);
        }

        for (String worldN: worldNames){

            File worldFile = new File(Bukkit.getWorldContainer(), worldN);
            deleteWorldFolder(worldFile);
        }

        createWorld(worldName, world_netherName, world_the_endName);
    }

    private void createWorld(String worldName, String world_netherName, String world_the_endName){

        long seed = new Random().nextLong();
        this.overworld = new WorldCreator(worldName).environment(World.Environment.NORMAL).seed(seed).createWorld();
        this.netherWorld = new WorldCreator(world_netherName).environment(World.Environment.NETHER).seed(seed).createWorld();
        new WorldCreator(world_the_endName).environment(World.Environment.THE_END).seed(seed).createWorld();
    }

    public void setSpawnToVillage(){

        StructureSearchResult strongholdSearch = this.overworld.locateNearestStructure(
                overworld.getSpawnLocation(), Structure.STRONGHOLD, 1000, false
        );

        if(strongholdSearch!=null) {
            this.stronghold=strongholdSearch.getLocation();
        }else {
            Utils.warning("Null Stronghold");
        }

        Structure tryVillage = getStructure();

        StructureSearchResult villageSearch = overworld.locateNearestStructure(
                overworld.getSpawnLocation(), tryVillage, 1000, false
        );

        if(villageSearch == null){
            villageSearch = overworld.locateNearestStructure(
                    overworld.getSpawnLocation(), Structure.VILLAGE_PLAINS, 1000, false);
        }

        if(villageSearch==null) return;

        Location strongholdLocation = villageSearch.getLocation();

        Location finalSpawnLocation = strongholdLocation.clone();
        finalSpawnLocation.setY(overworld.getHighestBlockYAt(strongholdLocation));

        overworld.setSpawnLocation(finalSpawnLocation);
        WorldBorder worldBorder = overworld.getWorldBorder();
        worldBorder.setCenter(finalSpawnLocation);
        worldBorder.setSize(260);

        WorldBorder netherWorldBorder = netherWorld.getWorldBorder();
        netherWorldBorder.setCenter(finalSpawnLocation.getX()/8, finalSpawnLocation.getZ()/8);
        netherWorldBorder.setSize(260);
    }

    private @NotNull Structure getStructure() {
        Structure tryVillage;

        if(this.village != null) {
            tryVillage = this.village;
        } else {
            Random random = new Random();
            int randomInt = random.nextInt(1, 6);

            switch (randomInt){
                case 1 -> tryVillage = Structure.VILLAGE_DESERT;
                case 2 -> tryVillage = Structure.VILLAGE_SAVANNA;
                case 3 -> tryVillage = Structure.VILLAGE_SNOWY;
                case 4 -> tryVillage = Structure.VILLAGE_TAIGA;
                default -> tryVillage = Structure.VILLAGE_PLAINS; // case 5
            }
        }
        return tryVillage;
    }

    public void setSpawnToStronghold(){

        Location finalSpawnLocation = stronghold.clone();
        finalSpawnLocation.setY(overworld.getHighestBlockYAt(stronghold));
        overworld.setSpawnLocation(finalSpawnLocation);
    }

    public void setWorldBorders(){

        Location finalSpawnLocation = stronghold.clone();
        finalSpawnLocation.setY(overworld.getHighestBlockYAt(stronghold));

        WorldBorder worldBorder = overworld.getWorldBorder();
        worldBorder.setCenter(finalSpawnLocation);
        worldBorder.setSize(260);

        WorldBorder netherWorldBorder = netherWorld.getWorldBorder();
        netherWorldBorder.setCenter(finalSpawnLocation.getX()/8, finalSpawnLocation.getZ()/8);
        netherWorldBorder.setSize(260);
    }

    public void preloadAsync(int radius){

        Location spawn = overworld.getSpawnLocation();
        int centreX=spawn.getBlockX() >> 4;
        int centreZ=spawn.getBlockZ() >> 4;

        Bukkit.getScheduler().runTaskTimer(Manhunt.getINSTANCE(), () -> {

            for(int x = -radius; x<=radius; x++){
                for(int z = -radius; z<=radius; z++){

                    int chunkX = centreX + x;
                    int chunkZ = centreZ + z;

                    Bukkit.getScheduler().runTask(Manhunt.getINSTANCE(), () -> {
                        overworld.getChunkAt(chunkX, chunkZ);
                        // work with chunk here
                    });
                }
            }
        }, 1L, 1L);
    }

    private void deleteWorldFolder(File file){

        if (!file.exists()) return;

        File [] array = file.listFiles();

        if (array == null) return;

        for (File f: array){
            if (f.isDirectory()) deleteWorldFolder(f);
            else f.delete();
        }
        file.delete();
    }
}
