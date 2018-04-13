package delta.games.lotro.gui.stats.deeds;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.utils.DateFormat;

/**
 * Controller for the "deed status edition" dialog.
 * @author DAM
 */
public class DeedStatusEditionDialogController extends DefaultFormDialogController<DeedStatus>
{
  // Data
  private DeedDescription _deed;
  // UI
  private JLabel _icon;
  private JLabel _name;
  // Controllers
  private CheckboxController _completed;
  private DateEditionController _completionDate;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   * @param deed Targeted deed.
   */
  public DeedStatusEditionDialogController(DeedDescription deed, DeedStatus status, WindowController parentController)
  {
    super(parentController,status);
    _deed=deed;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Deed status edition...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Deed status");
    panel.setBorder(pathsBorder);

    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Deed summary line
    {
      JPanel deedSummaryLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      DeedType type=_deed.getType();
      ImageIcon icon=LotroIconsManager.getDeedTypeIcon(type);
      _icon=GuiFactory.buildIconLabel(icon);
      deedSummaryLine.add(_icon);
      // Name
      String name=_deed.getName();
      _name=GuiFactory.buildLabel(name);
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      deedSummaryLine.add(_name);
      panel.add(deedSummaryLine,c);
      c.gridy++;
    }

    // Completed
    _completed=new CheckboxController("Completed");
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setCompleted(_completed.isSelected());
      }
    };
    _completed.getCheckbox().addActionListener(l);
    // Completion date
    DateCodec codec=DateFormat.getDateTimeCodec();
    _completionDate=new DateEditionController(codec);

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    gbc.gridx=0; gbc.gridy=1;
    panel.add(_completed.getCheckbox(),gbc);
    gbc.gridx=0; gbc.gridy=2;
    panel.add(GuiFactory.buildLabel("Completion date:"),gbc);
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    gbc.gridx=1;
    panel.add(_completionDate.getTextField(),gbc);

    // Set values
    boolean completed=_data.isCompleted()==Boolean.TRUE;
    _completed.setSelected(completed);
    setCompleted(completed);
    _completionDate.setDate(_data.getCompletionDate());
    return panel;
  }

  private void setCompleted(boolean completed)
  {
    _completionDate.setState(completed,completed);
  }

  @Override
  protected void okImpl()
  {
    // Completed?
    boolean completed=_completed.isSelected();
    _data.setCompleted(Boolean.valueOf(completed));
    // Completion date
    Long completionDate=_completionDate.getDate();
    _data.setCompletionDate(completionDate);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_completed!=null)
    {
      _completed.dispose();
      _completed=null;
    }
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
  }
}
