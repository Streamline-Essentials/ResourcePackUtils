package host.plas.runnables;

import lombok.Getter;
import lombok.Setter;
import net.streamline.api.interfaces.IStreamline;
import net.streamline.api.modules.ModuleUtils;
import net.streamline.api.objects.StreamlineResourcePack;
import net.streamline.api.savables.users.StreamlinePlayer;
import net.streamline.api.scheduler.ModuleRunnable;
import host.plas.ResourcePackUtils;

@Getter
public class PackTicker extends ModuleRunnable {
    @Setter
    private StreamlinePlayer player;
    @Setter
    private IStreamline.PlatformType type;
    @Setter
    private StreamlineResourcePack pack;

    public PackTicker(StreamlinePlayer player, IStreamline.PlatformType type, StreamlineResourcePack pack) {
        super(ResourcePackUtils.getInstance(), 1, ResourcePackUtils.getConfigs().connectWait());
        this.player = player;
        this.type = type;
        this.pack = pack;
    }

    @Override
    public void run() {
        ResourcePackUtils.getInstance().logInfo("&fSending resource pack to '" + player.getDisplayName() + "&f'...");

        switch (type) {
            case BUNGEE:
            case VELOCITY:
                if (ResourcePackUtils.getConfigs().isNetworkHandled()) {
                    ModuleUtils.sendResourcePack(pack, player);
                } else {
                    // do nothing;
                }
                break;
            case SPIGOT:
                if (ResourcePackUtils.getConfigs().isNetworkHandled()) {
                    // do nothing
                } else {
                    ModuleUtils.sendResourcePack(pack, player);
                }
                break;
        }

        cancel();
    }
}
