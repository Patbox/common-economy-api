package eu.pb4.common.economy.api;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

@SuppressWarnings({"unused"})
public interface EconomyAccount {
    /**
     * Accounts name, used for mods for a display
     */
    Text name();

    /**
     * Main account owner. Return Util.NIL_UUID if it's server/console account or it doesn't have a main owner
     */
    UUID owner();

    /**
     * Identifier allowing you to get this account. namespace should be equal to Provider's id.
     * @return
     */
    Identifier id();

    /**
     * Raw value of current account balance
     */
    long balance();

    default Text formattedBalance() {
        return this.currency().formatValueText(this.balance(), false);
    }

    /**
     * Increases account's balance by value. It should be only successful if full amount can be transferred
     */
    default EconomyTransaction increaseBalance(long value) {
        var t = this.canIncreaseBalance(value);

        if (t.isSuccessful()) {
            this.setBalance(t.finalBalance());
        }
        return t;
    }

    /**
     * Checks if account's balance can be increased by value. It should be only successful if full amount can be transferred.
     * All returned values should be the same as for successful operation
     */
    EconomyTransaction canIncreaseBalance(long value);

    /**
     * Decreases account's balance by value. It should be only successful if full amount can be transferred
     */
    default EconomyTransaction decreaseBalance(long value) {
        var t = this.canDecreaseBalance(value);

        if (t.isSuccessful()) {
            this.setBalance(t.finalBalance());
        }
        return t;
    }

    /**
     * Checks if account's balance can be increased by value. It should be only successful if full amount can be transferred.
     * All returned values should be the same as for successful operation
     */
    EconomyTransaction canDecreaseBalance(long value);

    /**
     * Sets account's balance to a value
     */
    void setBalance(long value);

    /**
     * Provider managing this account
     */
    EconomyProvider provider();

    /**
     * Currency used by this account
     */
    EconomyCurrency currency();

    /**
     * Icon for other mods to use
     */
    default ItemStack accountIcon() {
        return this.provider().icon();
    }
}
