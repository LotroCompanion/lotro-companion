package delta.games.lotro.utils.gui.tables;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.games.lotro.utils.gui.tables.DataItem.SEX;

/**
 * Test for the generic tables.
 * @author DAM
 */
public class MainTestGenericTables
{
  private GenericTableController<DataItem> _table;
  private List<TableColumnController<DataItem,?>> _controllers=new ArrayList<TableColumnController<DataItem,?>>();

  private boolean _doAdd=false;

  private void doIt()
  {
    _table=buildTable();
    JFrame f=new JFrame();
    JPanel panel=new JPanel(new BorderLayout());
    JScrollPane scroll=new JScrollPane(_table.getTable());
    panel.add(scroll,BorderLayout.CENTER);
    JButton b=new JButton("Update columns");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        updateColumns();
      }
    };
    b.addActionListener(al);
    panel.add(b,BorderLayout.SOUTH);
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
  }

  private void updateColumns()
  {
    if (_doAdd)
    {
      TableColumnController<DataItem,?> controller=_controllers.get(0);
      _table.addColumnController(controller);
    }
    else
    {
      TableColumnController<DataItem,?> controller=_controllers.get(0);
      _table.removeColumnController(controller);
    }
    _table.updateColumns();
    _doAdd=!_doAdd;
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
    _controllers.add(idColumn);
    // Name column
    for(int i=0;i<3;i++)
    {
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
      _controllers.add(nameColumn);
    }
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
    _controllers.add(sexColumn);
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
