package host.plas;

import lombok.Getter;
import net.streamline.api.modules.ModuleUtils;
import net.streamline.api.modules.SimpleModule;
import net.streamline.thebase.lib.pf4j.PluginWrapper;
import host.plas.commands.PackCommand;
import host.plas.configs.Configs;
import host.plas.listeners.MainListener;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class ResourcePackUtils extends SimpleModule {
    @Getter
    private static ResourcePackUtils instance;
    @Getter
    private static Configs configs;
    @Getter
    private static MainListener mainListener;

    public ResourcePackUtils(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public ConcurrentSkipListSet<String> authors() {
        return new ConcurrentSkipListSet<>(List.of("Quaint"));
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        configs = new Configs();
        mainListener = new MainListener();
        ModuleUtils.listen(mainListener, this);

        new PackCommand().register();
    }
}
