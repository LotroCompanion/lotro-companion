package delta.games.lotro.gui.items.legendary.shared;

import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import delta.common.utils.url.URLTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.io.xml.ItemXMLParser;
import delta.games.lotro.lore.items.legendary.LegendaryAttrs;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;

/**
 * Simple test class for the passives edition panel.
 * @author DAM
 */
public class MainTestPassivesEdition
{
  private static final Logger LOGGER=Logger.getLogger(MainTestPassivesEdition.class);

  private ItemInstance<? extends Item> buildTestAttrs()
  {
    ItemInstance<? extends Item> testItem=null;
    try
    {
      URL url=URLTools.getFromClassPath("sampleLI.xml",getClass().getPackage());
      Element root=DOMParsingTools.parse(url);
      ItemXMLParser parser=new ItemXMLParser();
      testItem=parser.parseItemInstance(root);
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not build test item",e);
    }
    return testItem;
  }

  private void doIt()
  {
    ItemInstance<? extends Item> testItem=buildTestAttrs();
    if (testItem instanceof LegendaryInstance)
    {
      LegendaryInstance legInstance=(LegendaryInstance)testItem;
      LegendaryAttrs legAttrs=legInstance.getLegendaryAttributes();
      Integer level=testItem.getEffectiveItemLevel();
      PassivesEditionPanelController controller=new PassivesEditionPanelController(null,legAttrs,level.intValue());

      JFrame f=new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.getContentPane().add(controller.getPanel());
      f.pack();
      f.setVisible(true);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestPassivesEdition().doIt();
  }
}
