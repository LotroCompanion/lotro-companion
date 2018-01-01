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
import delta.games.lotro.account.AccountsManager;

/**
 * Controller for the "new account" dialog.
 * @author DAM
 */
public class NewAccountDialogController extends DefaultFormDialogController<Object>
{
  private static final int ACCOUNT_NAME_SIZE=32;
  private JTextField _accountName;

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
    dialog.setTitle("New account...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildNewAccountPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Account");
    dataPanel.setBorder(pathsBorder);
    return dataPanel;
  }

  private JPanel buildNewAccountPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Account name
    _accountName=GuiFactory.buildTextField("");
    _accountName.setColumns(ACCOUNT_NAME_SIZE);

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc);
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_accountName,gbc);
    return panel;
  }

  @Override
  protected void okImpl()
  {
    String accountName=_accountName.getText();
    AccountsManager manager=AccountsManager.getInstance();
    Account account=manager.addAccount(accountName);
    if (account==null)
    {
      showErrorMessage("Account creation failed!");
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
      errorMsg="Invalid account name!";
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Account creation";
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
  }
}
