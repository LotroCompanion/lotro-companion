package delta.games.lotro.common.money.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.games.lotro.common.Money;

/**
 * Writes LOTRO money to XML documents.
 * @author DAM
 */
public class MoneyXMLWriter
{
  private static final String CDATA="CDATA";
  
  /**
   * Write a money amount rewards to an XML document.
   * @param hd Output transformer.
   * @param money Money to write.
   * @throws Exception If an error occurs.
   */
  public static void writeMoney(TransformerHandler hd, Money money)
      throws Exception
  {
    if ((money!=null) && (!money.isEmpty()))
    {
      AttributesImpl moneyAttrs=new AttributesImpl();
      moneyAttrs.addAttribute("","",MoneyXMLConstants.MONEY_GOLD_ATTR,CDATA,String.valueOf(money.getGoldCoins()));
      moneyAttrs.addAttribute("","",MoneyXMLConstants.MONEY_SILVER_ATTR,CDATA,String.valueOf(money.getSilverCoins()));
      moneyAttrs.addAttribute("","",MoneyXMLConstants.MONEY_COPPER_ATTR,CDATA,String.valueOf(money.getCopperCoins()));
      hd.startElement("","",MoneyXMLConstants.MONEY_TAG,moneyAttrs);
      hd.endElement("","",MoneyXMLConstants.MONEY_TAG);
    }
  }
}
