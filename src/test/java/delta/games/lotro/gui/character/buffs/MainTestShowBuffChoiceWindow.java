package delta.games.lotro.gui.character.buffs;

import java.util.List;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffRegistry;
import delta.games.lotro.character.stats.buffs.BuffsManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

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
    CharacterData c=new CharacterData();
    CharacterSummary summary=c.getSummary();
    summary.setCharacterClass(CharacterClass.CAPTAIN);
    summary.setRace(Race.MAN);
    BuffsManager buffs=new BuffsManager();
    List<Buff> possibleBuffs=BuffRegistry.getInstance().buildBuffSelection(c,buffs);
    Buff initialBuff=possibleBuffs.get(3);
    System.out.println(initialBuff);
    Buff buff=BuffChoiceWindowController.selectBuff(null,possibleBuffs,initialBuff);
    System.out.println(buff);
  }
}
