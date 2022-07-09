package delta.games.lotro.gui.character.status.collections;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.character.skills.filters.SkillCategoryFilter;
import delta.games.lotro.character.status.collections.CollectionsStatusBuilder;
import delta.games.lotro.character.status.collections.CollectionsStatusManager;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.io.SkillsStatusIo;
import delta.games.lotro.common.enums.CollectionCategory;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.gui.character.status.collections.summary.CollectionsStatusSummaryPanelController;
import delta.games.lotro.gui.character.status.skills.SkillsStatusPanelController;
import delta.games.lotro.lore.collections.CollectionDescription;
import delta.games.lotro.lore.collections.filters.CollectionCategoryFilter;

/**
 * Controller for a "mounts status" window.
 * @author DAM
 */
public class MountsStatusWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="MOUNTS_STATUS_WINDOW";

  // Controllers
  private SkillsStatusPanelController _skills;
  private CollectionsStatusSummaryPanelController _collections;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param file Character to use.
   */
  public MountsStatusWindowController(WindowController parent, CharacterFile file)
  {
    super(parent,null);
    SkillsStatusManager skillsStatusMgr=SkillsStatusIo.load(file);
    List<SkillDescription> skills=findSkills();
    _skills=new SkillsStatusPanelController(this,skills,skillsStatusMgr);
    CollectionsStatusManager collectionsStatusMgr=findStatus(file);
    _collections=new CollectionsStatusSummaryPanelController(this,collectionsStatusMgr);
  }

  private CollectionsStatusManager findStatus(CharacterFile file)
  {
    LotroEnum<CollectionCategory> categoryEnum=LotroEnumsRegistry.getInstance().get(CollectionCategory.class);
    CollectionCategory category=categoryEnum.getEntry(1);
    Filter<CollectionDescription> filter=new CollectionCategoryFilter(category);
    CollectionsStatusManager collectionsStatusMgr=new CollectionsStatusBuilder().build(file,filter);
    return collectionsStatusMgr;
  }

  private List<SkillDescription> findSkills()
  {
    SkillCategoryFilter filter=new SkillCategoryFilter();
    filter.setCategory(88);
    List<SkillDescription> skills=SkillsManager.getInstance().getAll();
    List<SkillDescription> ret=new ArrayList<SkillDescription>();
    for(SkillDescription skill : skills)
    {
      if (filter.accept(skill))
      {
        ret.add(skill);
      }
    }
    return ret;
  }

  @Override
  protected JDialog build()
  {
    JDialog window=super.build();
    window.setTitle("Mounts Status");
    window.pack();
    window.setSize(window.getWidth(),INITIAL_HEIGHT);
    window.setMinimumSize(new Dimension(window.getWidth(),MIN_HEIGHT));
    return window;
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
  protected JPanel buildFormPanel()
  {
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    JPanel skillsPanel=_skills.getPanel();
    tabs.add("Mounts",skillsPanel);
    JPanel collectionsPanel=_collections.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(collectionsPanel);
    tabs.add("Collections",scroll);
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    ret.add(tabs,BorderLayout.CENTER);
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    
    if (_collections!=null)
    {
      _collections.dispose();
      _collections=null;
    }
    if (_skills!=null)
    {
      _skills.dispose();
      _skills=null;
    }
  }
}
