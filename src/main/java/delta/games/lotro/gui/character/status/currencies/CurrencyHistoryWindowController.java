package delta.games.lotro.gui.character.status.currencies;

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
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyKeys;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.utils.charts.MultipleToonsDatedCurvesChartPanelController;

/**
 * Controller for a "currency history" window.
 * @author DAM
 */
public class CurrencyHistoryWindowController extends DefaultWindowController
{
  private static final String PREFERENCES_NAME="currencyHistory";
  private static final String TOON_NAMES_PREFERENCE="currencyHistory.registered.toon";

  private MultipleToonsDatedCurvesChartPanelController<CurrencyHistoryCurve> _panelController;
  private MultipleToonsCurrencyHistory _stats;
  private Currency _currency=Currencies.get().getByKey(CurrencyKeys.GOLD);

  /**
   * Constructor.
   */
  public CurrencyHistoryWindowController()
  {
    _stats=new MultipleToonsCurrencyHistory(Currencies.get().getByKey(_currency.getKey()));
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(TOON_NAMES_PREFERENCE);
    CharactersManager manager=CharactersManager.getInstance();
    if ((toonIds!=null) && (toonIds.size()>0))
    {
      for(String toonID : toonIds)
      {
        CharacterFile toon=manager.getToonById(toonID);
        if (toon!=null)
        {
          _stats.addToon(toon);
        }
      }
    }
    CurrencyHistoryCurveProvider provider=new CurrencyHistoryCurveProvider();
    DatedCurvesChartConfiguration configuration=new DatedCurvesChartConfiguration();
    configuration.setChartTitle(_currency.getName());
    configuration.setValueAxisLabel("Amount");
    configuration.setValueAxisTicks(new double[]{1,10,100,1000});
    _panelController=new MultipleToonsDatedCurvesChartPanelController<CurrencyHistoryCurve>(this,_stats,provider,configuration);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_panelController.getPanel();
    return panel;
  }

  /**
   * Get the window identifier for this window.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "CURRENCY_HISTORY";
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String title="Currency history";
    frame.setTitle(title);
    frame.pack();
    frame.setMinimumSize(new Dimension(500,380));
    frame.setSize(new Dimension(700,500));
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
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
    TypedProperties props=preferences.getPreferences(PREFERENCES_NAME);
    List<String> toonIds=new ArrayList<String>();
    for(CharacterFile toon : toons)
    {
      toonIds.add(toon.getIdentifier());
    }
    props.setStringList(TOON_NAMES_PREFERENCE,toonIds);
    _stats=null;
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }

  /**
   * Main method to test this window.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CurrencyHistoryWindowController controller=new CurrencyHistoryWindowController();
    controller.show();
  }
}
