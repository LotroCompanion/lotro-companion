package delta.games.lotro.gui.lore.items;

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
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.essences.EssencesSlotsSetup;

/**
 * Controller for a panel that shows slots setup.
 * @author DAM
 */
public class SlotsPanelController
{
  private static final int ICON_WIDTH=32;
  private static final int MARGIN=5;

  private JPanel _panel;
  private List<JLabel> _labels;

  /**
   * Constructor.
   */
  public SlotsPanelController()
  {
    _panel=GuiFactory.buildPanel(null);
    _panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
    _labels=new ArrayList<JLabel>();
  }

  /**
   * Configure the displayed slots.
   * @param setup Setup to show.
   */
  public void setupSlots(EssencesSlotsSetup setup)
  {
    _panel.removeAll();
    if (setup==null)
    {
      return;
    }
    int nbSlots=setup.getSocketsCount();
    while (_labels.size()<nbSlots)
    {
      _labels.add(GuiFactory.buildIconLabel(null));
    }
    for(int i=0;i<nbSlots;i++)
    {
      SocketType type=setup.getSlotType(i);
      Icon icon=LotroIconsManager.getEmptySocketIcon(type.getCode());
      JLabel label=_labels.get(i);
      _panel.add(Box.createRigidArea(new Dimension(MARGIN,0)));
      label.setIcon(icon);
      String tooltip=type.getLabel();
      label.setToolTipText(tooltip);
      _panel.add(label);
    }
    _panel.add(Box.createRigidArea(new Dimension(MARGIN,0)));
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
        EssencesSlotsSetup setup=(EssencesSlotsSetup)value;
        setupSlots(setup);
        return _panel;
      }
    };
    return renderer;
  }

  /**
   * Get the width of the rendered panel, in pixels.
   * @param slotsCount Slots count.
   * @return A pixel width.
   */
  public static int getWidthForSlotsCount(int slotsCount)
  {
    return (ICON_WIDTH*slotsCount)+MARGIN*(slotsCount+1);
  }
}
