package delta.games.lotro.gui.character.status.currencies;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.CurrenciesManager;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyHistory;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartController;
import delta.games.lotro.gui.character.status.curves.DatedCurvesProvider;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Controller for a "currency history" window.
 * @author DAM
 */
public class AccountServerCurrencyHistoryWindowController extends DefaultWindowController
{
  private DatedCurvesChartController _panelController;
  private Currency _currency=null;

  /**
   * Constructor.
   * @param account Account.
   * @param serverName Server name.
   * @param currencyID Current ID.
   */
  public AccountServerCurrencyHistoryWindowController(Account account, String serverName,String currencyID)
  {
    _currency=Currencies.get().getByKey(currencyID);
    CurrenciesManager mgr=new CurrenciesManager(account,serverName);
    CurrencyHistory history=mgr.getHistory(_currency);
    String curveName=account.getName()+"@"+serverName;
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
    configuration.setChartTitle(_currency.getName());
    configuration.setValueAxisLabel("Amount");
    configuration.setValueAxisTicks(new double[]{1,10,100,1000,10000,100000});
    _panelController=new DatedCurvesChartController(provider,configuration);
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
    return "LEVELLING";
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String title=_currency.getName();
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
    String[] IDs=new String[] {"1879224343","1879224344", "1879224345"};
    Account account=AccountsManager.getInstance().getAccountByName("glorfindel666");
    String serverName="Landroval";
    for(String ID : IDs)
    {
      AccountServerCurrencyHistoryWindowController controller=new AccountServerCurrencyHistoryWindowController(account,serverName,ID);
      controller.show();
    }
  }
}
