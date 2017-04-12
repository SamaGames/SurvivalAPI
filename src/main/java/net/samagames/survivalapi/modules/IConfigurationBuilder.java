package net.samagames.survivalapi.modules;

import com.google.gson.JsonElement;

import java.util.Map;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 12/04/2017
 */
public interface IConfigurationBuilder
{
    Map<String, Object> build();
    Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception;
}
