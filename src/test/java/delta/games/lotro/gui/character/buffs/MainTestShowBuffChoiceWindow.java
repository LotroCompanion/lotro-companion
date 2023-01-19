package delta.games.lotro.gui.character.buffs;

import java.util.List;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffRegistry;
import delta.games.lotro.character.stats.buffs.BuffsManager;
import delta.games.lotro.common.CharacterClass;

/**
 * Test for buff choice window.
 * @author DAM
 */
public class MainTestShowBuffChoiceWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterSummary summary=new CharacterSummary();
    summary.setCharacterClass(CharacterClass.CAPTAIN);
    RaceDescription man=RacesManager.getInstance().getByKey("man");
    summary.setRace(man);
    BuffsManager buffs=new BuffsManager();
    List<Buff> possibleBuffs=BuffRegistry.getInstance().buildBuffSelection(summary,buffs);
    Buff initialBuff=possibleBuffs.get(3);
    System.out.println(initialBuff);
    Buff buff=BuffChoiceWindowController.selectBuff(null,possibleBuffs,initialBuff);
    System.out.println(buff);
  }
}
