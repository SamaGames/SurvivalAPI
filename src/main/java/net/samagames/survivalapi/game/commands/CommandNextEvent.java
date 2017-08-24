package net.samagames.survivalapi.game.commands;

import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
public class CommandNextEvent implements CommandExecutor
{
    private static SurvivalGame game;

    /**
     * Set the game instance to use later
     *
     * @param instance Game instance
     */
    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }

    /**
     * Command executor
     *
     * @param commandSender Sender
     * @param command Command instance
     * @param s Command's name
     * @param strings Command's arguments
     *
     * @return {@code true} is successfully executed or {@code false}
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (game.getSurvivalGameLoop() != null && game.isGameStarted())
            game.getSurvivalGameLoop().forceNextEvent();
        else
            commandSender.sendMessage(ChatColor.RED + "Erreur: la partie n'a pas commencé !");

        return true;
    }
}
