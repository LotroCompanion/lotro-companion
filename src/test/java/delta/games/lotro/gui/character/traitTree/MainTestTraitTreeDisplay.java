package delta.games.lotro.gui.character.traitTree;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.TraitTree;
import delta.games.lotro.character.classes.TraitTreeStatus;
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
    status.initFromBuffs(data.getBuffs());
    TraitTreePanelController ctrl=new TraitTreePanelController(traitTree,status);
    JPanel panel=ctrl.getPanel();
    JFrame frame=new JFrame("Class: "+cClass.getLabel());
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //Create and set up the content pane.
    frame.setContentPane(panel);
    //Display the window.
    frame.pack();
    frame.setVisible(true);
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
