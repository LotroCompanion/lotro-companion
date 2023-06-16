package delta.games.lotro.gui.lore.items.chooser;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.w3c.dom.Element;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.io.xml.ItemXMLConstants;
import delta.games.lotro.lore.items.io.xml.ItemInstanceXMLParser;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Simple test for the item instances chooser.
 * @author DAM
 */
public class MainTestItemInstancesChooser
{
  private List<ItemInstance<? extends Item>> getItems(File input)
  {
    List<ItemInstance<? extends Item>> ret=new ArrayList<ItemInstance<? extends Item>>();
    Element root=DOMParsingTools.parse(input);
    if (root!=null)
    {
      ItemInstanceXMLParser parser=new ItemInstanceXMLParser();
      List<Element> itemTags=DOMParsingTools.getChildTagsByName(root,ItemXMLConstants.ITEM_TAG,false);
      for(Element itemTag : itemTags)
      {
        ItemInstance<? extends Item> item=parser.parseItemInstance(itemTag);
        ret.add(item);
      }
    }
    return ret;
  }

  /**
   * Build an item instance chooser window.
   * @param items Items to use.
   * @return the newly built chooser.
   */
  private ObjectChoiceWindowController<ItemInstance<? extends Item>> buildChooser(List<ItemInstance<? extends Item>> items)
  {
    // Table
    GenericTableController<ItemInstance<? extends Item>> itemsTable=ItemInstancesTableBuilder.buildTable(items);

    // Build and configure chooser
    ObjectChoiceWindowController<ItemInstance<? extends Item>> chooser=new ObjectChoiceWindowController<ItemInstance<? extends Item>>(null,null,itemsTable);
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose item:");
    // - dimension
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    return chooser;
  }

  private void doIt()
  {
    doIt("Meva");
    doIt("Giswald");
    doIt("Ethell");
  }

  private void doIt(String name)
  {
    File input=new File("../lotro-core/"+name+".txt.xml");
    List<ItemInstance<? extends Item>> items=getItems(input);
    ObjectChoiceWindowController<ItemInstance<? extends Item>> chooser=buildChooser(items);
    chooser.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestItemInstancesChooser().doIt();
  }
}
