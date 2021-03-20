package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.extraction.ExtractableElement;
import delta.games.lotro.memory.facade.data.ExtractableElementStatus;
import delta.games.lotro.memory.facade.data.ImportStatusData;

/**
 * Panel controller to show the results of extraction.
 * @author DAM
 */
public class ExtractionResultsPanelController implements Disposable
{
  private JPanel _panel;
  private Map<ExtractableElement,JLabel> _gadgets;

  /**
   * Constructor.
   */
  public ExtractionResultsPanelController()
  {
    _gadgets=new HashMap<ExtractableElement,JLabel>();
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
    ret.add(Box.createHorizontalStrut(100),cStrut);
    int row=1;
    for(ExtractableElement element : ExtractableElement.values())
    {
      // Element label
      JLabel elementLabel=GuiFactory.buildLabel(element.getLabel()+":");
      GridBagConstraints c=new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(elementLabel,c);
      // State label
      c.gridx++;
      JLabel stateLabel=GuiFactory.buildLabel("");
      _gadgets.put(element,stateLabel);
      ret.add(stateLabel,c);
      row++;
    }
    return ret;
  }

  /**
   * Update UI using the given data.
   * @param data Input data.
   */
  public void updateUi(ImportStatusData data)
  {
    for(ExtractableElement element : ExtractableElement.values())
    {
      ExtractableElementStatus status=data.getExtractableElementStatus(element);
      _gadgets.get(element).setText(status.getLabel());
    }
  }

  @Override
  public void dispose()
  {
    // UI
    if (_gadgets!=null)
    {
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
