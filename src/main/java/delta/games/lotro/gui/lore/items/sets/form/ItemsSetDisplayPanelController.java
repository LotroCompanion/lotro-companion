package delta.games.lotro.gui.lore.items.sets.form;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.sets.ItemsSet;

/**
 * Controller for a panel to display an items set.
 * @author DAM
 */
public class ItemsSetDisplayPanelController extends AbstractSetDisplayPanelController
{
  /**
   * Constructor.
   * @param parent Parent window.
   * @param set Items set to show.
   */
  public ItemsSetDisplayPanelController(NavigatorWindowController parent, ItemsSet set)
  {
    super(parent,set);
  }

  @Override
  protected String getTitlePrefix()
  {
    return "Items set";
  }

  @Override
  protected List<Item> getMembers()
  {
    List<Item> members=new ArrayList<Item>(_set.getMembers());
    return members;
  }
}
