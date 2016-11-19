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
  private Relic _selectedRelic;

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
   * @return The selected relic or <code>null</code> if the window was closed or canceled.
   */
  public static Relic selectRelic(WindowController parent)
  {
    RelicChoiceWindowController controller=new RelicChoiceWindowController(parent);
    Relic selectedRelic=controller.doShow();
    return selectedRelic;
  }

  private Relic doShow()
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
        _selectedRelic=_tableController.getSelectedRelic();
      }

      private void cancel()
      {
        _selectedRelic=null;
      }
    };
    _okCancelController.getOKButton().addActionListener(al);
    _okCancelController.getCancelButton().addActionListener(al);
    WindowController parent=getParentController();
    if (parent!=null)
    {
      Window parentWindow=parent.getWindow();
      JDialog thisDialog=getDialog();
      thisDialog.setLocationRelativeTo(parentWindow);
    }
    show(true);
    Relic selectedRelic=_selectedRelic;
    dispose();
    return selectedRelic;
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
