package delta.games.lotro.gui.log;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItemsFilter;
import delta.games.lotro.character.log.CharacterLogsManager;

/**
 * Controller for a "character log" window.
 * @author DAM
 */
public class CharacterLogWindowController
{
  private JDialog _dialog;
  private CharacterLogFilterController _filterController;
  private CharacterLogTableController _tableController;
  private CharacterFile _toon;
  private CharacterLogItemsFilter _filter;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterLogWindowController(CharacterFile toon)
  {
    _toon=toon;
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

  private CharacterLog getLog()
  {
    CharacterLog log=_toon.getLastCharacterLog();
    return log;
  }

  private JDialog build()
  {
    CharacterLog log=getLog();
    String name=log.getName();
    JPanel logPanel=new JPanel(new GridBagLayout());
    // Log frame
    JPanel logFramePanel=new JPanel(new BorderLayout());
    Border logFrameBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Log");
    logFramePanel.setBorder(logFrameBorder);
    // - table
    _tableController=new CharacterLogTableController(log,_filter);
    JTable table=_tableController.getTable();
    JScrollPane scroll=new JScrollPane(table);
    logFramePanel.add(scroll,BorderLayout.CENTER);
    // - control buttons
    JPanel controlPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
    JButton updateButton=new JButton("Update");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        updateLog();
      }
    };
    updateButton.addActionListener(al);
    controlPanel.add(updateButton);
    logFramePanel.add(controlPanel,BorderLayout.SOUTH);
    // Filter
    _filterController=new CharacterLogFilterController(log,_filter,_tableController);
    JPanel filterPanel=_filterController.getPanel();
    Border filterBorder=BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    logPanel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    logPanel.add(logFramePanel,c);
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
   * Update toon's log.
   */
  public void updateLog()
  {
    CharacterLogsManager logsManager=_toon.getLogsManager();
    boolean ok=logsManager.updateLog(); // todo shall use a waiting window here
    if (ok)
    {
      CharacterLog log=logsManager.getLastLog();
      _filterController.setLog(log);
      _tableController.setLog(log);
    }
    else
    {
      // todo shall do an error message here!
    }
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
    _toon=null;
  }
}
