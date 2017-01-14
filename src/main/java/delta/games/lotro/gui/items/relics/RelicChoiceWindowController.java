package delta.games.lotro.gui.items.relics;

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

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.OKCancelPanelController;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicFilter;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;
import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a "relic choice" dialog.
 * @author DAM
 */
public class RelicChoiceWindowController extends DefaultDialogController
{
  private RelicsFilterController _filterController;
  private RelicChoicePanelController _panelController;
  private RelicsTableController _tableController;
  private OKCancelPanelController _okCancelController;
  private RelicFilter _filter;
  private Relic _chosenRelic;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public RelicChoiceWindowController(WindowController parent)
  {
    super(parent);
    _okCancelController=new OKCancelPanelController();
    _filter=new RelicFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose relic: ");
    dialog.pack();
    dialog.setMinimumSize(new Dimension(900,300));
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return "RELIC_CHOOSER";
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
    RelicsManager relicsMgr=RelicsManager.getInstance();
    List<Relic> relics=relicsMgr.getAllRelics();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    _tableController=new RelicsTableController(relics,_filter);
    // Relics table
    _panelController=new RelicChoicePanelController(_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new RelicsFilterController(relics,_filter,_panelController);
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
   * @param type Relic type to use (locks filter) or <code>null</code>.
   * @param selectedRelic Selected relic.
   * @return The selected relic or <code>null</code> if the window was closed or canceled.
   */
  public static Relic selectRelic(WindowController parent, RelicType type, Relic selectedRelic)
  {
    RelicChoiceWindowController controller=new RelicChoiceWindowController(parent);
    if (parent!=null)
    {
      controller.getWindow().setLocationRelativeTo(parent.getWindow());
    }
    Relic chosenRelic=controller.doShow(type, selectedRelic);
    return chosenRelic;
  }

  private Relic doShow(RelicType type, Relic selectedRelic)
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
        hide();
      }

      private void ok()
      {
        _chosenRelic=_tableController.getSelectedRelic();
      }

      private void cancel()
      {
        _chosenRelic=null;
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
    // Filter
    if (type!=null)
    {
      _filter.setRelicType(type);
      _filterController.setFilter();
    }
    // Set selection
    _tableController.selectRelic(selectedRelic);
    // Show modal
    show(true);
    Relic chosenRelic=_chosenRelic;
    dispose();
    return chosenRelic;
  }

  @Override
  protected void doWindowClosing()
  {
    _chosenRelic=null;
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
