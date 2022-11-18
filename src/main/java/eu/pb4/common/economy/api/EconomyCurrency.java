package eu.pb4.common.economy.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A currency of a mod
 */
public interface EconomyCurrency {
    /**
     * Currency's name, used by mods for display information
     */
    Text name();

    /**
     * Identifier allowing you to get this instance. namespace should be equal to Provider's id.
     */
    Identifier id();

    default Text formatValueText(long value, boolean precise) {
        return Text.literal(this.formatValue(value, precise));
    }

    /**
     * Formats value for display/config storage
     *
     * @param value raw value
     * @param precise whatever it should be precise (down to lowest values)
     * @return balance formatted as string
     */
    String formatValue(long value, boolean precise);

    /**
     * Parses string input to raw value.
     * This method should be able to parse output of formatValue with precise = true
     *
     * @param value String value
     * @return raw amount
     * @throws NumberFormatException
     */
    long parseValue(String value) throws NumberFormatException;

    /**
     * Provider managing this currency
     */
    EconomyProvider provider();

    /**
     * Icons for other mods to use in guis
     */
    default ItemStack icon() {
        return Items.SUNFLOWER.getDefaultStack();
    }
}
