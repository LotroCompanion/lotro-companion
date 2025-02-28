package delta.games.lotro.gui.house;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import delta.games.lotro.gui.character.status.housing.HouseDisplayWindowController;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.house.HouseEntry;
import delta.games.lotro.house.HousesManager;
import delta.games.lotro.house.events.HouseEvent;
import delta.games.lotro.house.events.HouseEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for the houses management panel.
 * @author DAM
 */
public class HousesManagementController extends AbstractPanelController implements ActionListener,GenericEventsListener<HouseEvent>
{
  private static final String OPEN_HOUSE_ID="openHouse";
  private static final String REMOVE_HOUSE_ID="removeHouse";
  private HousesTableController _housesTable;
  private ToolbarController _toolbar;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public HousesManagementController(WindowController parentController)
  {
    super(parentController);
    JPanel panel=buildPanel();
    EventsManager.addListener(HouseEvent.class,this);
    setPanel(panel);
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _housesTable=buildHousesTable();
    JTable table=_housesTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  /**
   * Handle house events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(HouseEvent event)
  {
    HouseEventType type=event.getType();
    if ((type==HouseEventType.HOUSE_ADDED) || (type==HouseEventType.HOUSE_REMOVED))
    {
      Runnable r=new Runnable()
      {
        @Override
        public void run()
        {
          _housesTable.refresh();
        }
      };
      SwingUtilities.invokeLater(r);
    }
    else if (type==HouseEventType.HOUSE_UPDATED)
    {
      Runnable r=new Runnable()
      {
        @Override
        public void run()
        {
          GenericTableController<HouseEntry> tableCtrl=_housesTable.getTableController();
          tableCtrl.refresh(event.getHouse());
        }
      };
      SwingUtilities.invokeLater(r);
    }
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private HousesTableController buildHousesTable()
  {
    WindowController parent=getWindowController();
    HousesTableController tableController=new HousesTableController(parent,null,null);
    return tableController;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Open icon
    String openIconPath=getToolbarIconPath("open");
    ToolbarIconItem openIconItem=new ToolbarIconItem(OPEN_HOUSE_ID,openIconPath,OPEN_HOUSE_ID,"Open the selected house...","Open");
    model.addToolbarIconItem(openIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_HOUSE_ID,deleteIconPath,REMOVE_HOUSE_ID,"Remove the selected house...","Remove");
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
    if (OPEN_HOUSE_ID.equals(action))
    {
      openHouse();
    }
    else if (REMOVE_HOUSE_ID.equals(action))
    {
      deleteHouse();
    }
  }

  private void openHouse()
  {
    GenericTableController<HouseEntry> controller=_housesTable.getTableController();
    HouseEntry house=controller.getSelectedItem();
    if (house==null)
    {
      return;
    }
    _housesTable.showHouse(house);
  }

  private void deleteHouse()
  {
    GenericTableController<HouseEntry> controller=_housesTable.getTableController();
    HouseEntry house=controller.getSelectedItem();
    if (house==null)
    {
      return;
    }
    String houseName=house.getDisplayName();
    // Check deletion
    String text=Labels.getLabel("houses.management.deleteQuestion",new Object[]{houseName});
    String title=Labels.getLabel("houses.management.delete.title");
    WindowController parent=getWindowController();
    int result=GuiFactory.showQuestionDialog(parent.getWindow(),text,title,JOptionPane.YES_NO_OPTION);
    if (result==JOptionPane.OK_OPTION)
    {
      String id=HouseDisplayWindowController.getWindowIdentifier(house.getIdentifier());
      WindowsManager windowsMgr=parent.getWindowsManager();
      WindowController windowController=windowsMgr.getWindow(id);
      if (windowController!=null)
      {
        windowController.dispose();
      }
      HousesManager manager=HousesManager.getInstance();
      manager.removeHouse(house.getIdentifier());
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    EventsManager.removeListener(HouseEvent.class,this);
    if (_housesTable!=null)
    {
      _housesTable.dispose();
      _housesTable=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
  }
}
