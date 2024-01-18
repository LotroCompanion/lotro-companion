package delta.games.lotro.gui.utils.icons;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.skirmish.SkirmishTraitsManager;

/**
 * Test class for the display of skirmish trait icons.
 * @author DAM
 */
public class MainTestDisplaySkirmishTraitIcons
{
  private void doIt()
  {
    JPanel p=buildPanel();
    JScrollPane sp=new JScrollPane(p);
    JFrame f=new JFrame();
    f.add(sp);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.setVisible(true);
  }

  private JPanel buildPanel()
  {
    JPanel p=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    SkirmishTraitsManager mgr=SkirmishTraitsManager.getInstance();
    for(TraitDescription trait : mgr.getAll())
    {
      c.gridx=0;
      int nbTiers=trait.getTiersCount();
      BufferedImage noRank=TraitIconBuilder.getTraitIcon(trait,null);
      p.add(GuiFactory.buildIconLabel(new ImageIcon(noRank)),c);
      if (nbTiers>1)
      {
        for(int i=1;i<=nbTiers;i++)
        {
          c.gridx=i;
          BufferedImage icon=TraitIconBuilder.getTraitIcon(trait,Integer.valueOf(i));
          p.add(GuiFactory.buildIconLabel(new ImageIcon(icon)),c);
          i+=5;
        }
      }
      c.gridy++;
    }
    return p;
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainTestDisplaySkirmishTraitIcons().doIt();
  }
}
