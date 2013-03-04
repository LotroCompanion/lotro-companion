package delta.games.lotro.gui.toon;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.CharacterMainWindowController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.toolbar.ToolbarController;
import delta.games.lotro.gui.utils.toolbar.ToolbarIconItem;
import delta.games.lotro.gui.utils.toolbar.ToolbarModel;
import delta.games.lotro.utils.gui.WindowController;
import delta.games.lotro.utils.gui.WindowsManager;

/**
 * Controller for the toons management panel.
 * @author DAM
 */
public class ToonsManagementController implements ActionListener
{
  private static final String NEW_TOON_ID="newToon";
  private JPanel _panel;
  private WindowController _parentController;
  private ToonsTableController _toonsTable;
  private ToolbarController _toolbar;
  private NewToonDialogController _newToonDialog;
  private PropertyChangeListener _listener;
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
      initListeners();
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

  private void initListeners()
  {
    CharactersManager manager=CharactersManager.getInstance();
    _listener=new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent evt)
      {
        if (CharactersManager.TOON_ADDED.equals(evt.getPropertyName())) {
          _toonsTable.refresh();
        }
      }
    };
    manager.addPropertyChangeListener(_listener);
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
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_TOON_ID,newIconPath,NEW_TOON_ID,"Create a new toon...","New");
    model.addToolbarIconItem(newIconItem);
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
    String id=CharacterMainWindowController.getIdentifier(serverName,toonName);
    WindowController controller=_mainWindowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterMainWindowController(toon);
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
    if (_listener!=null)
    {
      CharactersManager.getInstance().removePropertyChangeListener(_listener);
      _listener=null;
    }
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
