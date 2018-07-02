package delta.games.lotro.gui.items.essences;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.Item;

/**
 * Test class for the essence choice UI.
 * @author DAM
 */
public class MainTestEssenceChoice
{
  private void doIt()
  {
    CharacterSummary c=new CharacterSummary();
    c.setCharacterClass(CharacterClass.MINSTREL);
    c.setLevel(110);
    Item ret=EssenceChoice.chooseEssence(null,c);
    System.out.println(ret);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestEssenceChoice().doIt();
  }
}
