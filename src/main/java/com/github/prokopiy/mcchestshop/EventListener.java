package com.github.prokopiy.mcchestshop;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.block.DirectionalData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import sun.management.snmp.jvmmib.JvmThreadingMeta;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;


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

        if (sign.getLocation().getBlockType().equals(BlockTypes.WALL_SIGN)) {
            player.sendMessage(plugin.fromLegacy("is WALL_SIGN"));
        }

//        player.getItemInHand(HandTypes.MAIN_HAND).get().createSnapshot().

        for (Text i : event.getText().lines()) {
            player.sendMessage(plugin.fromLegacy(i.toPlain()));
        }

        if (isValidSignChest(event.getText())){
            player.sendMessage(plugin.fromLegacy("is isValidSignChest"));
//            String owner = player.getName();

//            event.getText().lines().add(0, Text.of("add="+owner));
//            event.getText().lines().set(0, Text.of("add="+owner));
//            event.getOriginalText().lines().set(0, Text.of("ot add="+owner));
//            event.getTargetTile().lines().set(0, Text.of("tt add="+owner));
//            event.getTargetTile().getSignData().lines().set(0, Text.of("ttsd add="+owner));

//            setSignLine(event.getTargetTile(), 0, Text.of(owner));


            if (!event.getText().lines().get(0).toPlain().equals(player.getName()))
                event.setCancelled(true);
        }

    }


    @Listener
    public void onRightClick(InteractBlockEvent.Secondary.MainHand event, @Root Player player) {
        boolean sneaking = player.get(Keys.IS_SNEAKING).orElse(false);
        if (sneaking) player.sendMessage(plugin.fromLegacy("is sneaking"));
        if (player.hasPermission(Permissions.TRADE) && event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN) && sneaking) {
            player.sendMessage(plugin.fromLegacy("is WALL_SIGN"));

        }
    }


    public boolean setSignLine(TileEntity entity, int indx, Text text) {
        SignData sign = entity.get(SignData.class).get();
        if (text!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(indx, text));
//        if (line1!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(1, line1));
//        if (line2!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(2, line2));
//        if (line3!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(3, line3));
        entity.offer(sign);
        return true;
    }

    public boolean isValidSignChest(SignData signdata) {
        String l2 = signdata.lines().get(1).toPlain();
        plugin.getLogger().info("l2="+l2);
        if (!StringUtils.isNumeric(l2)) return false;
        else {
            String l3 = signdata.lines().get(2).toPlain();
            plugin.getLogger().info("l3="+l3);
            if (checkBuySell(l3)) {
                plugin.getLogger().info("true");
                return true;
            } else return false;
        }
    }

    public static boolean checkBuySell(String userNameString){
        final String PRICE3_PATTERN =
                "(^[Bb]\\s+[\\d]+(\\.\\d+)*$)|" +
                        "(^\\s*[\\d]+(\\.\\d+)*\\s+[Ss]\\s*$)|" +
                        "(^\\s*[Bb]\\s+[\\d]+(\\.\\d+)*){1}(\\s*:\\s*)(\\s*[\\d]+(\\.\\d+)*\\s+[Ss]\\s*$){1}";

        Pattern p = Pattern.compile(PRICE3_PATTERN);
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }

    public static BigDecimal getBuyPrice(String s) {
        int p1 = max(s.indexOf("B"), s.indexOf("b"))+1;
        if (p1 < 1) return new BigDecimal(0);
        int p2 = s.indexOf(":");
        if (p2 < 1) p2 = s.length();
        String ltrim = s.substring(p1, p2).replaceAll("^\\s+","");
        String rtrim = ltrim.replaceAll("\\s+$","");
        return new BigDecimal(rtrim);
    }

    public static BigDecimal getSellPrice(String s){
        int p2 = max(s.indexOf("S"), s.indexOf("s"));
        if (p2 < 1) return new BigDecimal(0);
        int p1 = s.indexOf(":");
        String ltrim = s.substring(p1+1, p2).replaceAll("^\\s+","");
        String rtrim = ltrim.replaceAll("\\s+$","");
        return new BigDecimal(rtrim);
    }


    public Direction getSignDirection(Sign sign) {
        return sign.getLocation().get(DirectionalData.class).get().get(Keys.DIRECTION).get();
    }

    public boolean isSign(TileEntity entity) {
        return entity.getType().equals(TileEntityTypes.SIGN);
    }

}
