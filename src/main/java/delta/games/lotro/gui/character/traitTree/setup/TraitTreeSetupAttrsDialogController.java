package delta.games.lotro.gui.character.traitTree.setup;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.traitTree.setup.TraitTreeSetup;

/**
 * Controller for the dialog to edit the attributes of a trait tree setup.
 * @author DAM
 */
public class TraitTreeSetupAttrsDialogController extends DefaultFormDialogController<TraitTreeSetup>
{
  private static final int NAME_SIZE=32;
  private JTextField _setupName;
  private JTextArea _setupDescription;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param setup Setup to edit.
   */
  public TraitTreeSetupAttrsDialogController(WindowController parentController, TraitTreeSetup setup)
  {
    super(parentController,setup);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Edit trait tree setup..."); // I18n
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildAttributesPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Trait tree template"); // I18n
    dataPanel.setBorder(pathsBorder);
    return dataPanel;
  }

  private JPanel buildAttributesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Name
    _setupName=GuiFactory.buildTextField("");
    _setupName.setColumns(NAME_SIZE);
    // Description
    _setupDescription=GuiFactory.buildTextArea("",true);
    _setupDescription.setRows(5);

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Description:"),gbc); // I18n
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_setupName,gbc);
    gbc.gridy++;
    panel.add(_setupDescription,gbc);
    return panel;
  }

  @Override
  protected void okImpl()
  {
    String name=_setupName.getText();
    String description=_setupDescription.getText();
    _data.setName(name);
    _data.setDescription(description);
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
    String name=_setupName.getText();
    if ((name==null) || (name.trim().length()==0))
    {
      errorMsg="Invalid name!"; // I18n
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Trait tree setup edition"; // I18n
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
    _setupName=null;
    _setupDescription=null;
  }
}
