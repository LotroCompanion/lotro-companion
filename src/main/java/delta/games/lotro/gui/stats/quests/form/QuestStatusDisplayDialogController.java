package delta.games.lotro.gui.stats.quests.form;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.gui.stats.deeds.form.AchievableStatusEditionPanelController;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;

/**
 * Controller for the "quest status edition" dialog.
 * @author DAM
 */
public class QuestStatusDisplayDialogController extends DefaultFormDialogController<AchievableStatus>
{
  private static final int MAX_HEIGHT=600;
  // Controllers
  private AchievableStatusEditionPanelController _statusEditor;
  // Data
  private AchievableStatus _original;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   */
  public QuestStatusDisplayDialogController(AchievableStatus status, WindowController parentController)
  {
    super(parentController,status);
    _original=status;
    QuestDescription quest=(QuestDescription)status.getAchievable();
    AchievableProxiesResolver.resolve(quest);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    QuestDescription quest=(QuestDescription)_data.getAchievable();
    String questName=quest.getName();
    dialog.setTitle("Quest status: "+questName);
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
    _statusEditor=new AchievableStatusEditionPanelController(this,_data,false);
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
      _statusEditor=null;
    }
  }
}
