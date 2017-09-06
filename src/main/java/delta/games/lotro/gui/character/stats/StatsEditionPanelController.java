package delta.games.lotro.gui.character.stats;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.NumericTools;
import delta.common.utils.i18n.Translator;
import delta.common.utils.i18n.TranslatorsManager;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for a stats edition panel.
 * @author DAM
 */
public class StatsEditionPanelController
{
  private JPanel _panel;
  private List<SingleStatController> _statControllers;

  /**
   * Constructor.
   */
  public StatsEditionPanelController()
  {
    _statControllers=new ArrayList<SingleStatController>();
    addNewStatController();
    _panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _panel.setOpaque(true);
    updateUi();
  }

  /**
   * Initialize the managed panel with the given stats.
   * @param stats Stats to set.
   */
  public void initFromStats(BasicStatsSet stats)
  {
    _statControllers.clear();
    for(STAT stat : STAT.values())
    {
      FixedDecimalsInteger value=stats.getStat(stat);
      if (value!=null)
      {
        SingleStatController ctrl=new SingleStatController();
        ctrl.setStat(stat,value);
        _statControllers.add(ctrl);
      }
    }
    if (_statControllers.size()==0)
    {
      addNewStatController();
    }
    updateUi();
  }

  /**
   * Get the current stats.
   * @return the current stats.
   */
  public BasicStatsSet getStats()
  {
    BasicStatsSet stats=new BasicStatsSet();
    for(SingleStatController ctrl : _statControllers)
    {
      ComboBoxController<STAT> comboCtrl=ctrl.getStatComboController();
      STAT stat=comboCtrl.getSelectedItem();
      if (stat!=null)
      {
        String valueStr=ctrl.getValue().getText();
        Float value=NumericTools.parseFloat(valueStr,false);
        if (value!=null)
        {
          stats.setStat(stat,value.floatValue());
        }
      }
    }
    return stats;
  }

  private void addNewStatController()
  {
    // Add "new" stat
    SingleStatController ctrl=new SingleStatController();
    _statControllers.add(ctrl);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void updateUi()
  {
    _panel.removeAll();
    int index=0;
    for(SingleStatController ctrl : _statControllers)
    {
      buildStatUi(_panel,ctrl,index);
      index++;
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildStatUi(JPanel panel,SingleStatController ctrl,int index)
  {
    GridBagConstraints c=new GridBagConstraints(0,index,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Value editor
    JTextField value=ctrl.getValue();
    panel.add(value,c);
    // Stat chooser
    c.gridx++;
    panel.add(ctrl.getStatComboController().getComboBox(),c);
    // Stat unit label
    JLabel unit=ctrl.getUnit();
    c.gridx++;
    panel.add(unit,c);
    // Delete button
    JButton deleteButton=ctrl.getDeleteButton();
    c.gridx++;
    panel.add(deleteButton,c);
    // Add button
    JButton addButton=ctrl.getAddButton();
    c.gridx++;
    panel.add(addButton,c);
  }

  private void handleButtonClick(JButton button, SingleStatController ctrl)
  {
    if (button==ctrl.getAddButton())
    {
      SingleStatController newCtrl=new SingleStatController();
      int index=_statControllers.indexOf(ctrl);
      _statControllers.add(index+1,newCtrl);
      updateUi();
    }
    else if (button==ctrl.getDeleteButton())
    {
      _statControllers.remove(ctrl);
      if (_statControllers.size()==0)
      {
        addNewStatController();
      }
      updateUi();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_statControllers!=null)
    {
      for(SingleStatController controller : _statControllers)
      {
        controller.dispose();
      }
      _statControllers=null;
    }
  }

  /**
   * Controller for the UI items of a single stat.
   * @author DAM
   */
  private class SingleStatController
  {
    private JTextField _value;
    private ComboBoxController<STAT> _statChooser;
    private JLabel _unit;
    private JButton _deleteButton;
    private JButton _addButton;

    public SingleStatController()
    {
      _value=GuiFactory.buildTextField("");
      _value.setColumns(6);
      _statChooser=buildStatChooser();
      _unit=GuiFactory.buildLabel("");
      // Delete button
      ImageIcon icon=IconsManager.getIcon("/resources/gui/icons/cross.png");
      _deleteButton=GuiFactory.buildButton("");
      _deleteButton.setIcon(icon);
      _deleteButton.setMargin(new Insets(1,1,1,1));
      _deleteButton.setContentAreaFilled(false);
      _deleteButton.setBorderPainted(false);
      ActionListener listener=new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          handleButtonClick((JButton)e.getSource(),SingleStatController.this);
        }
      };
      _deleteButton.addActionListener(listener);
      // Add button
      ImageIcon addIcon=IconsManager.getIcon("/resources/gui/icons/plus.png");
      _addButton=GuiFactory.buildButton("");
      _addButton.setIcon(addIcon);
      _addButton.setMargin(new Insets(1,1,1,1));
      _addButton.setContentAreaFilled(false);
      _addButton.setBorderPainted(false);
      _addButton.addActionListener(listener);
    }

    private ComboBoxController<STAT> buildStatChooser()
    {
      ComboBoxController<STAT> controller=new ComboBoxController<STAT>();
      String name=CharacterStatsPanelController.class.getPackage().getName()+".statLabels";
      TranslatorsManager translatorsMgr=TranslatorsManager.getInstance();
      Translator translator=translatorsMgr.getTranslatorByName(name,true,false);
      controller.addEmptyItem("");
      for(STAT stat : STAT.values())
      {
        String label=translator.translate(stat.getKey());
        controller.addItem(stat,label);
      }
      controller.selectItem(null);
      return controller;
    }

    public void setStat(STAT stat, FixedDecimalsInteger value)
    {
      _statChooser.selectItem(stat);
      _value.setText(value.toString());
      boolean isPercentage=stat.isPercentage();
      String label=isPercentage?"%":" ";
      _unit.setText(label);
    }

    /**
     * Get the managed text field.
     * @return the managed text field.
     */
    public JTextField getValue()
    {
      return _value;
    }

    public ComboBoxController<STAT> getStatComboController()
    {
      return _statChooser;
    }

    /**
     * Get the label for the stat unit.
     * @return a label.
     */
    public JLabel getUnit()
    {
      return _unit;
    }

    /**
     * Get the delete button associated with this stat.
     * @return a button.
     */
    public JButton getDeleteButton()
    {
      return _deleteButton;
    }

    /**
     * Get the add button associated with this stat.
     * @return a button.
     */
    public JButton getAddButton()
    {
      return _addButton;
    }

    /**
     * Release all managed resources.
     */
    public void dispose()
    {
      _value=null;
      if (_statChooser!=null)
      {
        _statChooser.dispose();
        _statChooser=null;
      }
      _unit=null;
      _deleteButton=null;
      _addButton=null;
    }
  }
}
