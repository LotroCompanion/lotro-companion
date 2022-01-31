package delta.games.lotro.gui.character.status.skills;

import java.util.List;

import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.filters.SkillStatusFilter;
import delta.games.lotro.character.status.skills.io.SkillsStatusIo;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.SkillCategory;

/**
 * Test class for the anchors status panel.
 * @author DAM
 */
public class MainTestSkillsStatusPanel
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    SkillsStatusManager statusMgr=SkillsStatusIo.load(toon);
    SkillStatusFilter filter=new SkillStatusFilter();
    filter.getKnownFilter().setIsKnownFlag(Boolean.FALSE);
    LotroEnum<SkillCategory> categories=LotroEnumsRegistry.getInstance().get(SkillCategory.class);
    SkillCategory travelSkills=categories.getEntry(88);
    List<SkillDescription> skills=SkillsManager.getInstance().getSkillsByCategory(travelSkills);
    SkillsStatusDisplayPanelController panelCtrl=new SkillsStatusDisplayPanelController(null,skills,statusMgr,filter);
    DefaultWindowController w=new DefaultWindowController();
    JScrollPane scroll=GuiFactory.buildScrollPane(panelCtrl.getPanel());
    w.getFrame().add(scroll);
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSkillsStatusPanel().doIt();
  }
}
