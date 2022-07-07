package me.syberiak.cmdr.config;

import net.minecraft.text.LiteralText;
import net.minecraft.client.MinecraftClient;
import com.terraformersmc.modmenu.gui.ModsScreen;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
// import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import net.minecraft.client.util.Window;

import me.syberiak.cmdr.CMDRLoader;

public class ManagerLaunchScreen extends LightweightGuiDescription {
    public ManagerLaunchScreen() {
        WPlainPanel root = new WPlainPanel();
        WBox content = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        setFullscreen(true);
        content.setHorizontalAlignment(HorizontalAlignment.CENTER);
        content.setVerticalAlignment(VerticalAlignment.CENTER);
        content.setSpacing(5);
        //root.setInsets(Insets.ROOT_PANEL);
        
        /*WSprite icon = new WSprite(new Identifier("minecraft:textures/item/redstone.png"));
        root.add(icon, 0, 2, 1, 1);*/
        
        WButton launchButton = new WButton(new LiteralText("CLICK ME!")/*new TranslatableText("gui.examplemod.examplebutton")*/);
        launchButton.setOnClick(() -> {
            try {
                Window window = MinecraftClient.getInstance().getWindow();
                if (window.isFullscreen()) {
                    window.toggleFullscreen();
                }
                CMDRLoader.openManager();
            } catch (Exception e) {System.out.println(e);}
        });
        content.add(launchButton, 200, 20);

        WButton backButton = new WButton(new LiteralText("Back")/*new TranslatableText("gui.examplemod.examplebutton")*/);
        backButton.setOnClick(() -> {
            MinecraftClient.getInstance().setScreen(new ModsScreen(null));
        });
        content.add(backButton, 200, 20);
        
        /*WLabel label = new WLabel(new LiteralText("Test"), 0xFFFFFF);
        root.add(label, 0, 4, 2, 1);*/

        root.add(content, 100, 100);
        root.validate(this);
    }
}
