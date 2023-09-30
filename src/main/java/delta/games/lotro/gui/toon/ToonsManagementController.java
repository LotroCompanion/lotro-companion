package delta.games.lotro.gui.toon;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.TableColumnsChooserController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterDataSummary;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.io.xml.CharacterXMLParser;
import delta.games.lotro.gui.character.CharacterFileWindowController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;
import delta.games.lotro.utils.gui.filechooser.FileChooserController;

/**
 * Controller for the toons management panel.
 * @author DAM
 */
public class ToonsManagementController implements ActionListener,GenericEventsListener<CharacterEvent>
{
  private static final String NEW_TOON_ID="newToon";
  private static final String IMPORT_TOON_ID="importToon";
  private static final String REMOVE_TOON_ID="removeToon";
  private JPanel _panel;
  private WindowController _parentController;
  private ToonsTableController _toonsTable;
  private ToolbarController _toolbar;
  private NewToonDialogController _newToonDialog;
  private WindowsManager _mainWindowsManager;
  private MainCharactersSelectionManager _selectionManager;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public ToonsManagementController(WindowController parentController)
  {
    _parentController=parentController;
    _mainWindowsManager=new WindowsManager();
    _selectionManager=new MainCharactersSelectionManager();
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
      EventsManager.addListener(CharacterEvent.class,this);
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel topPanel=buildTopPanel();
    ret.add(topPanel,BorderLayout.NORTH);
    _toonsTable=buildToonsTable();
    refreshTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private JPanel buildTopPanel()
  {
    // Toolbar
    _toolbar=buildToolBar();
    // Columns chooser
    JButton choose=GuiFactory.buildButton(Labels.getLabel("shared.chooseColumns.button"));
    {
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          TableColumnsChooserController<CharacterFile> chooser=new TableColumnsChooserController<CharacterFile>(_parentController,_toonsTable.getTableController());
          chooser.editModal();
        }
      };
      choose.addActionListener(al);
    }
    // Characters chooser
    JButton chooseCharacters=GuiFactory.buildButton("Choose characters..."); // I18n
    {
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean ok=_selectionManager.chooseCharacters(_parentController);
          if (ok)
          {
            _toonsTable.updateFilter();
          }
        }
      };
      chooseCharacters.addActionListener(al);
    }
    // Assembly
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    ret.add(_toolbar.getToolBar(),c);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    ret.add(choose,c);
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    ret.add(chooseCharacters,c);
    c=new GridBagConstraints(3,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
    ret.add(Box.createHorizontalGlue(),c);
    return ret;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if ((type==CharacterEventType.CHARACTER_ADDED) || (type==CharacterEventType.CHARACTER_REMOVED))
    {
      // Refresh toons table.
      refreshTable();
    }
  }

  private void refreshTable()
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    _toonsTable.setToons(toons);
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private ToonsTableController buildToonsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("MainCharTable");
    ToonsTableController tableController=new ToonsTableController(prefs,_selectionManager);
    tableController.addActionListener(this);
    return tableController;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_TOON_ID,newIconPath,NEW_TOON_ID,"Create a new character...","New"); // I18n
    model.addToolbarIconItem(newIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_TOON_ID,deleteIconPath,REMOVE_TOON_ID,"Remove the selected character...","Remove"); // I18n
    model.addToolbarIconItem(deleteIconItem);
    controller.addActionListener(this);
    // Import icon
    String importIconPath=getToolbarIconPath("import");
    ToolbarIconItem importIconItem=new ToolbarIconItem(IMPORT_TOON_ID,importIconPath,IMPORT_TOON_ID,"Import a character...","Import"); // I18n
    model.addToolbarIconItem(importIconItem);
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
    if (NEW_TOON_ID.equals(action))
    {
      startNewToon();
    }
    else if (REMOVE_TOON_ID.equals(action))
    {
      deleteToon();
    }
    else if (IMPORT_TOON_ID.equals(action))
    {
      importToon();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(action))
    {
      CharacterFile toon=(CharacterFile)event.getSource();
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
      int result=GuiFactory.showQuestionDialog(_parentController.getWindow(),"Do you really want to delete " + toonName+"@"+ serverName + "?","Delete?",JOptionPane.YES_NO_OPTION); // I18n
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

  private void importToon()
  {
    FileChooserController ctrl=new FileChooserController("import", "Import character..."); // I18n
    Window window=_parentController.getWindow();
    File fromFile=ctrl.chooseFile(window,"Import");
    if (fromFile!=null)
    {
      CharacterXMLParser parser=new CharacterXMLParser();
      CharacterData data=new CharacterData();
      boolean ok=parser.parseXML(fromFile,data);
      if (ok)
      {
        ok=importData(data);
        if (ok)
        {
          GuiFactory.showInformationDialog(window,"Import OK!","OK!"); // I18n
        }
        else
        {
          GuiFactory.showErrorDialog(window,"Import failed!","Error!"); // I18n
        }
      }
      else
      {
        GuiFactory.showErrorDialog(window,"Import failed (bad XML file)!","Error!"); // I18n
      }
    }
  }

  private boolean importData(CharacterData data)
  {
    boolean ok;
    CharactersManager manager=CharactersManager.getInstance();
    CharacterDataSummary dataSummary=data.getSummary();
    CharacterSummary summary=dataSummary.getSummary();
    CharacterFile toon=manager.getToonById(summary.getServer(),summary.getName());
    if (toon==null)
    {
      CharacterSummary newSummary=new CharacterSummary(summary);
      toon=manager.addToon(newSummary);
    }
    if (toon==null)
    {
      return false;
    }
    CharacterInfosManager infos=toon.getInfosManager();
    dataSummary.setSummary(toon.getSummary());
    ok=infos.writeNewCharacterData(data);
    return ok;
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
    EventsManager.removeListener(CharacterEvent.class,this);
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
    if (_toolbar!=null)
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
