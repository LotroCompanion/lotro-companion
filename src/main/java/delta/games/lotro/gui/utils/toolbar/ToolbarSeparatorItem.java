package delta.games.lotro.gui.utils.toolbar;

/**
 * Description of a toolbar separator item.
 * @author DAM
 */
public class ToolbarSeparatorItem implements ToolbarItem
{
  private String _itemId;

  /**
   * Constructor.
   * @param id Item id.
   */
  public ToolbarSeparatorItem(String id)
  {
    _itemId=id;
  }

  /**
   * Get the item identifier.
   * @return an item identifier.
   */
  public String getItemId()
  {
    return _itemId;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Toolbar separator item: item id=[").append(_itemId);
    sb.append(']');
    return sb.toString();
  }
}
