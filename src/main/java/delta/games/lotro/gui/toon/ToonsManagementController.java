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
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.gui.log.CharacterLogWindowController;
import delta.games.lotro.gui.utils.toolbar.ToolbarController;
import delta.games.lotro.gui.utils.toolbar.ToolbarIconItem;
import delta.games.lotro.gui.utils.toolbar.ToolbarModel;

/**
 * @author DAM
 */
public class ToonsManagementController implements ActionListener
{
  private static final String NEW_TOON_ID="newToon";
  private JPanel _panel;
  private ToonsTableController _toonsTable;
  private ToolbarController _toolbar;
  private NewToonDialogController _newToonDialog;
  private PropertyChangeListener _listener;

  /**
   * Constructor.
   */
  public ToonsManagementController()
  {
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
    JPanel ret=new JPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=new JScrollPane(table);
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

  private static String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon-16.png";
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
    CharacterLog log=toon.getLastCharacterLog();
    if (log!=null)
    {
      CharacterLogWindowController controller=new CharacterLogWindowController(toon);
      controller.show();
    }
  }

  private void startNewToon()
  {
    if (_newToonDialog==null)
    {
      _newToonDialog=new NewToonDialogController();
    }
    _newToonDialog.show();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
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
