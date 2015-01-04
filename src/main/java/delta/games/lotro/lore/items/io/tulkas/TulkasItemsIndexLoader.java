package delta.games.lotro.lore.items.io.tulkas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemSturdiness;

/**
 * Items/sets loader for Tulkas DB index.
 * @author DAM
 */
public class TulkasItemsIndexLoader extends TulkasItemsLoader
{
  /**
   * Inspect loaded data items to fetch possible values in fields.
   * @param items Loaded data items.
   */
  public void inspectItems(HashMap<Integer,HashMap<Object,Object>> items)
  {
    int[] keys={3,4,5};
    for(int key : keys)
    {
      List<Integer> sortedValues=inspect(key,items);
      System.out.println(sortedValues.size()+" : values"+key+": "+sortedValues);
    }
    
    //List<Integer> categories=inspect(3,items);
    //List<Integer> qualities=inspect(4,items);
    //List<Integer> durabilities=inspect(5,items);
    List<Integer> backgroundIconIDs=inspect(9,items);
    HashMap<Integer,List<String>> categories2Names=new HashMap<Integer,List<String>>(); 
    HashMap<Integer,List<String>> qualities2Names=new HashMap<Integer,List<String>>();
    HashMap<Integer,List<String>> durabilities2names=new HashMap<Integer,List<String>>();
    HashMap<Integer,List<String>> backgroundIconIDs2Names=new HashMap<Integer,List<String>>();
    for(HashMap<Object,Object> item:items.values())
    {
      String name=(String)item.get(Integer.valueOf(1));
      // Category
      {
        Integer category=(Integer)item.get(Integer.valueOf(3));
        List<String> names=categories2Names.get(category);
        if (names==null)
        {
          names=new ArrayList<String>();
          categories2Names.put(category,names);
        }
        names.add(name);
      }
      // Quality
      {
        Integer quality=(Integer)item.get(Integer.valueOf(4));
        List<String> names=qualities2Names.get(quality);
        if (names==null)
        {
          names=new ArrayList<String>();
          qualities2Names.put(quality,names);
        }
        names.add(name);
      }
      // Durabilities
      {
        Integer durability=(Integer)item.get(Integer.valueOf(5));
        List<String> names=durabilities2names.get(durability);
        if (names==null)
        {
          names=new ArrayList<String>();
          durabilities2names.put(durability,names);
        }
        names.add(name);
      }
      // Background icon ID
      {
        Integer backgroundIconID=(Integer)item.get(Integer.valueOf(9));
        List<String> names=backgroundIconIDs2Names.get(backgroundIconID);
        if (names==null)
        {
          names=new ArrayList<String>();
          backgroundIconIDs2Names.put(backgroundIconID,names);
        }
        names.add(name);
      }
    }
    /*
    for(Integer category : categories)
    {
      List<String> names=categories2Names.get(category);
      Collections.sort(names);
      System.out.println("**************** "+category+" **************");
      for(String name : names)
      {
        System.out.println("\t"+name);
      }
    }
    */
    /*
    for(Integer quality : qualities)
    {
      List<String> names=qualities2Names.get(quality);
      Collections.sort(names);
      System.out.println("**************** "+quality+" **************");
      for(String name : names)
      {
        System.out.println("\t"+name);
      }
    }
    */
    /*
    for(Integer durability : durabilities)
    {
      List<String> names=durabilities2names.get(durability);
      Collections.sort(names);
      System.out.println("**************** "+durability+" **************");
      for(String name : names)
      {
        System.out.println("\t"+name);
      }
    }
    */
    for(Integer backgroundIconID : backgroundIconIDs)
    {
      List<String> names=backgroundIconIDs2Names.get(backgroundIconID);
      Collections.sort(names);
      System.out.println("**************** "+backgroundIconID+" **************");
      for(String name : names)
      {
        System.out.println("\t"+name);
      }
    }
    System.out.println(backgroundIconIDs.size()+" : "+backgroundIconIDs);
  }
  
  List<Integer> inspect(int key, HashMap<Integer,HashMap<Object,Object>> items)
  {
    Set<Integer> values=new HashSet<Integer>();
    Integer k=Integer.valueOf(key);
    for(HashMap<Object,Object> item:items.values())
    {
      Integer v=(Integer)item.get(k);
      if (v!=null)
      {
        values.add(v);
      }
    }
    List<Integer> sortedValues=new ArrayList<Integer>(values);
    Collections.sort(sortedValues);
    return sortedValues;
  }

  /**
   * Build items from raw data items.
   * @param items Loaded data items.
   */
  public void buildItems(HashMap<Integer,HashMap<Object,Object>> items)
  {
    List<Integer> keys=new ArrayList<Integer>(items.keySet());
    Collections.sort(keys);
    int nbKeys=keys.size();
    System.out.println("Min: "+keys.get(0)+", max: "+keys.get(nbKeys-1));
    for(int i=0;i<nbKeys;i++)
    {
      Integer id=keys.get(i);
      HashMap<Object,Object> data=items.get(id);
      String name=(String)data.get(Integer.valueOf(1));
      String description=(String)data.get(Integer.valueOf(2));
      Integer categoryInt=(Integer)data.get(Integer.valueOf(3));
      Boolean isUnique=(Boolean)data.get(Integer.valueOf(7));
      Integer iconID=(Integer)data.get(Integer.valueOf(8));
      Integer backgroundIconid=(Integer)data.get(Integer.valueOf(9));
      Item item=new Item();
      item.setIdentifier(id.intValue());
      item.setName(name);
      item.setDescription(description);
      item.setIconURL(iconID+" - "+backgroundIconid);
      if (isUnique!=null)
      {
        item.setUnique(isUnique.booleanValue());
      }
      if (categoryInt!=null)
      {
        item.setSubCategory(String.valueOf(categoryInt));
      }
      // Quality
      {
        Integer qualityInt=(Integer)data.get(Integer.valueOf(4));
        ItemQuality quality=null;
        switch(qualityInt.intValue())
        {
          case 0: quality=null; break; // special items
          case 1: quality=ItemQuality.LEGENDARY; break;
          case 2: quality=ItemQuality.RARE; break;
          case 3: quality=ItemQuality.INCOMPARABLE; break;
          case 4: quality=ItemQuality.UNCOMMON; break;
          case 5: quality=ItemQuality.COMMON; break;
        }
        item.setQuality(quality);
      }
      // Sturdiness
      {
        Integer durabilityInt=(Integer)data.get(Integer.valueOf(5));
        ItemSturdiness sturdiness=null;
        switch(durabilityInt.intValue())
        {
          case 0: sturdiness=null; break; // consumables
          case 1: sturdiness=ItemSturdiness.SUBSTANTIAL; break;
          case 2: sturdiness=ItemSturdiness.BRITTLE; break;
          case 3: sturdiness=ItemSturdiness.NORMAL; break;
          case 4: sturdiness=ItemSturdiness.TOUGH; break;
          // 5 is unused
          case 6: sturdiness=null; break; // cosmetics
          case 7: sturdiness=ItemSturdiness.WEAK; break;
        }
        item.setSturdiness(sturdiness);
      }
      /*
      ItemsManager mgr=ItemsManager.getInstance();
      mgr.writeItemFile(item);
      */
      System.out.println(i);
      System.out.println(item.dump());
      writeItemToDB(item);
    }
  }
}
