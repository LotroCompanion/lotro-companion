package delta.games.lotro.gui.items.relics;

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
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicFilter;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;

/**
 * Controller for a "relic choice" dialog.
 * @author DAM
 */
public class RelicChoiceWindowController extends DefaultFormDialogController<Relic>
{
  private RelicsFilterController _filterController;
  private RelicChoicePanelController _panelController;
  private RelicsTableController _tableController;
  private RelicFilter _filter;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public RelicChoiceWindowController(WindowController parent)
  {
    super(parent,null);
    _filter=new RelicFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose relic: ");
    dialog.setMinimumSize(new Dimension(900,300));
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    RelicsManager relicsMgr=RelicsManager.getInstance();
    List<Relic> relics=relicsMgr.getAllRelics(false);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    _tableController=new RelicsTableController(relics,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ok();
      }
    };
    _tableController.getTableController().addActionListener(al);
    // Relics table
    _panelController=new RelicChoicePanelController(_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new RelicsFilterController(_filter,_panelController);
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
    Relic chosenRelic=controller.doShow(type, selectedRelic);
    return chosenRelic;
  }

  private Relic doShow(RelicType type, Relic selectedRelic)
  {
    // Ensure that the dialog is built
    getDialog();
    // Filter
    if (type!=null)
    {
      _filter.setRelicType(type);
      _filterController.setFilter();
    }
    // Set selection
    _tableController.selectRelic(selectedRelic);
    // Show modal
    Relic chosenRelic=editModal();
    return chosenRelic;
  }

  @Override
  protected void okImpl()
  {
    _data=_tableController.getSelectedRelic();
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
