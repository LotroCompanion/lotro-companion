package delta.games.lotro.gui.maps;

import java.awt.image.BufferedImage;
import java.io.File;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.dat.utils.DatIconsUtils;

/**
 * A tool to load radar tiles.
 * @author DAM
 */
public class MainLoadRadarTiles
{
  private static final File ROOT_DIR=new File("d:\\tmp\\radar");

  private DatRadarImageProvider _provider;

  /**
   * Constructor.
   */
  public MainLoadRadarTiles()
  {
    _provider=new DatRadarImageProvider(new DataFacade());
  }

  private void doIt()
  {
    handleRegion(1);
  }

  private void handleRegion(int region)
  {
    for(int blockX=0;blockX<=255;blockX++)
    {
      for(int blockY=0;blockY<=255;blockY++)
      {
        BufferedImage image=_provider.getImage(region,blockX,blockY);
        if (image!=null)
        {
          File toDir=new File(ROOT_DIR,String.valueOf(region));
          if (!toDir.exists())
          {
            toDir.mkdirs();
          }
          File to=new File(toDir,"tile-"+blockX+"-"+blockY+".png");
          DatIconsUtils.writeImage(image,to);
        }
      }
    }
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainLoadRadarTiles().doIt();
  }
}
