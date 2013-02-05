package delta.games.lotro.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.games.lotro.Config;
import delta.games.lotro.Preferences;
import delta.games.lotro.gui.stats.levelling.CharacterLevelWindowController;
import delta.games.lotro.gui.stats.warbands.WarbandsWindowController;
import delta.games.lotro.gui.toon.ToonsManagementController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.gui.DefaultWindowController;
import delta.games.lotro.utils.gui.WindowController;
import delta.games.lotro.utils.gui.WindowsManager;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController
{
  private ToonsManagementController _toonsManager;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController();
    _windowsManager=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("LOTRO Companion");
    frame.setSize(500,400);
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
    ActionListener alWarbands=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doWarbands();
      }
    };
    warbandsStats.addActionListener(alWarbands);
    statsMenu.add(warbandsStats);
    // - levelling
    JMenuItem levellingStats=GuiFactory.buildMenuItem("Levelling");
    ActionListener alLevelling=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doLevelling();
      }
    };
    levellingStats.addActionListener(alLevelling);
    statsMenu.add(levellingStats);
    
    JMenuBar menuBar=GuiFactory.buildMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(statsMenu);
    return menuBar;
  }

  @Override
  protected JComponent buildContents()
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    JPanel toonsPanel=_toonsManager.getPanel();
    tabbedPane.add("Toons",toonsPanel);
    return tabbedPane;
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
      controller.getFrame().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void doLevelling()
  {
    String id=CharacterLevelWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterLevelWindowController();
      _windowsManager.registerWindow(controller);
      controller.getFrame().setLocationRelativeTo(getFrame());
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
    if (_toonsManager!=null)
    {
      _toonsManager.dispose();
      _toonsManager=null;
    }
  }
}
