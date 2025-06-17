package dev.mikan.altairkit.api.gui;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PageUtils {

    public static List<ItemStack> getPageItems(List<ItemStack> items, int page, int spaces) {
        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;
        List<ItemStack> itemStackList = new ArrayList<>();

        for(int i = lowerBound; i < upperBound; ++i) {
            try {
                itemStackList.add(items.get(i));
            } catch (IndexOutOfBoundsException var8) {
                break;
            }
        }

        return itemStackList;
    }

    public static boolean isPageValid(List<ItemStack> items, int page, int spaces) {
        if (page <= 0) {
            return false;
        } else {
            int upperBound = page * spaces;
            int lowerBound = upperBound - spaces;
            return items.size() > lowerBound;
        }
    }

}
