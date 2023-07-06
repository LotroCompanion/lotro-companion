package delta.games.lotro.gui.character.status.levelling;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.levelling.LevelHistory;

/**
 * Controller for a "character level history" edition dialog.
 * @author DAM
 */
public class LevelHistoryEditionDialogController extends DefaultFormDialogController<LevelHistory>
{
  // Data
  private CharacterFile _toon;
  // Controllers
  private LevelHistoryEditionPanelController _editor;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public LevelHistoryEditionDialogController(WindowController parentController, CharacterFile toon)
  {
    super(parentController,toon.getLevelHistory());
    _toon=toon;
    int level=toon.getSummary().getLevel();
    _editor=new LevelHistoryEditionPanelController(level,_data);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Level history editor"); // I18n
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_editor.getPanel();
    return dataPanel;
  }

  @Override
  public void okImpl()
  {
    _editor.updateData();
    _toon.saveLevelHistory();
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
    _data=null;
    _toon=null;
  }
}
