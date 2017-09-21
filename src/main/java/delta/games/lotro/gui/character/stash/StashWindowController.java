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
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.character.storage.ItemsStash;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.items.ItemEditionWindowController;
import delta.games.lotro.gui.items.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;

/**
 * Controller for a "items stash" window.
 * @author DAM
 */
public class StashWindowController extends DefaultWindowController implements ActionListener,FilterUpdateListener,CharacterEventListener
{
  private static final String NEW_ITEM_ID="newItem";
  private static final String CLONE_ITEM_ID="cloneItem";
  private static final String REMOVE_ITEM_ID="removeItem";

  private StashItemsTableController _itemsTable;
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
    ItemFilterController filterController=new ItemFilterController();
    Filter<Item> filter=filterController.getFilter();
    filterController.setFilterUpdateListener(this);
    JPanel filterPanel=filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // Items table
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Items"));
    _itemsTable.getTableController().setFilter(filter);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Stash of "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setMinimumSize(new Dimension(500,380));
    frame.setSize(new Dimension(850,500));
    frame.setResizable(true);

    // Register stash updates events
    CharacterEventsManager.addListener(this);
    return frame;
  }

  /**
   * Handle character events.
   */
  public void eventOccured(CharacterEventType type, CharacterEvent event)
  {
    if (type==CharacterEventType.CHARACTER_STASH_UPDATED)
    {
      _itemsTable.getTableController().refresh();
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
  public void filterUpdated()
  {
    _itemsTable.getTableController().filterUpdated();
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (NEW_ITEM_ID.equals(command))
    {
      //startNewCharacterData();
    }
    else if (CLONE_ITEM_ID.equals(command))
    {
      cloneItem();
    }
    else if (REMOVE_ITEM_ID.equals(command))
    {
      removeItem();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(command))
    {
      Item data=(Item)e.getSource();
      editItem(data);
    }
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _itemsTable=buildTable();
    JTable table=_itemsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_ITEM_ID,newIconPath,NEW_ITEM_ID,"Create a new character configuration...","New");
    model.addToolbarIconItem(newIconItem);
    // Clone icon
    String cloneIconPath=getToolbarIconPath("copy");
    ToolbarIconItem cloneIconItem=new ToolbarIconItem(CLONE_ITEM_ID,cloneIconPath,CLONE_ITEM_ID,"Clone the selected character configuration...","Clone");
    model.addToolbarIconItem(cloneIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_ITEM_ID,deleteIconPath,REMOVE_ITEM_ID,"Remove the selected character...","Remove");
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

  private StashItemsTableController buildTable()
  {
    StashItemsTableController tableController=new StashItemsTableController(_toon);
    tableController.addActionListener(this);
    return tableController;
  }

  private void editItem(Item item)
  {
    ItemEditionWindowController ctrl=new ItemEditionWindowController(this,item);
    ctrl.show(true);
    _toon.saveStash();
  }

  private void cloneItem()
  {
    GenericTableController<Item> controller=_itemsTable.getTableController();
    Item item=controller.getSelectedItem();
    if (item!=null)
    {
      Item clone=ItemFactory.clone(item);
      clone.setStashIdentifier(null);
      ItemsStash stash=_toon.getStash();
      stash.addItem(clone);
      _toon.saveStash();
      _itemsTable.getTableController().refresh();
    }
  }

  private void removeItem()
  {
    GenericTableController<Item> controller=_itemsTable.getTableController();
    Item item=controller.getSelectedItem();
    if (item!=null)
    {
      ItemsStash stash=_toon.getStash();
      stash.removeItem(item.getStashIdentifier());
      _toon.saveStash();
      _itemsTable.getTableController().refresh();
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    CharacterEventsManager.removeListener(this);
    super.dispose();
    if (_itemsTable!=null)
    {
      _itemsTable.dispose();
      _itemsTable=null;
    }
    if (_toolbar==null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    _toon=null;
  }
}
