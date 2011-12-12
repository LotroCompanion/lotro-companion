package delta.games.lotro.gui;

import javax.swing.JFrame;
import javax.swing.JTable;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController
{
  private JFrame _frame;
  private ToonsTableController _toonsTable;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsTable=new ToonsTableController();
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
    JTable table=_toonsTable.getTable();
    frame.getContentPane().add(table);
    return frame;
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
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
  }
}
