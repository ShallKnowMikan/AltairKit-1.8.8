package dev.mikan.altairkit.utils;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class PlayerHead {

    private ItemStack skullItem = new ItemStack(Material.SKULL_ITEM,1, (short) 3);

    public PlayerHead(Player player) throws NoSuchFieldException, IllegalAccessException {

        GameProfile profile = ((CraftPlayer)player).getProfile();
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
        Field field = meta.getClass().getDeclaredField("profile");
        field.setAccessible(true);
        field.set(meta,profile);

        skullItem.setItemMeta(meta);
    }

    public PlayerHead(UUID playerUUID) throws NoSuchFieldException, IllegalAccessException {

        OfflinePlayer player = Bukkit.getPlayer(playerUUID);

        GameProfile profile = ((CraftOfflinePlayer)player).getProfile();
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
        Field field = meta.getClass().getDeclaredField("profile");
        field.setAccessible(true);
        field.set(meta,profile);

        skullItem.setItemMeta(meta);
    }

    public PlayerHead(OfflinePlayer player) throws NoSuchFieldException, IllegalAccessException {

        GameProfile profile = ((CraftOfflinePlayer)player).getProfile();
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
        Field field = meta.getClass().getDeclaredField("profile");
        field.setAccessible(true);
        field.set(meta,profile);

        skullItem.setItemMeta(meta);
    }

    public ItemStack getSkullItem() {
        return skullItem;
    }
}
