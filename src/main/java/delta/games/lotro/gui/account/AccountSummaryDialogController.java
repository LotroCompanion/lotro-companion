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
import delta.games.lotro.account.AccountSummary;

/**
 * Controller for the "account summary" dialog.
 * @author DAM
 */
public class AccountSummaryDialogController extends DefaultFormDialogController<AccountSummary>
{
  private static final int ACCOUNT_NAME_SIZE=32;
  // UI
  private JTextField _accountName;

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
    dialog.setTitle("Edit account summary...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Account summary");
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

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc);
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_accountName,gbc);
    return panel;
  }

  private void initData()
  {
    // Account name
    String name=_data.getName();
    _accountName.setText(name);
  }

  @Override
  protected void okImpl()
  {
    String accountName=_accountName.getText();
    _data.setName(accountName);
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
