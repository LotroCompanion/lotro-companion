package delta.games.lotro.gui.lore.emotes.explorer;

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
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.emotes.EmoteFilter;
import delta.games.lotro.gui.lore.emotes.EmoteFilterConfiguration;
import delta.games.lotro.gui.lore.emotes.EmoteFilterController;
import delta.games.lotro.gui.lore.emotes.EmoteUiUtils;
import delta.games.lotro.gui.lore.emotes.EmotesTableBuilder;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Controller for the emotes explorer window.
 * @author DAM
 */
public class EmotesExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="EMOTES_EXPLORER";

  private EmoteFilterController _filterController;
  private GenericTablePanelController<EmoteDescription> _panelController;
  private GenericTableController<EmoteDescription> _tableController;
  private EmoteFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public EmotesExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new EmoteFilter();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Emotes explorer"); // I18n
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
    initEmotesTable();
    // Table panel
    _panelController=new GenericTablePanelController<EmoteDescription>(this,_tableController);
    _panelController.getConfiguration().setBorderTitle("Emotes"); // I18n
    JPanel tablePanel=_panelController.getPanel();
    // Filter UI
    EmoteFilterConfiguration config=new EmoteFilterConfiguration();
    _filterController=new EmoteFilterController(_filter,config,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
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

  private void initEmotesTable()
  {
    _tableController=EmotesTableBuilder.buildTable();
    // - filter
    _tableController.setFilter(_filter);
    // - preferences
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("EmotesExplorer");
    _tableController.getPreferencesManager().setPreferences(prefs);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          EmoteDescription emote=(EmoteDescription)event.getSource();
          EmoteUiUtils.showEmoteWindow(EmotesExplorerWindowController.this,emote.getIdentifier());
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
