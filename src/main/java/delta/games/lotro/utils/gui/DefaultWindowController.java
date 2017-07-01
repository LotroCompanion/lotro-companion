package delta.games.lotro.utils.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconUtils;

/**
 * Default window controller.
 * @author DAM
 */
public class DefaultWindowController implements WindowController
{
  private JFrame _frame;
  private WindowListener _listener;

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

  /**
   * Get the managed window.
   * @return the managed window.
   */
  public Window getWindow()
  {
    return getFrame();
  }

  /**
   * Compute a window identifier.
   * @return A string that uniquely identifies the managed frame.
   */
  public String getWindowIdentifier()
  {
    return null;
  }

  protected JFrame build()
  {
    JFrame frame=new JFrame();
    JPanel backgroundPanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    frame.setContentPane(backgroundPanel);
    List<Image> icons=IconUtils.getIcons();
    frame.setIconImages(icons);
    JMenuBar menuBar=buildMenuBar();
    if (menuBar!=null)
    {
      frame.setJMenuBar(menuBar);
    }
    Container contentPane=frame.getContentPane();
    JComponent component=buildContents();
    if (component!=null)
    {
      contentPane.add(component,BorderLayout.CENTER);
    }
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
    _listener=closeWindowAdapter;

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

  /**
   * Perform window closing.
   */
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
   * Bring the managed window to front.
   */
  public void bringToFront()
  {
    JFrame frame=getFrame();
    frame.setVisible(true);
    frame.setState(Frame.NORMAL);
    frame.toFront();
  }

  /**
   * Set window title.
   * @param title Title to set.
   */
  public void setTitle(String title)
  {
    JFrame frame=getFrame();
    frame.setTitle(title);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_frame!=null)
    {
      if (_listener!=null)
      {
        _frame.removeWindowListener(_listener);
      }
      _frame.setVisible(false);
      _frame.removeAll();
      _frame.dispose();
      _frame=null;
    }
    _listener=null;
  }
}
