package delta.games.lotro.utils.gui.combobox;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

/**
 * Controller for a combo box.
 * @param <T> Type of managed data.
 * @author DAM
 */
public class ComboBoxController<T>
{
  private JComboBox _comboBox;
  private List<ComboBoxItem<T>> _items;

  /**
   * Constructor.
   * @param comboBox Embedded combo box.
   */
  public ComboBoxController(JComboBox comboBox)
  {
    _comboBox=comboBox;
    _items=new ArrayList<ComboBoxItem<T>>();
  }

  /**
   * Get the managed combo-box.
   * @return the managed combo-box.
   */
  public JComboBox getComboBox()
  {
    return _comboBox;
  }

  /**
   * Add empty item.
   * @param label Label for the empty item.
   */
  public void addEmptyItem(String label)
  {
    ComboBoxItem<T> item=new ComboBoxItem<T>(null,label);
    _items.add(item);
    _comboBox.addItem(item);
  }

  /**
   * Add a new item.
   * @param data Data item.
   * @param label Label to use to display this item.
   */
  public void addItem(T data, String label)
  {
    ComboBoxItem<T> item=new ComboBoxItem<T>(data,label);
    _items.add(item);
    _comboBox.addItem(item);
  }

  /**
   * Get the currently selected item.
   * @return A data item or <code>null</code> if nothing selected.
   */
  public T getSelectedItem()
  {
    T ret=null;
    int index=_comboBox.getSelectedIndex();
    if (index!=-1)
    {
      ComboBoxItem<T> item=_items.get(index);
      ret=item.getItem();
    }
    return ret;
  }

  private ComboBoxItem<T> getItemForData(T data)
  {
    for(ComboBoxItem<T> item : _items)
    {
      if (item.getItem()==data)
      {
        return item;
      }
    }
    return null;
  }

  /**
   * Select an item.
   * @param data Data item to select.
   */
  public void selectItem(T data)
  {
    ComboBoxItem<T> item=getItemForData(data);
    _comboBox.setSelectedItem(item);
  }
}
