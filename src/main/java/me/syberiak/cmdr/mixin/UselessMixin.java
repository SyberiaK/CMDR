package me.syberiak.cmdr.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.syberiak.cmdr.CMDRLoader;

@Mixin(TitleScreen.class)
public class UselessMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		CMDRLoader.LOGGER.info("This is a string printed by useless mixin...");	
	}
}
