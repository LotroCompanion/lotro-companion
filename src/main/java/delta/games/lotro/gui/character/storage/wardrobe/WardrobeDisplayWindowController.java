package delta.games.lotro.gui.character.storage.wardrobe;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventProperties;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.character.storage.wardrobe.Wardrobe;
import delta.games.lotro.character.storage.wardrobe.WardrobeItem;
import delta.games.lotro.character.storage.wardrobe.io.xml.WardrobeIO;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "wardrobe" window.
 * @author DAM
 */
public class WardrobeDisplayWindowController extends DefaultDialogController implements GenericEventsListener<AccountEvent>,FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="WARDROBE";

  // Data
  private AccountOnServer _accountOnServer;
  private WardrobeFilter _filter;
  private List<WardrobeItem> _items;
  // Controllers
  private WardrobeDisplayPanelController _panelController;
  private WardrobeFilterController _filterController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param accountOnServer Account/server to use.
   */
  public WardrobeDisplayWindowController(WindowController parent, AccountOnServer accountOnServer)
  {
    super(parent);
    _accountOnServer=accountOnServer;
    _filter=new WardrobeFilter();
    _items=new ArrayList<WardrobeItem>();
    updateContents();
    EventsManager.addListener(AccountEvent.class,this);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Display
    _panelController=new WardrobeDisplayPanelController(this,_filter);
    // Table
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new WardrobeFilterController(_filter,this);
    JPanel filterPanel=_filterController.getPanel();
    filterPanel.setBorder(GuiFactory.buildTitledBorder("Filter")); // I18n
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(800,350));
    dialog.setSize(800,700);
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
  public void eventOccurred(AccountEvent event)
  {
    AccountEventType type=event.getType();
    if (type==AccountEventType.WARDROBE_UPDATED)
    {
      Account account=event.getAccount();
      if (account==_accountOnServer.getAccount())
      {
        String serverName=event.getProperties().getStringProperty(AccountEventProperties.SERVER_NAME,"");
        if (serverName.equals(_accountOnServer.getServerName()))
        {
          updateContents();
        }
      }
    }
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
  }

  /**
   * Update contents.
   */
  private void updateContents()
  {
    // Title
    String name=_accountOnServer.getAccount().getAccountName();
    String serverName=_accountOnServer.getServerName();
    String title="Wardrobe for "+name+" @ "+serverName; // I18n
    getDialog().setTitle(title);
    // Update storage
    Wardrobe wardrobe=WardrobeIO.loadWardrobe(_accountOnServer);
    if (wardrobe==null)
    {
      wardrobe=new Wardrobe();
    }
    _items=new ArrayList<WardrobeItem>();
    _items.addAll(wardrobe.getAll());
    _panelController.update(_items);
    _filterController.update();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    EventsManager.removeListener(AccountEvent.class,this);
    // Data
    _accountOnServer=null;
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
    super.dispose();
  }
}
