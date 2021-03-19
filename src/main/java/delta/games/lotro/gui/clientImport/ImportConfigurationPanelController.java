package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.extraction.ExtractableElement;

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
    _gadgets=new HashMap<ExtractableElement,CheckboxController>();
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
    GridBagConstraints cStrut=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createHorizontalStrut(150),cStrut);
    int row=1;
    for(ExtractableElement element : ExtractableElement.values())
    {
      CheckboxController checkbox=new CheckboxController(element.getLabel());
      _gadgets.put(element,checkbox);
      GridBagConstraints c=new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(checkbox.getCheckbox(),c);
      row++;
    }
    return ret;
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
