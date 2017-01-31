package delta.games.lotro.gui.toon;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.gui.character.CharacterFileWindowController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.toolbar.ToolbarController;
import delta.games.lotro.gui.utils.toolbar.ToolbarIconItem;
import delta.games.lotro.gui.utils.toolbar.ToolbarModel;
import delta.games.lotro.utils.gui.WindowController;
import delta.games.lotro.utils.gui.WindowsManager;
import delta.games.lotro.utils.gui.tables.GenericTableController;

/**
 * Controller for the toons management panel.
 * @author DAM
 */
public class ToonsManagementController implements ActionListener,CharacterEventListener
{
  private static final String NEW_TOON_ID="newToon";
  private static final String REMOVE_TOON_ID="removeToon";
  private JPanel _panel;
  private WindowController _parentController;
  private ToonsTableController _toonsTable;
  private ToolbarController _toolbar;
  private NewToonDialogController _newToonDialog;
  private WindowsManager _mainWindowsManager;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public ToonsManagementController(WindowController parentController)
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
      CharacterEventsManager.addListener(this);
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  /**
   * Handle character events.
   */
  public void eventOccured(CharacterEventType type, CharacterEvent event)
  {
    if ((type==CharacterEventType.CHARACTER_ADDED) || (type==CharacterEventType.CHARACTER_REMOVED))
    {
      _toonsTable.refresh();
    }
    else if (type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      _toonsTable.refresh(toon);
    }
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private ToonsTableController buildToonsTable()
  {
    ToonsTableController tableController=new ToonsTableController();
    tableController.addActionListener(this);
    return tableController;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_TOON_ID,newIconPath,NEW_TOON_ID,"Create a new character...","New");
    model.addToolbarIconItem(newIconItem);
    // Remove icon
    String removeIconPath=getToolbarIconPath("erase");
    ToolbarIconItem eraseIconItem=new ToolbarIconItem(REMOVE_TOON_ID,removeIconPath,REMOVE_TOON_ID,"Remove the selected character...","Remove");
    model.addToolbarIconItem(eraseIconItem);
    controller.addActionListener(this);
    return controller;
  }

  /**
   * Action implementation.
   */
  public void actionPerformed(ActionEvent e)
  {
    String action=e.getActionCommand();
    if (NEW_TOON_ID.equals(action))
    {
      startNewToon();
    }
    else if (REMOVE_TOON_ID.equals(action))
    {
      deleteToon();
    }
    else if (ToonsTableController.DOUBLE_CLICK.equals(action))
    {
      CharacterFile toon=(CharacterFile)e.getSource();
      showToon(toon);
    }
  }

  private void showToon(CharacterFile toon)
  {
    String serverName=toon.getServerName();
    String toonName=toon.getName();
    String id=CharacterFileWindowController.getIdentifier(serverName,toonName);
    WindowController controller=_mainWindowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterFileWindowController(toon);
      _mainWindowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getPanel());
    }
    controller.bringToFront();
  }

  private void startNewToon()
  {
    if (_newToonDialog==null)
    {
      _newToonDialog=new NewToonDialogController(_parentController);
    }
    _newToonDialog.getDialog().setLocationRelativeTo(getPanel());
    _newToonDialog.show(true);
  }

  private void deleteToon()
  {
    GenericTableController<CharacterFile> controller=_toonsTable.getTableController();
    CharacterFile file=controller.getSelectedItem();
    if (file!=null)
    {
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
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_mainWindowsManager!=null)
    {
      _mainWindowsManager.disposeAll();
      _mainWindowsManager=null;
    }
    CharacterEventsManager.removeListener(this);
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
    if (_toolbar==null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    if (_newToonDialog!=null)
    {
      _newToonDialog.dispose();
      _newToonDialog=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
