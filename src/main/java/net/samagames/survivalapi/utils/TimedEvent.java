package net.samagames.survivalapi.utils;

import org.bukkit.ChatColor;

/**
 * TimedEvent class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class TimedEvent implements Runnable
{
    private final String name;
    private final ChatColor color;
    private final Runnable callback;
    private final boolean title;

    private int minutes;
    private int seconds;
    private boolean wasRun;

    /**
     * Constructor
     *
     * @param minutes Number of minutes
     * @param seconds Number of seconds
     * @param name Event's name
     * @param color Event's color
     * @param title Show a countdown title
     * @param callback Callback
     */
    public TimedEvent(int minutes, int seconds, String name, ChatColor color, boolean title, Runnable callback)
    {
        this.name = name;
        this.color = color;
        this.title = title;
        this.callback = callback;

        this.minutes = minutes;
        this.seconds = seconds;
        this.wasRun = false;
    }

    /**
     * Run the callback
     */
    @Override
    public void run()
    {
        this.callback.run();
    }

    /**
     * Decrement the event's duration, also run the callback if
     * the countdown is finished
     */
    public void decrement()
    {
        this.seconds--;

        if (this.seconds < 0)
        {
            this.minutes--;
            this.seconds = 59;
        }

        if ((this.minutes < 0 || this.seconds == 0 && this.minutes == 0) && !this.wasRun)
        {
            this.wasRun = true;
            this.run();
        }
    }

    /**
     * Copy the event and replace the time
     *
     * @param minute Number of minutes
     * @param seconds Number of seconds
     *
     * @return A copy of the event
     */
    public TimedEvent copy(int minute, int seconds)
    {
        return new TimedEvent(minute, seconds, this.name, this.color, this.title, this.callback);
    }

    /**
     * Get the event's name
     *
     * @return Event's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the event's color
     *
     * @return Event's color
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * Get the number of minutes
     *
     * @return Number of minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Get the number of seconds
     *
     * @return Number of seconds
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Has to show a title?
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isTitle()
    {
        return this.title;
    }
}