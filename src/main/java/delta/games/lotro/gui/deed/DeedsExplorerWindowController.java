package delta.games.lotro.gui.deed;

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
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.deed.form.DeedDisplayWindowController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.deeds.DeedDescription;

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

  /**
   * Preference file for the columns of the item chooser.
   */
  public static final String ITEM_CHOOSER_PROPERTIES_ID="ItemChooserColumn";
  /**
   * Preference file for the columns of the essence chooser.
   */
  public static final String ESSENCE_CHOOSER_PROPERTIES_ID="EssenceChooserColumn";
  /**
   * Name of the property for column IDs.
   */
  public static final String COLUMNS_PROPERTY="columns";

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
    frame.setTitle("Deeds explorer");
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(1000,500);
    return frame;
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
    _filterController=new DeedFilterController(_filter,_panelController);
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

  private void initDeedsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("DeedsExplorer");
    _tableController=new DeedsTableController(prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (DeedsTableController.DOUBLE_CLICK.equals(action))
        {
          DeedDescription deed=(DeedDescription)event.getSource();
          DeedDisplayWindowController window=new DeedDisplayWindowController(DeedsExplorerWindowController.this);
          window.setDeed(deed);
          window.show(false);
        }
      }
    };
    _tableController.addActionListener(al);
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
