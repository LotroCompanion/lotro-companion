package delta.games.lotro.gui.common.id;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.games.lotro.common.id.InternalGameId;

/**
 * Controller for a panel to edit an internal game identifier.
 * @author DAM
 */
public class InternalGameIdEditionPanelController
{
  private IntegerEditionController _high;
  private IntegerEditionController _low;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public InternalGameIdEditionPanelController()
  {
    _high=buildEditor();
    _low=buildEditor();
    _panel=buildPanel();
  }

  private IntegerEditionController buildEditor()
  {
    JTextField textField=GuiFactory.buildTextField("");
    IntegerEditionController editor=new IntegerEditionController(textField,8);
    return editor;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    // High
    JLabel highLabel=GuiFactory.buildLabel("High:");
    ret.add(highLabel,c);
    c.gridx++;
    ret.add(_high.getTextField(),c);
    c.gridx++;
    // Low
    JLabel lowLabel=GuiFactory.buildLabel("Low:");
    ret.add(lowLabel,c);
    c.gridx++;
    ret.add(_low.getTextField(),c);
    c.gridx++;
    return ret;
  }

  /**
   * Display an identifier ID.
   * @param id Identifier to show.
   */
  public void setId(InternalGameId id)
  {
    if (id!=null)
    {
      // High
      int high=id.getId1();
      _high.setValue(Integer.valueOf(high));
      // Low
      int low=id.getId2();
      _low.setValue(Integer.valueOf(low));
    }
  }

  /**
   * Get the currently edited value.
   * @return an identifier value or <code>null</code> if empty or invalid.
   */
  public InternalGameId getId()
  {
    InternalGameId ret=null;
    // Get values
    // - high
    Integer high=_high.getValue();
    // - low
    Integer low=_low.getValue();
    if ((high!=null) || (low!=null))
    {
      ret=new InternalGameId(high.intValue(),low.intValue());
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_high!=null)
    {
      _high.dispose();
      _high=null;
    }
    if (_low!=null)
    {
      _low.dispose();
      _low=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
