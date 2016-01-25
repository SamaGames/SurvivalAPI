package net.samagames.survivalapi.game.commands;

import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * CommandNextEvent class
 *
 * Copyright (c) for SamaGames
 * All right reserved
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
