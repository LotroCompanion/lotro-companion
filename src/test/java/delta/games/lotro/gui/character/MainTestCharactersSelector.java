package delta.games.lotro.gui.character;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;

/**
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
    for(CharacterFile toon : selectedToons)
    {
      System.out.println(toon.getIdentifier());
    }
  }
}
