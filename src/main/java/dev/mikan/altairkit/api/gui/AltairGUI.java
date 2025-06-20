package dev.mikan.altairkit.api.gui;

import dev.mikan.altairkit.AltairKit;
import dev.mikan.altairkit.utils.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public abstract class AltairGUI implements Singleton {

    protected ItemStack fillerItem;
    protected String title;
    protected int slots;
    protected Inventory gui;



    public AltairGUI(String title, int slots) {

        this.title = AltairKit.colorize(title);
        this.slots = slots;

    }




    public void show(Player player){
        Data.addToMap(player,title);
        player.openInventory(this.gui);

    }

    public void build(ItemStack fillerItem,boolean fill){
        if (fillerItem != null && !fill) {
            createSurroundedPage();
            return;
        }
        createGUI();
        if (fill){
            for (int i = 0; i < slots; i++) {
                if (this.gui.getItem(i) == null) this.gui.setItem(i,fillerItem);
            }
        }
    }

    /*
    * reset title without resetting the items inside
    * */
    protected void changeTitle(String title){
        this.title = AltairKit.colorize(title);
        if (fillerItem != null) createSurroundedPage();
        else createGUI();
    }

    private void createGUI(){
        this.gui = Bukkit.createInventory(null, slots, title);
    }



    protected void createSurroundedPage() {

        this.gui = Bukkit.createInventory(null, slots, title);

        if (this.fillerItem == null) return;

        switch (slots){
            case 54:
                load54SlotsGUI();
                break;
            case 45:
                load45SlotsGUI();
                break;
            case 36:
                load36SlotsGUI();
                break;
            case 27:
                load27SlotsGUI();
                break;
            case 18:
                load18SlotsGUI();
                break;
            case 9:
                load9SlotsGUI();
                break;
        }

    }




    private void load9SlotsGUI(){
        for(int i = 0; i < this.slots; ++i)
            if (i == 4)
                this.gui.setItem(i, this.fillerItem);
    }





    private void load18SlotsGUI(){
        for(int i = 0; i < this.slots; ++i)
            if (i == 0 || i == 8 || i == 9 || i == 17)
                this.gui.setItem(i, this.fillerItem);
    }




    private void load27SlotsGUI(){
        for(int i = 0; i < this.slots; ++i)
            if (i <= 9 || i >= 17)
                this.gui.setItem(i, this.fillerItem);
    }




    private void load36SlotsGUI(){
        for(int i = 0; i < this.slots; ++i)
            if (i <= 9 || i == 18 || i >= 26 || i == 17)
                this.gui.setItem(i, this.fillerItem);
    }



    private void load45SlotsGUI(){
        for(int i = 0; i < this.slots; ++i)
            if (i <= 9 || i == 18 || i == 27 || i == 26 || i == 17 || i >= 35)
                this.gui.setItem(i, this.fillerItem);
    }




    private void load54SlotsGUI(){
        for(int i = 0; i < this.slots; ++i)
            if (i <= 9 || i == 18 || i == 27 || i == 26 || i == 17 || i == 36 || i == 35 || i >= 44)
                this.gui.setItem(i, this.fillerItem);
    }

}
