package delta.games.lotro.utils.html;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Link generator that uses the navigation system.
 * @author DAM
 */
public class NavigatorLinkGenerator implements LinkGenerator
{
  @Override
  public String buildURL(Identifiable to)
  {
    PageIdentifier link=null;
    if (to instanceof Item)
    {
      Item item=(Item)to;
      link=ReferenceConstants.getItemReference(item.getIdentifier());
    }
    else if (to instanceof Achievable)
    {
      Achievable achievable=(Achievable)to;
      link=ReferenceConstants.getAchievableReference(achievable);
    }
    return (link!=null)?link.getFullAddress():"?";
  }
}
