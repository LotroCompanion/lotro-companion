package delta.games.lotro.gui.items;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a "character log" window.
 * @author DAM
 */
public class ItemChoiceWindowController extends DefaultDialogController
{
  private ItemFilterController _filterController;
  private ItemChoicePanelController _panelController;
  private ItemChoiceTableController _tableController;
  private List<Item> _items;
  private ItemNameFilter _filter;
  private Item _selectedItem;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param items Items to choose from.
   */
  public ItemChoiceWindowController(WindowController parent, List<Item> items)
  {
    super(parent);
    _items=items;
    _filter=new ItemNameFilter();
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="LOG#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    String title="Choose item:";
    dialog.setTitle(title);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setMinimumSize(new Dimension(400,300));
    return dialog;
  }
  

  @Override
  public String getWindowIdentifier()
  {
    return "ItemChooser";
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel logPanel=GuiFactory.buildPanel(new GridBagLayout());
    _tableController=new ItemChoiceTableController(_items,_filter);
    // Log frame
    _panelController=new ItemChoicePanelController(_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Control buttons
    JPanel controlPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
    JButton okButton=GuiFactory.buildButton("OK");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        _selectedItem=_tableController.getSelectedItem();
        hide();
      }
    };
    okButton.addActionListener(al);
    controlPanel.add(okButton);
    // Filter
    _filterController=new ItemFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    logPanel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    logPanel.add(tablePanel,c);
    GridBagConstraints cControl=new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,0,3,3),0,0);
    logPanel.add(controlPanel,cControl);
    return logPanel;
  }

  /**
   * Get the selected item.
   * @return the selected item.
   */
  public Item getSelectedItem()
  {
    return _selectedItem;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // TODO dispose on panel and filter
    _items=null;
  }
}
