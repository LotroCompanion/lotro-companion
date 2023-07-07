package delta.games.lotro.gui.lore.agents.mobs.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.agents.mobs.MobsFilterController;
import delta.games.lotro.gui.lore.agents.mobs.MobsTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.agents.mobs.MobDescription;
import delta.games.lotro.lore.agents.mobs.MobsManager;
import delta.games.lotro.lore.agents.mobs.filter.MobFilter;

/**
 * Controller for the mobs explorer window.
 * @author DAM
 */
public class MobsExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="MOBS_EXPLORER";

  private MobsFilterController _filterController;
  private MobsExplorerPanelController _panelController;
  private MobsTableController _tableController;
  private MobFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public MobsExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new MobFilter();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Mobs explorer"); // 18n
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
    List<MobDescription> mobs=MobsManager.getInstance().getMobs();
    // Table
    initTable(mobs);
    _panelController=new MobsExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new MobsFilterController(_filter,mobs,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // 18n
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

  private void initTable(List<MobDescription> mobs)
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("MobsExplorer");
    _tableController=new MobsTableController(prefs,_filter,mobs);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          MobDescription mob=(MobDescription)event.getSource();
          showMob(mob);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showMob(MobDescription mob)
  {
    /*
    String id=TitleDisplayWindowController.getId(title);
    WindowController window=_titleWindows.getWindow(id);
    if (window==null)
    {
      window=new TitleDisplayWindowController(this,title);
      _titleWindows.registerWindow(window);
    }
    window.show();
    */
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
