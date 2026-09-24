package org.velas.manhunt.dropSystem.loot;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class LootItem {

    private final ItemStack item;
    private final Material material;
    private final int amount;
    private final int weight;
    private final Map<Enchantment, Integer> enchantments;


    // Normal items with no enchants
    public LootItem(Material material, int weight, int minAmount, int maxAmount){
        this.item = null;
        this.material = material;
        this.weight = weight;
        this.amount = new Random().nextInt(minAmount, maxAmount+1);
        this.enchantments = null;
    }


    // ItemStack (used in default minecraft chests to drops)
    public LootItem(ItemStack item){
        this.item = item;
        this.material = item.getType();
        this.weight = 0;
        this.amount = item.getAmount();
        this.enchantments = item.getEnchantments();
    }


    // Items with enchants
    public LootItem(Material material, int weight, int minAmount, int maxAmount, Map<Enchantment, Integer> enchantments){
        this.item = null;
        this.material = material;
        this.weight = weight;
        this.amount = new Random().nextInt(minAmount, maxAmount+1);
        this.enchantments = enchantments;
    }

    public Material getMaterial(){
        return this.material;
    }

    public int getAmount(){
        return this.amount;
    }

    public int getWeight(){
        return this.weight;
    }

    public Map<Enchantment, Integer> getEnchantments(){

        if(this.enchantments == null) return null;

        return Collections.unmodifiableMap(this.enchantments);
    }

    public ItemStack getItem(){
        return this.item;
    }
}
