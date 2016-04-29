package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.items.filters.ItemNameFilter;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController
{
  // Data
  private ItemNameFilter _filter;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  private ItemChoicePanelController _panelController;

  /**
   * Constructor.
   * @param filter Item filter.
   * @param panelController Associated log panel controller.
   */
  public ItemFilterController(ItemNameFilter filter, ItemChoicePanelController panelController)
  {
    _filter=filter;
    _panelController=panelController;
  }

  private void updateFilter()
  {
    _panelController.updateFilter();
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
    {
      containsPanel.add(GuiFactory.buildLabel("Label filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      DocumentListener dl=new DocumentListener()
      {
        public void removeUpdate(DocumentEvent e)
        {
          doIt();
        }
        
        public void insertUpdate(DocumentEvent e)
        {
          doIt();
        }
        
        public void changedUpdate(DocumentEvent e)
        {
          doIt();
        }

        private void doIt()
        {
          String text=_contains.getText();
          if (text.length()==0) text=null;
          _filter.setPattern(text);
          updateFilter();
        }
      };
      _contains.getDocument().addDocumentListener(dl);
    }

    GridBagConstraints c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    _panelController=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}
