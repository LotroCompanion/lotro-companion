package delta.games.lotro.gui.clientImport;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.extraction.ExtractableElement;
import delta.games.lotro.memory.extraction.session.status.ExtractableElementStatus;
import delta.games.lotro.memory.extraction.session.status.ImportStatusData;

/**
 * Panel controller to show the results of extraction.
 * @author DAM
 */
public class ExtractionResultsPanelController implements Disposable
{
  private static final Color ORANGE=new Color(0xF7,0x82,0x00);
  private static final Color GREEN=new Color(0x5E,0xBD,0x3E);

  private JPanel _panel;
  private Map<ExtractableElement,JLabel> _gadgets;

  /**
   * Constructor.
   */
  public ExtractionResultsPanelController()
  {
    _gadgets=new EnumMap<ExtractableElement,JLabel>(ExtractableElement.class);
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
    int nbColumns=2;
    int nbItems=ExtractableElement.values().length;
    int nbItemsPerColumns=(nbItems/nbColumns)+((nbItems%nbColumns!=0)?1:0);
    int row=1;
    int column=0;
    for(ExtractableElement element : ExtractableElement.values())
    {
      // Element label
      JLabel elementLabel=GuiFactory.buildLabel(element.getLabel()+":");
      GridBagConstraints c=new GridBagConstraints(column*2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(elementLabel,c);
      // State label
      c.gridx++;
      JLabel stateLabel=GuiFactory.buildLabel("");
      _gadgets.put(element,stateLabel);
      ret.add(stateLabel,c);
      row++;
      if (row==nbItemsPerColumns+1)
      {
        row=1;
        column++;
      }
    }
    for(int i=0;i<nbColumns;i++)
    {
      GridBagConstraints cStrut=new GridBagConstraints((2*i)+1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      ret.add(Box.createHorizontalStrut(100),cStrut);
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
      JLabel label=_gadgets.get(element);
      label.setText(status.getLabel());
      Color color=getColorFromStatus(status);
      label.setForeground(color);
    }
  }

  private Color getColorFromStatus(ExtractableElementStatus status)
  {
    if (status==ExtractableElementStatus.FAILED)
    {
      return Color.RED;
    }
    if (status==ExtractableElementStatus.IGNORED)
    {
      return Color.GRAY;
    }
    if (status==ExtractableElementStatus.NOT_FOUND)
    {
      return ORANGE;
    }
    if (status==ExtractableElementStatus.IN_PROGRESS)
    {
      return Color.LIGHT_GRAY;
    }
    if (status==ExtractableElementStatus.DONE)
    {
      return GREEN;
    }
    return Color.BLACK;
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
