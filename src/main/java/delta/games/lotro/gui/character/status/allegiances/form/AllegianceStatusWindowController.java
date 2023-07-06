package delta.games.lotro.gui.character.status.allegiances.form;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.lore.allegiances.AllegianceDescription;

/**
 * Controller for an allegiance status display window.
 * @author DAM
 */
public class AllegianceStatusWindowController extends DefaultDisplayDialogController<AllegianceStatus>
{
  // Controllers
  private AllegianceStatusPanelController _statusController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Data to show.
   */
  public AllegianceStatusWindowController(WindowController parent, AllegianceStatus status)
  {
    super(parent,status);
    _statusController=new AllegianceStatusPanelController(this,status);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(800,500));
    String allegianceName=_data.getAllegiance().getName();
    dialog.setTitle("Allegiance status: "+allegianceName); // I18n
    dialog.setSize(new Dimension(800,700));
    dialog.setMaximumSize(new Dimension(1000,700));
    return dialog;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier(_data.getAllegiance());
  }

  /**
   * Get the window identifier for an allegiance.
   * @param allegiance Allegiance to use.
   * @return An identifier.
   */
  public static final String getIdentifier(AllegianceDescription allegiance)
  {
    return String.valueOf(allegiance.getIdentifier());
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=_statusController.getPanel();
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _data=null;
    // Controllers
    if (_statusController!=null)
    {
      _statusController.dispose();
      _statusController=null;
    }
  }
}
