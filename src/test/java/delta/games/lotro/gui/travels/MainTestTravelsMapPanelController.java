package delta.games.lotro.gui.travels;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;

/**
 * @author dm
 */
class MainTestTravelsMapPanelController
{
  public static void main(String[] args)
  {
    CharacterFile file=new LotroTestUtils().getToonByName("Giswald");
    TravelsMapPanelController c=new TravelsMapPanelController(file);
    JFrame f=new JFrame();
    JPanel mapPanel=c.getPanel();
    f.getContentPane().add(mapPanel,BorderLayout.CENTER);
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.pack();
    f.setVisible(true);
  }
}
