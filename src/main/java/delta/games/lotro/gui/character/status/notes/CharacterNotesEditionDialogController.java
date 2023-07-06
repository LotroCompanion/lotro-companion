package delta.games.lotro.gui.character.status.notes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.notes.CharacterNotes;

/**
 * Controller for the dialog to edit the character notes.
 * @author DAM
 */
public class CharacterNotesEditionDialogController extends DefaultFormDialogController<CharacterNotes>
{
  private JTextArea _notes;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param notes Notes to edit.
   */
  public CharacterNotesEditionDialogController(WindowController parentController, CharacterNotes notes)
  {
    super(parentController,notes);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Edit character notes..."); // I18n
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Notes
    _notes=GuiFactory.buildTextArea("",true);
    _notes.setRows(20);
    _notes.setColumns(50);
    JScrollPane scroll=GuiFactory.buildScrollPane(_notes);

    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Notes:"),gbc); // I18n
    gbc=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,5,5,5),0,0);
    panel.add(scroll,gbc);

    fillData();

    return panel;
  }

  private void fillData()
  {
    // Notes
    _notes.setText(_data.getText());
  }

  @Override
  protected void okImpl()
  {
    String notes=_notes.getText();
    _data.setText(notes);
  }

  @Override
  protected boolean checkInput()
  {
    return true;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _notes=null;
  }
}
