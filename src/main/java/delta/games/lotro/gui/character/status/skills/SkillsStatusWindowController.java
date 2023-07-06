package delta.games.lotro.gui.character.status.skills;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.io.SkillsStatusIo;

/**
 * Controller for a window that shows the status of some skills.
 * @author DAM
 */
public class SkillsStatusWindowController extends DefaultWindowController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="SKILLS_STATUS_WINDOW";

  // Controllers
  private SkillsStatusPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param skills Skills to show.
   * @param toon Character file.
   */
  public SkillsStatusWindowController(WindowController parent, List<SkillDescription> skills, CharacterFile toon)
  {
    super(parent);
    SkillsStatusManager status=SkillsStatusIo.load(toon);
    _panelController=new SkillsStatusPanelController(this,skills,status);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Skills Status"); // I18n
    frame.pack();
    frame.setSize(frame.getWidth(),INITIAL_HEIGHT);
    frame.setMinimumSize(new Dimension(frame.getWidth(),MIN_HEIGHT));
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    return _panelController.getPanel();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
