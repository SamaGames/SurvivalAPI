package net.samagames.survivalapi.gen.biomes;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class IDRegistry<T> implements Iterable<T>
{
    private final IdentityHashMap<T, Integer> identityMap;
    private final List<T> objectList = Lists.newArrayList();

    public IDRegistry(int size)
    {
        this.identityMap = new IdentityHashMap(size);
    }

    public void put(T key, Integer value)
    {
        this.identityMap.put(key, value);

        while (this.objectList.size() <= value)
        {
            this.objectList.add(null);
        }

        this.objectList.set(value, key);
    }

    public void remove(T key)
    {
        this.put(key, null);
    }

    public int get(T key)
    {
        Integer integer = this.identityMap.get(key);
        return integer == null ? -1 : integer.intValue();
    }

    public final T getByValue(int value)
    {
        return (T)(value >= 0 && value < this.objectList.size() ? this.objectList.get(value) : null);
    }

    public Iterator<T> iterator()
    {
        return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
    }
}