package host.plas.commands;

import lombok.Getter;
import net.streamline.api.command.ModuleCommand;
import net.streamline.api.configs.given.MainMessagesHandler;
import net.streamline.api.modules.ModuleUtils;
import net.streamline.api.savables.users.StreamlinePlayer;
import net.streamline.api.savables.users.StreamlineUser;
import net.streamline.api.utils.UserUtils;
import host.plas.ResourcePackUtils;

import java.util.concurrent.ConcurrentSkipListSet;

public class PackCommand extends ModuleCommand {
    @Getter
    private final String messageResultPackSent;
    @Getter
    private final String messageErrorPlayerNotOnline;
    @Getter
    private final String permissionSend;

    public PackCommand() {
        super(ResourcePackUtils.getInstance(),
                "packutils",
                "streamline.command.packutils.default",
                "rpu", "putils"
        );

        messageResultPackSent = getCommandResource().getOrSetDefault("messages.result.pack.sent", "&eSuccessfully sent resource pack to &f%this_player%&8!");

        messageErrorPlayerNotOnline = getCommandResource().getOrSetDefault("messages.error.player.not-online", "&cThat player is not online!");

        permissionSend = getCommandResource().getOrSetDefault("permissions.send", "streamline.command.packutils.send");
    }

    @Override
    public void run(StreamlineUser sender, String[] args) {
        if (args.length < 1) {
            ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.ARGUMENTS_TOO_FEW.get()));
            return;
        }

        if (args[0] == null) {
            ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.ARGUMENTS_TOO_FEW.get()));
            return;
        }

        if (args[0].isBlank() || args[0].isEmpty()) {
            ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.ARGUMENTS_TOO_FEW.get()));
            return;
        }

        switch (args[0]) {
            case "send":
                if (! sender.hasPermission(permissionSend)) {
                    ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.PERMISSIONS.get()));
                    return;
                }

                if (args.length < 2) {
                    ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.ARGUMENTS_TOO_FEW.get()));
                    return;
                }

                if (args[1] == null) {
                    ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.ARGUMENTS_TOO_FEW.get()));
                    return;
                }

                if (args[1].isBlank() || args[1].isEmpty()) {
                    ModuleUtils.sendMessage(sender, getWithOther(sender.getUuid(), MainMessagesHandler.MESSAGES.INVALID.ARGUMENTS_TOO_FEW.get()));
                    return;
                }

                String playerName = args[1];
                StreamlinePlayer player = UserUtils.getOrGetPlayerByName(playerName);

                if (player == null) {
                    ModuleUtils.sendMessage(sender, MainMessagesHandler.MESSAGES.INVALID.PLAYER_OTHER.get());
                    return;
                }

                if (! player.isOnline()) {
                    ModuleUtils.sendMessage(sender, getWithOther(sender, messageErrorPlayerNotOnline.replace("%this_player%", player.getDisplayName()), player));
                    return;
                }

                ModuleUtils.sendResourcePack(ResourcePackUtils.getConfigs().getResourcePack(), player);

                ModuleUtils.sendMessage(sender, getWithOther(sender, messageResultPackSent.replace("%this_player%", player.getDisplayName()), player));
                break;
        }
    }

    @Override
    public ConcurrentSkipListSet<String> doTabComplete(StreamlineUser sender, String[] args) {
        ConcurrentSkipListSet<String> tabComplete = new ConcurrentSkipListSet<>();

        if (args.length == 1) {
            if (sender.hasPermission(permissionSend)) {
                tabComplete.add("send");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("send")) {
                if (sender.hasPermission(permissionSend)) {
                    tabComplete.addAll(ModuleUtils.getOnlinePlayerNames());
                }
            }
        }

        return tabComplete;
    }
}
