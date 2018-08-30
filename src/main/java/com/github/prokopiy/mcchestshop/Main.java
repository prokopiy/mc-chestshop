package com.github.prokopiy.mcchestshop;

import com.github.prokopiy.mcchestshop.commands.Whatsthis;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;

@Plugin(
        id = "mc-chestshop",
        name = "MC Chestshop",
        description = "Spongeforge Chest shop"
)
public class Main {
    private static Main instance;

    private CommandManager cmdManager = Sponge.getCommandManager();


    @Inject
    private Logger logger;


    @Listener
    public void Init(GameInitializationEvent event) throws IOException, ObjectMappingException {
        instance = this;
//        this.config = new Config(this);
        Sponge.getEventManager().registerListeners(this, new EventListener(this));

//        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GroupData.class), new GroupDataSerializer());
//        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(BlockData.class), new BlockData.BlockDataSerializer());

        loadCommands(); logger.info("Load commands...");
//        loadData(); logger.info("Load data...");
    }


    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }


    private void loadCommands() {

        // /placelimiter whatsthis
        CommandSpec whatsThis = CommandSpec.builder()
                .description(Text.of("Show the block ID the player is looking at"))
                .executor(new Whatsthis(this))
                .permission(Permissions.WHATS_THIS)
                .build();




//        // /placelimiter block
//        CommandSpec block = CommandSpec.builder()
//                .description(Text.of("Base placerestrict block command"))
//                .executor(new Help(this))
//                .child(blockAddLookAt, "add")
//                .child(blockRemove, "remove")
//                .build();
//


        // /placerestrict
        CommandSpec chestshop = CommandSpec.builder()
                .description(Text.of("Base placelimiter command"))
//                .executor(new Help(this))
                .child(whatsThis, "whatsthis")
//                .child(block, "block")
//                .child(group, "group")
                .build();

        cmdManager.register(this, chestshop, "chestshop");
    }


    public String getLocationID(Location<World> location) {
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

    public String getItemStackID(ItemStack itemStack) {
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


    public Logger getLogger() {
        return logger;
    }

    public Text fromLegacy(String legacy) {
        return TextSerializers.FORMATTING_CODE.deserializeUnchecked(legacy);
    }
}
