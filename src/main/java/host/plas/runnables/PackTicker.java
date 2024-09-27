package host.plas.runnables;

import lombok.Getter;
import lombok.Setter;
import singularity.data.players.CosmicPlayer;
import singularity.interfaces.ISingularityExtension;
import singularity.modules.ModuleUtils;
import singularity.objects.CosmicResourcePack;
import singularity.scheduler.ModuleRunnable;
import host.plas.ResourcePackUtils;

@Setter
@Getter
public class PackTicker extends ModuleRunnable {
    private CosmicPlayer player;
    private ISingularityExtension.PlatformType type;
    private CosmicResourcePack pack;

    public PackTicker(CosmicPlayer player, ISingularityExtension.PlatformType type, CosmicResourcePack pack) {
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
