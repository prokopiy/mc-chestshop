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
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Plugin(
        id = "mc-chestshop",
        name = "MC Chestshop",
        description = "Spongeforge Chest shop"
)
public class Main {
    private static Main instance;

    private CommandManager cmdManager = Sponge.getCommandManager();

    public Optional<UserStorageService> userStorage;

    @Inject
    private Logger logger;

    public static Main getInstance() {
        return instance;
    }


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
        userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
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

    public Optional<User> getUser(UUID uuid) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.get().get(uuid);
    }

    public Logger getLogger() {
        return logger;
    }

    public Text fromLegacy(String legacy) {
        return TextSerializers.FORMATTING_CODE.deserializeUnchecked(legacy);
    }
}
