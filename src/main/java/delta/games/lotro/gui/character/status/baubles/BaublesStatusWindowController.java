package delta.games.lotro.gui.character.status.baubles;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.io.SkillsStatusIo;
import delta.games.lotro.gui.character.status.skills.SkillsStatusPanelController;
import delta.games.lotro.lore.collections.baubles.BaublesManager;

/**
 * Controller for a "baubles status" window.
 * @author DAM
 */
public class BaublesStatusWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Window identifier.
   */
  public static final String WINDOW_IDENTIFIER="BAUBLES_STATUS_WINDOW";
  // Controllers
  private SkillsStatusPanelController _skills;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param file Character to use.
   */
  public BaublesStatusWindowController(WindowController parent, CharacterFile file)
  {
    super(parent,null);
    SkillsStatusManager skillsStatusMgr=SkillsStatusIo.load(file);
    List<SkillDescription> skills=BaublesManager.getInstance().getAll();
    _skills=new SkillsStatusPanelController(this,skills,skillsStatusMgr);
  }

  @Override
  protected JDialog build()
  {
    JDialog window=super.build();
    window.setTitle("Baubles Status"); // I18n
    window.pack();
    window.setSize(window.getWidth(),INITIAL_HEIGHT);
    window.setMinimumSize(new Dimension(window.getWidth(),MIN_HEIGHT));
    return window;
  }

  @Override
  public String getWindowIdentifier()
  {
    return WINDOW_IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _skills.getPanel();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_skills!=null)
    {
      _skills.dispose();
      _skills=null;
    }
  }
}
