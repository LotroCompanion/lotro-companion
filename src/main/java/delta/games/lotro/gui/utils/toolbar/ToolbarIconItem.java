package delta.games.lotro.gui.utils.toolbar;

/**
 * Description of a toolbar icon item.
 * @author DAM
 */
public class ToolbarIconItem implements ToolbarItem
{
  private String _itemId;
  private String _iconPath;
  private String _actionId;
  private String _tooltip;
  private String _alternativeText;
  
  /**
   * Constructor.
   * @param itemId Item identifier.
   * @param iconPath Icon path.
   * @param actionId Action identifier.
   * @param tooltip Tooltip.
   * @param altText Alternative text.
   */
  public ToolbarIconItem(String itemId, String iconPath, String actionId, String tooltip, String altText)
  {
    _itemId=itemId;
    _iconPath=iconPath;
    _actionId=actionId;
    _tooltip=tooltip;
    _alternativeText=altText;
  }

  /**
   * Get the item identifier.
   * @return an item identifier.
   */
  public String getItemId()
  {
    return _itemId;
  }

  /**
   * Get the icon path.
   * @return an icon path or <code>null</code>.
   */
  public String getIconPath()
  {
    return _iconPath;
  }

  /**
   * Get the action identifier. 
   * @return an action identifier or <code>null</code>.
   */
  public String getActionId()
  {
    return _actionId;
  }

  /**
   * Get the tooltip. 
   * @return a tooltip or <code>null</code>.
   */
  public String getTooltip()
  {
    return _tooltip;
  }

  /**
   * Get the alternative text. 
   * @return a string or <code>null</code>.
   */
  public String getAlternativeText()
  {
    return _alternativeText;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Toolbar icon item: item id=[").append(_itemId);
    sb.append("] icon path=[").append(_iconPath);
    sb.append("] action id=[").append(_actionId);
    sb.append("] tooltip=[").append(_tooltip).append(']');
    sb.append("] alternative text=[").append(_alternativeText).append(']');
    return sb.toString();
  }
}
