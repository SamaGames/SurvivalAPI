package net.samagames.survivalapi.modules.craft;

import net.md_5.bungee.api.ChatColor;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Map;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class InventorsModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public InventorsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event)
    {
        if (event.getRecipe().getResult() == null || event.getRecipe().getResult().getType() == Material.AIR)
            return ;

        String name;
        switch (event.getRecipe().getResult().getType())
        {
            case DIAMOND_AXE:
                name = "Hache en diamant";
                break ;
            case DIAMOND_BOOTS:
                name = "Bottes en diamant";
                break ;
            case DIAMOND_CHESTPLATE:
                name = "Plastron en diamant";
                break ;
            case DIAMOND_HELMET:
                name = "Casque en diamant";
                break ;
            case DIAMOND_LEGGINGS:
                name = "Jambes en diamant";
                break ;
            case DIAMOND_PICKAXE:
                name = "Pioche en diamant";
                break ;
            case DIAMOND_SPADE:
                name = "Pelle en diamant";
                break ;
            case DIAMOND_SWORD:
                name = "Épée en diamant";
                break ;
            default:
                name = null;
        }

        if (name == null)
            return;

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD).append("[").append(ChatColor.YELLOW);
        builder.append("Inventors");
        builder.append(ChatColor.GOLD).append("]").append(ChatColor.YELLOW);
        builder.append(" Le joueur ").append(ChatColor.GOLD).append(event.getWhoClicked().getName()).append(ChatColor.YELLOW);
        builder.append(" a crafté : ").append(ChatColor.GOLD);
        builder.append(name);
        builder.append(ChatColor.YELLOW).append(" !");

        Bukkit.broadcastMessage(builder.toString());
    }
}
