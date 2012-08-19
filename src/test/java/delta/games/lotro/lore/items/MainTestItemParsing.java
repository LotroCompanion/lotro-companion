package delta.games.lotro.lore.items;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.io.web.ItemPageParser;

/**
 * Test for item description parsing.
 * @author DAM
 */
public class MainTestItemParsing
{
  private String[] getTestURLs()
  {
    String url0="http://lorebook.lotro.com/wiki/Tool:Superior_Tools_of_the_Explorer";
    String url1="http://lorebook.lotro.com/wiki/Item:Glossy_Eye";
    String url2="http://lorebook.lotro.com/wiki/Armour:Jacket_of_the_Impossible_Shot";
    String url3="http://lorebook.lotro.com/wiki/Item:Bright_Lamp";
    String url4="http://lorebook.lotro.com/wiki/Item:Westfold_Tome_of_the_Wind-rider";
    String url5="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879219223";
    String url6="http://lorebook.lotro.com/wiki/Weapon:Black_Ash_Crossbow";
    String url7="http://lorebook.lotro.com/wiki/Weapon:Battle_Crossbow_of_the_Spirit"; // +5 Damage to The Dead
    String url8="http://lorebook.lotro.com/wiki/Weapon:Blade_of_the_Valiant"; // Westerness damage
    String url9="http://lorebook.lotro.com/wiki/Weapon:Ornate_Black_Ash_Bow"; // Light damage
    String url10="http://lorebook.lotro.com/wiki/Weapon:Methathol"; // Ancient-dwarf damage
    String url11="http://lorebook.lotro.com/wiki/Weapon:Cumaed"; // Beleriand
    String url2Items="http://lorebook.lotro.com/wiki/Weapon:Backed_Hand_Axe_%28Level_23%29";
    String url6DifferentItems="http://lorebook.lotro.com/wiki/Item:Bold_Bracelet_%28Level_58%29";
    String[] urls={ url0, url1, url2, url3, url4, url5, url6, url7, url8, url9, url10, url11,
     url2Items, url6DifferentItems };
    //String[] urls={ url6DifferentItems };
    return urls;
  }

  /**
   * Parse some items.
   * @return A list of items.
   */
  public List<Item> parseItems()
  {
    List<Item> ret=new ArrayList<Item>();
    ItemPageParser parser=new ItemPageParser();
    String[] urls=getTestURLs();
    for(String url : urls)
    {
      List<Item> items=parser.parseItemPage(url);
      if ((items!=null) && (items.size()>0))
      {
        ret.addAll(items);
      }
    }
    return ret;
  }

  private void doIt()
  {
    String[] urls=getTestURLs();
    List<Item> items=parseItems();
    int index=0;
    for(Item item : items)
    {
      if (item!=null)
      {
        System.out.println(item.dump());
      }
      else
      {
        System.out.println("Item ["+urls[index]+"] is null!");
      }
      index++;
    }
  }

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestItemParsing().doIt();
  }
}
