package delta.games.lotro.gui.character.chooser;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;

/**
 * Controller for a "character selector" window.
 * @author DAM
 */
public class CharactersSelectorWindowController extends DefaultFormDialogController<List<CharacterFile>>
{
  // Controllers
  private CharactersSelectorPanelController _controller;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toons Managed toons.
   * @param selectedToons Selected toons.
   * @param enabledToons Enabled toons.
   */
  private CharactersSelectorWindowController(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons, List<CharacterFile> enabledToons)
  {
    super(parent,selectedToons);
    _controller=new CharactersSelectorPanelController(toons);
    for(CharacterFile toon : selectedToons)
    {
      _controller.setToonSelected(toon,true);
    }
    for(CharacterFile toon : enabledToons)
    {
      _controller.setToonEnabled(toon,true);
    }
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    String title="Characters selector";
    dialog.setTitle(title);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel checkBoxesPanel=_controller.getPanel();
    return checkBoxesPanel;
  }

  /**
   * Show the toon selection dialog.
   * @param parent Parent controller.
   * @param toons Toons to show.
   * @param selectedToons Pre-selected toons.
   * @param enabledToons Enabled toons.
   * @return A list of selected toons or <code>null</code> if the window was closed or canceled.
   */
  public static List<CharacterFile> selectToons(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons, List<CharacterFile> enabledToons)
  {
    CharactersSelectorWindowController controller=new CharactersSelectorWindowController(parent,toons,selectedToons,enabledToons);
    List<CharacterFile> newSelectedToons=controller.editModal();
    return newSelectedToons;
  }

  @Override
  protected void okImpl()
  {
    _data=_controller.getSelectedToons();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
    // Controllers
    super.dispose();
  }
}
