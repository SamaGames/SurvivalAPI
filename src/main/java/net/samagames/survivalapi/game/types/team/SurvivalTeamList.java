package net.samagames.survivalapi.game.types.team;

import net.samagames.survivalapi.game.SurvivalTeam;
import org.bukkit.ChatColor;

import java.util.*;

public class SurvivalTeamList implements List<SurvivalTeam>
{
    private final ArrayList<SurvivalTeam> internalList;

    public SurvivalTeamList()
    {
        this.internalList = new ArrayList<>();
    }

    @Override
    public int size()
    {
        return internalList.size();
    }

    @Override
    public boolean isEmpty()
    {
        return internalList.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return internalList.contains(o);
    }

    @Override
    public Iterator<SurvivalTeam> iterator()
    {
        return internalList.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return internalList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        //noinspection SuspiciousToArrayCall
        return internalList.toArray(a);
    }

    @Override
    public boolean add(SurvivalTeam team)
    {
        return internalList.add(team);
    }

    @Override
    public boolean remove(Object o)
    {
        return internalList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return internalList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends SurvivalTeam> c)
    {
        return internalList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends SurvivalTeam> c)
    {
        return internalList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return internalList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return internalList.retainAll(c);
    }

    @Override
    public void clear()
    {
        internalList.clear();
    }

    @Override
    public SurvivalTeam get(int index)
    {
        return internalList.get(index);
    }

    @Override
    public SurvivalTeam set(int index, SurvivalTeam element)
    {
        return internalList.set(index, element);
    }

    @Override
    public void add(int index, SurvivalTeam element)
    {
        internalList.add(index, element);
    }

    @Override
    public SurvivalTeam remove(int index)
    {
        return internalList.remove(index);
    }

    @Override
    public int indexOf(Object o)
    {
        return internalList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return internalList.lastIndexOf(o);
    }

    @Override
    public ListIterator<SurvivalTeam> listIterator()
    {
        return internalList.listIterator();
    }

    @Override
    public ListIterator<SurvivalTeam> listIterator(int index)
    {
        return internalList.listIterator(index);
    }

    @Override
    public List<SurvivalTeam> subList(int fromIndex, int toIndex)
    {
        return internalList.subList(fromIndex, toIndex);
    }

    public SurvivalTeam getTeam(UUID player)
    {
        for (SurvivalTeam team : internalList)
            if (team.hasPlayer(player))
                return team;

        return null;
    }

    public SurvivalTeam getTeam(ChatColor color)
    {
        for (SurvivalTeam team : internalList)
            if (team.getChatColor() == color)
                return team;

        return null;
    }
}