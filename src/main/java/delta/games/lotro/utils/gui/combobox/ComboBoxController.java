package delta.games.lotro.utils.gui.combobox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a combo box.
 * @param <T> Type of managed data.
 * @author DAM
 */
public class ComboBoxController<T>
{
  private JComboBox _comboBox;
  private List<ComboBoxItem<T>> _items;
  private List<ItemSelectionListener<T>> _listeners;

  /**
   * Constructor.
   */
  public ComboBoxController()
  {
    _comboBox=GuiFactory.buildComboBox();
    _items=new ArrayList<ComboBoxItem<T>>();
    _listeners=null;
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

  /**
   * Add a listener for item selection.
   * @param listener Listener to add.
   */
  public void addListener(ItemSelectionListener<T> listener)
  {
    if (_listeners==null)
    {
      _listeners=new ArrayList<ItemSelectionListener<T>>();
      ActionListener al=new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          callListeners();
        }
      };
      _comboBox.addActionListener(al);
    }
    _listeners.add(listener);
  }

  /**
   * Remove a listener for item selection.
   * @param listener Listener to remove.
   */
  public void removeListener(ItemSelectionListener<T> listener)
  {
    _listeners.remove(listener);
  }

  private void callListeners()
  {
    T item=getSelectedItem();
    for(ItemSelectionListener<T> listener : _listeners)
    {
      listener.itemSelected(item);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _listeners=null;
  }
}
