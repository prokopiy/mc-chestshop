package com.github.prokopiy.mcchestshop;

import com.github.prokopiy.mcchestshop.utils.ChestUtils;
import com.github.prokopiy.mcchestshop.utils.Id;
import com.github.prokopiy.mcchestshop.utils.SignShop;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class EventListener {

    private static final Pattern PATTERN_META = Pattern.compile("\\.[\\d+]*$");

    private Main plugin;
    public EventListener(Main instance) {
        plugin = instance;
    }


    @Listener
    public void onChangeSignEvent(ChangeSignEvent event, @Root Player player) {
//        player.sendMessage(plugin.fromLegacy("&conChangeSignEvent"));
//        if (event.getTransactions().get(0).getFinal().getState().getType().equals(BlockTypes.AIR)) {return;}


        Sign sign = event.getTargetTile();
//        player.sendMessage(plugin.fromLegacy("getKeys: " + sign.getKeys().toString()));
//        player.sendMessage(plugin.fromLegacy(SignShop.getSignDirection(sign).toString()));

        if (sign.getLocation().getBlockType().equals(BlockTypes.WALL_SIGN)) {
//            player.sendMessage(plugin.fromLegacy("is WALL_SIGN"));
            if (SignShop.isValidSignShop(event.getText())){
//                player.sendMessage(plugin.fromLegacy("is isValidSignChest"));
                player.sendMessage(plugin.fromLegacy("&d[ChestShop] &eShop created!"));
                if (!event.getText().lines().get(0).toPlain().equals(player.getName())){
                    if (!player.hasPermission(Permissions.ADMIN)) {
                        player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cShop creation canceled!"));
                        event.setCancelled(true);
                    }
                }
            }
        }

//        for (Text i : event.getText().lines()) {
//            player.sendMessage(plugin.fromLegacy(i.toPlain()));
//        }

    }


    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onRightClick(InteractBlockEvent.Secondary.MainHand event, @Root Player player) {
//        boolean sneaking = player.get(Keys.IS_SNEAKING).orElse(false);

//        player.sendMessage(plugin.fromLegacy("onRightClick: start"));
        if (player.hasPermission(Permissions.TRADE) && event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN)) {
//                player.sendMessage(plugin.fromLegacy("onRightClick: is WALL_SIGN"));

            TileEntity entity = event.getTargetBlock().getLocation().get().getTileEntity().get();
            Sign sign = (Sign) event.getTargetBlock().getLocation().get().getTileEntity().get();
            SignData signData = entity.get(SignData.class).get();
            if (SignShop.isValidSignShop(signData)){
//                    player.sendMessage(plugin.fromLegacy("onRightClick: isValidSignChest"));
//                    player.sendMessage(plugin.fromLegacy("onRightClick: sign 1 = " + signData.lines().get(1).toPlain()));
                if (signData.lines().get(0).toPlain().equals(player.getName())) {
//                        player.sendMessage(plugin.fromLegacy("onRightClick: is your SignShop"));
//                        if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && player.getItemInHand(HandTypes.MAIN_HAND).get().getType().getType().equals(ItemTypes.FLINT)) {
                    if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
//                            player.getItemInHand(HandTypes.MAIN_HAND).get()
                        String id = Id.getItemStackID(player.getItemInHand(HandTypes.MAIN_HAND).get());
                        player.sendMessage(plugin.fromLegacy("&d[ChestShop] &eShop item set to&b " + SignShop.getSignItemId(sign)));
                        setSignLine(entity, 3, Text.of(TextColors.DARK_BLUE, id));
                    }
                } else {
//                    player.sendMessage(plugin.fromLegacy("onRightClick: is NOT your SignShop"));
                    Direction d = SignShop.getSignDirection(sign);
//                        player.sendMessage(plugin.fromLegacy("onRightClick: getSignDirection=" + d.toString()));

                    Location l = getNearLocationByDirection(sign.getLocation(), d);
//                        player.sendMessage(plugin.fromLegacy("onRightClick: getNearLocationByDirection=" + l.getPosition().toString()));

//                        TileEntityCarrier chest = (TileEntityCarrier) l.getTileEntity().get();
                    if ((l.getBlock().getType().equals(BlockTypes.CHEST) || l.getBlock().getType().equals(BlockTypes.TRAPPED_CHEST)) && (!signData.lines().get(3).toPlain().isEmpty())) {
                        if (l.getTileEntity().isPresent()) {
                            TileEntityCarrier carrier = (TileEntityCarrier) l.getTileEntity().get();
                            Inventory chest = getInventory(carrier);

                            if (chest!=null){
//                                player.sendMessage(plugin.fromLegacy("onRightClick: chest ok!"));


                                Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
                                if (serviceOpt.isPresent()) {
                                    EconomyService economyService = serviceOpt.get();

                                    Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());

                                    if (plugin.userStorage.isPresent()) {
//                                            player.sendMessage(plugin.fromLegacy("onRightClick: (plugin.userStorage.isPresent())"));
                                        Optional<User> user = plugin.userStorage.get().get(SignShop.getSignOwnerName(sign));
//                                                Optional<Player> owner = plugin.userStorage.get().get(SignShop.getSignOwnerName(sign)).get().getPlayer();
//                                            player.sendMessage(plugin.fromLegacy("onRightClick: SignShop.getSignOwnerName(sign)=" + SignShop.getSignOwnerName(sign)));

                                        if (user.isPresent()) {
//                                                player.sendMessage(plugin.fromLegacy("onRightClick: (user.isPresent())"));
                                            Optional<UniqueAccount> oOpt = economyService.getOrCreateAccount(user.get().getUniqueId());

                                            if ((uOpt.isPresent()) && (oOpt.isPresent())) {
//                                                    player.sendMessage(plugin.fromLegacy("onRightClick: ((uOpt.isPresent()) && (oOpt.isPresent()))"));
                                                UniqueAccount userAccount = uOpt.get();
                                                UniqueAccount ownerAccount = oOpt.get();

                                                BigDecimal buyPrice = SignShop.getSignBuyPrice(sign);
                                                BigDecimal sellPrice = SignShop.getSignSellPrice(sign);

                                                if (buyPrice.doubleValue() > 0) {
                                                    Optional<Player> owner = user.get().getPlayer();
                                                    if (ChestUtils.containsItem(chest, SignShop.getSignItemId(sign), SignShop.getSignItemCount(sign))){
                                                        if (userAccount.transfer(ownerAccount,economyService.getDefaultCurrency(),buyPrice,
                                                                Cause.of(EventContext.empty(),(Main.getInstance()))).getResult().equals(ResultType.SUCCESS)) {
                                                            purchase(chest, sign, player);
                                                            player.sendMessage(plugin.fromLegacy("&d[ChestShop] &eYou buy&b " + SignShop.getSignItemCount(sign).toString()  + "&e*&b" + SignShop.getSignItemId(sign) + " &eby&b " + buyPrice.toString() + " &efrom &b" + ownerAccount.getDisplayName().toPlain()));

                                                            if (owner.isPresent()) {
                                                                owner.get().sendMessage(plugin.fromLegacy("&d[ChestShop] &b" + player.getName() + " &ebuy&b " + SignShop.getSignItemCount(sign).toString()  + "&e*&b" + SignShop.getSignItemId(sign) + " &eby&b " + buyPrice.toString() + " &efrom you"));
                                                            }

                                                        } else {
                                                            player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cTransfer false!"));
                                                        }

                                                    }else{
                                                        player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cThere are no items in the shop!"));
                                                        if (owner.isPresent()) {
                                                            owner.get().sendMessage(plugin.fromLegacy("&d[ChestShop] &cNo&b " + SignShop.getSignItemCount(sign).toString()  + "&e*&b" + SignShop.getSignItemId(sign) + " &cin the chest!"));
                                                        }
                                                    }

                                                }

                                                if (sellPrice.doubleValue() > 0) {
//                                                    player.sendMessage(plugin.fromLegacy("onRightClick: (sellPrice.doubleValue() > 0)"));
                                                    if (ChestUtils.canPut(chest, SignShop.getSignItemId(sign), SignShop.getSignItemCount(sign))){
//                                                        player.sendMessage(plugin.fromLegacy("onRightClick: ChestUtils.canPut"));
                                                        if (ChestUtils.containsItem(player.getInventory(), SignShop.getSignItemId(sign), SignShop.getSignItemCount(sign))){
//                                                            player.sendMessage(plugin.fromLegacy("onRightClick: ChestUtils.containsItem"));
                                                            if (ownerAccount.transfer(userAccount,economyService.getDefaultCurrency(),sellPrice,
                                                                    Cause.of(EventContext.empty(),(Main.getInstance()))).getResult().equals(ResultType.SUCCESS)) {
                                                                sell(chest, sign, player);
                                                                player.sendMessage(plugin.fromLegacy("&d[ChestShop] &eYou sell&b " + SignShop.getSignItemCount(sign).toString()  + "&e*&b" + SignShop.getSignItemId(sign) + " &eby&b " + sellPrice.toString() + " &eto &b" + ownerAccount.getDisplayName().toPlain()));
                                                            } else {
                                                                player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cTransfer false!"));
                                                            }

                                                        } else {
                                                            player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cYou don't have the required items!"));
                                                        }
                                                    }else{
                                                        player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cChest is full!"));
                                                    }
                                                }



                                            } else {
                                                player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cOwner not found!"));
                                            }
                                        } else {

                                        }
                                    }
                                } else {
                                    // handle there not being an economy implementation
                                }
                            }else{
                                player.sendMessage(plugin.fromLegacy("&d[ChestShop] &cChest not found!"));
                            }
                        }
                    } else {
//                        player.sendMessage(plugin.fromLegacy("onRightClick: is NOT chest!"));
                    }
                }
            }
        }
    }



    private boolean sell(Inventory chest, Sign sign, Player player){
//        player.sendMessage(plugin.fromLegacy("sell: started!"));
        BigDecimal price = SignShop.getSignSellPrice(sign);
        int count = SignShop.getSignItemCount(sign);
        String itemID = SignShop.getSignItemId(sign);

        if ((price.doubleValue() > 0) && (count > 0)){
            for (Inventory slot : player.getInventory().slots()) {
                if ((count > 0) && (slot.peek().isPresent())) {
                    if (Id.getItemStackID(slot.peek().get()).equals(itemID)) {
//                        player.sendMessage(plugin.fromLegacy("sell: Id.getItemStackID(slot.peek().get()).equals(itemID)"));
                        int peekQuantity = slot.peek().get().getQuantity();
//                        player.sendMessage(plugin.fromLegacy("sell: peekQuantity >= count"));
                        ItemStack buy_ent = slot.peek().get().copy();
                        buy_ent.setQuantity(min(count, peekQuantity));

                        if (peekQuantity > count) {
                            ItemStack rest_ent = slot.peek().get().copy();
                            rest_ent.setQuantity(peekQuantity-count);
                            slot.clear();
                            slot.set(rest_ent);

                        } else {
                            slot.clear();
                        }

                        InventoryTransactionResult result = chest.offer(buy_ent);
                        Collection<ItemStackSnapshot> rejectedItems = result.getRejectedItems();
                        count -= peekQuantity;
                    }
                }
            }
        }
        return true;
    }


    private boolean purchase(Inventory chest, Sign sign, Player player){
//        player.sendMessage(plugin.fromLegacy("purchase: started!"));
        BigDecimal price = SignShop.getSignBuyPrice(sign);
        int count = SignShop.getSignItemCount(sign);
        String itemID = SignShop.getSignItemId(sign);

        if ((price.doubleValue() > 0) && (count > 0)){
//            player.sendMessage(plugin.fromLegacy("purchase: price.doubleValue() > 0) && (count > 0"));
            for (Inventory slot : chest.slots()) {
                if ((count > 0) && (slot.peek().isPresent())) {
                    if (Id.getItemStackID(slot.peek().get()).equals(itemID)) {
//                        player.sendMessage(plugin.fromLegacy("purchase: Id.getItemStackID(slot.peek().get()).equals(itemID)"));
                        int peekQuantity = slot.peek().get().getQuantity();
//                        player.sendMessage(plugin.fromLegacy("purchase: peekQuantity >= count"));
                        ItemStack buy_ent = slot.peek().get().copy();
                        buy_ent.setQuantity(min(count, peekQuantity));

                        if (peekQuantity > count) {
                            ItemStack rest_ent = slot.peek().get().copy();
                            rest_ent.setQuantity(peekQuantity-count);
                            slot.clear();
                            slot.set(rest_ent);

                        } else {
                            slot.clear();
                        }

                        InventoryTransactionResult result = player.getInventory().offer(buy_ent);
                        Collection<ItemStackSnapshot> rejectedItems = result.getRejectedItems();
                        count -= peekQuantity;


                        if (rejectedItems.size() > 0)
                        {
                            Location<World> location = player.getLocation();
                            World world = location.getExtent();
                            for (ItemStackSnapshot rejectedSnapshot : rejectedItems)
                            {
                                Item rejectedItem = (Item) world.createEntity(EntityTypes.ITEM, location.getPosition());
                                rejectedItem.offer(Keys.REPRESENTED_ITEM, rejectedSnapshot);
                                try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                                    frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
                                    world.spawnEntity(rejectedItem);
                                }
                            }

//                                player.sendMessage(Text.of(TextColors.BLUE, "[SpongyChest]: ", TextColors.YELLOW, "Some of the items could not be added to your inventory, so they have been thrown on the ground instead."));
                        }
                    }
                }
            }
        }
        return true;
    }



    public Inventory getInventory(TileEntity entity) {
        if (entity instanceof TileEntityCarrier) {
            TileEntityCarrier carrier = (TileEntityCarrier) entity;
            return carrier.getInventory();
        } else {
            return null;
        }
    }

    public Location getNearLocationByDirection(Location loc, Direction dir){
        Location l = loc.sub(dir.asBlockOffset());
        return l;
    }



    public boolean setSignLine(TileEntity entity, int indx, Text text) {
        SignData sign = entity.get(SignData.class).get();
        if (text!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(indx, text));
        entity.offer(sign);
        return true;
    }


    public boolean isSign(TileEntity entity) {
        return entity.getType().equals(TileEntityTypes.SIGN);
    }

}
