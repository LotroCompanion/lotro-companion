package delta.games.lotro.gui.common.status;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.status.StatusMetadata;
import delta.games.lotro.gui.utils.dates.DateAgeLabelController;

/**
 * Controller for a panel to display status metadata.
 * @author DAM
 */
public class StatusMetadataPanelController
{
  // Controllers
  private DateAgeLabelController _dateAge;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   */
  public StatusMetadataPanelController()
  {
    _dateAge=new DateAgeLabelController();
    _panel=buildPanel();
  }

  /**
   * Set the data to display.
   * @param data Data to display.
   */
  public void setData(StatusMetadata data)
  {
    // Date
    Long date=null;
    if (data!=null)
    {
      long d=data.getTimeStamp();
      if (d!=0)
      {
        date=Long.valueOf(d);
      }
    }
    _dateAge.setDate(date);
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(GuiFactory.buildLabel("Date: ")); // I18n
    ret.add(_dateAge.getLabel());
    return ret;
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
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    if (_dateAge!=null)
    {
      _dateAge.dispose();
      _dateAge=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
