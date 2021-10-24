package delta.games.lotro.gui.lore.items.legendary2;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;

/**
 * Controller for an item instance edition window.
 * @author DAM
 */
public class LegendaryInstance2EditionWindowController extends DefaultFormDialogController<ItemInstance<? extends Item>>
{
  // Controllers
  private LegendaryInstance2EditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Item instance.
   */
  public LegendaryInstance2EditionWindowController(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    super(parent,itemInstance);
    _panelController=new LegendaryInstance2EditionPanelController(this,itemInstance);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  @Override
  public void configureWindow()
  {
    Item reference=_data.getReference();
    String name=reference.getName();
    String title="Edit legendary data for "+name;
    getDialog().setTitle(title);
  }

  @Override
  protected boolean checkInput()
  {
    return _panelController.checkInput();
  }

  @Override
  protected void okImpl()
  {
    LegendaryInstance2 legendaryInstance=(LegendaryInstance2)_data;
    LegendaryInstanceAttrs2 attrs=legendaryInstance.getLegendaryAttributes();
    _panelController.getData(_data,attrs);
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
