package delta.games.lotro.gui.items;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for an item choice window.
 * @author DAM
 */
public class ItemChoiceWindowController extends DefaultFormDialogController<Item>
{
  /**
   * Preference file for the columns of the item chooser.
   */
  public static final String ITEM_CHOOSER_PROPERTIES_ID="ItemChooserColumn";
  /**
   * Preference file for the columns of the essence chooser.
   */
  public static final String ESSENCE_CHOOSER_PROPERTIES_ID="EssenceChooserColumn";
  /**
   * Name of the property for column IDs.
   */
  public static final String COLUMNS_PROPERTY="columns";

  private AbstractItemFilterPanelController _filterController;
  private ItemChoicePanelController _panelController;
  private ItemChoiceTableController _tableController;
  private List<Item> _items;
  private Filter<Item> _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param prefs User preferences.
   * @param items Items to choose from.
   * @param filter Filter to use.
   * @param ui Filter UI controller.
   */
  public ItemChoiceWindowController(WindowController parent, TypedProperties prefs, List<Item> items, Filter<Item> filter, AbstractItemFilterPanelController ui)
  {
    super(parent,null);
    _items=items;
    _filter=filter;
    _filterController=ui;
    _tableController=new ItemChoiceTableController(prefs,_items,_filter);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose item:");
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    _panelController=new ItemChoicePanelController(this,_tableController);
    _filterController.setFilterUpdateListener(_panelController);
    JPanel tablePanel=_panelController.getPanel();
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ok();
      }
    };
    GenericTableController<Item> controller=_tableController.getTableController();
    controller.addActionListener(al);
    // Filter
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  @Override
  protected void okImpl()
  {
    _data=_tableController.getSelectedItem();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _filter=null;
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    _items=null;
  }
}
