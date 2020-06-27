package delta.games.lotro.gui.character.traitTree;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.TraitTreeStatus;

/**
 * Controller for the "trait tree edition" dialog.
 * @author DAM
 */
public class TraitTreeEditionDialog extends DefaultFormDialogController<TraitTreeStatus>
{
  private TraitTreePanelController _treePanel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   */
  public TraitTreeEditionDialog(WindowController parentController, TraitTreeStatus status)
  {
    super(parentController,status);
    _treePanel=new TraitTreePanelController(status);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Trait tree edition...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _treePanel.getPanel();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_treePanel!=null)
    {
      _treePanel.dispose();
      _treePanel=null;
    }
  }
}
