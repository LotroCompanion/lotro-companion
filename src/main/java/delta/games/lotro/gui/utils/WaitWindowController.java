package delta.games.lotro.gui.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Controller for a wait window.
 * @author DAM
 */
public class WaitWindowController
{
  private JDialog _dialog;
  private JLabel _label;
  private JProgressBar _progressBar;

  /**
   * Constructor.
   */
  public WaitWindowController()
  {
    buildGUI();
  }

  /**
   * Get the managed dialog.
   * @return the managed dialog.
   */
  public JDialog getDialog()
  {
    return _dialog;
  }

  /**
   * Set the displayed label.
   * @param label Label to set.
   */
  public void setLabel(String label)
  {
    if (_label!=null)
    {
      _label.setText(label);
    }
  }

  /**
   * Set the title of this dialog.
   * @param title Title to set.
   */
  public void setTitle(String title)
  {
    if (_dialog!=null)
    {
      _dialog.setTitle(title);
    }
  }

  /**
   * Set the range of the progress bar.
   * @param min Minimum value to set.
   * @param max Maximum value to set.
   */
  public void setRange(int min, int max)
  {
    _progressBar.setMinimum(min);
    _progressBar.setMaximum(max);
  }

  /**
   * Set the value of the progress bar.
   * @param value Value to srt.
   */
  public void setValue(int value)
  {
    _progressBar.setValue(value);
    //_progressBar.setString(nb+"/"+_pool.getNbJobs());
  }

  private void buildGUI()
  {
    _label=new JLabel();
    _progressBar=new JProgressBar(SwingConstants.HORIZONTAL,0,100);
    //_progressBar.setStringPainted(true);
    JPanel panel=new JPanel(new GridBagLayout());
    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(_label,c);
    c.gridy=1;c.fill=GridBagConstraints.BOTH;
    panel.add(_progressBar,c);
    JDialog dialog=new JDialog();
    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setModal(false);
  }

  /**
   * Show this dialog.
   */
  public void show()
  {
    _dialog.pack();
    _dialog.setVisible(true);
  }

  /**
   * Release the managed resources.
   */
  public void dispose()
  {
    if (_dialog!=null)
    {
      _dialog.setVisible(false);
      _dialog.removeAll();
      _dialog.dispose();
      _dialog=null;
    }
    _label=null;
    _progressBar=null;
  }
}
