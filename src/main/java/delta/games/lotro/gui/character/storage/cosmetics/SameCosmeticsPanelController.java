package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

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
  private List<SameCosmeticsGroupPanelController> _groups;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param groups Groups to show.
   */
  public SameCosmeticsPanelController(WindowController parent, List<CosmeticItemsGroup> groups)
  {
    init(parent,groups);
    _panel=buildPanel();
  }

  private void init(WindowController parent, List<CosmeticItemsGroup> groups)
  {
    _groups=new ArrayList<SameCosmeticsGroupPanelController>();
    for(CosmeticItemsGroup group : groups)
    {
      SameCosmeticsGroupPanelController ctrl=new SameCosmeticsGroupPanelController(parent,group);
      _groups.add(ctrl);
    }
    _panel=buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(SameCosmeticsGroupPanelController ctrl : _groups)
    {
      JPanel groupPanel=ctrl.getPanel();
      groupPanel.setBorder(GuiFactory.buildTitledBorder(""));
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(groupPanel,c);
      y++;
    }
    return ret;
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
      for(SameCosmeticsGroupPanelController group : _groups)
      {
        group.dispose();
      }
      _groups=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
