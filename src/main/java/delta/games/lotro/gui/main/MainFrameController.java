package delta.games.lotro.gui.main;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.Preferences;
import delta.games.lotro.Config;
import delta.games.lotro.gui.about.AboutDialogController;
import delta.games.lotro.gui.stats.levelling.CharacterLevelWindowController;
import delta.games.lotro.gui.stats.warbands.WarbandsWindowController;
import delta.games.lotro.gui.toon.ToonsManagementController;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.ui.MapWindowController;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController implements ActionListener
{
  private static final String LEVELLING_ID="levelling";
  private static final String WARBANDS_ID="warbands";
  private static final String MAP_ID="map";

  private ToolbarController _toolbar;
  private ToonsManagementController _toonsManager;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController(this);
    _windowsManager=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("LOTRO Companion");
    frame.setSize(630,400);
    frame.setLocation(100,100);
    frame.getContentPane().setBackground(GuiFactory.getBackgroundColor());
    return frame;
  }

  @Override
  protected JMenuBar buildMenuBar()
  {
    JMenu fileMenu=GuiFactory.buildMenu("File");
    JMenuItem quit=GuiFactory.buildMenuItem("Quit");
    ActionListener alQuit=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doQuit();
      }
    };
    quit.addActionListener(alQuit);
    fileMenu.add(quit);

    // Statistics
    JMenu statsMenu=GuiFactory.buildMenu("Statistics");
    // - warbands
    JMenuItem warbandsStats=GuiFactory.buildMenuItem("Warbands");
    warbandsStats.setActionCommand(WARBANDS_ID);
    warbandsStats.addActionListener(this);
    statsMenu.add(warbandsStats);
    // - levelling
    JMenuItem levellingStats=GuiFactory.buildMenuItem("Levelling");
    levellingStats.setActionCommand(LEVELLING_ID);
    levellingStats.addActionListener(this);
    statsMenu.add(levellingStats);
    // - map
    JMenuItem map=GuiFactory.buildMenuItem("Middle-earth Map");
    map.setActionCommand(MAP_ID);
    map.addActionListener(this);
    statsMenu.add(map);

    // Help
    JMenu helpMenu=GuiFactory.buildMenu("Help");
    // - about
    JMenuItem aboutMenuItem=GuiFactory.buildMenuItem("About...");
    ActionListener alAbout=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doAbout();
      }
    };
    aboutMenuItem.addActionListener(alAbout);
    helpMenu.add(aboutMenuItem);

    JMenuBar menuBar=GuiFactory.buildMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(statsMenu);
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(helpMenu);
    return menuBar;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    JPanel toonsPanel=_toonsManager.getPanel();
    tabbedPane.add("Characters",toonsPanel);
    ret.add(tabbedPane,BorderLayout.CENTER);
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Levelling icon
    String levellingIconPath=getToolbarIconPath("levelling");
    ToolbarIconItem levellingIconItem=new ToolbarIconItem(LEVELLING_ID,levellingIconPath,LEVELLING_ID,"Levelling...","Levelling");
    model.addToolbarIconItem(levellingIconItem);
    // Warbands icon
    String warbandsIconPath=getToolbarIconPath("warbands");
    ToolbarIconItem warbandsIconItem=new ToolbarIconItem(WARBANDS_ID,warbandsIconPath,WARBANDS_ID,"Warbands...","Warbands");
    model.addToolbarIconItem(warbandsIconItem);
    // Map icon
    String mapIconPath=getToolbarIconPath("globe");
    ToolbarIconItem mapIconItem=new ToolbarIconItem(MAP_ID,mapIconPath,MAP_ID,"Map...","Map");
    model.addToolbarIconItem(mapIconItem);
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  @Override
  protected void doWindowClosing()
  {
    doQuit();
  }

  private void doWarbands()
  {
    String id=WarbandsWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new WarbandsWindowController();
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void doMap()
  {
    WindowController controller=_windowsManager.getWindow(MapWindowController.IDENTIFIER);
    if (controller==null)
    {
      File mapsDir=Config.getInstance().getMapsDir();
      MapsManager mapsManager=new MapsManager(mapsDir);
      mapsManager.load();
      controller=new MapWindowController(mapsManager);
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (LEVELLING_ID.equals(cmd))
    {
        doLevelling();
    }
    else if (WARBANDS_ID.equals(cmd))
    {
      doWarbands();
    }
    else if (MAP_ID.equals(cmd))
    {
      doMap();
    }
  }

  private void doLevelling()
  {
    String id=CharacterLevelWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterLevelWindowController();
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void doAbout()
  {
    String id=AboutDialogController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      JFrame thisFrame=getFrame();
      controller=new AboutDialogController(this);
      _windowsManager.registerWindow(controller);
      Window w=controller.getWindow();
      w.setLocationRelativeTo(thisFrame);
      Point p=w.getLocation();
      w.setLocation(p.x+100,p.y+100);
    }
    controller.bringToFront();
  }

  private void doQuit()
  {
    int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to quit?","Quit?",JOptionPane.YES_NO_OPTION);
    if (result==JOptionPane.OK_OPTION)
    {
      dispose();
    }
    Preferences preferences=Config.getInstance().getPreferences();
    preferences.saveAllPreferences();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_toolbar==null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    if (_toonsManager!=null)
    {
      _toonsManager.dispose();
      _toonsManager=null;
    }
  }
}
