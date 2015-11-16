package net.samagames.survivalapi.game.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class NaturalListener implements Listener
{
    /**
     * Disable weather
     *
     * @param event Event
     */
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event)
    {
        event.setCancelled(true);
    }
}
