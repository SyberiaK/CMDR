package me.syberiak.cmdr.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;


@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return s -> new CottonClientScreen(new ManagerLaunchScreen());
    }
}