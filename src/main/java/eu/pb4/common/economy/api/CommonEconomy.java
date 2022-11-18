package eu.pb4.common.economy.api;

import com.mojang.authlib.GameProfile;
import eu.pb4.common.economy.impl.EconomyImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings({"unused"})
public final class CommonEconomy {
    @Nullable
    public static EconomyAccount getAccount(ServerPlayerEntity player, Identifier account) {
        return getAccount(player.server, player.getGameProfile(), account);
    }

    @Nullable
    public static EconomyAccount getAccount(MinecraftServer server, GameProfile profile, Identifier account) {
        return EconomyImpl.get(server, profile, account);
    }

    public static Collection<EconomyAccount> getAccounts(ServerPlayerEntity player) {
        return getAccounts(player.server, player.getGameProfile());
    }

    public static Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile) {
        return EconomyImpl.getAll(server, profile);
    }

    public static Collection<EconomyAccount> getAccounts(ServerPlayerEntity player, EconomyCurrency currency) {
        return getAccounts(player.server, player.getGameProfile(), currency);
    }

    public static Collection<EconomyAccount> getAccounts(MinecraftServer server, GameProfile profile, EconomyCurrency currency) {
        return EconomyImpl.getAll(server, profile, currency);
    }

    @Nullable
    public static EconomyCurrency getCurrency(MinecraftServer server, Identifier account) {
        return EconomyImpl.getCurrency(server, account);
    }

    public static Collection<EconomyCurrency> getCurrencies(MinecraftServer server) {
        return EconomyImpl.getCurrencies(server);
    }

    public static EconomyProvider getProvider(String id) {
        return EconomyImpl.getProvider(id);
    }

    public static Collection<EconomyProvider> providers() {
        return EconomyImpl.providers();
    }

    public static <T extends EconomyProvider> T register(String providerId, T provider) {
        EconomyImpl.register(providerId, provider);
        return provider;
    }
}
