package delta.games.lotro.gui.items.legendary.non_imbued;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacyInstance;

/**
 * Controller for the UI items of a single default non-imbued legacy.
 * @author DAM
 */
public class SingleDefaultNonImbuedLegacyEditionController extends SingleNonImbuedLegacyEditionController<DefaultNonImbuedLegacyInstance>
{
  /**
   * Constructor.
   * @param parent Parent window.
   * @param legacyInstance Legacy to edit.
   * @param constraints Constraints.
   */
  public SingleDefaultNonImbuedLegacyEditionController(WindowController parent, DefaultNonImbuedLegacyInstance legacyInstance, ClassAndSlot constraints)
  {
    super(parent,legacyInstance,constraints);
  }

  private DefaultNonImbuedLegacy getDefaultLegacy()
  {
    return (DefaultNonImbuedLegacy)_legacy;
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(ImbuedLegacyInstance storage)
  {
    // Put UI data into the given storage
  }

  protected void handleButtonClick(JButton button)
  {
    //
  }

  protected StatsProvider getStatsProvider()
  {
    DefaultNonImbuedLegacy legacy=getDefaultLegacy();
    StatsProvider statsProvider=legacy.getEffect().getStatsProvider();
    return statsProvider;
  }

  protected void updateIcon()
  {
    DefaultNonImbuedLegacy legacy=getDefaultLegacy();
    int iconId=legacy.getIconId();
    ImageIcon icon=LotroIconsManager.getLegacyIcon(iconId);
    _icon.setIcon(icon);
  }
}
