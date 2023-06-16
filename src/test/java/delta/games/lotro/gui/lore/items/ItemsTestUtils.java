package delta.games.lotro.gui.lore.items;

import java.net.URL;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import delta.common.utils.url.URLTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.io.xml.ItemInstanceXMLParser;

/**
 * Utility methods related to items test code.
 * @author DAM
 */
public class ItemsTestUtils
{
  private static final Logger LOGGER=Logger.getLogger(ItemsTestUtils.class);

  /**
   * Test samples.
   */
  public static final String[] TEST_SAMPLES=
  {
    "dyed helm.xml",
    "helm-thorin.xml",
    "indigo dyed gauntlets.xml",
    "orange dye.xml",
    "colored description.xml"
  };

  /**
   * Load an item instance for tests.
   * @param baseClass Reference class to find the test file.
   * @param name Name of the test file.
   * @return the loaded item instance.
   */
  public static ItemInstance<? extends Item> loadItemInstance(Class<?> baseClass, String name)
  {
    ItemInstance<? extends Item> itemInstance=null;
    try
    {
      URL url=URLTools.getFromClassPath(name,baseClass);
      Element root=DOMParsingTools.parse(url);
      ItemInstanceXMLParser parser=new ItemInstanceXMLParser();
      itemInstance=parser.parseItemInstance(root);
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not build test item instance",e);
    }
    return itemInstance;
  }
}
