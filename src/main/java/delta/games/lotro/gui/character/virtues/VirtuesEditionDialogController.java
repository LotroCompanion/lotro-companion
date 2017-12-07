package delta.games.lotro.gui.character.virtues;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.virtues.VirtuesSet;

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
   */
  public VirtuesEditionDialogController(WindowController parentController, VirtuesSet virtues)
  {
    super(parentController,virtues);
    _virtuesEdition=new VirtuesEditionPanelController();
    _virtuesEdition.setVirtues(virtues);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Virtues edition");
    dialog.setResizable(false);
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
   * @return The edited virtues or <code>null</code> if the window was closed or canceled.
   */
  public static VirtuesSet editVirtues(WindowController parent, VirtuesSet virtues)
  {
    VirtuesEditionDialogController controller=new VirtuesEditionDialogController(parent,virtues);
    VirtuesSet editedVirtues=controller.editModal();
    return editedVirtues;
  }
}
