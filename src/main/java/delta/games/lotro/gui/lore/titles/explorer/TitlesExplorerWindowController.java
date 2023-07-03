package delta.games.lotro.gui.lore.titles.explorer;

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
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.titles.TitleFilterController;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.lore.titles.TitlesTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.filters.TitleFilter;

/**
 * Controller for the titles explorer window.
 * @author DAM
 */
public class TitlesExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="TITLES_EXPLORER";

  private TitleFilterController _filterController;
  private TitleExplorerPanelController _panelController;
  private TitlesTableController _tableController;
  private TitleFilter _filter;
  private WindowsManager _titleWindows;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public TitlesExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new TitleFilter();
    _titleWindows=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Titles explorer");
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
    initTitlesTable();
    _panelController=new TitleExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new TitleFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTitlesTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("TitlesExplorer");
    _tableController=new TitlesTableController(this,prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          TitleDescription title=(TitleDescription)event.getSource();
          showTitle(title);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showTitle(TitleDescription title)
  {
    TitleUiUtils.showTitleWindow(this,title.getIdentifier());
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_titleWindows!=null)
    {
      _titleWindows.disposeAll();
      _titleWindows=null;
    }
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
