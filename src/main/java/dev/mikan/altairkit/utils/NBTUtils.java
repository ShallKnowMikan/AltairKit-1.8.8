package dev.mikan.altairkit.utils;


import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

@UtilityClass
public class NBTUtils {


    public org.bukkit.inventory.ItemStack set(org.bukkit.inventory.ItemStack item, String key ,Object value) {

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        if (value instanceof String)
            tag.setString(key, value.toString());
        else if (value instanceof Integer)
            tag.setInt(key, (Integer) value);
        else if (value instanceof Double)
            tag.setDouble(key, (Double) value);
        else if (value instanceof Byte)
            tag.setByte(key, (Byte) value);
        else if (value instanceof Float)
            tag.setFloat(key, (Float) value);
        else if (value instanceof Boolean)
            tag.setBoolean(key, (Boolean) value);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public Object value(org.bukkit.inventory.ItemStack item,String key,Class<?> type) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (nmsItem.hasTag()) {
            NBTTagCompound tag = nmsItem.getTag();
            if (tag.hasKey(key)) {
                if (type.isAssignableFrom(String.class))
                    return tag.getString(key);
                else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class))
                    return tag.getInt(key);
                else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class))
                    return tag.getDouble(key);
                else if (type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class))
                    return tag.getByte(key);
                else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class))
                    return tag.getFloat(key);
                else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class))
                    return tag.getBoolean(key);

            }
        }
        return null;
    }


    public org.bukkit.inventory.ItemStack remove(org.bukkit.inventory.ItemStack item,String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (nmsItem.hasTag()) {
            NBTTagCompound tag = nmsItem.getTag();

            if (tag.hasKey(key)) {
                tag.remove(key);
                nmsItem.setTag(tag);
            }
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public boolean hasKey(org.bukkit.inventory.ItemStack item,String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        return nmsItem.hasTag();
    }
}


