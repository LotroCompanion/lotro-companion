package delta.games.lotro.gui.deed.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.deed.edit.events.DeedEvent;
import delta.games.lotro.gui.deed.edit.events.DeedEventType;
import delta.games.lotro.gui.deed.table.DeedsTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for the deeds management panel.
 * @author DAM
 */
public class DeedsManagementController implements ActionListener,GenericEventsListener<DeedEvent>
{
  private static final String NEW_DEED_ID="newDeed";
  private static final String REMOVE_DEED_ID="removeDeed";
  private JPanel _panel;
  private WindowController _parentController;
  private DeedsTableController _deedsTable;
  private ToolbarController _toolbar;
  //private NewToonDialogController _newToonDialog;
  private WindowsManager _mainWindowsManager;

  /**
   * Constructor.
   * @param parentController Parent window.
   */
  public DeedsManagementController(WindowController parentController)
  {
    _parentController=parentController;
    _mainWindowsManager=new WindowsManager();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
      EventsManager.addListener(DeedEvent.class,this);
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _deedsTable=buildDeedsTable();
    JTable table=_deedsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  /**
   * Handle events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(DeedEvent event)
  {
    DeedEventType type=event.getType();
    if ((type==DeedEventType.DEED_ADDED) || (type==DeedEventType.DEED_REMOVED))
    {
      _deedsTable.refresh();
    }
    else if (type==DeedEventType.DEED_UPDATED)
    {
      DeedDescription deed=event.getDeed();
      _deedsTable.refresh(deed);
    }
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private DeedsTableController buildDeedsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("DeedsEditor");
    DeedsTableController tableController=new DeedsTableController(prefs,null);
    tableController.addActionListener(this);
    return tableController;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_DEED_ID,newIconPath,NEW_DEED_ID,"Create a new deed...","New");
    model.addToolbarIconItem(newIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_DEED_ID,deleteIconPath,REMOVE_DEED_ID,"Remove the selected deed...","Remove");
    model.addToolbarIconItem(deleteIconItem);
    controller.addActionListener(this);
    return controller;
  }

  /**
   * Action implementation.
   * @param event Source event.
   */
  @Override
  public void actionPerformed(ActionEvent event)
  {
    String action=event.getActionCommand();
    if (NEW_DEED_ID.equals(action))
    {
      startNewDeed();
    }
    else if (REMOVE_DEED_ID.equals(action))
    {
      deleteDeed();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(action))
    {
      DeedDescription deed=(DeedDescription)event.getSource();
      editDeed(deed);
    }
  }

  private void editDeed(DeedDescription deed)
  {
    DeedEditionWindowController edit=new DeedEditionWindowController(_parentController,deed);
    edit.show(true);
  }

  private void startNewDeed()
  {
    /*
    if (_newToonDialog==null)
    {
      _newToonDialog=new NewToonDialogController(_parentController);
    }
    _newToonDialog.getDialog().setLocationRelativeTo(getPanel());
    _newToonDialog.show(true);
    */
  }

  private void deleteDeed()
  {
    GenericTableController<DeedDescription> controller=_deedsTable.getTableController();
    DeedDescription file=controller.getSelectedItem();
    if (file!=null)
    {
      /*
      String serverName=file.getServerName();
      String toonName=file.getName();
      // Check deletion
      int result=GuiFactory.showQuestionDialog(_parentController.getWindow(),"Do you really want to delete " + toonName+"@"+ serverName + "?","Delete?",JOptionPane.YES_NO_OPTION);
      if (result==JOptionPane.OK_OPTION)
      {
        String id=CharacterFileWindowController.getIdentifier(serverName,toonName);
        WindowController windowController=_mainWindowsManager.getWindow(id);
        if (windowController!=null)
        {
          windowController.dispose();
        }
        CharactersManager manager=CharactersManager.getInstance();
        manager.removeToon(file);
      }
      */
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    //_parentController=null;
    if (_mainWindowsManager!=null)
    {
      _mainWindowsManager.disposeAll();
      _mainWindowsManager=null;
    }
    EventsManager.removeListener(DeedEvent.class,this);
    if (_deedsTable!=null)
    {
      _deedsTable.dispose();
      _deedsTable=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    /*
    if (_newToonDialog!=null)
    {
      _newToonDialog.dispose();
      _newToonDialog=null;
    }
    */
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
