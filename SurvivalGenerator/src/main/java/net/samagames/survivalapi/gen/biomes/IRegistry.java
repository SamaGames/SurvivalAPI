package net.samagames.survivalapi.gen.biomes;

public interface IRegistry<K, V> extends Iterable<V>
{
    void register(int id, K entry, V value);

    V getObject(K name);

    /**
     * Gets the name we use to identify the given object.
     */
    K getNameForObject(V obj);

    /**
     * Does this registry contain an entry for the given key?
     */
    boolean containsKey(K key);

    /**
     * Gets the integer ID we use to identify the given object.
     */
    int getIDForObject(V obj);

    /**
     * Gets the object identified by the given ID.
     */
    V getObjectById(int id);
}