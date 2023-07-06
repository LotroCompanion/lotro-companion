package delta.games.lotro.gui.character.status.deeds.form;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.character.status.achievables.form.AchievableFormConfig;
import delta.games.lotro.gui.character.status.achievables.form.AchievableStatusPanelController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;

/**
 * Controller for the "deed status edition" dialog.
 * @author DAM
 */
public class DeedStatusDialogController extends DefaultFormDialogController<AchievableStatus>
{
  private static final int MAX_HEIGHT=600;
  // Controllers
  private AchievableStatusPanelController _statusEditor;
  // Data
  private AchievableStatus _original;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   */
  public DeedStatusDialogController(AchievableStatus status, WindowController parentController)
  {
    super(parentController,clone(status));
    _original=status;
    DeedDescription deed=(DeedDescription)status.getAchievable();
    AchievableProxiesResolver.resolve(deed);
  }

  private static AchievableStatus clone(AchievableStatus status)
  {
    AchievableStatus ret=new AchievableStatus(status.getAchievable());
    ret.copyFrom(status);
    return ret;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Deed status edition..."); // I18n
    dialog.setResizable(true);
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width+30,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    AchievableFormConfig config=new AchievableFormConfig(AchievableUIMode.DEED,true);
    _statusEditor=new AchievableStatusPanelController(this,_data,config);
    return _statusEditor.getPanel();
  }

  @Override
  protected void okImpl()
  {
    _statusEditor.onOkImpl();
    _original.copyFrom(_data);
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
      _statusEditor.dispose();
      _statusEditor=null;
    }
  }
}
