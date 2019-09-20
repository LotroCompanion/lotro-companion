package delta.games.lotro.gui.items;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.items.chooser.ItemInstancesTableBuilder;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.plugins.lotrocompanion.links.ItemsFileParser;

/**
 * Displpay the chat link items for a collection of toons.
 * @author DAM
 */
public class MainTestChatLinkItemsDisplay
{
  private static void handleToon(CharacterFile toon)
  {
    ItemsFileParser parser=new ItemsFileParser();
    List<ItemInstance<? extends Item>> items=parser.getItemsForToon(toon);
    if (items.size()>0)
    {
      // Table
      final GenericTableController<ItemInstance<? extends Item>> itemsTable=ItemInstancesTableBuilder.buildTable(items);
      DefaultWindowController controller=new DefaultWindowController()
      {
        @Override
        protected JFrame build()
        {
          JFrame frame=super.build();
          frame.setMinimumSize(new Dimension(400,300));
          frame.setSize(950,700);
          return frame;
        }

        protected JComponent buildContents()
        {
          JTable table=itemsTable.getTable();
          JScrollPane scroll=GuiFactory.buildScrollPane(table);
          return scroll;
        }
      };
      String title=toon.getName()+"@"+toon.getServerName();
      controller.setTitle(title);
      controller.show();
    }
  }

  /**
   * Handle an account on a server.
   * @param account Targeted account.
   * @param serverName Targeted server.
   */
  private static void handleAccountOnServer(Account account, String serverName)
  {
    String accountName=account.getName();
    List<CharacterFile> toonsToUse=AccountUtils.getCharacters(accountName,serverName);
    for(CharacterFile toon : toonsToUse)
    {
      handleToon(toon);
    }
  }

  /**
   * Main method to test this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Account account=AccountsManager.getInstance().getAccountByName("glorfindel666");
    if (account!=null)
    {
      handleAccountOnServer(account,"Landroval");
      handleAccountOnServer(account,"Elendilmir");
    }
  }
}
