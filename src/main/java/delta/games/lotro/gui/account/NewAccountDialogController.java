package delta.games.lotro.gui.account;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountReference;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for the "new account" dialog.
 * @author DAM
 */
public class NewAccountDialogController extends DefaultFormDialogController<Object>
{
  private static final int ACCOUNT_NAME_SIZE=32;
  private JTextField _accountName;
  private JTextField _subscriptionKey;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public NewAccountDialogController(WindowController parentController)
  {
    super(parentController,null);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    String title=Labels.getLabel("account.new.window.title");
    dialog.setTitle(title);
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildNewAccountPanel();
    String borderTitle=Labels.getLabel("account.new.border.title");
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder(borderTitle);
    dataPanel.setBorder(pathsBorder);
    return dataPanel;
  }

  private JPanel buildNewAccountPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Account name
    _accountName=GuiFactory.buildTextField("");
    _accountName.setColumns(ACCOUNT_NAME_SIZE);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    String nameField=Labels.getFieldLabel("account.new.field.name");
    panel.add(GuiFactory.buildLabel(nameField),gbc);
    gbc=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_accountName,gbc);
    // Subscription key
    _subscriptionKey=GuiFactory.buildTextField("");
    _subscriptionKey.setColumns(ACCOUNT_NAME_SIZE);
    gbc=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    String subscriptionField=Labels.getFieldLabel("account.new.field.subcription");
    panel.add(GuiFactory.buildLabel(subscriptionField),gbc);
    gbc=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_subscriptionKey,gbc);

    return panel;
  }

  @Override
  protected void okImpl()
  {
    String accountName=_accountName.getText();
    String subscriptionKey=_subscriptionKey.getText();
    AccountsManager manager=AccountsManager.getInstance();
    AccountReference accountID=new AccountReference(accountName,subscriptionKey);
    Account account=manager.addAccount(accountID);
    if (account==null)
    {
      String message=Labels.getLabel("account.new.creation.error.message");
      showErrorMessage(message);
    }
  }

  @Override
  protected boolean checkInput()
  {
    String errorMsg=checkData();
    if (errorMsg==null)
    {
      return true;
    }
    showErrorMessage(errorMsg);
    return false;
  }

  private String checkData()
  {
    String errorMsg=null;
    String accountName=_accountName.getText();
    if ((accountName==null) || (accountName.trim().length()==0))
    {
      errorMsg=Labels.getLabel("account.new.validation.error.invalidAccountName");
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title=Labels.getLabel("account.new.creation.error.title");
    JDialog dialog=getDialog();
    GuiFactory.showErrorDialog(dialog,errorMsg,title);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _accountName=null;
    _subscriptionKey=null;
  }
}
