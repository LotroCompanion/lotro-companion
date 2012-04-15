package delta.games.lotro.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.games.lotro.gui.toon.ToonsManagementController;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController
{
  private JFrame _frame;
  private ToonsManagementController _toonsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController();
  }

  /**
   * Get the managed dialog.
   * @return the managed dialog.
   */
  public JFrame getFrame()
  {
    if (_frame==null)
    {
      _frame=build();
    }
    return _frame;
  }

  private JFrame build()
  {
    JFrame frame=new JFrame();
    frame.setTitle("LOTRO Companion");
    JMenuBar menuBar=buildMenuBar();
    frame.setJMenuBar(menuBar);
    JTabbedPane pane=buildMainContents();
    frame.getContentPane().add(pane);
    frame.setSize(500,400);
    frame.setLocation(100,100);

    WindowAdapter closeWindowAdapter=new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        quit();
      }
    };
    frame.addWindowListener(closeWindowAdapter);

    return frame;
  }

  private JMenuBar buildMenuBar()
  {
    JMenu fileMenu=new JMenu("File");
    JMenuItem quit=new JMenuItem("Quit");
    ActionListener alQuit=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        quit();
      }
    };
    quit.addActionListener(alQuit);
    fileMenu.add(quit);
    JMenuBar menuBar=new JMenuBar();
    menuBar.add(fileMenu);
    return menuBar;
  }

  private JTabbedPane buildMainContents()
  {
    JTabbedPane tabbedPane=new JTabbedPane();
    JPanel toonsPanel=_toonsManager.getPanel();
    tabbedPane.add("Toons",toonsPanel);
    return tabbedPane;
  }

  private void quit()
  {
    dispose();
  }

  /**
   * Show the managed window.
   */
  public void show()
  {
    JFrame frame=getFrame();
    frame.setVisible(true);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_frame!=null)
    {
      _frame.removeAll();
      _frame.dispose();
      _frame=null;
    }
    if (_toonsManager!=null)
    {
      _toonsManager.dispose();
      _toonsManager=null;
    }
  }
}
