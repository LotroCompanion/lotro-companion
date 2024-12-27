package delta.games.lotro.gui.character.status.effects;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.effects.EffectInstance;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for an effect instance edition window.
 * @author DAM
 */
public class EffectInstanceEditionWindowController extends DefaultFormDialogController<EffectInstance>
{
  // Controllers
  private EffectInstanceEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param effectInstance Effect instance.
   */
  public EffectInstanceEditionWindowController(WindowController parent, EffectInstance effectInstance)
  {
    super(parent,effectInstance);
    _panelController=new EffectInstanceEditionPanelController(this,_data);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  @Override
  public void configureWindow()
  {
    JDialog dialog=getDialog();
    dialog.pack();
    dialog.setMinimumSize(dialog.getSize());
  }

  @Override
  protected boolean checkInput()
  {
    String errorMsg=_panelController.checkData();
    if (errorMsg==null)
    {
      return true;
    }
    showErrorMessage(errorMsg);
    return false;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title=Labels.getLabel("effect.edition.error.title");
    JDialog dialog=getDialog();
    GuiFactory.showErrorDialog(dialog,errorMsg,title);
  }

  @Override
  protected void okImpl()
  {
    _panelController.updateFromUi();
    super.okImpl();
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
