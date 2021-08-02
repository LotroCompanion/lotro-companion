package delta.games.lotro.gui.lore.items.utils;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;

/**
 * Controller for the UI items to display an icon, a name, and some stats.
 * @author DAM
 */
public class IconNameStatsBundle
{
  protected static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  // UI
  protected JButton _icon;
  protected MultilineLabel2 _name;
  protected MultilineLabel2 _stats;

  /**
   * Constructor.
   */
  public IconNameStatsBundle()
  {
    // Icon
    _icon=GuiFactory.buildIconButton();
    // Label
    _name=new MultilineLabel2();
    // Stats
    _stats=new MultilineLabel2(10);
  }

  /**
   * Get the managed icon.
   * @return the managed icon.
   */
  public JButton getIcon()
  {
    return _icon;
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
    _icon=null;
    _name=null;
    _stats=null;
  }
}
