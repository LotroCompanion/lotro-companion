package delta.games.lotro.gui.character.status.deeds.statistics.curves;

import java.util.List;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.stats.deeds.statistics.curves.DeedCurvesBuilder;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Simple test class to show deed curves.
 * @author DAM
 */
public class MainTestDeedStatisticsCurves
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    List<CharacterFile> toons=utils.getAllFiles();
    for(CharacterFile toon : toons)
    {
      DeedCurvesBuilder builder=new DeedCurvesBuilder();
      DatedCurve<?> lotroPointsCurve=builder.getCurve(toon);
      String name=lotroPointsCurve.getName();
      List<?> points=lotroPointsCurve.getPoints();
      int nbPoints=points.size();
      System.out.println("Curve name: "+name+" ("+nbPoints+" points)");
      for(Object point : points)
      {
        System.out.println("\t"+point);
      }
    }
  }
}
