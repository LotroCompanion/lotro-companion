package delta.games.lotro.gui.character.status.traits.mountedAppearances;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.status.traits.shared.AvailableTraitsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.gui.character.status.traits.TraitsStatusPanelController;

/**
 * Test class for the mounted appearances status panel.
 * @author DAM
 */
public class MainTestMountedAppearancesStatusPanelController
{
  private List<TraitDescription> getMountedAppearancesTraits()
  {
    List<TraitDescription> ret=new ArrayList<TraitDescription>();
    for(TraitDescription trait : TraitsManager.getInstance().getAll())
    {
      if (trait.getNature().getCode()==16)
      {
        ret.add(trait);
      }
    }
    return ret;
  }

  private void doIt()
  {
    DefaultWindowController c=new DefaultWindowController();
    JFrame f=c.getFrame();
    AvailableTraitsStatus status=new AvailableTraitsStatus();
    status.addTraitID(1879250430);
    List<TraitDescription> traits=getMountedAppearancesTraits();
    TraitsStatusPanelController s=new TraitsStatusPanelController(null,traits,status);
    JPanel panel=s.getPanel();
    f.add(panel);
    f.pack();
    f.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestMountedAppearancesStatusPanelController().doIt();
  }
}
