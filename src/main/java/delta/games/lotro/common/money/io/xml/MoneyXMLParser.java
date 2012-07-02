package delta.games.lotro.common.money.io.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.Money;

/**
 * Parser for money stored in XML.
 * @author DAM
 */
public class MoneyXMLParser
{
  /**
   * Load money from XML.
   * @param root Quest description tag.
   * @param money Storage for loaded data.
   */
  public static void loadMoney(Element root, Money money)
  {
    if (money!=null)
    {
      Element moneyTag=DOMParsingTools.getChildTagByName(root,MoneyXMLConstants.MONEY_TAG);
      if (moneyTag!=null)
      {
        NamedNodeMap attrs=moneyTag.getAttributes();
        int gold=DOMParsingTools.getIntAttribute(attrs,MoneyXMLConstants.MONEY_GOLD_ATTR,0);
        money.setGoldCoins(gold);
        int silver=DOMParsingTools.getIntAttribute(attrs,MoneyXMLConstants.MONEY_SILVER_ATTR,0);
        money.setSilverCoins(silver);
        int copper=DOMParsingTools.getIntAttribute(attrs,MoneyXMLConstants.MONEY_COPPER_ATTR,0);
        money.setCopperCoins(copper);
      }
    }
  }
}
