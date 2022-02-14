package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;

/**
 * Controller for a panel to show a collection of 'same cosmetics' groups.
 * @author DAM
 */
public class SameCosmeticsPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SameCosmeticsGroupPanelController> _groups;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SameCosmeticsPanelController(WindowController parent)
  {
    _parent=parent;
    _panel=GuiFactory.buildPanel(new GridBagLayout());
    _groups=new ArrayList<SameCosmeticsGroupPanelController>();
  }

  /**
   * Update the display.
   * @param groups Groups to display.
   */
  public void updateDisplay(List<CosmeticItemsGroup> groups)
  {
    disposeGroups();
    for(CosmeticItemsGroup group : groups)
    {
      SameCosmeticsGroupPanelController ctrl=new SameCosmeticsGroupPanelController(_parent,group);
      _groups.add(ctrl);
    }
    fillPanel();
    _panel.revalidate();
    _panel.repaint();
  }

  private void fillPanel()
  {
    _panel.removeAll();
    int y=0;
    for(SameCosmeticsGroupPanelController ctrl : _groups)
    {
      JPanel groupPanel=ctrl.getPanel();
      groupPanel.setBorder(GuiFactory.buildTitledBorder(""));
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      _panel.add(groupPanel,c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    _panel.add(Box.createVerticalGlue(),c);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_groups!=null)
    {
      disposeGroups();
      _groups=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }

  private void disposeGroups()
  {
    for(SameCosmeticsGroupPanelController group : _groups)
    {
      group.dispose();
    }
    _groups.clear();
  }
}
