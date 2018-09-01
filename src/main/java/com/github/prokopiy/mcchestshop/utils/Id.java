package com.github.prokopiy.mcchestshop.utils;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class Id {

    public static String getLocationID(Location<World> location) {
        if (location == null) {
            return null;
        } else {
            String itemID = null;
            if (location.getTileEntity().isPresent()) {
                try {
                    itemID = location.getTileEntity().get().getType().getId().toLowerCase().replaceAll(" ", "_");
                } catch (Exception e) {};
            } else {
                try {
                    itemID = location.getBlockType().getName().toLowerCase().replaceAll(" ", "_");
                } catch (Exception e) {};
            }
            return itemID;
        }
    }

    public static String getItemStackID(ItemStack itemStack) {
//        final List<ItemData> items = new ArrayList<ItemData>(plugin.getItemData());
        DataContainer container = itemStack.toContainer();
        DataQuery query = DataQuery.of('/', "UnsafeDamage");
        String itemID = itemStack.getType().getId();

        int unsafeDamage = 0;
        if (container.get(query).isPresent()) {
            unsafeDamage = Integer.parseInt(container.get(query).get().toString());
        }
        if (unsafeDamage != 0) {
            itemID = itemID + ":" + unsafeDamage;
        }
        return itemID;
    }
}
