package delta.games.lotro.gui.emotes.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.emotes.EmotesTableController;
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

  private EmotesExplorerPanelController _panelController;
  private EmotesTableController _tableController;
  //private RecipeFilter _filter;
  private WindowsManager _emoteWindows;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public EmotesExplorerWindowController(WindowController parent)
  {
    super(parent);
    //_filter=new RecipeFilter();
    _emoteWindows=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Emotes explorer");
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
    _panelController=new EmotesExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    //_filterController=new RecipeFilterController(_filter,_panelController);
    //JPanel filterPanel=_filterController.getPanel();
    //TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    //filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    //panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  private void initEmotesTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("EmotesExplorer");
    _tableController=new EmotesTableController(prefs,null/*_filter*/);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          EmoteDescription emote=(EmoteDescription)event.getSource();
          //showEmote(emote);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showEmote(EmoteDescription emote)
  {
  /*
    int id=_recipeWindows.getAll().size();
    RecipeDisplayWindowController window=new RecipeDisplayWindowController(this,id);
    window.setRecipe(recipe);
    window.show(false);
    _recipeWindows.registerWindow(window);
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
    if (_emoteWindows!=null)
    {
      _emoteWindows.disposeAll();
      _emoteWindows=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    /*
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    */
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
