package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;

/**
 * Panel to display the a legendary instance (reloaded).
 * @author DAM
 */
public class LegendaryInstance2DisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // UI
  private JPanel _panel;
  private JLabel _name;
  // Controller
  private TraceriesSetDisplayController _traceries;

  /**
   * Constructor.
   * @param itemInstance Item instance.
   */
  public LegendaryInstance2DisplayPanelController(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    _name=GuiFactory.buildLabel("");
    _traceries=new TraceriesSetDisplayController(itemInstance);
    _panel=buildPanel();
    update();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Name
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,0),0,0);
    ret.add(_name,c);
    y++;
    JPanel traceries=_traceries.getPanel();
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    ret.add(traceries,c);
    y++;
    return ret;
  }

  /**
   * Update the data to display.
   */
  public void update()
  {
    LegendaryInstance2 legendaryInstance=(LegendaryInstance2)_itemInstance;
    LegendaryInstanceAttrs2 attrs=legendaryInstance.getLegendaryAttributes();
    // Name
    String name=attrs.getLegendaryName();
    _name.setText(name);
    _name.setVisible(name.length()>0);
    // Traceries
    _traceries.update();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // Controllers
    if (_traceries!=null)
    {
      _traceries.dispose();
      _traceries=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
  }
}
