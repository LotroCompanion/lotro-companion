package delta.games.lotro.region;

import delta.games.lotro.region.io.web.RegionPageParser;

/**
 * Test for region description parsing.
 * @author DAM
 */
public class MainTestRegionParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String angmar="http://lorebook.lotro.com/wiki/Region:Angmar";
    RegionPageParser parser=new RegionPageParser();
    parser.parseRegionPage(angmar);
  }
}
