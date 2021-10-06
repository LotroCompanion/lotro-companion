package delta.games.lotro.gui.lore.items.legendary2;

import javax.swing.JPanel;

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
  // Controller
  private TraceriesSetDisplayController _traceries;

  /**
   * Constructor.
   * @param itemInstance Item instance.
   */
  public LegendaryInstance2DisplayPanelController(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    LegendaryInstance2 leg2=(LegendaryInstance2)itemInstance;
    _traceries=new TraceriesSetDisplayController(leg2.getLegendaryAttributes().getSocketsSetup());
    _panel=buildPanel();
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
    JPanel traceries=_traceries.getPanel();
    return traceries;
  }

  /**
   * Update the data to display.
   */
  public void update()
  {
    LegendaryInstance2 legendaryInstance2=(LegendaryInstance2)_itemInstance;
    LegendaryInstanceAttrs2 attrs=legendaryInstance2.getLegendaryAttributes();
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
  }
}
