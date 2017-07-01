package delta.games.lotro.gui.character;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.log.LotroTestUtils;

/**
 * Test for character equipment access.
 * @author DAM
 */
public class MainTestEquipmentLoading
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
        EquipmentPanelController ctrl=new EquipmentPanelController(null,toon,infos);
        JPanel panel=ctrl.getPanel();
        JFrame frame=new JFrame("Equipment for "+toon.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        frame.setContentPane(panel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    }
  }
}
