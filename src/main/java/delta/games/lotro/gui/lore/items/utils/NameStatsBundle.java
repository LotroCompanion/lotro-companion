package delta.games.lotro.gui.lore.items.utils;

import delta.common.ui.swing.labels.MultilineLabel2;

/**
 * Controller for the UI items to display an icon, a name, and some stats.
 * @author DAM
 */
public class NameStatsBundle
{
  // UI
  protected MultilineLabel2 _name;
  protected MultilineLabel2 _stats;

  /**
   * Constructor.
   */
  public NameStatsBundle()
  {
    // Label
    _name=new MultilineLabel2();
    // Stats
    _stats=new MultilineLabel2(10);
  }

  /**
   * Get the gadget for the name.
   * @return a multi-lines label.
   */
  public MultilineLabel2 getNameGadget()
  {
    return _name;
  }

  /**
   * Get the gadget for the stats.
   * @return a multi-lines label.
   */
  public MultilineLabel2 getStatsGadget()
  {
    return _stats;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _name=null;
    _stats=null;
  }
}
