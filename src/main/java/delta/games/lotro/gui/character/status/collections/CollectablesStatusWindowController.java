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
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.races.RaceDescription;
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
import delta.games.lotro.lore.collections.mounts.MountDescription;
import delta.games.lotro.lore.collections.mounts.MountsManager;

/**
 * Controller for a "collectables status" window (mounts or pets).
 * @author DAM
 */
public class CollectablesStatusWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Status type.
   * @author DAM
   */
  public enum STATUS_TYPE
  {
    /**
     * Mounts.
     */
    MOUNTS,
    /**
     * Pets.
     */
    PETS
  }

  // Data
  private STATUS_TYPE _type;
  // Controllers
  private SkillsStatusPanelController _skills;
  private CollectionsStatusSummaryPanelController _collections;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param file Character to use.
   * @param type Status type.
   */
  public CollectablesStatusWindowController(WindowController parent, CharacterFile file, STATUS_TYPE type)
  {
    super(parent,null);
    _type=type;
    SkillsStatusManager skillsStatusMgr=SkillsStatusIo.load(file);
    List<SkillDescription> skills=findSkills(file);
    _skills=new SkillsStatusPanelController(this,skills,skillsStatusMgr);
    CollectionsStatusManager collectionsStatusMgr=findStatus(file);
    _collections=new CollectionsStatusSummaryPanelController(this,collectionsStatusMgr);
  }

  private CollectionsStatusManager findStatus(CharacterFile file)
  {
    LotroEnum<CollectionCategory> categoryEnum=LotroEnumsRegistry.getInstance().get(CollectionCategory.class);
    int code=(_type==STATUS_TYPE.PETS)?2:1;
    CollectionCategory category=categoryEnum.getEntry(code);
    Filter<CollectionDescription> filter=new CollectionCategoryFilter(category);
    CollectionsStatusManager collectionsStatusMgr=new CollectionsStatusBuilder().build(file,filter);
    return collectionsStatusMgr;
  }

  private List<SkillDescription> findSkills(CharacterFile file)
  {
    List<SkillDescription> ret=findSkills();
    if (_type==STATUS_TYPE.PETS)
    {
      return ret;
    }
    // Filter mounts using size (tall mounts for tall races)
    CharacterSummary summary=file.getSummary();
    RaceDescription race=summary.getRace();
    boolean tall=race.isTall();
    List<SkillDescription> filtered=new ArrayList<SkillDescription>();
    MountsManager mountsMgr=MountsManager.getInstance();
    for(SkillDescription skill : ret)
    {
      MountDescription mount=mountsMgr.getMount(skill.getIdentifier());
      if (mount!=null)
      {
        if (mount.isTall()==tall)
        {
          filtered.add(skill);
        }
      }
    }
    return filtered;
  }

  private List<SkillDescription> findSkills()
  {
    SkillCategoryFilter filter=new SkillCategoryFilter();
    int code=(_type==STATUS_TYPE.PETS)?145:88;
    filter.setCategory(code);
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
    window.setTitle((_type==STATUS_TYPE.PETS)?"Pets Status":"Mounts Status"); // I18n
    window.pack();
    window.setSize(window.getWidth(),INITIAL_HEIGHT);
    window.setMinimumSize(new Dimension(window.getWidth(),MIN_HEIGHT));
    return window;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getWindowIdentifier(_type);
  }

  /**
   * Get the window identifier for the given status type.
   * @param type Status type.
   * @return A window identifier.
   */
  public static String getWindowIdentifier(STATUS_TYPE type)
  {
    if (type==STATUS_TYPE.MOUNTS) return "MOUNTS_STATUS_WINDOW";
    if (type==STATUS_TYPE.PETS) return "PETS_STATUS_WINDOW";
    return "?";
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
    String tabName=(_type==STATUS_TYPE.PETS)?"Pets":"Mounts";
    tabs.add(tabName,skillsPanel);
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
