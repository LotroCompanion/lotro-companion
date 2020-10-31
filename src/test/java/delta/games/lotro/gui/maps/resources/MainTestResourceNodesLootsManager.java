package delta.games.lotro.gui.maps.resources;

import java.util.List;

import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingUtils;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.items.Item;

/**
 * Simple test class for the resource nodes loots manager.
 * @author DAM
 */
public class MainTestResourceNodesLootsManager
{
  private void doIt()
  {
    doProfession("Cook");
    doProfession("Prospector");
    doProfession("Forester");
    doProfession("Scholar");
  }

  private void doProfession(String name)
  {
    Profession profession=CraftingUtils.getProfessionByName(name);
    System.out.println("Profession: "+name);
    for(CraftingLevel level : profession.getLevels())
    {
      System.out.println("\tUsing: "+level);
      ResourceNodesLootManager mgr=new ResourceNodesLootManager(level);
      // Source items
      System.out.println("\t\tSource items:");
      List<Item> sourceItems=mgr.getSourceItems();
      for(Item sourceItem : sourceItems)
      {
        System.out.println("\t\t\t"+sourceItem.getName()+" - "+sourceItem.getSubCategory());
      }
      // Global loot
      System.out.println("\t\tGlobal loot:");
      List<Item> lootItems=mgr.getGlobalLoots();
      for(Item lootItem : lootItems)
      {
        System.out.println("\t\t\t"+lootItem.getName()+" - "+lootItem.getSubCategory());
      }
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    MainTestResourceNodesLootsManager mgr=new MainTestResourceNodesLootsManager();
    mgr.doIt();
  }
}
