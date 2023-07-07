package delta.games.lotro.gui.kinship;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
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
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipsManager;
import delta.games.lotro.kinship.events.KinshipEvent;
import delta.games.lotro.kinship.events.KinshipEventType;
import delta.games.lotro.kinship.io.xml.KinshipsIO;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for the kinships management panel.
 * @author DAM
 */
public class KinshipsManagementController implements ActionListener,GenericEventsListener<KinshipEvent>
{
  private static final String REMOVE_KINSHIP_ID="removeKinship";
  private JPanel _panel;
  private WindowController _parentController;
  private KinshipsTableController _kinshipTables;
  private ToolbarController _toolbar;
  private WindowsManager _mainWindowsManager;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public KinshipsManagementController(WindowController parentController)
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
      EventsManager.addListener(KinshipEvent.class,this);
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _kinshipTables=buildKinshipsTable();
    JTable table=_kinshipTables.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  /**
   * Handle kinship events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(KinshipEvent event)
  {
    KinshipEventType type=event.getType();
    if ((type==KinshipEventType.KINSHIP_ADDED) || (type==KinshipEventType.KINSHIP_REMOVED))
    {
      _kinshipTables.refresh();
    }
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private KinshipsTableController buildKinshipsTable()
  {
    KinshipsTableController tableController=new KinshipsTableController();
    tableController.addActionListener(this);
    return tableController;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_KINSHIP_ID,deleteIconPath,REMOVE_KINSHIP_ID,"Remove the selected kinship...","Remove");
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
    if (REMOVE_KINSHIP_ID.equals(action))
    {
      deleteKinship();
    }
    else if (KinshipsTableController.DOUBLE_CLICK.equals(action))
    {
      Kinship kinship=(Kinship)event.getSource();
      showKinship(kinship);
    }
  }

  private void showKinship(Kinship kinship)
  {
    String name=kinship.getName();
    String id=KinshipWindowController.getIdentifier(name);
    WindowController controller=_mainWindowsManager.getWindow(id);
    if (controller==null)
    {
      KinshipsIO.fullyLoadKinship(kinship);
      controller=new KinshipWindowController(kinship);
      _mainWindowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getPanel());
    }
    controller.bringToFront();
  }

  private void deleteKinship()
  {
    GenericTableController<Kinship> controller=_kinshipTables.getTableController();
    Kinship kinship=controller.getSelectedItem();
    if (kinship!=null)
    {
      String name=kinship.getName();
      // Check deletion
      int result=GuiFactory.showQuestionDialog(_parentController.getWindow(),"Do you really want to delete kinship " + name + "?","Delete?",JOptionPane.YES_NO_OPTION); // I18n
      if (result==JOptionPane.OK_OPTION)
      {
        String id=KinshipWindowController.getIdentifier(name);
        WindowController windowController=_mainWindowsManager.getWindow(id);
        if (windowController!=null)
        {
          windowController.dispose();
        }
        KinshipsManager manager=KinshipsManager.getInstance();
        manager.removeKinship(kinship);
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
    EventsManager.removeListener(KinshipEvent.class,this);
    if (_kinshipTables!=null)
    {
      _kinshipTables.dispose();
      _kinshipTables=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
