package delta.games.lotro.gui.stats.levelling;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.OKCancelPanelController;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.level.LevelHistory;

/**
 * Controller for a "character level history" edition dialog.
 * @author DAM
 */
public class LevelHistoryEditionDialogController extends DefaultDialogController implements ActionListener
{
  // Data
  private CharacterFile _toon;
  private LevelHistory _data;
  // Controllers
  private LevelHistoryEditionPanelController _editor;
  private OKCancelPanelController _okCancelController;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public LevelHistoryEditionDialogController(WindowController parentController, CharacterFile toon)
  {
    super(parentController);
    _toon=toon;
    _data=_toon.getLevelHistory();
    int level=toon.getSummary().getLevel();
    _editor=new LevelHistoryEditionPanelController(level,_data);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Level history editor");
    dialog.setResizable(false);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel dataPanel=_editor.getPanel();
    panel.add(dataPanel,BorderLayout.CENTER);
    _okCancelController=new OKCancelPanelController();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().addActionListener(this);
    _okCancelController.getCancelButton().addActionListener(this);
    return panel;
  }

  public void actionPerformed(ActionEvent event)
  {
    String action=event.getActionCommand();
    if (OKCancelPanelController.OK_COMMAND.equals(action))
    {
      ok();
    }
    else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
    {
      cancel();
    }
  }

  private void ok()
  {
    _editor.updateData();
    _toon.saveLevelHistory();
    dispose();
  }

  private void cancel()
  {
    dispose();
  }

  @Override
  protected void doWindowClosing()
  {
    cancel();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_editor!=null)
    {
      _editor.dispose();
      _editor=null;
    }
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _editor=null;
    }
    _data=null;
    _toon=null;
  }
}
