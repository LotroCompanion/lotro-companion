package delta.games.lotro.gui.lore.items.legendary2;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.Config;
import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;

/**
 * Simple test class for the imbued legendary attributes edition panel.
 * @author DAM
 */
public class MainTestLegendaryAttrs2Edition
{
  private void doIt()
  {
    final ItemInstance<? extends Item> item=LegendariesTestUtils.loadItemInstance("BurglarNewLegWeapon.xml");
    int maxLevel=Config.getInstance().getMaxCharacterLevel();
    final LegendaryInstance2EditionPanelController controller=new LegendaryInstance2EditionPanelController(null,maxLevel,item);
    final LegendaryInstanceAttrs2 attrs=LegendariesTestUtils.getLegendaryAttrs2(item);

    DefaultFormDialogController<LegendaryInstanceAttrs2> dialog=new DefaultFormDialogController<LegendaryInstanceAttrs2>(null,attrs)
    {
      @Override
      protected JPanel buildFormPanel()
      {
        return controller.getPanel();
      }

      @Override
      protected void okImpl()
      {
        super.okImpl();
        controller.getData(item,attrs);
      }
    };
    LegendaryInstanceAttrs2 result=dialog.editModal();
    System.out.println("Result: "+result);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendaryAttrs2Edition().doIt();
  }
}
