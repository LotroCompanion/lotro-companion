package delta.games.lotro.gui.character.stash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.storage.stash.ItemsStash;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.items.ItemInstanceEditionWindowController;
import delta.games.lotro.gui.lore.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.lore.items.chooser.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.filters.ItemInstanceFilter;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "items stash" window.
 * @author DAM
 */
public class StashWindowController extends DefaultWindowController implements ActionListener,FilterUpdateListener,GenericEventsListener<CharacterEvent>
{
  private static final String CLONE_ITEM_ID="cloneItem";
  private static final String REMOVE_ITEM_ID="removeItem";

  private GenericTableController<ItemInstance<? extends Item>> _itemsTable;
  private ToolbarController _toolbar;
  private CharacterFile _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public StashWindowController(CharacterFile toon)
  {
    _toon=toon;
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="STASH#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Build table
    JPanel tablePanel=buildTablePanel();

    // Filter
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.forStashFilter();
    ItemFilterController filterController=new ItemFilterController(cfg,null,null);
    final Filter<Item> filter=filterController.getFilter();
    filterController.setFilterUpdateListener(this);
    JPanel filterPanel=filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
    filterPanel.setBorder(filterBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // Items table
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Items")); // I18n
    ItemInstanceFilter instanceFilter=new ItemInstanceFilter(filter);
    _itemsTable.setFilter(instanceFilter);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Stash of "+name+" @ "+serverName; // I18n
    frame.setTitle(title);
    frame.pack();
    frame.setMinimumSize(new Dimension(500,380));
    frame.setSize(new Dimension(850,500));
    frame.setResizable(true);

    // Register stash updates events
    EventsManager.addListener(CharacterEvent.class,this);
    return frame;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_STASH_UPDATED)
    {
      _itemsTable.refresh();
    }
  }

  @Override
  public String getWindowIdentifier()
  {
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=getIdentifier(serverName,toonName);
    return id;
  }

  /**
   * Filter updated callback.
   */
  @Override
  public void filterUpdated()
  {
    _itemsTable.filterUpdated();
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (CLONE_ITEM_ID.equals(command))
    {
      cloneItemInstance();
    }
    else if (REMOVE_ITEM_ID.equals(command))
    {
      removeItemInstance();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(command))
    {
      @SuppressWarnings("unchecked")
      ItemInstance<? extends Item> data=(ItemInstance<? extends Item>)e.getSource();
      editItemInstance(data);
    }
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _itemsTable=StashItemsTableBuilder.buildTable(_toon);
    _itemsTable.addActionListener(this);
    JTable table=_itemsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Clone icon
    String cloneIconPath=getToolbarIconPath("copy");
    ToolbarIconItem cloneIconItem=new ToolbarIconItem(CLONE_ITEM_ID,cloneIconPath,CLONE_ITEM_ID,"Clone the selected item...","Clone");
    model.addToolbarIconItem(cloneIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_ITEM_ID,deleteIconPath,REMOVE_ITEM_ID,"Remove the selected item...","Remove");
    model.addToolbarIconItem(deleteIconItem);
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private void editItemInstance(ItemInstance<? extends Item> item)
  {
    ItemInstance<? extends Item> editedItem=ItemFactory.cloneInstance(item);
    ItemInstanceEditionWindowController ctrl=new ItemInstanceEditionWindowController(this,_toon.getSummary(),editedItem);

    ItemInstance<? extends Item> resultItem=ctrl.editModal();
    if (resultItem!=null)
    {
      ItemsStash stash=_toon.getStash();
      stash.removeItem(item.getStashIdentifier());
      editedItem.setWearer(null);
      stash.addItem(editedItem);
      _toon.saveStash();
    }
  }

  private void cloneItemInstance()
  {
    ItemInstance<? extends Item> item=_itemsTable.getSelectedItem();
    if (item!=null)
    {
      ItemInstance<? extends Item> clone=ItemFactory.cloneInstance(item);
      clone.setWearer(null);
      clone.setStashIdentifier(null);
      ItemsStash stash=_toon.getStash();
      stash.addItem(clone);
      _toon.saveStash();
      _itemsTable.refresh();
    }
  }

  private void removeItemInstance()
  {
    ItemInstance<? extends Item> item=_itemsTable.getSelectedItem();
    if (item!=null)
    {
      ItemsStash stash=_toon.getStash();
      stash.removeItem(item.getStashIdentifier());
      _toon.saveStash();
      _itemsTable.refresh();
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    EventsManager.removeListener(CharacterEvent.class,this);
    super.dispose();
    if (_itemsTable!=null)
    {
      _itemsTable.dispose();
      _itemsTable=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    _toon=null;
  }
}
