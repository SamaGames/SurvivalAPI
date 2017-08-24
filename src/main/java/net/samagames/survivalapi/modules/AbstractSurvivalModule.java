package net.samagames.survivalapi.modules;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
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
public abstract class AbstractSurvivalModule implements Listener
{
    protected final SurvivalPlugin plugin;
    protected final SurvivalAPI api;
    protected final Map<String, Object> moduleConfiguration;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public AbstractSurvivalModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        this.plugin = plugin;
        this.api = api;
        this.moduleConfiguration = moduleConfiguration;
    }

    /**
     * Fired when the game starts
     *
     * @param game Game instance
     */
    public void onGameStart(SurvivalGame game) {}

    /**
     * Get module's dependencies
     *
     * @return A list of modules
     */
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        return new ArrayList<>();
    }
}
