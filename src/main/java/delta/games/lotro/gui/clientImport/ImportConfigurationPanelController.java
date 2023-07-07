package delta.games.lotro.gui.clientImport;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.extraction.ExtractableElement;
import delta.games.lotro.memory.facade.data.ImportConfiguration;

/**
 * Panel controller for the import configuration.
 * @author DAM
 */
public class ImportConfigurationPanelController implements Disposable
{
  private JPanel _panel;
  private Map<ExtractableElement,CheckboxController> _gadgets;

  /**
   * Constructor.
   */
  public ImportConfigurationPanelController()
  {
    _gadgets=new EnumMap<ExtractableElement,CheckboxController>(ExtractableElement.class);
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    JPanel checkboxesPanel=buildCheckboxesPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(checkboxesPanel,c);
    JPanel buttonsPanel=buildButtonsPanel();
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(buttonsPanel,c);
    return ret;
  }

  private JPanel buildCheckboxesPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int nbColumns=2;
    int nbItems=ExtractableElement.values().length;
    int nbItemsPerColumns=(nbItems/nbColumns)+((nbItems%nbColumns!=0)?1:0);
    int row=0;
    int column=0;
    for(ExtractableElement element : ExtractableElement.values())
    {
      CheckboxController checkbox=new CheckboxController(element.getLabel());
      checkbox.setSelected(true);
      _gadgets.put(element,checkbox);
      GridBagConstraints c=new GridBagConstraints(column,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(checkbox.getCheckbox(),c);
      row++;
      if (row==nbItemsPerColumns)
      {
        row=0;
        column++;
      }
    }
    return ret;
  }

  private JPanel buildButtonsPanel()
  {
    JButton checkAll=GuiFactory.buildButton("Check all"); // I18n
    ActionListener alCheckAll=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setAll(true);
      }
    };
    checkAll.addActionListener(alCheckAll);
    JButton uncheckAll=GuiFactory.buildButton("Uncheck all"); // I18n
    ActionListener alUncheckAll=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setAll(false);
      }
    };
    uncheckAll.addActionListener(alUncheckAll);
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(checkAll);
    ret.add(uncheckAll);
    return ret;
  }

  private void setAll(boolean selected)
  {
    for(CheckboxController ctrl : _gadgets.values())
    {
      ctrl.setSelected(selected);
    }
  }

  /**
   * Indicates if the given element is enabled or not.
   * @param element Targeted element.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isEnabled(ExtractableElement element)
  {
    CheckboxController checkbox=_gadgets.get(element);
    if (checkbox!=null)
    {
      return checkbox.isSelected();
    }
    return false;
  }

  /**
   * Get the edited configuration.
   * @return a configuration.
   */
  public ImportConfiguration getConfig()
  {
    ImportConfiguration config=new ImportConfiguration();
    for(ExtractableElement element : ExtractableElement.values())
    {
      boolean isEnabled=isEnabled(element);
      config.setEnabled(element,isEnabled);
    }
    return config;
  }

  /**
   * Set the UI state.
   * @param enabled Enabled/disabled.
   */
  public void setUiState(boolean enabled)
  {
    for(CheckboxController checkbox : _gadgets.values())
    {
      checkbox.setState(enabled);
    }
  }

  @Override
  public void dispose()
  {
    // UI
    if (_gadgets!=null)
    {
      for(CheckboxController checkbox : _gadgets.values())
      {
        checkbox.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
