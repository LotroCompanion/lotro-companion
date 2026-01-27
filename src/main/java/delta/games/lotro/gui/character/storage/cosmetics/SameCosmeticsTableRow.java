package delta.games.lotro.gui.character.storage.cosmetics;

import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;

/**
 * A row in the 'same cosmetics' table.
 * @author DAM
 */
public class SameCosmeticsTableRow
{
  private CosmeticItemsGroup _group;
  private StoredItem _item;

  /**
   * Constructor.
   * @param group Managed group.
   * @param item Item in group.
   */
  public SameCosmeticsTableRow(CosmeticItemsGroup group, StoredItem item)
  {
    _group=group;
    _item=item;
  }

  /**
   * Get the managed group.
   * @return a group.
   */
  public CosmeticItemsGroup getGroup()
  {
    return _group;
  }

  /**
   * Get the managed item.
   * @return a stored item.
   */
  public StoredItem getStoredItem()
  {
    return _item;
  }
}
