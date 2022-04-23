package delta.games.lotro.gui.character.cosmetics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.cosmetics.Outfit;
import delta.games.lotro.character.cosmetics.OutfitsManager;
import delta.games.lotro.character.cosmetics.io.xml.OutfitsIO;

/**
 * Test for the outfit panel.
 * @author DAM
 */
public class MainTestOutfitPanel
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterFile toon=CharactersManager.getInstance().getToonById("Landroval","Kargarth");
    {
      String name=toon.getName();
      System.out.println("Loading toon ["+name+"]");
      OutfitsManager mgr=OutfitsIO.loadOutfits(toon);
      if (mgr==null)
      {
        return;
      }
      for(Integer outfitIndex : mgr.getOutfitIndexes())
      {
        Outfit outfit=mgr.getOutfit(outfitIndex.intValue());
        OutfitPanelController ctrl=new OutfitPanelController(null,outfit);
        JPanel panel=ctrl.getPanel();
        JFrame frame=new JFrame("Outfits for "+toon.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
      }
    }
  }
}
