package com.github.prokopiy.mcchestshop;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.block.DirectionalData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import sun.management.snmp.jvmmib.JvmThreadingMeta;

import java.util.Optional;
import java.util.regex.Pattern;


public class EventListener {

    private static final Pattern PATTERN_META = Pattern.compile("\\.[\\d+]*$");

    private Main plugin;
    public EventListener(Main instance) {
        plugin = instance;
    }


    @Listener
    public void onChangeSignEvent(ChangeSignEvent event, @Root Player player) {
        player.sendMessage(plugin.fromLegacy("&conChangeSignEvent"));
//        if (event.getTransactions().get(0).getFinal().getState().getType().equals(BlockTypes.AIR)) {return;}


        Sign sign = event.getTargetTile();
        player.sendMessage(plugin.fromLegacy("getKeys: " + sign.getKeys().toString()));

        player.sendMessage(plugin.fromLegacy(getSignDirection(sign).toString()));


    }

    public Direction getSignDirection(Sign sign) {

//        Location<World> blockLoc = sign.getLocation();
        return sign.getLocation().get(DirectionalData.class).get().get(Keys.DIRECTION).get();

//        Optional<DirectionalData> optionalData = blockLoc.get(DirectionalData.class);
//        if (optionalData.isPresent()) {
//            DirectionalData data = optionalData.get();
////            if (data.get(Keys.DIRECTION).get().equals(Direction. .NORTH)) {
//            return data.get(Keys.DIRECTION).get();
//        } else {
//            return null;
//        }
    }

    public boolean isSign(TileEntity entity) {
        return entity.getType().equals(TileEntityTypes.SIGN);
    }

}
