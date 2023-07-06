package delta.games.lotro.gui.character.status.currencies;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.utils.charts.MultipleToonsDatedCurvesChartPanelController;

/**
 * Controller for a "currency history" window for several characters.
 * @author DAM
 */
public class MultipleCharactersCurrencyHistoryWindowController extends DefaultWindowController
{
  private static final String PREFERENCES_NAME="currencyHistory";
  private static final String TOON_NAMES_PREFERENCE="currencyHistory.registered.toon";

  private MultipleToonsDatedCurvesChartPanelController<CurrencyHistoryCurve> _panelController;
  private MultipleToonsCurrencyHistory _stats;
  private CurrencyChoicePanelController _currencyChoice;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public MultipleCharactersCurrencyHistoryWindowController(WindowController parent)
  {
    super(parent);
    _stats=new MultipleToonsCurrencyHistory();
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
    configuration.setChartTitle("");
    configuration.setValueAxisLabel("Amount"); // I18n
    configuration.setValueAxisTicks(new double[]{1,10,100,1000,10000,100000,1000000,10000000,100000000,1000000000});
    configuration.setUseSquareMoves(false);
    _panelController=new MultipleToonsDatedCurvesChartPanelController<CurrencyHistoryCurve>(this,_stats,provider,configuration);
    List<Currency> currencies=Currencies.getAvailableCurrencies(true,false,false);
    TypedProperties currencySelectionProps=preferences.getPreferences(CurrenciesPreferences.SELECTED_CURRENCIES_PROPERTY_NAME);
    CurrenciesPreferences.initCurrenciesPreferencesForCharacterOnly(currencySelectionProps);
    _currencyChoice=new CurrencyChoicePanelController(this,currencies,currencySelectionProps);
    Currency currency=_currencyChoice.getCurrencySelector().getSelectedItem();
    setCurrency(currency);
  }

  /**
   * Update the currency to display.
   * @param currency Currency to use.
   */
  public void setCurrency(Currency currency)
  {
    _stats.setCurrency(currency);
    _stats.refreshData();
    _panelController.getChartController().refresh();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Currencies chooser
    JPanel currenciesChooser=_currencyChoice.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(currenciesChooser,c);
    ItemSelectionListener<Currency> listener=new ItemSelectionListener<Currency>()
    {
      @Override
      public void itemSelected(Currency currency)
      {
        handleCurrencyChange(currency);
      }
    };
    _currencyChoice.getCurrencySelector().addListener(listener);
    // Chart and character selection
    JPanel mainPanel=_panelController.getPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(mainPanel,c);
    return ret;
  }

  private void handleCurrencyChange(Currency currency)
  {
    setCurrency(currency);
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
    String title="Currency history"; // I18n
    frame.setTitle(title);
    frame.pack();
    frame.setMinimumSize(new Dimension(500,380));
    frame.setSize(new Dimension(700,500));
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
    return getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
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
    if (_currencyChoice!=null)
    {
      _currencyChoice.dispose();
      _currencyChoice=null;
    }
  }
}
