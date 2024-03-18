package delta.games.lotro.gui.character.gear;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;

/**
 * Test for character equipment display.
 * @author DAM
 */
public class MainTestEquipmentDisplay
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    //List<CharacterFile> toons=utils.getAllFiles();
    CharacterFile toon=utils.getMainToon();
    //for(CharacterFile toon : toons)
    {
      String name=toon.getName();
      System.out.println("Loading toon ["+name+"]");
      CharacterInfosManager manager=new CharacterInfosManager(toon);
      CharacterData infos=manager.getLastCharacterDescription();
      if (infos!=null)
      {
        DefaultWindowController c=new DefaultWindowController();
        ROEquipmentPanelController ctrl=new ROEquipmentPanelController(c,infos.getEquipment());
        JPanel panel=ctrl.getPanel();
        JFrame f=c.getFrame();
        f.setTitle("Equipment for "+toon.getName());
        f.add(panel);
        f.pack();
        f.setVisible(true);
      }
    }
  }
}
