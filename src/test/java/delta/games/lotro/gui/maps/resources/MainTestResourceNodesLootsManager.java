package delta.games.lotro.gui.maps.resources;

import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingUtils;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Simple test class for the resource nodes loots manager.
 * @author DAM
 */
public class MainTestResourceNodesLootsManager
{
  private ResourceNodesLootManager _mgr=new ResourceNodesLootManager();

  private void doIt()
  {
    doProfession("Prospector");
    doProfession("Forester");
    doProfession("Scholar");
  }

  private void doProfession(String name)
  {
    Profession profession=CraftingUtils.getProfessionByName(name);
    for(CraftingLevel level : profession.getLevels())
    {
      _mgr.getLoots(level);
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
