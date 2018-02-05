package delta.games.lotro.gui.deed;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Controller for a deed edition window.
 * @author DAM
 */
public class DeedEditionWindowController extends DefaultFormDialogController<DeedDescription>
{
  private DeedEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param deed Deed.
   */
  public DeedEditionWindowController(WindowController parent, DeedDescription deed)
  {
    super(parent,deed);
    _panelController=new DeedEditionPanelController(this,deed);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(dialog.getPreferredSize());
    dialog.setTitle(_data.getName());
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  @Override
  protected void okImpl()
  {
    _panelController.getItem();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
