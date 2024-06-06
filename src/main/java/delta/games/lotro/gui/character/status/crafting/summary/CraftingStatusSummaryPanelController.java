package delta.games.lotro.gui.character.status.crafting.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.status.crafting.summary.CraftingStatusSummary;
import delta.games.lotro.character.status.crafting.summary.ProfessionStatusSummary;

/**
 * Panel to show a crafting status summary.
 * @author DAM
 */
public class CraftingStatusSummaryPanelController extends AbstractPanelController
{
  private List<ProfessionStatusSummaryPanelController> _professionControllers;

  /**
   * Constructor.
   */
  public CraftingStatusSummaryPanelController()
  {
    super();
    _professionControllers=new ArrayList<ProfessionStatusSummaryPanelController>();
    setPanel(GuiFactory.buildPanel(new GridBagLayout()));
  }

  /**
   * Set the status to show.
   * @param summary Status to show.
   */
  public void setStatus(CraftingStatusSummary summary)
  {
    disposeChildPanels();
    for(ProfessionStatusSummary professionSummary : summary.getProfessionStatuses())
    {
      ProfessionStatusSummaryPanelController ctrl=new ProfessionStatusSummaryPanelController();
      ctrl.setStatus(professionSummary);
      _professionControllers.add(ctrl);
    }
    fillPanel();
  }

  private void fillPanel()
  {
    JPanel panel=getPanel();
    panel.removeAll();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    for(ProfessionStatusSummaryPanelController ctrl : _professionControllers)
    {
      JPanel childPanel=ctrl.getPanel();
      panel.add(childPanel,c);
      c.gridy++;
    }
    boolean visible=(panel.getComponentCount()>0);
    panel.setVisible(visible);
  }

  private void disposeChildPanels()
  {
    for(ProfessionStatusSummaryPanelController ctrl : _professionControllers)
    {
      ctrl.dispose();
    }
    _professionControllers.clear();
  }
}
