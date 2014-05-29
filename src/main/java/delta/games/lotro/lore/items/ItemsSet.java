package delta.games.lotro.lore.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import delta.common.utils.text.EndOfLine;

/**
 * Set of items.
 * <p>
 * Wearing more than one item of a set gives extra bonuses.
 * @author DAM
 */
public class ItemsSet
{
  private String _key;
  private String _name;
  private List<Integer> _itemIds;
  private List<String> _itemKeys;
  private HashMap<Integer,List<String>> _bonuses;

  /*
    <set name="Armour of the Hytbold Cleanser" level="97">
    <piece id="1879247216" name="Boots of the Hytbold Cleanser"/>
  <piece id="1879247219" name="Gloves of the Hytbold Cleanser"/>
  <piece id="1879247220" name="Leggings of the Hytbold Cleanser"/>
  <piece id="1879247223" name="Shoulderpads of the Hytbold Cleanser"/>
  <piece id="1879247228" name="Jacket of the Hytbold Cleanser"/>
  <piece id="1879247230" name="Helm of the Hytbold Cleanser"/>
    <effect pieceCount="2"><![CDATA[+38 Will]]></effect>
  <effect pieceCount="2"><![CDATA[+1176 Finesse Rating]]></effect>
  <effect pieceCount="3"><![CDATA[+10% Smouldering Wrath Damage]]></effect>
  <effect pieceCount="4"><![CDATA[+388 Critical Rating]]></effect>
  <effect pieceCount="5"><![CDATA[Fiery Ridicule reduces recovery time of Essay of Fire by 3 seconds]]></effect>
  <effect pieceCount="5"><![CDATA[ ]]></effect>
    <description></description>
  </set>
   */

  /**
   * Constructor.
   */
  public ItemsSet()
  {
    _key=null;
    _name=null;
    _itemIds=new ArrayList<Integer>();
    _itemKeys=new ArrayList<String>();
    _bonuses=new HashMap<Integer,List<String>>();
  }
  
  /**
   * Get the identifier of this set.
   * @return An identifier.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Set the key of this set.
   * @param key Key to set.
   */
  public void setKey(String key)
  {
    _key=key;
  }
  
  /**
   * Get the name of this set.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this set.
   * @param name Name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Add a new item in this set.
   * @param itemId Item identifier.
   * @param itemKey Item key.
   */
  public void addItem(int itemId, String itemKey)
  {
    _itemIds.add(Integer.valueOf(itemId));
    _itemKeys.add(itemKey);
  }

  /**
   * Get the identifiers of the items in this set.
   * @return An array of item identifiers.
   */
  public String[] getItemKeys()
  {
    int nb=_itemIds.size();
    String[] ret=_itemIds.toArray(new String[nb]);
    return ret;
  }

  /**
   * Get the identifiers of the items in this set.
   * @return An array of item identifiers.
   */
  public int[] getItemIds()
  {
    int nb=_itemKeys.size();
    int[] ret=new int[nb];
    for(int i=0;i<nb;i++) ret[i]=_itemIds.get(i).intValue();
    return ret;
  }

  /**
   * Register a bonus line.
   * @param nb Number of items to have this bonus.
   * @param bonus Bonus to add.
   */
  public void addBonus(int nb, String bonus)
  {
    Integer key=Integer.valueOf(nb);
    List<String> bonuses=_bonuses.get(key);
    if (bonuses==null)
    {
      bonuses=new ArrayList<String>();
      _bonuses.put(key,bonuses);
    }
    bonuses.add(bonus);
  }

  /**
   * Get the number of items that give bonuses.
   * @return An array of sorted number of items.
   */
  public int[] getNumberOfItemsForBonuses()
  {
    Set<Integer> nbs=_bonuses.keySet();
    int nb=nbs.size();
    int[] ret=new int[nb];
    int i=0;
    for(Integer key : nbs)
    {
      ret[i]=key.intValue();
      i++;
    }
    Arrays.sort(ret);
    return ret;
  }

  /**
   * Get the bonus effects for a given number of items.
   * @param nbItems Number of items to look for.
   * @return An array of bonus effects.
   */
  public String[] getBonus(int nbItems)
  {
    String[] ret;
    List<String> bonuses=_bonuses.get(Integer.valueOf(nbItems));
    if (bonuses!=null)
    {
      int nb=bonuses.size();
      ret=bonuses.toArray(new String[nb]);
    }
    else
    {
      ret=new String[0];
    }
    return ret;
  }

  /**
   * Dump the contents of this set of items as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Name: ").append(_name);
    if (_key!=null)
    {
      sb.append(" (id=");
      sb.append(_key);
      sb.append(')');
    }
    sb.append(EndOfLine.NATIVE_EOL);
    for(Integer itemId : _itemIds)
    {
      sb.append('\t').append(itemId);
      sb.append(EndOfLine.NATIVE_EOL);
    }
    int[] nbs=getNumberOfItemsForBonuses();
    for(int i=0;i<nbs.length;i++)
    {
      sb.append('\t').append(nbs[i]).append(" items:");
      sb.append(EndOfLine.NATIVE_EOL);
      List<String> bonuses=_bonuses.get(Integer.valueOf(nbs[i]));
      for(String bonus : bonuses)
      {
        sb.append("\t\t").append(bonus).append(EndOfLine.NATIVE_EOL);
      }
    }
    return sb.toString().trim();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
