package delta.games.lotro.gui.character.storage.synopsis;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.character.storage.StorageFilterController;
import delta.games.lotro.gui.character.storage.own.StorageButtonsPanelController;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a global "storage synopsis" window (all accounts/servers).
 * @author DAM
 */
public class GlobalStorageDisplayWindowController extends DefaultDialogController implements GenericEventsListener<CharacterEvent>,FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="GLOBAL_STORAGE";

  // Data
  private StorageFilter _filter;
  private List<StoredItem> _items;
  // Controllers
  private GlobalStorageDisplayPanelController _panelController;
  private StorageFilterController _filterController;
  private StorageButtonsPanelController _buttonsController;
  // Listeners
  private GenericEventsListener<AccountEvent> _accountEventsListener;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public GlobalStorageDisplayWindowController(WindowController parent)
  {
    super(parent);
    _filter=new StorageFilter();
    _items=new ArrayList<StoredItem>();
    updateContents();
    EventsManager.addListener(CharacterEvent.class,this);
    _accountEventsListener=new GenericEventsListener<AccountEvent>()
    {
      @Override
      public void eventOccurred(AccountEvent event)
      {
        AccountEventType type=event.getType();
        if (type==AccountEventType.STORAGE_UPDATED)
        {
          updateContents();
        }
      }
    };
    EventsManager.addListener(AccountEvent.class,_accountEventsListener);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Display
    _panelController=new GlobalStorageDisplayPanelController(this,_filter);
    // Table
    JPanel tablePanel=_panelController.getPanel();
    // Buttons
    _buttonsController=new StorageButtonsPanelController(this,_filter,_items);
    // Filter
    _filterController=new StorageFilterController(_filter,this);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder(Labels.getLabel("shared.title.filter"));
    filterPanel.setBorder(filterBorder);
    // Buttons panel
    JPanel buttonsPanel=_buttonsController.getPanel();
    // - filter+buttons panel
    JPanel filterAndButtonsPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      filterAndButtonsPanel.add(filterPanel,c);
      c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      filterAndButtonsPanel.add(buttonsPanel,c);
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterAndButtonsPanel,c);
    c=new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(850,350));
    dialog.setSize(850,700);
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_STORAGE_UPDATED)
    {
      updateContents();
    }
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    _buttonsController.filterUpdated();
  }

  /**
   * Update contents.
   */
  private void updateContents()
  {
    // Title
    String title=Labels.getLabel("storage.synopsis.window.title");
    getDialog().setTitle(title);
    // Update storage
    _items=GlobalStorageUtils.buildAllAccountsItems();
    _panelController.update(_items);
    _buttonsController.update(_items);
    _filterController.update(_items);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Listeners
    EventsManager.removeListener(CharacterEvent.class,this);
    EventsManager.removeListener(AccountEvent.class,_accountEventsListener);
    _accountEventsListener=null;
    // Data
    _filter=null;
    _items=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_buttonsController!=null)
    {
      _buttonsController.dispose();
      _buttonsController=null;
    }
    super.dispose();
  }
}
