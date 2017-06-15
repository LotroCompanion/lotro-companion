package delta.games.lotro.gui.stats.traitPoints;

import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPoints;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;

/**
 * Test class for the trait points edition window.
 * @author DAM
 */
public class MainTestTraitPointsWindow
{
  private void doIt()
  {
    CharacterFile file=init();
    TraitPointsEditionWindowController windowController=new TraitPointsEditionWindowController(null,file);
    windowController.show();
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

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestTraitPointsWindow().doIt();
  }
}
