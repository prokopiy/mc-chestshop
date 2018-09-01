package com.github.prokopiy.mcchestshop.utils;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.block.DirectionalData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.util.Direction;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

public class SignShop {

    public static boolean isValidSignShop(SignData signdata) {
        String l2 = signdata.lines().get(1).toPlain();
//        plugin.getLogger().info("l2="+l2);
        if (!StringUtils.isNumeric(l2)) return false;
        else {
            String l3 = signdata.lines().get(2).toPlain();
//            plugin.getLogger().info("l3="+l3);
            if (checkBuySell(l3)) {
//                plugin.getLogger().info("true");
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

    public static String getSignOwnerName(Sign sign){
        return sign.lines().get(0).toPlain();
    }

    public static String getSignItemId(Sign sign){
        return sign.lines().get(3).toPlain();
    }

    public static Integer getSignItemCount(Sign sign) {
        return Integer.parseInt(sign.lines().get(1).toPlain());
    }

    public static BigDecimal getSignBuyPrice(Sign sign) {
        return getBuyPrice(sign.lines().get(2).toPlain());
    }
    public static BigDecimal getSignSellPrice(Sign sign) {
        return getSellPrice(sign.lines().get(2).toPlain());
    }

    public static Direction getSignDirection(Sign sign) {
        return sign.getLocation().get(DirectionalData.class).get().get(Keys.DIRECTION).get();
    }


}
