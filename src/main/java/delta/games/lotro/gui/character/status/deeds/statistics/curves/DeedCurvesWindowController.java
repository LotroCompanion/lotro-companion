package delta.games.lotro.gui.character.status.deeds.statistics.curves;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.utils.CharacterUtils;
import delta.games.lotro.character.utils.MultipleToonsStats;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.stats.deeds.statistics.curves.DeedCurvesBuilder;
import delta.games.lotro.utils.charts.MultipleToonsDatedCurvesChartPanelController;

/**
 * Controller for a "deed curves" window.
 * @author DAM
 */
public class DeedCurvesWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String WINDOW_IDENTIFIER="DEED_CURVES";
  private static final String DEEDS_PREFERENCES_NAME="deeds";
  private static final String REGISTERED_TOONS_PREFERENCE_NAME="registered.toon";

  private MultipleToonsDatedCurvesChartPanelController<CharacterFile> _chartPanelController;
  private MultipleToonsStats<CharacterFile> _stats;

  /**
   * Constructor.
   */
  public DeedCurvesWindowController()
  {
    _stats=new MultipleToonsStats<CharacterFile>()
    {
      @Override
      public CharacterFile loadToonStats(CharacterFile toon)
      {
        return toon;
      }
    };
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(DEEDS_PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(REGISTERED_TOONS_PREFERENCE_NAME);
    List<CharacterFile> toons=CharacterUtils.getToons(toonIds);
    for(CharacterFile toon : toons)
    {
      _stats.addToon(toon);
    }
    DeedCurvesBuilder provider=new DeedCurvesBuilder();
    DatedCurvesChartConfiguration configuration=new DatedCurvesChartConfiguration();
    configuration.setChartTitle("LOTRO points acquisition by deeds"); // I18n
    configuration.setValueAxisLabel("LOTRO points"); // I18n
    configuration .setValueAxisTicks(new double[]{10,100,1000});
    _chartPanelController=new MultipleToonsDatedCurvesChartPanelController<CharacterFile>(this,_stats,provider,configuration);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_chartPanelController.getPanel();
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    frame.setTitle("Deed curves"); // I18n
    frame.pack();
    frame.setMinimumSize(new Dimension(500,380));
    frame.setSize(new Dimension(700,500));
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return WINDOW_IDENTIFIER;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    List<CharacterFile> toons=_stats.getToonsList();
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(DEEDS_PREFERENCES_NAME);
    List<String> toonIds=new ArrayList<String>();
    for(CharacterFile toon : toons)
    {
      toonIds.add(toon.getIdentifier());
    }
    props.setStringList(REGISTERED_TOONS_PREFERENCE_NAME,toonIds);
    _stats=null;
    if (_chartPanelController!=null)
    {
      _chartPanelController.dispose();
      _chartPanelController=null;
    }
  }
}
