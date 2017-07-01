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

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for an item choice window.
 * @author DAM
 */
public class ItemChoiceWindowController extends DefaultDialogController
{
  private AbstractItemFilterPanelController _filterController;
  private ItemChoicePanelController _panelController;
  private ItemChoiceTableController _tableController;
  private List<Item> _items;
  private Filter<Item> _filter;
  private Item _selectedItem;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param items Items to choose from.
   * @param filter Filter to use.
   * @param ui Filter UI controller.
   */
  public ItemChoiceWindowController(WindowController parent, List<Item> items, Filter<Item> filter, AbstractItemFilterPanelController ui)
  {
    super(parent);
    _items=items;
    _filter=filter;
    _filterController=ui;
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
    dialog.setSize(1000,dialog.getHeight());
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    _tableController=new ItemChoiceTableController(_items,_filter);
    // Table
    _panelController=new ItemChoicePanelController(_tableController);
    _filterController.setFilterUpdateListener(_panelController);
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
    GenericTableController<Item> controller=_tableController.getTableController();
    controller.addActionListener(al);
    controlPanel.add(okButton);
    // Filter
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    GridBagConstraints cControl=new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,0,3,3),0,0);
    panel.add(controlPanel,cControl);
    return panel;
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
    _filter=null;
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    _items=null;
  }
}
