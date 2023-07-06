package delta.games.lotro.gui.character.status.currencies;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.storage.currencies.CurrenciesFacade;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyHistory;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartController;
import delta.games.lotro.gui.character.status.curves.DatedCurvesProvider;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Controller for a "currency history" chart panel.
 * @author DAM
 */
public class SingleCharacterCurrencyHistoryPanelController
{
  // Data
  private CurrenciesFacade _facade;
  // Controllers
  private DatedCurvesChartController _panelController;

  /**
   * Constructor.
   * @param facade Facade for data access.
   */
  public SingleCharacterCurrencyHistoryPanelController(CurrenciesFacade facade)
  {
    _facade=facade;
    _panelController=null;
  }

  /**
   * Set the curve to show.
   * @param currency Currency to show.
   */
  public void setCurve(Currency currency)
  {
    // Cleanup
    dispose();
    // Gather data
    CurrencyHistory history=_facade.getCurrencyHistory(currency);
    String context=_facade.getContext();
    // Setup curve
    initCurve(context,history);
  }

  private void initCurve(String curveName, CurrencyHistory history)
  {
    final CurrencyHistoryCurve historyCurve=new CurrencyHistoryCurve(curveName,history);
    final CurrencyHistoryCurveProvider curveProvider=new CurrencyHistoryCurveProvider();
    DatedCurvesProvider provider=new DatedCurvesProvider()
    {
      /**
       * Get the identifiers for the curves to show.
       * @return a list of curve identifiers.
       */
      public List<String> getCurveIds()
      {
        List<String> id=new ArrayList<String>();
        id.add("1");
        return id;
      }

      /**
       * Get a curve using its identifier.
       * @param curveId Curve identifier.
       * @return A curve or <code>null</code> if not found.
       */
      public DatedCurve<?> getCurve(String curveId)
      {
        DatedCurve<?> curve=curveProvider.getCurve(historyCurve);
        return curve;
      }
    };
    DatedCurvesChartConfiguration configuration=new DatedCurvesChartConfiguration();
    Currency currency=history.getCurrency();
    configuration.setChartTitle(currency.getName());
    configuration.setValueAxisLabel("Amount"); // I18n
    configuration.setValueAxisTicks(new double[]{1,10,100,1000,10000,100000,1000000,10000000,100000000,1000000000});
    configuration.setUseSquareMoves(false);
    _panelController=new DatedCurvesChartController(provider,configuration);
  }

  /**
   * Get the managed panel controller.
   * @return A panel controller.
   */
  public DatedCurvesChartController getPanelController()
  {
    return _panelController;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
