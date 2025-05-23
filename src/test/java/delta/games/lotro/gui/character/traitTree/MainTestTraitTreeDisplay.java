package delta.games.lotro.gui.character.traitTree;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;

/**
 * Test for trait tree display.
 * @author DAM
 */
public class MainTestTraitTreeDisplay
{
  private CharacterData getData()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile file=charsMgr.getToonById("Landroval","Giswald");
    CharacterData data=file.getInfosManager().getLastCharacterDescription();
    System.out.println(data);
    return data;
  }

  private void showTree(CharacterData data)
  {
    TraitTreeStatus status=data.getTraits().getTraitTreeStatus();
    int level=data.getLevel();
    TraitTreeWindowController window=new TraitTreeWindowController(null,level,status);
    window.show();
  }

  private void editTree(CharacterData data)
  {
    TraitTreeStatus status=data.getTraits().getTraitTreeStatus();
    int level=data.getLevel();
    TraitTreeEditionDialog dialog=new TraitTreeEditionDialog(null,level,status);
    TraitTreeStatus result=dialog.editModal();
    if (result!=null)
    {
      System.out.println("After: "+result);
    }
  }

  private void doIt()
  {
    CharacterData data=getData();
    showTree(data);
    editTree(data);
  }

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestTraitTreeDisplay().doIt();
  }
}
