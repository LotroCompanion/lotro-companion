package delta.games.lotro.gui.character.main;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.io.xml.CharacterDataIO;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.gui.character.config.CharacterDataWindowController;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.gui.filechooser.FileChooserController;

/**
 * Controller for a "character configurations" panel.
 * @author DAM
 */
public class CharacterConfigurationsPanelController extends AbstractPanelController implements ActionListener
{
  private static final String NEW_TOON_DATA_ID="newToonData";
  private static final String CLONE_TOON_DATA_ID="cloneToonData";
  private static final String EXPORT_TOON_DATA_ID="exportToonData";
  private static final String REMOVE_TOON_DATA_ID="removeToonData";

  // Data
  private CharacterFile _toon;
  // Controllers
  private WindowController _parent;
  private CharacterDataTableController _toonsTable;
  private ToolbarController _toolbar;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Managed toon.
   */
  public CharacterConfigurationsPanelController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    _parent=parent;
    _toon=toon;
    setPanel(buildPanel());
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (NEW_TOON_DATA_ID.equals(command))
    {
      startNewCharacterData();
    }
    else if (CLONE_TOON_DATA_ID.equals(command))
    {
      cloneCharacterData();
    }
    else if (EXPORT_TOON_DATA_ID.equals(command))
    {
      exportCharacterData();
    }
    else if (REMOVE_TOON_DATA_ID.equals(command))
    {
      removeCharacterData();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(command))
    {
      CharacterData data=(CharacterData)e.getSource();
      showCharacterData(data);
    }
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    ret.setBorder(GuiFactory.buildTitledBorder("Configurations")); // I18n
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_TOON_DATA_ID,newIconPath,NEW_TOON_DATA_ID,"Create a new character configuration...","New");
    model.addToolbarIconItem(newIconItem);
    // Clone icon
    String cloneIconPath=getToolbarIconPath("copy");
    ToolbarIconItem cloneIconItem=new ToolbarIconItem(CLONE_TOON_DATA_ID,cloneIconPath,CLONE_TOON_DATA_ID,"Clone the selected character configuration...","Clone");
    model.addToolbarIconItem(cloneIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_TOON_DATA_ID,deleteIconPath,REMOVE_TOON_DATA_ID,"Remove the selected character...","Remove");
    model.addToolbarIconItem(deleteIconItem);
    // Export icon
    String exportIconPath=getToolbarIconPath("export");
    ToolbarIconItem exportIconItem=new ToolbarIconItem(EXPORT_TOON_DATA_ID,exportIconPath,EXPORT_TOON_DATA_ID,"Export the selected character configuration...","Export");
    model.addToolbarIconItem(exportIconItem);
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private CharacterDataTableController buildToonsTable()
  {
    CharacterDataTableController tableController=new CharacterDataTableController(_toon);
    tableController.addActionListener(this);
    return tableController;
  }

  private void startNewCharacterData()
  {
    CharacterData newInfos=CharacterFactory.buildNewData(_toon.getSummary());
    // Compute stats
    CharacterStatsComputer computer=new CharacterStatsComputer();
    newInfos.getStats().setStats(computer.getStats(newInfos));
    boolean ok=_toon.getInfosManager().writeNewCharacterData(newInfos);
    if (ok)
    {
      showCharacterData(newInfos);
    }
  }

  private void showCharacterData(CharacterData data)
  {
    String id=CharacterDataWindowController.getIdentifier(data);
    WindowsManager windowsManager=_parent.getWindowsManager();
    WindowController controller=windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterDataWindowController(_parent,_toon,data);
      windowsManager.registerWindow(controller);
      Window thisWindow=SwingUtilities.getWindowAncestor(_toonsTable.getTable());
      controller.getWindow().setLocationRelativeTo(thisWindow);
    }
    controller.bringToFront();
  }

  private void cloneCharacterData()
  {
    GenericTableController<CharacterData> controller=_toonsTable.getTableController();
    CharacterData data=controller.getSelectedItem();
    if (data!=null)
    {
      // Build new configuration
      CharacterData newInfos=new CharacterData(data);
      newInfos.setDate(Long.valueOf(System.currentTimeMillis()));
      // Register new configuration
      CharacterInfosManager infos=_toon.getInfosManager();
      infos.writeNewCharacterData(newInfos);
    }
  }

  private void exportCharacterData()
  {
    GenericTableController<CharacterData> controller=_toonsTable.getTableController();
    CharacterData data=controller.getSelectedItem();
    if (data!=null)
    {
      FileChooserController ctrl=new FileChooserController("export", "Export character..."); // I18n
      File toFile=ctrl.chooseFile(_parent.getWindow(),"Export"); // I18n
      if (toFile!=null)
      {
        boolean doIt=true;
        if (!toFile.getName().toLowerCase().endsWith(".xml"))
        {
          toFile=new File(toFile.getParentFile(),toFile.getName()+".xml");
        }
        if (toFile.exists())
        {
          doIt=false;
          int result=GuiFactory.showQuestionDialog(_parent.getWindow(),"Do you really want to overwrite the selected file?","Overwrite?",JOptionPane.YES_NO_OPTION); // I18n
          if (result==JOptionPane.OK_OPTION)
          {
            doIt=true;
          }
        }
        if (doIt)
        {
          boolean ok=CharacterDataIO.saveInfo(toFile,data);
          Window window=_parent.getWindow();
          if (ok)
          {
            GuiFactory.showInformationDialog(window,"Export OK!","OK!"); // I18n
          }
          else
          {
            GuiFactory.showErrorDialog(window,"Export failed!","Error!"); // I18n
          }
        }
      }
    }
  }

  private void removeCharacterData()
  {
    GenericTableController<CharacterData> controller=_toonsTable.getTableController();
    CharacterData data=controller.getSelectedItem();
    if (data!=null)
    {
      // Check deletion
      String serverName=data.getServer();
      String toonName=data.getName();
      int result=GuiFactory.showQuestionDialog(_parent.getWindow(),"Do you really want to delete this configuration of " + toonName+"@"+ serverName + "?","Delete?",JOptionPane.YES_NO_OPTION); // I18n
      if (result==JOptionPane.OK_OPTION)
      {
        String id=CharacterDataWindowController.getIdentifier(data);
        WindowsManager windowsManager=_parent.getWindowsManager();
        WindowController windowController=windowsManager.getWindow(id);
        if (windowController!=null)
        {
          windowController.dispose();
        }
        boolean ok=_toon.getInfosManager().remove(data);
        if (ok)
        {
          CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_REMOVED,_toon,data);
          EventsManager.invokeEvent(event);
        }
      }
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
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
  }
}
