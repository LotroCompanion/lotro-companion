package delta.games.lotro.gui.items.legendary.non_imbued;

import java.net.URL;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import delta.common.utils.url.URLTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.items.legendary.non_imbued.NonImbuedAttrsEditionPanelController;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.io.xml.ItemXMLParser;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryAttrs;

/**
 * Simple test class for the non-imbued legendary attributes edition panel.
 * @author DAM
 */
public class MainTestNonImbuedLegendaryAttrsEdition
{
  private static final Logger LOGGER=Logger.getLogger(MainTestNonImbuedLegendaryAttrsEdition.class);

  private NonImbuedLegendaryAttrs  buildTestAttrs()
  {
    NonImbuedLegendaryAttrs nonImbuedLegAttrs=null;
    try
    {
      URL url=URLTools.getFromClassPath("sampleLI.xml",getClass().getPackage());
      Element root=DOMParsingTools.parse(url);
      ItemXMLParser parser=new ItemXMLParser();
      ItemInstance<? extends Item> itemInstance=parser.parseItemInstance(root);
      if (itemInstance instanceof LegendaryInstance)
      {
        LegendaryInstance legInstance=(LegendaryInstance)itemInstance;
        LegendaryInstanceAttrs legAttrs=legInstance.getLegendaryAttributes();
        nonImbuedLegAttrs=legAttrs.getNonImbuedAttrs();
      }
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not build test non-imbued legendary attrs",e);
    }
    return nonImbuedLegAttrs;
  }

  private void doIt()
  {
    NonImbuedLegendaryAttrs attrs=buildTestAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.CLASS_SLOT);
    NonImbuedAttrsEditionPanelController controller=new NonImbuedAttrsEditionPanelController(null,attrs,constraints);

    JFrame f=new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(controller.getPanel());
    f.pack();
    f.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestNonImbuedLegendaryAttrsEdition().doIt();
  }
}
