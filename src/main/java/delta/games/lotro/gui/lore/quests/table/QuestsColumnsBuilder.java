package delta.games.lotro.gui.lore.quests.table;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.lore.quests.QuestDescription;

/**
 * Builder for columns that show quests data.
 * @author DAM
 */
public class QuestsColumnsBuilder
{
  /**
   * Build a 'quest name' column.
   * @return a column.
   */
  public static DefaultTableColumnController<QuestDescription,String> buildQuestNameColumn()
  {
    CellDataProvider<QuestDescription,String> nameCell=new CellDataProvider<QuestDescription,String>()
    {
      @Override
      public String getData(QuestDescription quest)
      {
        return quest.getName();
      }
    };
    DefaultTableColumnController<QuestDescription,String> nameColumn=new DefaultTableColumnController<QuestDescription,String>(QuestColumnIds.NAME.name(),"Name",String.class,nameCell);
    nameColumn.setWidthSpecs(100,300,200);
    return nameColumn;
  }
}
