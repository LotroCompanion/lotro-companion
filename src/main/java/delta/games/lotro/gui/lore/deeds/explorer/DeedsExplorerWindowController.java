package delta.games.lotro.gui.lore.deeds.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.deeds.filter.DeedFilterController;
import delta.games.lotro.gui.lore.deeds.table.DeedsTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.filters.DeedFilter;

/**
 * Controller for the deeds explorer window.
 * @author DAM
 */
public class DeedsExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="DEEDS_EXPLORER";

  private DeedFilterController _filterController;
  private DeedExplorerPanelController _panelController;
  private DeedsTableController _tableController;
  private DeedFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public DeedsExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new DeedFilter();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Deeds explorer"); // 18n
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initDeedsTable();
    _panelController=new DeedExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new DeedFilterController(this,_filter,_panelController,true);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // 18n
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  private void initDeedsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("DeedsExplorer");
    _tableController=new DeedsTableController(this,prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          DeedDescription deed=(DeedDescription)event.getSource();
          showDeed(deed);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showDeed(DeedDescription deed)
  {
    PageIdentifier ref=ReferenceConstants.getAchievableReference(deed);
    NavigationUtils.navigateTo(ref,this);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
