package delta.games.lotro.gui.character.status.skills;

import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsFinder;
import delta.games.lotro.character.skills.filters.SkillCategoryFilter;
import delta.games.lotro.gui.character.status.skills.SkillsStatusWindowController;

/**
 * Test class for the skill status window.
 * @author DAM
 */
public class MainTestSkillsStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Glumlug");
    SkillCategoryFilter filter=new SkillCategoryFilter();
    filter.setCategory(102);
    SkillsFinder finder=new SkillsFinder(filter,toon.getSummary());
    List<SkillDescription> skills=finder.find();
    SkillsStatusWindowController window=new SkillsStatusWindowController(null,skills,toon);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSkillsStatusWindowController().doIt();
  }
}
