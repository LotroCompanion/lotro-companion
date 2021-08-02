package delta.games.lotro.gui.lore.items.legendary.titles;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Simple test class for the legendary title edition panel.
 * @author DAM
 */
public class MainTestLegendaryTitleEdition
{
  private LegendaryInstanceAttrs  buildTestAttrs()
  {
    ItemInstance<? extends Item> item=LegendariesTestUtils.loadItemInstance("CaptainEmblemSecondAge75NonImbued.xml");
    LegendaryInstanceAttrs attrs=LegendariesTestUtils.getLegendaryAttrs(item);
    return attrs;
  }

  private void doIt()
  {
    final LegendaryInstanceAttrs attrs=buildTestAttrs();
    final LegendaryTitleEditionPanelController controller=new LegendaryTitleEditionPanelController(null,attrs);

    DefaultFormDialogController<LegendaryInstanceAttrs> dialog=new DefaultFormDialogController<LegendaryInstanceAttrs>(null,attrs)
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
        controller.getData(attrs);
      }
    };
    LegendaryInstanceAttrs result=dialog.editModal();
    System.out.println("Result: "+result);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendaryTitleEdition().doIt();
  }
}
