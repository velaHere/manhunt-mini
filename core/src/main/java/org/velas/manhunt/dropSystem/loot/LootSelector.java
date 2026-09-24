package org.velas.manhunt.dropSystem.loot;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootSelector {

    public static List<LootItem> selectFromGroup(LootItemGroup group){

        List<LootItem> items = group.items();
        int noOfItemsToSelect = group.noOfItemsToSelect();

        if (noOfItemsToSelect > items.size())
            throw new IllegalArgumentException(
                    "Cannot select " + noOfItemsToSelect + " items from a pool of " + items.size()
            );

        List<LootItem> result = new ArrayList<>();
        Random random = new Random();

        List<LootItem> pool = new ArrayList<>(items);


        for(int i = 0; i<noOfItemsToSelect; i++){

            int total = 0;
            for(LootItem item: pool) total += item.getWeight();

            int r = random.nextInt(total);
            LootItem chosen = null;

            for (LootItem item: pool){
                r -= item.getWeight();
                if(r<0){
                    chosen = item;
                    break;
                }
            }

            if (chosen != null){
                result.add(chosen);
                pool.remove(chosen);
            }
        }

        return result;
    }

    public static List<LootItem> selectFromGroups(List<LootItemGroup> groups, int noOfItemsToSelect){

        List<LootItem> pool = new ArrayList<>();

        for(LootItemGroup group: groups){
            pool.addAll(group.items());
        }

        if (noOfItemsToSelect > pool.size()) {
            throw new IllegalArgumentException(
                    "Cannot select " + noOfItemsToSelect + " items from a combined pool of " + pool.size()
            );
        }

        return selectFromGroup(new LootItemGroup(pool, noOfItemsToSelect));
    }
}
