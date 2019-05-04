package delta.games.lotro.gui.quests.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;

/**
 * Controller for a "quest display" window.
 * @author DAM
 */
public class QuestDisplayWindowController extends DefaultDialogController
{
  // Controllers
  private QuestDisplayPanelController _controller;
  private int _id;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param id Identifier.
   */
  public QuestDisplayWindowController(WindowController parent, int id)
  {
    super(parent);
    _id=id;
  }

  /**
   * Set quest to display.
   * @param quest Quest to display.
   */
  public void setQuest(QuestDescription quest)
  {
    disposeQuestPanel();
    AchievableProxiesResolver.resolve(quest);
    _controller=new QuestDisplayPanelController(this,quest);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Quest: "+quest.getName());
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(false);
  }

  @Override
  public String getWindowIdentifier()
  {
    return "QUEST_DISPLAY#"+_id;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposeQuestPanel();
    super.dispose();
  }

  private void disposeQuestPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
