package delta.games.lotro.gui.character.cosmetics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.cosmetics.OutfitsManager;
import delta.games.lotro.character.cosmetics.io.xml.OutfitsIO;

/**
 * Test for the outfit panel.
 * @author DAM
 */
public class MainTestOutfitsPanel
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    for(CharacterFile toon : utils.getAllFiles())
    {
      String name=toon.getName();
      System.out.println("Loading toon ["+name+"]");
      OutfitsManager mgr=OutfitsIO.loadOutfits(toon);
      if (mgr==null)
      {
        continue;
      }
      OutfitsDisplayPanelController ctrl=new OutfitsDisplayPanelController(null);
      ctrl.updateContents(mgr);
      JPanel panel=ctrl.getPanel();
      JFrame frame=new JFrame("Outfits for "+toon.getName());
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.setContentPane(panel);
      frame.pack();
      frame.setVisible(true);
    }
  }
}
