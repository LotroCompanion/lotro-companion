package delta.games.lotro.gui.character.status.allegiances.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.lore.allegiances.AllegianceDescription;
import delta.games.lotro.lore.allegiances.AllegiancesManager;

/**
 * Controller for a panel that display the status for a group of allegiances.
 * @author DAM
 */
public class AllegiancesGroupStatusPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private List<SingleAllegianceGadgetsController> _gadgets;

  /**
   * Constructor.
   * @param group Group to show.
   */
  public AllegiancesGroupStatusPanelController(String group)
  {
    _gadgets=new ArrayList<SingleAllegianceGadgetsController>();
    init(group);
    _panel=buildPanel();
    _panel.setBorder(GuiFactory.buildTitledBorder(group));
  }

  private void init(String group)
  {
    AllegiancesManager mgr=AllegiancesManager.getInstance();
    List<AllegianceDescription> allegiances=mgr.getAllegiances(group);
    for(AllegianceDescription allegiance : allegiances)
    {
      SingleAllegianceGadgetsController gadgets=new SingleAllegianceGadgetsController(allegiance);
      _gadgets.add(gadgets);
    }
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(SingleAllegianceGadgetsController gadgets : _gadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(gadgets.getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
      ret.add(gadgets.getNameGadget(),c);
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(gadgets.getStateGadget(),c);
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
   * Set the status to display.
   * @param statusMgr Allegiances status manager.
   */
  public void setStatus(AllegiancesStatusManager statusMgr)
  {
    for(SingleAllegianceGadgetsController gadgets : _gadgets)
    {
      AllegianceDescription allegiance=gadgets.getAllegiance();
      AllegianceStatus status=statusMgr.get(allegiance,false);
      gadgets.setAllegianceStatus(status);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_gadgets!=null)
    {
      for(SingleAllegianceGadgetsController gadget : _gadgets)
      {
        gadget.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
  }
}
