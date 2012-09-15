package delta.games.lotro.utils.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author DAM
 */
public class DefaultWindowController
{
  private JFrame _frame;
  
  /**
   * Get the managed frame.
   * @return the managed frame.
   */
  public JFrame getFrame()
  {
    if (_frame==null)
    {
      _frame=build();
    }
    return _frame;
  }

  protected JFrame build()
  {
    JFrame frame=new JFrame();
    JMenuBar menuBar=buildMenuBar();
    frame.setJMenuBar(menuBar);
    JComponent component=buildContents();
    frame.getContentPane().add(component);
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    WindowAdapter closeWindowAdapter=new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        doWindowClosing();
      }
    };
    frame.addWindowListener(closeWindowAdapter);

    return frame;
  }

  protected JMenuBar buildMenuBar()
  {
    return null;
  }

  protected JComponent buildContents()
  {
    return new JPanel();
  }

  protected void doWindowClosing()
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
      _frame.setVisible(false);
      _frame.removeAll();
      _frame.dispose();
      _frame=null;
    }
  }
}
