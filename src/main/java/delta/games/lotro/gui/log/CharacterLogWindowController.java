package delta.games.lotro.gui.log;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItemsFilter;

/**
 * Controller for a "character log" window.
 * @author DAM
 */
public class CharacterLogWindowController
{
  private JDialog _dialog;
  private CharacterLogFilterController _filterController;
  private CharacterLogTableController _tableController;
  private CharacterLog _log;
  private CharacterLogItemsFilter _filter;

  /**
   * Constructor.
   * @param log Managed log.
   */
  public CharacterLogWindowController(CharacterLog log)
  {
    _log=log;
    _filter=new CharacterLogItemsFilter();
  }

  /**
   * Show the managed dialog.
   */
  public void show()
  {
    JDialog dialog=getDialog();
    dialog.setVisible(true);
  }

  /**
   * Get the managed dialog.
   * @return the managed dialog.
   */
  public JDialog getDialog()
  {
    if (_dialog==null)
    {
      _dialog=build();
    }
    return _dialog;
  }

  private JDialog build()
  {
    String name=_log.getName();
    JPanel logPanel=new JPanel(new GridBagLayout());
    // Table
    _tableController=new CharacterLogTableController(_log,_filter);
    JTable table=_tableController.getTable();
    JScrollPane scroll=new JScrollPane(table);
    Border tableBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Log");
    scroll.setBorder(tableBorder);
    // Filter
    _filterController=new CharacterLogFilterController(_log,_filter,_tableController);
    JPanel filterPanel=_filterController.getPanel();
    Border filterBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Filter");
    filterPanel.setBorder(filterBorder);
    // Log panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    logPanel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    logPanel.add(scroll,c);
    // Dialog
    JDialog dialog=new JDialog();
    dialog.setContentPane(logPanel);
    String title="Character log for: "+name;
    dialog.setTitle(title);
    dialog.pack();
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    return dialog;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_dialog!=null)
    {
      _dialog.setVisible(false);
      _dialog.removeAll();
      _dialog.dispose();
      _dialog=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _log=null;
  }
}
