package delta.games.lotro.gui.utils.icons;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.utils.maths.ArrayProgression;

/**
 * Builder for trait icons.
 * @author DAM
 */
public class TraitIconBuilder
{
  /**
   * Get the buffered image that represents a ranked trait.
   * @param trait Trait to use.
   * @param rank Rank.
   * @return A buffered image.
   */
  public static BufferedImage getTraitIcon(TraitDescription trait, Integer rank)
  {
    List<BufferedImage> images=new ArrayList<BufferedImage>();
    // Background
    int traitIconId=trait.getIconId();
    BufferedImage background=IconsManager.getImage("/traits/"+traitIconId+".png");
    images.add(background);
    // Static overlay
    Integer staticIconOverlayId=trait.getStaticIconOverlayId();
    if (staticIconOverlayId!=null)
    {
      BufferedImage staticOverlay=IconsManager.getImage("/traits/"+staticIconOverlayId+".png");
      images.add(staticOverlay);
    }
    // Rank overlay
    if ((rank!=null) && (rank.intValue()>=1))
    {
      ArrayProgression progression=trait.getRankOverlayProgression();
      if (progression!=null)
      {
        Number rankIconId=progression.getRawValue(rank.intValue());
        if (rankIconId!=null)
        {
          BufferedImage rankOverlay=IconsManager.getImage("/traits/"+rankIconId+".png");
          images.add(rankOverlay);
        }
      }
    }
    BufferedImage[] layers=images.toArray(new BufferedImage[images.size()]);
    return LayeredIconBuilder.buildImage(layers);
  }
}
