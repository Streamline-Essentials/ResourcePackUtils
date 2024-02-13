package host.plas.listeners;

import lombok.Getter;
import lombok.Setter;
import net.streamline.api.events.server.LoginCompletedEvent;
import net.streamline.api.events.server.LogoutEvent;
import net.streamline.api.interfaces.IStreamline;
import net.streamline.api.modules.ModuleUtils;
import net.streamline.api.objects.StreamlineResourcePack;
import net.streamline.api.savables.users.StreamlinePlayer;
import host.plas.ResourcePackUtils;
import tv.quaint.events.BaseEventListener;
import tv.quaint.events.processing.BaseProcessor;
import host.plas.runnables.PackTicker;

import java.util.concurrent.ConcurrentHashMap;

public class MainListener implements BaseEventListener {
    @Getter @Setter
    private static ConcurrentHashMap<StreamlinePlayer, Boolean> packedMap = new ConcurrentHashMap<>();

    @BaseProcessor
    public void onJoin(LoginCompletedEvent event) {
        StreamlinePlayer player = event.getResource();
        IStreamline.PlatformType type = ModuleUtils.getPlatformType();
        StreamlineResourcePack pack = ResourcePackUtils.getConfigs().getResourcePack();
        new PackTicker(player, type, pack);
    }

    @BaseProcessor
    public void onLeave(LogoutEvent event) {
        StreamlinePlayer player = event.getResource();
        getPackedMap().remove(player);
    }
}
