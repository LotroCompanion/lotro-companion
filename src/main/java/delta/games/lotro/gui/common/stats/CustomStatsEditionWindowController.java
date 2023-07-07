package delta.games.lotro.gui.common.stats;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.stats.StatsManager;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for an custom stats edition window.
 * @author DAM
 */
public class CustomStatsEditionWindowController extends DefaultFormDialogController<ItemInstance<? extends Item>>
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // Controllers
  private CustomStatsEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Data to edit.
   */
  public CustomStatsEditionWindowController(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    super(parent,itemInstance);
    _itemInstance=itemInstance;
    StatsManager statsManager=_itemInstance.getStatsManager();
    StatsProvider provider=itemInstance.getReference().getStatsProvider();
    _panelController=new CustomStatsEditionPanelController(this,statsManager,provider);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(dialog.getPreferredSize());
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
    String title="Edit stats for "+name; // I18n
    getDialog().setTitle(title);
  }

  @Override
  protected void okImpl()
  {
    _panelController.getData(_data.getStatsManager());
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
