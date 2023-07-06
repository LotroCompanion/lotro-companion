package delta.games.lotro.gui.character.status.titles;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.titles.TitleStatus;
import delta.games.lotro.character.status.titles.TitlesStatusManager;
import delta.games.lotro.character.status.titles.filter.TitleStatusFilter;
import delta.games.lotro.gui.character.status.titles.filter.TitleStatusFilterController;
import delta.games.lotro.gui.character.status.titles.table.TitlesStatusTableController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.titles.TitleFilterController;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;

/**
 * Controller for a titles status display window.
 * @author DAM
 */
public class TitlesStatusWindowController extends DefaultDisplayDialogController<TitlesStatusManager> implements FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="TITLES_STATUS";

  private static final int MAX_HEIGHT=900;

  // Data
  private List<TitleDescription> _titles;
  private TitleStatusFilter _filter;
  // Controllers
  private TitleStatusFilterController _statusFilterController;
  private TitleFilterController _filterController;
  private TitlesStatusPanelController _panelController;
  private TitlesStatusTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public TitlesStatusWindowController(WindowController parent, TitlesStatusManager status)
  {
    super(parent,status);
    _titles=TitlesManager.getInstance().getAll();
    _filter=new TitleStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setTitle("Titles status"); // I18n
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new TitlesStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Titles filter
    _filterController=new TitleFilterController(_filter.getTitleFilter(),this);
    JPanel titleFilterPanel=_filterController.getPanel();
    TitledBorder titleFilterBorder=GuiFactory.buildTitledBorder("Title Filter"); // I18n
    titleFilterPanel.setBorder(titleFilterBorder);
    // Title status filter
    _statusFilterController=new TitleStatusFilterController(_filter,this);
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    TitledBorder statusFilterBorder=GuiFactory.buildTitledBorder("Status Filter"); // I18n
    statusFilterPanel.setBorder(statusFilterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(titleFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusFilterPanel,c);
    c=new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,2,3,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("TitlesStatus");
    _tableController=new TitlesStatusTableController(_data,this,prefs,_filter,_titles,this);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          TitleStatus status=(TitleStatus)event.getSource();
          showTitleStatus(status);
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
  }

  private void showTitleStatus(TitleStatus status)
  {
    TitleDescription title=status.getTitle();
    TitleUiUtils.showTitleWindow(this,title.getIdentifier());
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _titles=null;
    _filter=null;
    // Controllers
    if (_statusFilterController!=null)
    {
      _statusFilterController.dispose();
      _statusFilterController=null;
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
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
  }
}
