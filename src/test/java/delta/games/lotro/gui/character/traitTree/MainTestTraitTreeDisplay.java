package delta.games.lotro.gui.character.traitTree;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.traitTree.TraitTree;
import delta.games.lotro.character.stats.buffs.BuffsManager;
import delta.games.lotro.character.status.traitTree.BuffsManagerToTraitTreeStatus;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.common.CharacterClass;

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
    CharacterClass cClass=data.getCharacterClass();
    ClassDescription classDescription=ClassesManager.getInstance().getClassDescription(cClass);
    TraitTree traitTree=classDescription.getTraitTree();
    TraitTreeStatus status=new TraitTreeStatus(traitTree);
    BuffsManager buffs=data.getBuffs();
    BuffsManagerToTraitTreeStatus.initFromBuffs(status,buffs);
    TraitTreeEditionDialog dialog=new TraitTreeEditionDialog(null,data,status);
    TraitTreeStatus result=dialog.editModal();
    if (result!=null)
    {
      BuffsManagerToTraitTreeStatus.updateBuffsFromTraitTreeStatus(result,buffs);
      System.out.println("After: "+buffs);
    }
  }

  private void doIt()
  {
    CharacterData data=getData();
    showTree(data);
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
