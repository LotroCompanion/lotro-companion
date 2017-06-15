package delta.games.lotro.gui.stats.traitPoints;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointFilter;
import delta.games.lotro.stats.traitPoints.TraitPoints;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;

/**
 * @author DAM
 */
public class MainTestTraitPointsWindow
{
  private void doIt()
  {
    CharacterFile file=init();
    TraitPointFilter filter=new TraitPointFilter();
    TraitPointsTableController tableController=new TraitPointsTableController(file,filter);
    JTable table=tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    JFrame frame=new JFrame();
    frame.getContentPane().add(scroll,BorderLayout.CENTER);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private CharacterFile init()
  {
    CharacterFile file=new LotroTestUtils().getToonByName("Meva");
    TraitPointsStatus status=new TraitPointsStatus();
    List<TraitPoint> points=TraitPoints.get().getRegistry().getPointsForClass(CharacterClass.MINSTREL);
    int index=0;
    for(TraitPoint point : points)
    {
      status.setStatus(point.getId(),(index%3==0));
      index++;
    }
    TraitPoints.get().save(file,status);
    return file;
  }

  public static void main(String[] args)
  {
    new MainTestTraitPointsWindow().doIt();
  }
}
