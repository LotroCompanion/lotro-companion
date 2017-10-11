package delta.games.lotro.gui.character.buffs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffFilter;

/**
 * Controller for a "buff choice" dialog.
 * @author DAM
 */
public class BuffChoiceWindowController extends DefaultFormDialogController<Buff>
{
  private BuffsFilterController _filterController;
  private BuffChoicePanelController _panelController;
  private BuffsTableController _tableController;
  private List<Buff> _possibleBuffs;
  private BuffFilter _filter;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param possibleBuffs Buffs to chose from.
   */
  public BuffChoiceWindowController(WindowController parent, List<Buff> possibleBuffs)
  {
    super(parent,null);
    _possibleBuffs=possibleBuffs;
    _filter=new BuffFilter();
  }

  @Override
  public String getWindowIdentifier()
  {
    return "BUFF_CHOOSER";
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose buff: ");
    dialog.setSize(500,500);
    dialog.setMinimumSize(new Dimension(500,400));
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    _tableController=new BuffsTableController(_possibleBuffs,_filter);
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ok();
      }
    };
    _tableController.getTableController().addActionListener(al);
    // Buffs table
    _panelController=new BuffChoicePanelController(_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new BuffsFilterController(_possibleBuffs,_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  /**
   * Show the buff selection dialog.
   * @param parent Parent controller.
   * @param possibleBuffs Possible buffs.
   * @param selectedBuff Selected buff.
   * @return The selected buff or <code>null</code> if the window was closed or canceled.
   */
  public static Buff selectBuff(WindowController parent, List<Buff> possibleBuffs, Buff selectedBuff)
  {
    BuffChoiceWindowController controller=new BuffChoiceWindowController(parent,possibleBuffs);
    if (parent!=null)
    {
      controller.getWindow().setLocationRelativeTo(parent.getWindow());
    }
    Buff chosenBuff=controller.doShow(selectedBuff);
    return chosenBuff;
  }

  private Buff doShow(Buff selectedBuff)
  {
    // Set selection
    _tableController.selectBuff(selectedBuff);
    // Show modal
    Buff chosenBuff=editModal();
    return chosenBuff;
  }

  @Override
  protected void okImpl()
  {
    _data=_tableController.getSelectedBuff();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    _filter=null;
  }
}
