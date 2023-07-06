package delta.games.lotro.gui.character.status.traitPoints;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.status.traitPoints.TraitPointsStatus;

/**
 * Controller for a traits points edition window.
 * @author DAM
 */
public class TraitPointsEditionWindowController extends DefaultFormDialogController<TraitPointsStatus>
{
  private TraitPointsEditionPanelController _panelController;
  private CharacterSummary _summary;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param summary Character summary.
   * @param status Status to edit.
   */
  public TraitPointsEditionWindowController(WindowController parent, CharacterSummary summary, TraitPointsStatus status)
  {
    super(parent,status);
    _summary=summary;
    _panelController=new TraitPointsEditionPanelController(this,summary,status);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(800,dialog.getHeight());
    String name=_summary.getName();
    int level=_summary.getLevel();
    dialog.setTitle("Trait points for "+name+" ("+level+")"); // I18n
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _summary=null;
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
