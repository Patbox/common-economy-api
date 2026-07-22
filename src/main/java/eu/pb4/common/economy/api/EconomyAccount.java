package eu.pb4.common.economy.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.math.BigInteger;
import java.util.UUID;

/// An account for holding currency. MAY have zero or more owners.
@SuppressWarnings("unused")
public interface EconomyAccount {
    /// Account's name. SHOULD be used for mods for display purposes.
    Component name();

    /// Main account owner. MUST return [net.minecraft.util.Util.NIL_UUID] if it is a server/console account or doesn't
    /// have a main owner.
    UUID owner();

    /// Identifier for this account type. Namespace MUST be equal to provider's namespace.
    Identifier id();

    /// Raw value of current account balance.
    BigInteger balance();

    default Component formattedBalance() {
        return this.currency().formatValueComponent(this.balance(), false);
    }

    /// Increases account's balance by value.
    ///
    /// Returned transaction MUST be successful if full amount was added. Transaction MUST be unsuccessful otherwise.
    /// There MUST NOT be any amount transferred for an unsuccessful transaction.
    default EconomyTransaction increaseBalance(BigInteger value) {
        var t = this.canIncreaseBalance(value);

        if (t.isSuccessful()) {
            this.setBalance(t.finalBalance());
        }
        return t;
    }

    default EconomyTransaction increaseBalance(long value) {
        return increaseBalance(BigInteger.valueOf(value));
    }

    /// Checks if account's balance can be increased by value.
    ///
    /// Returned transaction MUST be successful if full amount can be added. Transaction MUST be unsuccessful otherwise.
    EconomyTransaction canIncreaseBalance(BigInteger value);
    
    default EconomyTransaction canIncreaseBalance(long value) {
        return canIncreaseBalance(BigInteger.valueOf(value));
    }

    /// Decreases account's balance by value.
    ///
    /// Returned transaction MUST be successful if full amount can be removed.
    /// Transaction MUST be unsuccessful otherwise. There MUST NOT be any amount transferred for an unsuccessful
    /// transaction.
    default EconomyTransaction decreaseBalance(BigInteger value) {
        var t = this.canDecreaseBalance(value);

        if (t.isSuccessful()) {
            this.setBalance(t.finalBalance());
        }
        return t;
    }

    default EconomyTransaction decreaseBalance(long value) {
        var t = this.canDecreaseBalance(value);

        if (t.isSuccessful()) {
            this.setBalance(t.finalBalance());
        }
        return t;
    }

    /// Checks if account's balance can be decreased by value.
    ///
    /// Returned transaction MUST be successful if full amount can be removed. Transaction MUST be unsuccessful
    /// otherwise.
    ///
    /// If successful, a subsequent call to [#decreaseBalance(BigInteger)] with the same value MUST succeed, provided
    /// the account is not externally modified between calls.
    EconomyTransaction canDecreaseBalance(BigInteger value);
    
    default EconomyTransaction canDecreaseBalance(long value) {
        return canDecreaseBalance(BigInteger.valueOf(value));
    }

    /// Sets account's balance to value.
    void setBalance(BigInteger value);

    default void setBalance(long value) {
        setBalance(BigInteger.valueOf(value));
    }

    /// Provider managing this account.
    EconomyProvider provider();

    /// Currency used by this account.
    EconomyCurrency currency();

    /// An icon other mods SHOULD use.
    default ItemStack accountIcon() {
        return this.provider().icon();
    }
}
