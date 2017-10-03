package delta.games.lotro.gui.character.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test class for the characters selector.
 * @author DAM
 */
public class MainTestCharactersSelector
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    List<CharacterFile> selectedToons=new ArrayList<CharacterFile>();
    selectedToons=CharactersSelectorWindowController.selectToons(null,toons,selectedToons,toons);
    if (selectedToons!=null)
    {
      for(CharacterFile toon : selectedToons)
      {
        System.out.println(toon.getIdentifier());
      }
    }
  }
}
