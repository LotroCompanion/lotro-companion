package delta.games.lotro.gui.character.status.skills.synopsis;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.character.skills.filters.SkillCategoryFilter;

/**
 * Test class for the skill status window.
 * @author DAM
 */
public class MainTestSkillsSynopsisWindowController
{
  private List<SkillDescription> findSkills(int category)
  {
    SkillCategoryFilter filter=new SkillCategoryFilter();
    filter.setCategory(category);
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

  private void doIt()
  {
    List<SkillDescription> skills=findSkills(145); // 145: pets, 88: mounts
    SkillsSynopsisWindowController window=new SkillsSynopsisWindowController(null,skills);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSkillsSynopsisWindowController().doIt();
  }
}
