package delta.games.lotro.gui.lore.items;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a panel that shows a summary of the items (for recipes, barters...).
 * @author DAM
 */
public class ItemsSummaryPanelController
{
  private JPanel _panel;
  private List<JLabel> _labels;

  /**
   * Constructor.
   */
  public ItemsSummaryPanelController()
  {
    _panel=GuiFactory.buildPanel(null);
    _panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
    _labels=new ArrayList<JLabel>();
  }

  /**
   * Configure the displayed items.
   * @param items Items to show.
   */
  public void setupItemIcons(List<CountedItem<Item>> items)
  {
    _panel.removeAll();
    int nbItems=items.size();
    while (_labels.size()<nbItems)
    {
      _labels.add(GuiFactory.buildIconLabel(null));
    }
    for(int i=0;i<nbItems;i++)
    {
      CountedItem<Item> item=items.get(i);
      JLabel label=_labels.get(i);
      _panel.add(Box.createRigidArea(new Dimension(5,0)));
      Icon icon=LotroIconsManager.getItemIcon(item.getIcon());
      int quantity=item.getQuantity();
      String text=(quantity!=1)?String.valueOf(quantity):"";
      IconWithText iconWithText=new IconWithText(icon,text,Color.WHITE);
      label.setIcon(iconWithText);
      _panel.add(label);
    }
    _panel.add(Box.createHorizontalGlue());
  }

  /**
   * Build a table cell renderer that displays the ingredients of a recipe.
   * @return A renderer.
   */
  public TableCellRenderer buildRenderer()
  {
    TableCellRenderer renderer=new TableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        @SuppressWarnings("unchecked")
        List<CountedItem<Item>> items=(List<CountedItem<Item>>)value;
        setupItemIcons(items);
        return _panel;
      }
    };
    return renderer;
  }
}
