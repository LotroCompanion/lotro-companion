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
import delta.games.lotro.lore.crafting.recipes.CraftingResult;
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipeVersion;
import delta.games.lotro.lore.items.ItemProxy;

/**
 * Controller for a panel that shows a summary of the items of a recipe (ingredients or result).
 * @author DAM
 */
public class ItemsSummaryPanelController
{
  private JPanel _panel;
  private List<JLabel> _labels;

  /**
   * Mode (ingredients or results).
   * @author DAM
   */
  public enum Mode
  {
    /**
     * Ingredients.
     */
    INGREDIENTS,
    /**
     * Results.
     */
    RESULTS
  }

  private Mode _mode;

  /**
   * Constructor.
   * @param mode Mode to use.
   */
  public ItemsSummaryPanelController(Mode mode)
  {
    _mode=mode;
    _panel=GuiFactory.buildPanel(null);
    _panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
    _labels=new ArrayList<JLabel>();
  }

  /**
   * Configure the given label to show the given item/quantity.
   * @param label Label to configure.
   * @param item Item to show.
   * @param quantity Quantity.
   */
  private void setupItemIcon(JLabel label, ItemProxy item, int quantity)
  {
    Icon icon=LotroIconsManager.getItemIcon(item.getIcon());
    String text=(quantity!=1)?String.valueOf(quantity):"";
    IconWithText iconWithText=new IconWithText(icon,text,Color.WHITE);
    label.setIcon(iconWithText);
  }

  private void setupPanel(Recipe recipe)
  {
    _panel.removeAll();
    if (_mode==Mode.INGREDIENTS)
    {
      List<Ingredient> ingredients=recipe.getIngredients();
      int nbIngredients=ingredients.size();
      while (_labels.size()<nbIngredients)
      {
        _labels.add(GuiFactory.buildIconLabel(null));
      }
      for(int i=0;i<nbIngredients;i++)
      {
        Ingredient ingredient=ingredients.get(i);
        ItemProxy item=ingredient.getItem();
        int quantity=ingredient.getQuantity();
        setupItemIcon(_labels.get(i),item,quantity);
        _panel.add(Box.createRigidArea(new Dimension(5,0)));
        _panel.add(_labels.get(i));
      }
    }
    else
    {
      while (_labels.size()<2)
      {
        _labels.add(GuiFactory.buildIconLabel(null));
      }
      RecipeVersion version=recipe.getVersions().get(0);
      CraftingResult regular=version.getRegular();
      {
        ItemProxy regularItem=regular.getItem();
        int regularQuantity=regular.getQuantity();
        setupItemIcon(_labels.get(0),regularItem,regularQuantity);
        _panel.add(Box.createRigidArea(new Dimension(5,0)));
        _panel.add(_labels.get(0));
      }
      CraftingResult critical=version.getCritical();
      if (critical!=null)
      {
        ItemProxy criticalItem=critical.getItem();
        int criticalQuantity=critical.getQuantity();
        setupItemIcon(_labels.get(1),criticalItem,criticalQuantity);
        _panel.add(Box.createRigidArea(new Dimension(5,0)));
        _panel.add(_labels.get(1));
      }
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
        setupPanel(recipe);
        int height=_panel.getPreferredSize().height;
        if (height!=0)
        {
          if (table.getRowHeight()!=height)
          {
            table.setRowHeight(height);
          }
        }
        return _panel;
      }
    };
    return renderer;
  }
}
