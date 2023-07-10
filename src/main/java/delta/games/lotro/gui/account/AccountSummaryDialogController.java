package delta.games.lotro.gui.account;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.AccountSummary;
import delta.games.lotro.account.AccountType;
import delta.games.lotro.gui.utils.l10n.DateFormat;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for the "account summary" dialog.
 * @author DAM
 */
public class AccountSummaryDialogController extends DefaultFormDialogController<AccountSummary>
{
  private static final int ACCOUNT_NAME_SIZE=16;
  private static final int SUBSCRIPTION_KEY_SIZE=32;
  private static final int USER_DATA_SIZE=32;
  // UI
  private JTextField _accountName;
  private JTextField _subscription;
  private DateEditionController _signupDate;
  private ComboBoxController<AccountType> _accountType;
  private JTextField _userData;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param summary Data to edit.
   */
  public AccountSummaryDialogController(WindowController parentController, AccountSummary summary)
  {
    super(parentController,summary);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle(Labels.getLabel("account.summary.edition.title"));
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildPanel();
    String borderTitle=Labels.getLabel("account.summary.edition.border");
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder(borderTitle);
    dataPanel.setBorder(pathsBorder);
    initData();
    return dataPanel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Account name
    _accountName=GuiFactory.buildTextField("");
    _accountName.setColumns(ACCOUNT_NAME_SIZE);
    _accountName.setEditable(false);
    // Subscription key
    _subscription=GuiFactory.buildTextField("");
    _subscription.setColumns(SUBSCRIPTION_KEY_SIZE);
    _subscription.setEditable(false);
    // Sign-up date
    _signupDate=new DateEditionController(DateFormat.getDateCodec());
    // Account type
    _accountType=buildAccountTypeCombo();
    // User data
    _userData=GuiFactory.buildTextField("");
    _userData.setColumns(USER_DATA_SIZE);

    Insets insets=new Insets(5,5,5,5);
    // Labels
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel(Labels.getFieldLabel("account.summary.edition.field.name")),gbc);
    gbc.gridy++;
    panel.add(GuiFactory.buildLabel(Labels.getFieldLabel("account.summary.edition.field.subscription")),gbc);
    gbc.gridy++;
    panel.add(GuiFactory.buildLabel(Labels.getFieldLabel("account.summary.edition.field.signupType")),gbc);
    gbc.gridy++;
    panel.add(GuiFactory.buildLabel(Labels.getFieldLabel("account.summary.edition.field.type")),gbc);
    gbc.gridy++;
    panel.add(GuiFactory.buildLabel(Labels.getFieldLabel("account.summary.edition.field.comments")),gbc);
    gbc.gridy++;
    // Editors
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_accountName,gbc);
    gbc.gridy++;
    panel.add(_subscription,gbc);
    gbc.gridy++;
    panel.add(_signupDate.getTextField(),gbc);
    gbc.gridy++;
    panel.add(_accountType.getComboBox(),gbc);
    gbc.gridy++;
    panel.add(_userData,gbc);
    gbc.gridy++;
    return panel;
  }

  /**
   * Build a controller for a combo box to choose an account type.
   * @return A new controller.
   */
  private ComboBoxController<AccountType> buildAccountTypeCombo()
  {
    ComboBoxController<AccountType> ctrl=new ComboBoxController<AccountType>();
    ctrl.addEmptyItem("");
    for(AccountType accountType : AccountType.values())
    {
      ctrl.addItem(accountType,accountType.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private void initData()
  {
    // Account name
    String accountName=_data.getName();
    _accountName.setText(accountName);
    // Subscription key
    String subscriptionKey=_data.getSubscriptionKey();
    _subscription.setText(subscriptionKey);
    // Signup date
    Long signupDate=_data.getSignupDate();
    _signupDate.setDate(signupDate);
    // Account type
    AccountType accountType=_data.getAccountType();
    _accountType.selectItem(accountType);
    // User data
    String userData=_data.getUserData();
    _userData.setText(userData);
  }

  @Override
  protected void okImpl()
  {
    // Signup date
    Long signupDate=_signupDate.getDate();
    _data.setSignupDate(signupDate);
    // Account type
    AccountType accountType=_accountType.getSelectedItem();
    _data.setAccountType(accountType);
    // User data
    String userData=_userData.getText();
    _data.setUserData(userData);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _accountName=null;
    if (_signupDate!=null)
    {
      _signupDate.dispose();
      _signupDate=null;
    }
    if (_accountType!=null)
    {
      _accountType.dispose();
      _accountType=null;
    }
    _userData=null;
  }
}
