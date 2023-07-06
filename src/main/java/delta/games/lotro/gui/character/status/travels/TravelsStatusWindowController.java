package delta.games.lotro.gui.character.status.travels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsFinder;
import delta.games.lotro.character.skills.filters.SkillCategoryFilter;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.io.SkillsStatusIo;
import delta.games.lotro.character.status.travels.AnchorsStatusManager;
import delta.games.lotro.character.status.travels.io.AnchorsStatusIo;
import delta.games.lotro.gui.character.status.skills.SkillsStatusPanelController;

/**
 * Controller for a window that show the status of travel-related features.
 * @author DAM
 */
public class TravelsStatusWindowController extends DefaultWindowController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="TRAVELS_STATUS_WINDOW";

  // Controllers
  private AnchorsStatusDisplayPanelController _anchorsPanel;
  private SkillsStatusPanelController _travelSkillsPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public TravelsStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    List<SkillDescription> skills=findSkills(toon.getSummary());
    SkillsStatusManager skillsStatus=SkillsStatusIo.load(toon);
    _travelSkillsPanel=new SkillsStatusPanelController(this,skills,skillsStatus);
    AnchorsStatusManager anchorsStatus=AnchorsStatusIo.load(toon);
    _anchorsPanel=new AnchorsStatusDisplayPanelController(this,anchorsStatus);
  }

  private List<SkillDescription> findSkills(BasicCharacterAttributes character)
  {
    SkillCategoryFilter filter=new SkillCategoryFilter();
    filter.setCategory(102);
    SkillsFinder finder=new SkillsFinder(filter,character);
    List<SkillDescription> skills=finder.find();
    List<SkillDescription> ret=new ArrayList<SkillDescription>();
    for(SkillDescription skill : skills)
    {
      if (skill.getIdentifier()!=1879276393) // Skip "Smell the Roses"
      {
        ret.add(skill);
      }
    }
    return ret;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Travels Status"); // I18n
    frame.pack();
    frame.setSize(frame.getWidth(),INITIAL_HEIGHT);
    frame.setMinimumSize(new Dimension(frame.getWidth(),MIN_HEIGHT));
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JTabbedPane tab=GuiFactory.buildTabbedPane();
    // Travel skills
    JPanel travelSkillsPanel=_travelSkillsPanel.getPanel();
    tab.add("Travel Skills", travelSkillsPanel); // I18n
    // Milestones
    JPanel anchorsPanel=_anchorsPanel.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(anchorsPanel);
    tab.add("Milestones", scroll); // I18n
    panel.add(tab,BorderLayout.CENTER);
    return panel;
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
    if (_anchorsPanel!=null)
    {
      _anchorsPanel.dispose();
      _anchorsPanel=null;
    }
    if (_travelSkillsPanel!=null)
    {
      _travelSkillsPanel.dispose();
      _travelSkillsPanel=null;
    }
  }
}
