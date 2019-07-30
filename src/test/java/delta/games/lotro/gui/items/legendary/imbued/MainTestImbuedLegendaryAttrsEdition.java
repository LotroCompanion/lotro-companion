package delta.games.lotro.gui.items.legendary.imbued;

import java.io.File;

import javax.swing.JFrame;

import delta.common.utils.io.FileIO;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryAttrs;
import delta.games.lotro.plugins.lotrocompanion.links.ChatItemLinksDecoder;

/**
 * @author DAM
 */
public class MainTestImbuedLegendaryAttrsEdition
{
  /*
  private ImbuedLegendaryAttrs buildTestAttrs()
  {
    ImbuedLegendaryAttrs attrs=new ImbuedLegendaryAttrs();
    return attrs;
  }
  */

  private ImbuedLegendaryAttrs  buildTestAttrs()
  {
    ImbuedLegendaryAttrs imbuedLegAttrs=null;
    try
    {
      File linksDir=new File("D:\\shared\\damien\\dev\\lotrocompanion\\lua\\links");
      // Tilmo
      //File tilmo=new File(linksDir,"tilmo");
      //File club=new File(tilmo,"club.txt.bin");
      // Lorewyne
      File lorewyne=new File(linksDir,"lorewyne");
      File book=new File(lorewyne,"legendary book.plugindata.bin");
      
      byte[] buffer=FileIO.readFile(book);
      ChatItemLinksDecoder decoder=new ChatItemLinksDecoder();
      ItemInstance<? extends Item> itemInstance=decoder.decodeBuffer(buffer);
      if (itemInstance instanceof LegendaryInstance)
      {
        LegendaryInstance legInstance=(LegendaryInstance)itemInstance;
        LegendaryInstanceAttrs legAttrs=legInstance.getLegendaryAttributes();
        imbuedLegAttrs=legAttrs.getImbuedAttrs();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return imbuedLegAttrs;
  }

  private void doIt()
  {
    ImbuedLegendaryAttrs attrs=buildTestAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.MAIN_HAND);
    ImbuedLegacyInstanceEditionPanelController controller=new ImbuedLegacyInstanceEditionPanelController(null,attrs,constraints);

    JFrame f=new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(controller.getPanel());
    f.pack();
    f.setVisible(true);
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainTestImbuedLegendaryAttrsEdition().doIt();
  }

}
