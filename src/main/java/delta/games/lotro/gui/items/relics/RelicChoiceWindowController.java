package delta.games.lotro.gui.items.relics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicFilter;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "relic choice" window.
 * @author DAM
 */
public class RelicChoiceWindowController extends DefaultWindowController
{
  private RelicsFilterController _filterController;
  private RelicChoicePanelController _panelController;
  private RelicsTableController _tableController;
  private RelicFilter _filter;

  /**
   * Constructor.
   */
  public RelicChoiceWindowController()
  {
    _filter=new RelicFilter();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Choose relic: ");
    frame.pack();
    frame.setMinimumSize(new Dimension(900,300));
    return frame;
  }
  

  @Override
  public String getWindowIdentifier()
  {
    return "RELIC_CHOOSER";
  }

  @Override
  protected JComponent buildContents()
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
