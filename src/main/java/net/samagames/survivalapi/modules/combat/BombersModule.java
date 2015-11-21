package net.samagames.survivalapi.modules.combat;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

public class BombersModule extends AbstractSurvivalModule
{
    public BombersModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give an unbreakable flint and steel to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack flintAndSteel = new ItemStack(Material.FLINT_AND_STEEL, 1);
        flintAndSteel.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().addItem(flintAndSteel);
    }

    /**
     * Drop a TNT when a entity die
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event)
    {
        event.getDrops().add(new ItemStack(Material.TNT, 1));
    }
}
