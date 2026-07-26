package eu.pb4.common.economy.api;

import com.mojang.authlib.GameProfile;
import eu.pb4.common.economy.impl.EconomyImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// The entrypoint for mods to provide an economy.
@SuppressWarnings({"unused"})
public interface EconomyProvider {
    Component name();

    /// Gets the account with the given type that a player owns. Returns `null` if the player has no such account.
    ///
    /// @param player Owner
    /// @param account Path of account type id
    @Nullable
    default EconomyAccount getAccount(ServerPlayer player, String account) {
        return this.getAccount(player.level().getServer(), player.getGameProfile(), account);
    }

    /// Gets the account with the given type that a player owns. Returns \`null\` if the player has no such account.
    ///
    /// @param server Current server
    /// @param profile GameProfile of owner
    /// @param accountId Path of account type id
    @Nullable
    EconomyAccount getAccount(MinecraftServer server, GameProfile profile, String accountId);

    /// Gets all accounts controlled by player that are managed by this provider.
    default Collection<EconomyAccount> getAccounts(ServerPlayer player) {
        return this.getAccounts(player.level().getServer(), player.getGameProfile());
    }

    /// Gets all accounts controlled by player that are managed by this provider.
    Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile);

    /// Gets all `currency` accounts controlled by player that are managed by this provider.
    ///
    /// @param currency The currency that all found accounts MUST hold.
    default Collection<EconomyAccount> getAccounts(ServerPlayer player, EconomyCurrency currency) {
        return this.getAccounts(player.level().getServer(), player.getGameProfile(), currency);
    }

    /// Gets all `currency` accounts controlled by player that are managed by this provider.
    ///
    /// @param currency The currency that all found accounts MUST hold.
    default Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        List<EconomyAccount> list = new ArrayList<>();
        for (var x : getAccounts(server, profile)) {
            if (x.currency() == currency) {
                list.add(x);
            }
        }
        return list;
    }

    /// Gets currency from id. If the currency is not known to this provider, it MUST return `null`.
    @Nullable
    EconomyCurrency getCurrency(MinecraftServer server, String currencyId);

    /// Gets all currencies handled by this provider
    Collection<EconomyCurrency> getCurrencies(MinecraftServer server);

    /// Gets default account id path for given player and currency. It MAY be the same for every player. SHOULD return
    /// an account if one matches. Otherwise, returns `null`.
    @Nullable
    default String defaultAccount(ServerPlayer player, EconomyCurrency currency) {
        return defaultAccount(player.level().getServer(), player.getGameProfile(), currency);
    }

    /// Gets default account id path for given player and currency. It MAY be the same for every player. SHOULD return
    /// an account if one matches. Otherwise, returns `null`.
    @Nullable
    String defaultAccount(MinecraftServer server, GameProfile profile, EconomyCurrency currency);

    /// Gets default account for given player and currency. It SHOULD NOT be the same for every player, as they
    /// typically have separate accounts. SHOULD return an account if one matches. Otherwise, returns `null`.
    @Nullable
    default EconomyAccount getDefaultAccount(ServerPlayer player, EconomyCurrency currency) {
        var id = defaultAccount(player, currency);
        return  id != null ? this.getAccount(player, id) : null;
    }

    /// Gets default account for given player and currency. It SHOULD NOT be the same for every player, as they
    /// typically have separate accounts. SHOULD return an account if one matches. Otherwise, returns `null`.
    @Nullable
    default EconomyAccount getDefaultAccount(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        var id = defaultAccount(server, profile, currency);
        return  id != null ? this.getAccount(server, profile, id) : null;
    }

    /// Icon other mods SHOULD use to represent this provider.
    default ItemStack icon() {
        return Items.SUNFLOWER.getDefaultInstance();
    }

    /// A unique id namespace for this provider.
    default String id() {
        return EconomyImpl.getId(this);
    }
}
