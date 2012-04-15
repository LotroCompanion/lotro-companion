package delta.games.lotro.gui.utils;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Controller for a panel with OK and cancel button.
 * @author DAM
 */
public class OKCancelPanelController
{
  /**
   * OK command.
   */
  public static final String OK_COMMAND="OK";
  /**
   * Cancel command.
   */
  public static final String CANCEL_COMMAND="CANCEL";
  private JPanel _panel;
  private JButton _okButton;
  private JButton _cancelButton;

  /**
   * Constructor.
   */
  public OKCancelPanelController()
  {
    build();
  }

  private void build()
  {
    _panel=new JPanel();
    _panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
    _okButton=new JButton("OK");
    _okButton.setActionCommand(OK_COMMAND);
    _panel.add(_okButton);
    _cancelButton=new JButton("Cancel");
    _cancelButton.setActionCommand(CANCEL_COMMAND);
    _panel.add(_cancelButton);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Get the OK button.
   * @return the OK button.
   */
  public JButton getOKButton()
  {
    return _okButton;
  }

  /**
   * Get the cancel button.
   * @return the cancel button.
   */
  public JButton getCancelButton()
  {
    return _cancelButton;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _okButton=null;
    _cancelButton=null;
  }
}
