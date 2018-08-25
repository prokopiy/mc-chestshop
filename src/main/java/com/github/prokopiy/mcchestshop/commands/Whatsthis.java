package com.github.prokopiy.mcchestshop.commands;

import com.github.prokopiy.mcchestshop.Main;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
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
            throw new CommandException(plugin.fromLegacy("Only players can run this command"));
        }
        Player player = (Player) src;


        BlockRay<World> blockRay = BlockRay.from(player).stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();
        Optional<BlockRayHit<World>> hitOpt = blockRay.end();
        if (hitOpt.isPresent()) {
            BlockRayHit<World> hit = hitOpt.get();
            String itemId = plugin.getLocationID(hit.getLocation());


            String msg = itemId;
            player.sendMessage(plugin.fromLegacy(msg));
        } else {
            throw new CommandException(Text.of("Is null!"));
        }

        return CommandResult.success();
    }
}
