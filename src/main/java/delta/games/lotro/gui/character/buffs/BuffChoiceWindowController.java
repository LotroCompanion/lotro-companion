package delta.games.lotro.gui.character.buffs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.OKCancelPanelController;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffFilter;

/**
 * Controller for a "buff choice" dialog.
 * @author DAM
 */
public class BuffChoiceWindowController extends DefaultDialogController
{
  private BuffsFilterController _filterController;
  private BuffChoicePanelController _panelController;
  private BuffsTableController _tableController;
  private OKCancelPanelController _okCancelController;
  private List<Buff> _possibleBuffs;
  private BuffFilter _filter;
  private Buff _chosenBuff;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param possibleBuffs Buffs to chose from.
   */
  public BuffChoiceWindowController(WindowController parent, List<Buff> possibleBuffs)
  {
    super(parent);
    _possibleBuffs=possibleBuffs;
    _okCancelController=new OKCancelPanelController();
    _filter=new BuffFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose buff: ");
    dialog.setSize(500,500);
    //dialog.pack();
    dialog.setMinimumSize(new Dimension(500,200));
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return "BUFF_CHOOSER";
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel editionPanel=buildEditionPanel();
    panel.add(editionPanel,BorderLayout.CENTER);
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    return panel;
  }

  private JPanel buildEditionPanel()
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
   * Show the relic selection dialog.
   * @param parent Parent controller.
   * @param possibleBuffs Possible buffs.
   * @param selectedBuff Selected relic.
   * @return The selected relic or <code>null</code> if the window was closed or canceled.
   */
  public static Buff selectBuff(WindowController parent, List<Buff> possibleBuffs, Buff selectedBuff)
  {
    BuffChoiceWindowController controller=new BuffChoiceWindowController(parent,possibleBuffs);
    if (parent!=null)
    {
      controller.getWindow().setLocationRelativeTo(parent.getWindow());
    }
    Buff chosenRelic=controller.doShow(selectedBuff);
    return chosenRelic;
  }

  private Buff doShow(Buff selectedBuff)
  {
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (OKCancelPanelController.OK_COMMAND.equals(action))
        {
          ok();
        }
        else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
        {
          cancel();
        }
      }
    };
    _okCancelController.getOKButton().addActionListener(al);
    _okCancelController.getCancelButton().addActionListener(al);

    // Build dialog
    JDialog thisDialog=getDialog();
    // Set parent
    WindowController parent=getParentController();
    if (parent!=null)
    {
      Window parentWindow=parent.getWindow();
      thisDialog.setLocationRelativeTo(parentWindow);
    }
    // Set selection
    _tableController.selectBuff(selectedBuff);
    // Show modal
    show(true);
    Buff chosenBuff=_chosenBuff;
    dispose();
    return chosenBuff;
  }

  @Override
  protected void doWindowClosing()
  {
    cancel();
  }

  private void ok()
  {
    _chosenBuff=_tableController.getSelectedBuff();
    hide();
  }

  private void cancel()
  {
    _chosenBuff=null;
    hide();
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
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
    }
    _filter=null;
  }
}
