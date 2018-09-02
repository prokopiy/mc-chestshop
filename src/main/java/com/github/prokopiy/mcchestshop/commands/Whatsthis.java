package com.github.prokopiy.mcchestshop.commands;

import com.github.prokopiy.mcchestshop.Main;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Whatsthis implements CommandExecutor {
    private final Main plugin;

    public Whatsthis(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(plugin.fromLegacy("&d[ChestShop] &bOnly players can run this command"));
        }
        Player player = (Player) src;
        Optional<ItemStack> optionalItemStack = player.getItemInHand(HandTypes.MAIN_HAND);

        if (!optionalItemStack.isPresent()) {
            throw new CommandException(plugin.fromLegacy("&d[ChestShop] &bYou must be holding an item"));
        }

        ItemStack itemStack = optionalItemStack.get();

        DataContainer container = itemStack.toContainer();
        DataQuery query = DataQuery.of('/', "UnsafeDamage");

        int unsafeDamage = Integer.parseInt(container.get(query).get().toString());

        String item = itemStack.getType().getId();

        if (unsafeDamage != 0) {
            item = item + ":" + unsafeDamage;
        }

        player.sendMessage(plugin.fromLegacy("&d[ChestShop] &eItem is: &b" + item));

        return CommandResult.success();
    }
}
