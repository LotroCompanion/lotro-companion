package delta.games.lotro.gui.lore.items.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.lore.items.chooser.ItemFilterController;
import delta.games.lotro.gui.lore.items.table.ItemsTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for the items explorer window.
 * @author DAM
 */
public class ItemsExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="ITEMS_EXPLORER";

  private ItemFilterController _filterController;
  private ItemsExplorerPanelController _panelController;
  private ItemsTableController _tableController;
  private ItemFilterConfiguration _cfg;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public ItemsExplorerWindowController(WindowController parent)
  {
    super(parent);
    _cfg=new ItemFilterConfiguration();
    _cfg.initFromItems(ItemsManager.getInstance().getAllItems());
    _cfg.forItemExplorerFilter();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Items explorer");
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Filter
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties filterProps=preferences.getPreferences("ItemExplorerFilter");
    _filterController=new ItemFilterController(_cfg,null,filterProps);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Table
    initItemsTable();
    _panelController=new ItemsExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    _filterController.setFilterUpdateListener(_panelController);
    _panelController.filterUpdated();
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  private void initItemsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("ItemsExplorer");
    List<Item> items=ItemsManager.getInstance().getAllItems();
    _tableController=new ItemsTableController(prefs,_filterController.getFilter(),items);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          Item item=(Item)event.getSource();
          showItem(item);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showItem(Item item)
  {
    int itemId=item.getIdentifier();
    PageIdentifier ref=ReferenceConstants.getItemReference(itemId);
    NavigationUtils.navigateTo(ref,this);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
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
  }
}
