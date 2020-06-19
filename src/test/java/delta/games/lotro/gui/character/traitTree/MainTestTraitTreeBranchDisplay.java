package delta.games.lotro.gui.character.traitTree;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.TraitTree;
import delta.games.lotro.character.classes.TraitTreeBranch;
import delta.games.lotro.common.CharacterClass;

/**
 * Test for trait tree branch display.
 * @author DAM
 */
public class MainTestTraitTreeBranchDisplay
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(CharacterClass cClass : CharacterClass.ALL_CLASSES)
    {
      ClassDescription classDescription=ClassesManager.getInstance().getClassDescription(cClass);
      TraitTree traitTree=classDescription.getTraitTree();
      for(TraitTreeBranch branch : traitTree.getBranches())
      {
        TraitTreeBranchPanelController ctrl=new TraitTreeBranchPanelController(branch);
        JPanel panel=ctrl.getPanel();
        JFrame frame=new JFrame("Branch: "+branch.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Create and set up the content pane.
        frame.setContentPane(panel);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    }
  }
}
