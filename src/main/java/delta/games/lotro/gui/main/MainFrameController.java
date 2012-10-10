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

import delta.games.lotro.gui.toon.ToonsManagementController;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController
{
  private ToonsManagementController _toonsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("LOTRO Companion");
    frame.setSize(500,400);
    frame.setLocation(100,100);

    return frame;
  }

  @Override
  protected JMenuBar buildMenuBar()
  {
    JMenu fileMenu=new JMenu("File");
    JMenuItem quit=new JMenuItem("Quit");
    ActionListener alQuit=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doQuit();
      }
    };
    quit.addActionListener(alQuit);
    fileMenu.add(quit);
    JMenuBar menuBar=new JMenuBar();
    menuBar.add(fileMenu);
    return menuBar;
  }

  @Override
  protected JComponent buildContents()
  {
    JTabbedPane tabbedPane=new JTabbedPane();
    JPanel toonsPanel=_toonsManager.getPanel();
    tabbedPane.add("Toons",toonsPanel);
    return tabbedPane;
  }

  @Override
  protected void doWindowClosing()
  {
    doQuit();
  }

  private void doQuit()
  {
    int result=JOptionPane.showConfirmDialog(getFrame(),"Do you really want to quit?","Quit?",JOptionPane.YES_NO_OPTION);
    if (result==JOptionPane.OK_OPTION)
    {
      dispose();
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_toonsManager!=null)
    {
      _toonsManager.dispose();
      _toonsManager=null;
    }
  }
}
