package delta.games.lotro.gui.maps.resources.filter;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.maps.resources.ResourceNodesLootManager;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a window to control the resource nodes filter.
 * @author DAM
 */
public class ResourceNodeFilterWindowController extends DefaultDialogController
{
  private ResourceNodeFilterPanelController _filterPanel;

  /**
   * Constructor.
   * @param parent
   */
  public ResourceNodeFilterWindowController(WindowController parent)
  {
    super(parent);
    _filterPanel=new ResourceNodeFilterPanelController(parent);
  }

  /**
   * Get the resource nodes filter.
   * @return a filter.
   */
  public ResourceNodesFilter getFilter()
  {
    return _filterPanel.getFilter();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_filterPanel.getPanel();
    return panel;
  }

  /**
   * Override this method to configure the window
   * just after it has been built!
   */
  @Override
  public void configureWindow()
  {
    JDialog dialog=getDialog();
    dialog.setTitle("Resource nodes filter"); // I18n
    dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    dialog.setResizable(false);
  }

  /**
   * Perform window closing.
   */
  @Override
  protected void doWindowClosing()
  {
    // Nothing!
  }

  /**
   * Set the data to display.
   * @param lootMgr Loot manager.
   * @param items Data to show.
   */
  public void setItems(ResourceNodesLootManager lootMgr, List<Item> items)
  {
    _filterPanel.setItems(lootMgr,items);
    pack();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_filterPanel!=null)
    {
      _filterPanel.dispose();
      _filterPanel=null;
    }
  }
}
