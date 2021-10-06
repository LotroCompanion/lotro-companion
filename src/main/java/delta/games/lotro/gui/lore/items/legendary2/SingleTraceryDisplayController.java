package delta.games.lotro.gui.lore.items.legendary2;

import javax.swing.Icon;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.utils.IconNameStatsBundle;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.Tracery;

/**
 * Controller for the UI items to display a single tracery.
 * @author DAM
 */
public class SingleTraceryDisplayController extends IconNameStatsBundle
{
  /**
   * Constructor.
   */
  public SingleTraceryDisplayController()
  {
    super();
    // Initialize with nothing slotted
    setTracery(null);
  }

  /**
   * Set current essence.
   * @param traceryInstance Essence to set.
   */
  public void setTracery(SocketEntryInstance traceryInstance)
  {
    // Set icon
    Icon icon=null;
    Item item=null;
    Tracery tracery=(traceryInstance!=null)?traceryInstance.getTracery():null;
    if (tracery!=null)
    {
      item=tracery.getItem();
      icon=ItemUiTools.buildItemIcon(item);
    }
    else
    {
      icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
    }
    _icon.setIcon(icon);
    // Text
    String text="";
    if (item!=null)
    {
      text=item.getName();
    }
    _name.setText(text,1);
    // Stats
    if (tracery!=null)
    {
      BasicStatsSet stats=traceryInstance.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _stats.setText(lines);
    }
    else
    {
      _stats.setText(new String[0]);
    }
  }
}
