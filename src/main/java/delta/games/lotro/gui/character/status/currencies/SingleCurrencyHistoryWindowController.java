package delta.games.lotro.gui.character.status.currencies;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.currencies.CurrenciesFacade;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.common.Scope;

/**
 * Controller for a "currency history" window.
 * @author DAM
 */
public class SingleCurrencyHistoryWindowController extends DefaultDialogController
{
  // Controllers
  private SingleCurrencyHistoryPanelController _panelController;
  private CurrencyChoicePanelController _currencyChoice;
  // UI
  private JPanel _chartHostPanel;

  /**
   * Build a window controller to show account/server currencies.
   * @param parent Parent window.
   * @param account Account.
   * @param serverName Server name.
   * @return A window controller.
   */
  public static SingleCurrencyHistoryWindowController buildAccountServerWindow(WindowController parent, Account account, String serverName)
  {
    CurrenciesFacade facade=new CurrenciesFacade(account,serverName);
    SingleCurrencyHistoryPanelController panel=new SingleCurrencyHistoryPanelController(facade);
    Set<Scope> scopes=new HashSet<Scope>();
    scopes.add(Scope.SERVER);
    scopes.add(Scope.ACCOUNT);
    SingleCurrencyHistoryWindowController ret=new SingleCurrencyHistoryWindowController(parent,panel,scopes);
    return ret;
  }

  /**
   * Build a window controller to show character currencies.
   * @param parent Parent window.
   * @param toon Character to use.
   * @return A window controller.
   */
  public static SingleCurrencyHistoryWindowController buildCharacterWindow(WindowController parent, CharacterFile toon)
  {
    CurrenciesFacade facade=new CurrenciesFacade(toon);
    SingleCurrencyHistoryPanelController panel=new SingleCurrencyHistoryPanelController(facade);
    Set<Scope> scopes=new HashSet<Scope>();
    scopes.add(Scope.CHARACTER);
    scopes.add(Scope.SERVER);
    scopes.add(Scope.ACCOUNT);
    SingleCurrencyHistoryWindowController ret=new SingleCurrencyHistoryWindowController(parent,panel,scopes);
    return ret;
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param panelController Currency history display panel.
   * @param scopes Scopes to use.
   */
  public SingleCurrencyHistoryWindowController(WindowController parent, SingleCurrencyHistoryPanelController panelController, Set<Scope> scopes)
  {
    super(parent);
    _panelController=panelController;
    _currencyChoice=new CurrencyChoicePanelController(scopes);
    _panelController.setCurve(_currencyChoice.getCurrencySelector().getSelectedItem());
  }

  @Override
  protected JComponent buildContents()
  {
    ItemSelectionListener<Currency> listener=new ItemSelectionListener<Currency>()
    {
      @Override
      public void itemSelected(Currency currency)
      {
        handleCurrencyChange(currency);
      }
    };
    _currencyChoice.getCurrencySelector().addListener(listener);
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_currencyChoice.getPanel(),c);
    _chartHostPanel=GuiFactory.buildPanel(new BorderLayout());
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(_chartHostPanel,c);
    return ret;
  }

  private void handleCurrencyChange(Currency currency)
  {
    System.out.println("Using currency: "+currency);
    _panelController.setCurve(currency);
    // Update contents
    _chartHostPanel.removeAll();
   _chartHostPanel.add(_panelController.getPanelController().getPanel(),BorderLayout.CENTER);
   _chartHostPanel.revalidate();
   _chartHostPanel.repaint();
  }

  /**
   * Get the window identifier for this window.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "SINGLE_CURRENCY";
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(500,380));
    dialog.pack();
    return dialog;
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
    if (_currencyChoice!=null)
    {
      _currencyChoice.dispose();
      _currencyChoice=null;
    }
  }
}
