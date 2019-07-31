package delta.games.lotro.gui.items.legendary.shared;

import java.net.URL;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import delta.common.utils.url.URLTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.io.xml.ItemXMLParser;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Utility methods to test the edition of legendary item instances.
 * @author DAM
 */
public class LegendariesTestUtils
{
  private static final Logger LOGGER=Logger.getLogger(LegendariesTestUtils.class);

  /**
   * Load an item instance for tests.
   * @param name Name of the test file.
   * @return the loaded item instance.
   */
  public static ItemInstance<? extends Item> loadItemInstance(String name)
  {
    ItemInstance<? extends Item> itemInstance=null;
    try
    {
      URL url=URLTools.getFromClassPath(name,LegendariesTestUtils.class.getPackage());
      Element root=DOMParsingTools.parse(url);
      ItemXMLParser parser=new ItemXMLParser();
      itemInstance=parser.parseItemInstance(root);
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not build test item instance",e);
    }
    return itemInstance;
  }

  /**
   * Extract the legendary instance attributes from an item instance.
   * @param itemInstance Source item instance.
   * @return the extracted attributes or <code>null</code> if not found.
   */
  public static LegendaryInstanceAttrs getLegendaryAttrs(ItemInstance<? extends Item> itemInstance)
  {
    LegendaryInstanceAttrs legAttrs=null;
    if (itemInstance instanceof LegendaryInstance)
    {
      LegendaryInstance legInstance=(LegendaryInstance)itemInstance;
      legAttrs=legInstance.getLegendaryAttributes();
    }
    return legAttrs;
  }
}
