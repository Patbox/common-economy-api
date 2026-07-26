package eu.pb4.common.economy.api;

import eu.pb4.common.economy.impl.EconomyImpl;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.math.BigInteger;

/// A currency. Held by owners in [EconomyAccount]s. MUST be a singleton.
@SuppressWarnings("unused")
public interface EconomyCurrency {
    /// Currency's name, SHOULD be used by mods for display information.
    Component name();

    /// Identifier of this currency. Namespace MUST be equal to provider's namespace.
    Identifier id();

    default Component formatValueComponent(BigInteger value, boolean precise) {
        return Component.literal(this.formatValue(value, precise));
    }

    /// Creates an [ItemStack] displaying given amount of this currency.
    /// The returned stack MUST NOT have any redeemable value, though the caller MAY give it value.
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

    /// Formats value for display or config storage. MUST NOT include currency name. For example, return "10" instead of
    /// "10 points".
    /// If `precise`, the returned String MUST be precise to the smallest fraction.
    String formatValue(BigInteger value, boolean precise);

    default String formatValue(long value, boolean precise) {
        return formatValue(BigInteger.valueOf(value), precise);
    }

    /// Parses string input to raw value. MUST be the reverse of [#formatValue(BigInteger, boolean)] where
    /// `precise = true`. In other words, `parseValue(formatValue(value, true))` MUST equal `value`.
    ///
    /// @param value String value
    /// @return raw amount
    BigInteger parseValue(String value) throws NumberFormatException;

    /// Provider managing this currency.
    EconomyProvider provider();

    /// Icons that other mods SHOULD use in GUIs.
    default ItemStack icon() {
        return Items.SUNFLOWER.getDefaultInstance();
    }
}
