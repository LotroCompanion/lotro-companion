package delta.games.lotro.gui.lore.items;

import javax.swing.JComponent;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for an item instance display window.
 * @author DAM
 */
public class ItemInstanceDisplayWindowController extends DefaultDialogController
{
  // Data
  private int _id;
  // Controllers
  private ItemInstanceDisplayPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Item instance.
   */
  public ItemInstanceDisplayWindowController(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    super(parent);
    _panelController=new ItemInstanceDisplayPanelController(this,itemInstance);
  }

  @Override
  public String getWindowIdentifier()
  {
    return "ITEM_INSTANCE_DISPLAY#"+_id;
  }

  @Override
  public void configureWindow()
  {
    WindowsManager windowsMgr=getParentController().getWindowsManager();
    _id=windowsMgr.getAll().size();
    windowsMgr.registerWindow(this);
    pack();
    automaticLocationSetup();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
