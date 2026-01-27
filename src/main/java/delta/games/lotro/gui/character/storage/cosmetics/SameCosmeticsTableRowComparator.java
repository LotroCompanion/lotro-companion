package delta.games.lotro.gui.character.storage.cosmetics;

import java.util.Comparator;

/**
 * @author dm
 */
public class SameCosmeticsTableRowComparator implements Comparator<SameCosmeticsTableRow>
{

  @Override
  public int compare(SameCosmeticsTableRow o1, SameCosmeticsTableRow o2)
  {
    int index1=o1.getGroup().getGroupID();
    int index2=o2.getGroup().getGroupID();
    int ret=Integer.compare(index1,index2);
    if (ret==0)
    {
      int itemIndex1=o1.getGroup().getItems().indexOf(o1.getStoredItem());
      int itemIndex2=o2.getGroup().getItems().indexOf(o2.getStoredItem());
      ret=Integer.compare(itemIndex1,itemIndex2);
    }
    return ret;
  }
}
