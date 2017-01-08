package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.utils.gui.text.DynamicTextEditionController;
import delta.games.lotro.utils.gui.text.TextListener;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController extends AbstractItemFilterPanelController
{
  // Data
  private ItemNameFilter _filter;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  // Controllers
  private DynamicTextEditionController _textController;

  /**
   * Constructor.
   * @param filter Item filter.
   */
  public ItemFilterController(ItemNameFilter filter)
  {
    _filter=filter;
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
      setFilter();
      updateFilter();
    }
    return _panel;
  }

  private void setFilter()
  {
    String contains=_filter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Label filter
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    containsPanel.add(GuiFactory.buildLabel("Label filter:"));
    _contains=GuiFactory.buildTextField("");
    _contains.setColumns(20);
    containsPanel.add(_contains);
    TextListener listener=new TextListener()
    {
      public void textChanged(String newText)
      {
        if (newText.length()==0) newText=null;
        _filter.setPattern(newText);
        updateFilter();
      }
    };
    _textController=new DynamicTextEditionController(_contains,listener);
    GridBagConstraints c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}
