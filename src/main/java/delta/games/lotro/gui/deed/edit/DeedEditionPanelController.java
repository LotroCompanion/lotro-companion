package delta.games.lotro.gui.deed.edit;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.deed.DeedUiUtils;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;

/**
 * Controller for an deed edition panel.
 * @author DAM
 */
public class DeedEditionPanelController
{
  // Data
  private DeedDescription _item;
  // GUI
  private JPanel _panel;
  //private WindowController _parent;

  private JLabel _icon;
  private ComboBoxController<DeedType> _type;
  private ComboBoxController<String> _category;
  private JTextField _name;
  private ComboBoxController<Integer> _requiredLevel;
  private JTextArea _description;
  private JTextArea _objectives;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param deed Deed to edit.
   */
  public DeedEditionPanelController(WindowController parent, DeedDescription deed)
  {
    //_parent=parent;
    _item=deed;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Icon
      _icon=GuiFactory.buildIconLabel(null);
      panelLine.add(_icon);
      // Name
      _name=GuiFactory.buildTextField("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      _name.setColumns(25);
      panelLine.add(_name);
    }

    // Line 2
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Category
      _category=DeedUiUtils.buildCategoryCombo();
      panelLine.add(_category.getComboBox());
      // Type
      _type=DeedUiUtils.buildDeedTypeCombo();
      panelLine.add(_type.getComboBox());
      // Item level
      _requiredLevel=new ComboBoxController<Integer>(true,Integer.class);
      panelLine.add(GuiFactory.buildLabel("Required level:"));
      panelLine.add(_requiredLevel.getComboBox());
    }

    // Description
    _description=GuiFactory.buildTextArea("",false);
    JScrollPane descriptionPane=GuiFactory.buildScrollPane(_description);
    descriptionPane.setBorder(GuiFactory.buildTitledBorder("Description"));
    _description.setColumns(40);
    _description.setLineWrap(true);
    _description.setWrapStyleWord(true);
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(descriptionPane,c);
    c.gridy++;
    // Objectives
    _objectives=GuiFactory.buildTextArea("",false);
    JScrollPane objectivesPane=GuiFactory.buildScrollPane(_objectives);
    objectivesPane.setBorder(GuiFactory.buildTitledBorder("Objectives"));
    _objectives.setColumns(40);
    _objectives.setLineWrap(true);
    _objectives.setWrapStyleWord(true);
    panel.add(objectivesPane,c);
    c.gridy++;

    _panel=panel;
    setItem();
    return _panel;
  }

  /**
   * Set the deed to display.
   */
  private void setItem()
  {
    String name=_item.getName();
    // Name
    _name.setText(name);
    // Type
    _type.selectItem(_item.getType());
    // Icon
    ImageIcon icon=LotroIconsManager.getDeedTypeIcon(_item.getType());
    _icon.setIcon(icon);
    // Category
    _category.selectItem(_item.getCategory());
    // Required level
    _requiredLevel.selectItem(_item.getMinLevel());
    // Description
    _description.setText(_item.getDescription());
    // Objectives
    _objectives.setText(_item.getObjectives());
  }

  /**
   * Get the current value of the edited item.
   * @return An item.
   */
  public DeedDescription getItem()
  {
    // Name
    _item.setName(_name.getText());
    // Type
    _item.setType(_type.getSelectedItem());
    // Category
    _item.setCategory(_category.getSelectedItem());
    // Required level
    _item.setMinLevel(_requiredLevel.getSelectedItem());
    // Description
    _item.setDescription(_description.getText());
    // Objectives
    _item.setObjectives(_objectives.getText());
    return _item;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _item=null;
    // Controllers
    //_parent=null;
    if (_type!=null)
    {
      _type.dispose();
      _type=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_requiredLevel!=null)
    {
      _requiredLevel.dispose();
      _requiredLevel=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _description=null;
    _objectives=null;
  }
}
