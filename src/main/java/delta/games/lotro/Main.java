package delta.games.lotro;

import javax.swing.JFrame;

import delta.games.lotro.gui.main.MainFrameController;

/**
 * Main for LOTRO companion.
 * @author DAM
 */
public class Main
{
  /**
   * Main method of LOTRO companion.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    //MainFrameController controller=new MainFrameController();
    //controller.show();

    //NewToonDialogController controller=new NewToonDialogController();
    //controller.show();

    //CharactersManager cm=CharactersManager.getInstance();
    //List<CharacterFile> toons=cm.getAllToons();
    /*
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    
    for(CharacterFile toon : toons)
    {
      CharacterLog log=toon.getLastCharacterLog();
      if (log!=null)
      {
        CharacterLogWindowController controller=new CharacterLogWindowController(log);
        controller.show();
      }
    }
    */
    MainFrameController controller=new MainFrameController();
    JFrame frame=controller.getFrame();
    frame.setVisible(true);
  }
}
