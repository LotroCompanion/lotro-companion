package delta.games.lotro.utils.gui.tables;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.games.lotro.utils.gui.tables.DataItem.SEX;

/**
 * @author dm
 */
public class MainTestGenericTables
{

  private void doIt()
  {
    GenericTableController<DataItem> table=buildTable();
    JFrame f=new JFrame();
    JPanel panel=new JPanel(new BorderLayout());
    JScrollPane scroll=new JScrollPane(table.getTable());
    panel.add(scroll,BorderLayout.CENTER);
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
  }

  private GenericTableController<DataItem> buildTable()
  {
    List<DataItem> items=new ArrayList<DataItem>();
    ListDataProvider<DataItem> provider=new ListDataProvider<DataItem>(items);
    DataItem item1=new DataItem(1,"MORCELLET",SEX.MALE);
    items.add(item1);
    DataItem item2=new DataItem(2,"SOURICE",SEX.FEMALE);
    items.add(item2);
    GenericTableController<DataItem> table=new GenericTableController<DataItem>(provider);

    // ID column
    CellDataProvider<DataItem,Long> idCell=new CellDataProvider<DataItem,Long>()
    {
      public Long getData(DataItem item)
      {
        return Long.valueOf(item.getId());
      }
    };
    TableColumnController<DataItem,Long> idColumn=new TableColumnController<DataItem,Long>("ID",Long.class,idCell);
    idColumn.setWidthSpecs(100,100,100);
    table.addColumnController(idColumn);
    // Name column
    CellDataProvider<DataItem,String> nameCell=new CellDataProvider<DataItem,String>()
    {
      public String getData(DataItem item)
      {
        return item.getName();
      }
    };
    TableColumnController<DataItem,String> nameColumn=new TableColumnController<DataItem,String>("NOM",String.class,nameCell);
    nameColumn.setWidthSpecs(100,200,150);
    table.addColumnController(nameColumn);
    // Sex column
    CellDataProvider<DataItem,SEX> sexCell=new CellDataProvider<DataItem,SEX>()
    {
      public SEX getData(DataItem item)
      {
        return item.getSex();
      }
    };
    TableColumnController<DataItem,SEX> sexColumn=new TableColumnController<DataItem,SEX>("SEX",SEX.class,sexCell);
    sexColumn.setWidthSpecs(100,200,150);
    table.addColumnController(sexColumn);
    return table;
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainTestGenericTables().doIt();
  }
}
