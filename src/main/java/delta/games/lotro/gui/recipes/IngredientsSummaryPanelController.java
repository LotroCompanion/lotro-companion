package delta.games.lotro.gui.recipes;

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
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.items.ItemProxy;

/**
 * Controller for a panel that shows a summary of the ingredients of a recipe.
 * @author DAM
 */
public class IngredientsSummaryPanelController
{
  private JPanel _panel;
  private List<JLabel> _labels;

  /**
   * Constructor.
   */
  public IngredientsSummaryPanelController()
  {
    _panel=GuiFactory.buildPanel(null);
    _panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
    _labels=new ArrayList<JLabel>();
  }

  /**
   * Configure the given label to show the given ingredient.
   * @param label Label to configure.
   * @param ingredient Ingredient to show.
   */
  private void setupIngredientIcon(JLabel label, Ingredient ingredient)
  {
    ItemProxy itemProxy=ingredient.getItem();
    Icon icon=LotroIconsManager.getItemIcon(itemProxy.getIcon());
    int quantity=ingredient.getQuantity();
    String text=(quantity!=1)?String.valueOf(quantity):"";
    IconWithText iconWithText=new IconWithText(icon,text,Color.WHITE);
    label.setIcon(iconWithText);
  }

  private void setupPanel(List<Ingredient> ingredients)
  {
    int nbIngredients=ingredients.size();
    while (_labels.size()<nbIngredients)
    {
      _labels.add(GuiFactory.buildIconLabel(null));
    }
    _panel.removeAll();
    for(int i=0;i<nbIngredients;i++)
    {
      setupIngredientIcon(_labels.get(i),ingredients.get(i));
      _panel.add(Box.createRigidArea(new Dimension(5,0)));
      _panel.add(_labels.get(i));
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
        Recipe recipe=(Recipe)value;
        List<Ingredient> ingredients=recipe.getIngredients();
        setupPanel(ingredients);
        int height=_panel.getPreferredSize().height;
        if (table.getRowHeight()!=height)
        {
          table.setRowHeight(height);
        }
        return _panel;
      }
    };
    return renderer;
  }
}
