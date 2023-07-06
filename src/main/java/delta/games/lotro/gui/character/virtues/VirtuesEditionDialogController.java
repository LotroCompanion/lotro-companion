package delta.games.lotro.gui.character.virtues;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.character.status.virtues.VirtuesStatus;

/**
 * Controller for the virtues edition dialog.
 * @author DAM
 */
public class VirtuesEditionDialogController extends DefaultFormDialogController<VirtuesSet>
{
  private VirtuesEditionPanelController _virtuesEdition;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param virtues Virtues to edit.
   * @param characterLevel Character level.
   * @param status Current virtues status.
   */
  public VirtuesEditionDialogController(WindowController parentController, VirtuesSet virtues, int characterLevel, VirtuesStatus status)
  {
    super(parentController,virtues);
    _virtuesEdition=new VirtuesEditionPanelController(this,characterLevel,status);
    _virtuesEdition.setVirtues(virtues);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Virtues edition"); // I18n
    dialog.setResizable(true);
    dialog.setMinimumSize(new Dimension(700,650));
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel virtuesPanel=_virtuesEdition.getPanel();
    return virtuesPanel;
  }

  @Override
  public void okImpl()
  {
    _data=_virtuesEdition.getVirtues();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_virtuesEdition!=null)
    {
      _virtuesEdition.dispose();
      _virtuesEdition=null;
    }
  }

  /**
   * Edit virtues.
   * @param parent Parent controller.
   * @param virtues Virtues to show.
   * @param characterLevel Character level.
   * @param status Current virtues status.
   * @return The edited virtues or <code>null</code> if the window was closed or canceled.
   */
  public static VirtuesSet editVirtues(WindowController parent, VirtuesSet virtues, int characterLevel, VirtuesStatus status)
  {
    VirtuesEditionDialogController controller=new VirtuesEditionDialogController(parent,virtues,characterLevel,status);
    VirtuesSet editedVirtues=controller.editModal();
    return editedVirtues;
  }
}
