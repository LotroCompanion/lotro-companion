package delta.games.lotro.gui.stats.deeds.form;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;

/**
 * Controller for the "deed status edition" dialog.
 * @author DAM
 */
public class DeedStatusEditionDialogControllerNew extends DefaultFormDialogController<AchievableStatus>
{
  // Controllers
  private AchievableStatusEditionPanelController _statusEditor;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   */
  public DeedStatusEditionDialogControllerNew(AchievableStatus status, WindowController parentController)
  {
    super(parentController,status);
    DeedDescription deed=(DeedDescription)status.getAchievable();
    AchievableProxiesResolver.resolve(deed);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Deed status edition...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    _statusEditor=new AchievableStatusEditionPanelController(this,_data);
    return _statusEditor.getPanel();
  }

  @Override
  protected void okImpl()
  {
    System.out.println("OK");
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_statusEditor!=null)
    {
      _statusEditor=null;
    }
  }
}
