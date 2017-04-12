package net.samagames.survivalapi.modules.gameplay;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.tools.ItemUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RapidFoodModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RapidFoodModule extends AbstractSurvivalModule
{
    private final Map<EntityType, List<ConfigurationBuilder.IRapidFoodHook>> drops;
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RapidFoodModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.drops = (Map<EntityType, List<ConfigurationBuilder.IRapidFoodHook>>) moduleConfiguration.get("drops");
        this.random = new Random();
    }

    /**
     * Bread dropping
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        Material material = event.getBlock().getType();

        if (material == Material.DEAD_BUSH)
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BREAD, this.random.nextInt(3) + 1));
    }

    /**
     * Soup dropping
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if (event.getEntity().hasMetadata("playerDrop"))
            return;

        Material material = event.getEntity().getItemStack().getType();

        if (material == Material.BROWN_MUSHROOM || material == Material.RED_MUSHROOM)
            event.getEntity().setItemStack(new ItemStack(Material.MUSHROOM_SOUP, 2));
        else if (material == Material.POTATO_ITEM)
            event.getEntity().setItemStack(new ItemStack(Material.BAKED_POTATO, 1));
        else if (material == Material.CARROT_ITEM)
            event.getEntity().setItemStack(new ItemStack(Material.GOLDEN_CARROT, 1));
        else if (material == Material.WHEAT)
            event.getEntity().setItemStack(new ItemStack(Material.BREAD, 1));
        else if (material == Material.PUMPKIN)
            event.getEntity().setItemStack(new ItemStack(Material.PUMPKIN_PIE, 1));
    }

    /**
     * Drop cooked food
     * 
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        List<ItemStack> newDrops = null;

        if (this.drops.containsKey(entity.getType()))
        {
            newDrops = new ArrayList<>();

            for (ConfigurationBuilder.IRapidFoodHook rapidFoodHook : this.drops.get(entity.getType()))
                newDrops.addAll(rapidFoodHook.getDrops(event.getDrops(), this.random));
        }

        if (newDrops != null && !newDrops.isEmpty())
        {
            event.getDrops().clear();
            event.getDrops().addAll(newDrops);
        }

        event.setDroppedExp(event.getDroppedExp() * 2);
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        List<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private final Map<EntityType, List<IRapidFoodHook>> drops;

        public ConfigurationBuilder()
        {
            this.drops = new HashMap<>();
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("drops", this.drops);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("drops"))
            {
                JsonArray dropsJson = configuration.get("drops").getAsJsonArray();

                for (int i = 0; i < dropsJson.size(); i++)
                {
                    JsonObject dropJson = dropsJson.get(i).getAsJsonObject();
                    EntityType entityType = EntityType.valueOf(dropJson.get("entity").getAsString());

                    JsonArray stacksJson = dropJson.get("stacks").getAsJsonArray();
                    Map<Material, Pair<ItemStack, String>> matchStack = new HashMap<>();
                    List<Pair<ItemStack, String>> addStacks = new ArrayList<>();

                    for (int j = 0; j < stacksJson.size(); j++)
                    {
                        JsonObject stackJson = stacksJson.get(j).getAsJsonObject();
                        String stackType = stackJson.get("type").getAsString();

                        ItemStack drop = ItemUtils.strToStack(stackJson.get("drop").getAsString());
                        String quantity = "1";

                        JsonObject quantityJson = stackJson.get("quantity").getAsJsonObject();
                        String quantityType = quantityJson.get("type").getAsString();
                        String quantityValue = quantityJson.get("value").getAsString();

                        if (quantityType.equals("multiply") && !stackType.equals("add"))
                            quantity = "M" + quantityValue;
                        else if (quantityType.equals("fixed"))
                            quantity = "F" + quantityValue;
                        else if (quantityType.equals("random"))
                            quantity = "R" + quantityValue;

                        if (stackType.equals("replace"))
                        {
                            Material match = Material.matchMaterial(stackJson.get("match").getAsString());
                            matchStack.put(match, Pair.of(drop, quantity));
                        }
                        else if (stackType.equals("add"))
                        {
                            addStacks.add(Pair.of(drop, quantity));
                        }
                    }

                    this.addDrop(entityType, (drops, random) ->
                    {
                        List<ItemStack> newDrops = new ArrayList<>();

                        for (ItemStack stack : drops)
                        {
                            if (matchStack.containsKey(stack.getType()))
                            {
                                Pair<ItemStack, String> newDropPair = matchStack.get(stack.getType());
                                ItemStack newDrop = newDropPair.getKey();
                                int quantity = 1;

                                if (newDropPair.getValue().startsWith("M"))
                                    quantity = stack.getAmount() * Integer.parseInt(newDropPair.getValue().substring(1));
                                else if (newDropPair.getValue().startsWith("F"))
                                    quantity = Integer.parseInt(newDropPair.getValue().substring(1));
                                else if (newDropPair.getValue().startsWith("R"))
                                    quantity = random.nextInt(Integer.parseInt(newDropPair.getValue().substring(1))) + 1;

                                newDrop.setAmount(quantity);
                                newDrops.add(newDrop);
                            }
                        }

                        for (Pair<ItemStack, String> stack : addStacks)
                        {
                            ItemStack newDrop = stack.getKey();
                            int quantity = 1;

                            if (stack.getValue().startsWith("F"))
                                quantity = Integer.parseInt(stack.getValue().substring(1));
                            else if (stack.getValue().startsWith("R"))
                                quantity = random.nextInt(Integer.parseInt(stack.getValue().substring(1))) + 1;

                            newDrop.setAmount(quantity);
                            newDrops.add(newDrop);
                        }

                        return newDrops;
                    }, true);
                }
            }

            return this.build();
        }

        public ConfigurationBuilder addDefaults()
        {
            this.addDrop(EntityType.COW, (drops, random) ->
            {
                List<ItemStack> newDrops = new ArrayList<>();

                for (ItemStack stack : drops)
                {
                    if (stack.getType() == Material.RAW_BEEF)
                        newDrops.add(new ItemStack(Material.COOKED_BEEF, stack.getAmount() * 2));
                    else if (stack.getType() == Material.LEATHER)
                        newDrops.add(new ItemStack(Material.LEATHER, stack.getAmount() * 2));
                }

                return newDrops;
            }, false);

            this.addDrop(EntityType.HORSE, (drops, random) ->
            {
                List<ItemStack> newDrops = new ArrayList<>();

                for (ItemStack stack : drops)
                {
                    if (stack.getType() == Material.RAW_BEEF)
                        newDrops.add(new ItemStack(Material.COOKED_BEEF, stack.getAmount() * 2));
                    else if (stack.getType() == Material.LEATHER)
                        newDrops.add(new ItemStack(Material.LEATHER, stack.getAmount() * 2));
                }

                return newDrops;
            }, false);

            this.addDrop(EntityType.SHEEP, (drops, random) ->
            {
                List<ItemStack> newDrops = drops.stream().filter(stack -> stack.getType() == Material.MUTTON).map(stack -> new ItemStack(Material.COOKED_MUTTON, stack.getAmount() * 2)).collect(Collectors.toList());

                if (random.nextInt(1) >= 0)
                    newDrops.add(new ItemStack(Material.LEATHER, random.nextInt(5) + 1));

                if (random.nextInt(1) >= 0)
                    newDrops.add(new ItemStack(Material.STRING, random.nextInt(2) + 1));

                return newDrops;
            }, false);

            this.addDrop(EntityType.PIG, (drops, random) ->
            {
                List<ItemStack> newDrops = drops.stream().filter(stack -> stack.getType() == Material.PORK).map(stack -> new ItemStack(Material.GRILLED_PORK, stack.getAmount() * 2)).collect(Collectors.toList());

                if (random.nextInt(1) == 0)
                    newDrops.add(new ItemStack(Material.LEATHER, random.nextInt(5) + 1));

                return newDrops;
            }, false);

            this.addDrop(EntityType.CHICKEN, (drops, random) ->
            {
                List<ItemStack> newDrops = new ArrayList<>();

                for (ItemStack stack : drops)
                {
                    if (stack.getType() == Material.RAW_CHICKEN)
                        newDrops.add(new ItemStack(Material.COOKED_CHICKEN, stack.getAmount() * 2));
                    else if (stack.getType() == Material.FEATHER)
                        newDrops.add(new ItemStack(Material.ARROW, stack.getAmount()));
                }

                return newDrops;
            }, false);

            this.addDrop(EntityType.SKELETON, (drops, random) ->
            {
                List<ItemStack> newDrops = new ArrayList<>();

                for (ItemStack stack : drops)
                {
                    if (stack.getType() == Material.ARROW)
                    {
                        newDrops.add(new ItemStack(Material.ARROW, stack.getAmount() * 2));
                    }
                    if (stack.getType() == Material.BOW)
                    {
                        stack.setDurability((short) 0);
                        newDrops.add(stack);
                    }
                }

                return newDrops;
            }, false);

            this.addDrop(EntityType.SQUID, (drops, random) -> Collections.singletonList(new ItemStack(Material.COOKED_FISH, random.nextInt(2) + 1)), false);
            this.addDrop(EntityType.RABBIT, (drops, random) -> drops.stream().filter(stack -> stack.getType() == Material.RABBIT).map(stack -> new ItemStack(Material.COOKED_RABBIT, stack.getAmount() * 2)).collect(Collectors.toList()), false);
            this.addDrop(EntityType.ZOMBIE, (drops, random) -> drops.stream().filter(stack -> stack.getType() == Material.ROTTEN_FLESH).map(stack -> new ItemStack(Material.COOKED_BEEF, stack.getAmount() * 2)).collect(Collectors.toList()), false);
            this.addDrop(EntityType.BAT, (drops, random) -> Collections.singletonList(new ItemStack(Material.COOKED_MUTTON, random.nextInt(2) + 1)), false);

            return this;
        }

        public ConfigurationBuilder addDrop(EntityType type, IRapidFoodHook rapidFoodHook, boolean override)
        {
            if (!this.drops.containsKey(type))
            {
                this.drops.put(type, Collections.singletonList(rapidFoodHook));
            }
            else
            {
                if (override)
                {
                    this.drops.remove(type);
                    this.drops.put(type, Collections.singletonList(rapidFoodHook));
                }
                else
                {
                    this.drops.get(type).add(rapidFoodHook);
                }
            }

            return this;
        }

        public interface IRapidFoodHook
        {
            List<ItemStack> getDrops(List<ItemStack> drops, Random random);
        }
    }
}
