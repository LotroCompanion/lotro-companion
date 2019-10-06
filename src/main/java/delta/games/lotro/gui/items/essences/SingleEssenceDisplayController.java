package delta.games.lotro.gui.items.essences;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.gui.utils.StatDisplayUtils;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for the UI items to display a single essence.
 * @author DAM
 */
public class SingleEssenceDisplayController
{
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  // Data
  private Item _essence;
  // UI
  private JLabel _essenceIcon;
  private MultilineLabel2 _essenceName;
  private MultilineLabel2 _essenceStats;

  /**
   * Constructor.
   */
  public SingleEssenceDisplayController()
  {
    _essence=null;
    // Icon
    _essenceIcon=GuiFactory.buildIconLabel(null);
    // Label
    _essenceName=new MultilineLabel2();
    // Stats
    _essenceStats=new MultilineLabel2();
    // Initialize with no essence
    setEssence(null);
  }

  /**
   * Get the managed essence.
   * @return the managed essence.
   */
  public Item getEssence()
  {
    return _essence;
  }

  /**
   * Set current essence.
   * @param essence Essence to set.
   */
  public void setEssence(Item essence)
  {
    // Store essence
    _essence=essence;
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
    _essenceIcon.setIcon(icon);
    // Text
    String text="";
    if (essence!=null)
    {
      text=essence.getName();
    }
    _essenceName.setText(text,1);
    // Stats
    if (essence!=null)
    {
      BasicStatsSet stats=essence.getStats();
      String[] lines=StatDisplayUtils.getStatsDisplayLines(stats);
      _essenceStats.setText(lines);
    }
    else
    {
      _essenceStats.setText(new String[0]);
    }
  }

  /**
   * Get the managed essence button.
   * @return the managed essence button.
   */
  public JLabel getEssenceIcon()
  {
    return _essenceIcon;
  }

  /**
   * Get the label for the essence.
   * @return a label.
   */
  public MultilineLabel2 getEssenceNameGadget()
  {
    return _essenceName;
  }

  /**
   * Get the label for the stats.
   * @return a label.
   */
  public MultilineLabel2 getEssenceStatsGadget()
  {
    return _essenceStats;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _essence=null;
    // UI
    _essenceIcon=null;
    _essenceName=null;
    _essenceStats=null;
  }
}
