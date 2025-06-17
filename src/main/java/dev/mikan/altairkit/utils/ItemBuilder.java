package dev.mikan.altairkit.utils;

import dev.mikan.altairkit.AltairKit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(ItemStack item){
        this.item = item;
    }

    public ItemBuilder(Material material){
        this.item = new ItemStack(material);
    }

    public ItemBuilder setMaterial(Material material){
        this.item.setType(material);
        return this;
    }

    public ItemStack toItemStack(){
        return this.item;
    }


    public ItemBuilder setLore(List<String> lore){


        ItemMeta meta = this.item.getItemMeta();
        if (lore == null)
            meta.setLore(new ArrayList<>());
        else {
            lore.forEach(AltairKit::colorize);
            meta.setLore(lore);
        }
        this.item.setItemMeta(meta);
        return this;
    }


    public ItemBuilder setName(String name){
        name = AltairKit.colorize(name);
        ItemMeta meta = this.item.getItemMeta();

        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setData(byte data){
        ItemMeta meta = this.item.getItemMeta();
        this.item = new ItemStack(this.item.getType(), this.item.getAmount(), (short) 0, data);
        item.setItemMeta(meta);
        return this;
    }

    public static ItemStack setMaterial(ItemStack item, Material material){
        item.setType(material);
        return item;
    }

    public static ItemStack setLore(ItemStack item, List<String> lore){

        ItemMeta meta = item.getItemMeta();
        if (lore == null)
            meta.setLore(new ArrayList<>());
        else{
            lore.forEach(AltairKit::colorize);
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setName(ItemStack item, String name){
        name = AltairKit.colorize(name);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setLoreAndName(ItemStack item, String name,List<String> lore){
        name = AltairKit.colorize(name);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore == null)
            meta.setLore(new ArrayList<>());
        else {
            lore.forEach(AltairKit::colorize);
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }


    public static ItemStack setData(ItemStack item,byte data){
        item.getData().setData(data);
        return item;
    }

}
