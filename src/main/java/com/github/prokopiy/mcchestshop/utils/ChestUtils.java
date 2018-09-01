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
//    private Main plugin;

    public static boolean containsItem(Inventory chest, String itemID, int count){
        int cnt = 0;

        for (Inventory slot : chest.slots()) {
            if (slot.peek().isPresent()) {
                if (Id.getItemStackID(slot.peek().get()).equals(itemID)) cnt += slot.peek().get().getQuantity();

            }
        }


        return cnt >= count;
    }

//    public static boolean containsItem(TileEntityChest chest, ItemStackSnapshot snapshot)
//    {
//        int foundItems = 0;
//        Item item = Item.getByNameOrId(snapshot.getType().getId());
//
//        if (item != null)
//        {
//            for (int i = 0; i < chest.getSizeInventory(); i++)
//            {
//                ItemStack stack = chest.getStackInSlot(i);
//
//                if (stack != null && stack.getItem().equals(item)) // TODO: Metadata && stack.getMetadata() == snapshot.)
//                {
//                    // getStackSize
//                    foundItems += stack.stackSize;
//
//                    if (foundItems >= snapshot.getCount())
//                    {
//                        return true;
//                    }
//                }
//            }
//        }
//
//        return false;
//    }
//
//    public static void removeItems(TileEntityChest chest, ItemStackSnapshot snapshot)
//    {
//        int neededItems = snapshot.getCount();
//        int foundItems = 0;
//        Item item = Item.getByNameOrId(snapshot.getType().getId());
//
//        if (item != null)
//        {
//            for (int i = 0; i < chest.getSizeInventory(); i++)
//            {
//                ItemStack stack = chest.getStackInSlot(i);
//
//                if (stack != null && stack.getItem().equals(item)) // TODO: Metadata && stack.getMetadata()
//                {
//                    if (neededItems >= foundItems + stack.stackSize)
//                    {
//                        chest.removeStackFromSlot(i);
//                        foundItems += stack.stackSize;
//                    }
//                    else
//                    {
//                        int amount = (foundItems + stack.stackSize) - neededItems;
//                        stack.stackSize = amount;
//                        foundItems = neededItems;
//                    }
//                }
//
//                if (foundItems == neededItems)
//                {
//                    return;
//                }
//            }
//        }
//    }
}
