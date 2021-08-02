package delta.games.lotro.gui.lore.items.essences;

import javax.swing.Icon;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.utils.IconNameStatsBundle;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for the UI items to display a single essence.
 * @author DAM
 */
public class SingleEssenceDisplayController extends IconNameStatsBundle
{
  /**
   * Constructor.
   */
  public SingleEssenceDisplayController()
  {
    super();
    // Initialize with no essence
    setEssence(null);
  }

  /**
   * Set current essence.
   * @param essence Essence to set.
   */
  public void setEssence(Item essence)
  {
    // Set essence icon
    Icon icon=null;
    if (essence!=null)
    {
      icon=ItemUiTools.buildItemIcon(essence);
    }
    else
    {
      icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
    }
    _icon.setIcon(icon);
    // Text
    String text="";
    if (essence!=null)
    {
      text=essence.getName();
    }
    _name.setText(text,1);
    // Stats
    if (essence!=null)
    {
      BasicStatsSet stats=essence.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _stats.setText(lines);
    }
    else
    {
      _stats.setText(new String[0]);
    }
  }
}
