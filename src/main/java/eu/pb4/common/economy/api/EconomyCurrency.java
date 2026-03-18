package eu.pb4.common.economy.api;

import eu.pb4.common.economy.impl.EconomyImpl;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.math.BigInteger;

/**
 * A currency of a mod
 */
public interface EconomyCurrency {
    /**
     * Currency's name, used by mods for display information
     */
    Component name();

    /**
     * Identifier allowing you to get this instance. namespace should be equal to Provider's id.
     */
    Identifier id();

    default Component formatValueComponent(BigInteger value, boolean precise) {
        return Component.literal(this.formatValue(value, precise));
    }

    default ItemStack formatValueStack(BigInteger value) {
        var stack = this.icon().copy();
        stack.set(DataComponents.CUSTOM_NAME, this.formatValueComponent(value, false)
            .copy()
            .withStyle(s -> s.applyTo(EconomyImpl.WHITE_NON_ITALIC_STYLE)));

        return stack;
    }

    default Component formatValueComponent(long value, boolean precise) {
        return formatValueComponent(BigInteger.valueOf(value), precise);
    }

    default ItemStack formatValueStack(long value) {
        return formatValueStack(BigInteger.valueOf(value));
    }

    /**
     * Formats value for display/config storage
     *
     * @param value raw value
     * @param precise whatever it should be precise (down to lowest values)
     * @return balance formatted as string
     */
    String formatValue(BigInteger value, boolean precise);

    default String formatValue(long value, boolean precise) {
        return formatValue(BigInteger.valueOf(value), precise);
    }

    /**
     * Parses string input to raw value.
     * This method should be able to parse output of formatValue with precise = true
     *
     * @param value String value
     * @return raw amount
     * @throws NumberFormatException
     */
    BigInteger parseValue(String value) throws NumberFormatException;

    /**
     * Provider managing this currency
     */
    EconomyProvider provider();

    /**
     * Icons for other mods to use in guis
     */
    default ItemStack icon() {
        return Items.SUNFLOWER.getDefaultInstance();
    }
}
