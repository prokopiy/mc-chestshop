package com.github.prokopiy.mcchestshop.utils;

//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntityChest;
import com.github.prokopiy.mcchestshop.Main;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class ChestUtils {

    public static boolean containsItem(Inventory chest, String itemID, int count){
        int cnt = 0;

        for (Inventory slot : chest.slots()) {
            if (slot.peek().isPresent()) {
                if (Id.getItemStackID(slot.peek().get()).equals(itemID)) cnt += slot.peek().get().getQuantity();

            }
        }


        return cnt >= count;
    }

    public static boolean canPut(Inventory chest, String itemID, int count){
        int cnt = 0;

//        plugin.getLogger().info("canPut: chest size=" + String.valueOf(chest.size()));
        for (Inventory slot : chest.slots()) {
//            plugin.getLogger().info("canPut:" + String.valueOf(slot.size()));
            if (slot.size() == 0) return true;
            if (slot.peek().isPresent()) {
                if (slot.peek().get().isEmpty()) {
//                    plugin.getLogger().info("canPut: slot empty");

                    cnt+=count;

                }

//                if (Id.getItemStackID(slot.peek().get()).equals(itemID)) {
//                    cnt += slot.peek().get().getMaxStackQuantity() - slot.peek().get().getQuantity();
//                }
            }
        }
        return cnt >= count;
    }



}
