package delta.games.lotro.gui.recipes.form;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;

/**
 * Controller for a recipe display panel.
 * @author DAM
 */
public class ItemDisplayGadgets
{
  private JLabel _icon; // Item icon, with optional count
  private JLabel _name; // Item name
  private JLabel _comment; // Optional comment, either before or after icon+name

  /**
   * Constructor.
   */
  public ItemDisplayGadgets()
  {
  }

  /**
   * Set contents.
   * @param icon Icon to show.
   * @param name Name to show.
   * @param comment Comment to show.
   */
  public void set(Icon icon, String name, String comment)
  {
    _icon=GuiFactory.buildIconLabel(icon);
    _name=GuiFactory.buildLabel(name);
    _comment=GuiFactory.buildLabel(comment);
  }

  /**
   * Get the managed icon label.
   * @return an icon label.
   */
  public JLabel getIcon()
  {
    return _icon;
  }

  /**
   * Get the managed name label.
   * @return a label.
   */
  public JLabel getName()
  {
    return _name;
  }

  /**
   * Get the managed comment label.
   * @return a label.
   */
  public JLabel getComment()
  {
    return _comment;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _icon=null;
    _name=null;
    _comment=null;
  }
}
