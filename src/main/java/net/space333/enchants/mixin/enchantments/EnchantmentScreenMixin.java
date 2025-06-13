package net.space333.enchants.mixin.enchantments;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.space333.enchants.Enchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin extends Screen {

    protected EnchantmentScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;", ordinal = 0))
    private MutableText removeName(String key, Object[] args) {
        MutableText enchantmentText = (MutableText) args[0];
        String enchantmentString = enchantmentText.getString();
        String enchant = enchantmentString.substring(0, enchantmentString.lastIndexOf(" "));
        String level = enchantmentString.substring(enchantmentString.lastIndexOf(" ") + 1);

        MutableText enchantText = Text.literal(enchant).formatted(Formatting.OBFUSCATED);
        MutableText levelText = Text.literal(level);
        if(level.length() <= 3) {
            return Text.translatable("container.enchants.clue", enchantText, levelText).formatted(Formatting.WHITE);
        }
        else {
            return enchantmentText.formatted(Formatting.WHITE).formatted(Formatting.OBFUSCATED);
        }



    }


}
