package delta.games.lotro.gui.character.status.quests;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.misc.Disposable;
import delta.common.ui.swing.selection.SelectionChangedEvent;
import delta.common.ui.swing.selection.SelectionChangedListener;
import delta.common.ui.swing.selection.SelectionManager;
import delta.common.ui.swing.tables.GenericTableController;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.common.blacklist.Blacklist;
import delta.games.lotro.common.blacklist.filter.BlackListFilter;
import delta.games.lotro.common.blacklist.io.BlackListIO;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller for the blacklist UI.
 * @param <T> Type of managed elements.
 * @author DAM
 */
public class BlacklistController<T extends Identifiable> implements Disposable
{
  private static final String ADD="Add";
  private static final String REMOVE="Remove";
  // Data
  private Blacklist _blacklist;
  private BlackListFilter _filter;
  // Table controller
  private GenericTableController<T> _table;
  // Buttons
  private JPanel _panel;
  private JButton _add;
  private JButton _remove;
  private CheckboxController _active;
  // Listeners
  private FilterUpdateListener _listener;

  /**
   * Constructor.
   * @param blacklist Blacklist.
   * @param table Associated table.
   * @param listener Filter update listener.
   * @param filter Blacklist filter.
   */
  public BlacklistController(Blacklist blacklist, GenericTableController<T> table, FilterUpdateListener listener, BlackListFilter filter)
  {
    _blacklist=blacklist;
    _table=table;
    _listener=listener;
    _filter=filter;
    // Setup buttons
    ActionListener alButtons=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        JButton button=(JButton)e.getSource();
        handleButton(button.getActionCommand());
      }
    };
    _add=GuiFactory.buildButton("Add"); // I18n
    _add.setActionCommand(ADD);
    _add.addActionListener(alButtons);
    _remove=GuiFactory.buildButton("Remove"); // I18n
    _remove.setActionCommand(REMOVE);
    _remove.addActionListener(alButtons);
    _active=new CheckboxController("Active"); // I18n
    _active.setSelected(true);
    ActionListener alCheckbox=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setActive(_active.isSelected());
      }
    };
    _active.getCheckbox().addActionListener(alCheckbox);
    _panel=buildButtonsPanel();
    // Setup selection listener
    setupSelectionListener();
  }

  private void setupSelectionListener()
  {
    SelectionChangedListener<T> listener=new SelectionChangedListener<T>()
    {
      public void selectionChanged(SelectionChangedEvent<T> event)
      {
        handleSelectionChange(event.getSelectedElements());
      }
    };
    SelectionManager<T> selectionMgr=_table.getSelectionManager();
    selectionMgr.getListeners().addListener(listener);
    handleSelectionChange(selectionMgr.getSelection());
  }

  private void handleSelectionChange(List<T> selectedItems)
  {
    boolean useAdd=false;
    boolean useRemove=false;
    for(T selectedItem : selectedItems)
    {
      int id=selectedItem.getIdentifier();
      boolean blacklisted=_blacklist.isBlacklisted(id);
      if (blacklisted)
      {
        useRemove=true;
      }
      else
      {
        useAdd=true;
      }
    }
    _add.setEnabled(useAdd);
    _remove.setEnabled(useRemove);
  }

  
  private void setActive(boolean active)
  {
    if (_filter.isEnabled()!=active)
    {
      _filter.setEnabled(active);
      if (_listener!=null)
      {
        _listener.filterUpdated();
      }
    }
  }

  private JPanel buildButtonsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_active.getCheckbox());
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    ret.add(_add,c);
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(_remove,c);
    ret.setBorder(GuiFactory.buildTitledBorder("Blacklist")); // I18n
    return ret;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void handleButton(String actionID)
  {
    List<T> selection=_table.getSelectionManager().getSelection();
    if (ADD.equals(actionID))
    {
      for(T item : selection)
      {
        _blacklist.add(item.getIdentifier());
      }
      if (_listener!=null)
      {
        _listener.filterUpdated();
      }
    }
    else if (REMOVE.equals(actionID))
    {
      for(T item : selection)
      {
        _blacklist.remove(item.getIdentifier());
      }
      if (_listener!=null)
      {
        _listener.filterUpdated();
      }
    }
  }

  @Override
  public void dispose()
  {
    if (_blacklist!=null)
    {
      BlackListIO.save(_blacklist);
      _blacklist=null;
    }
    _filter=null;
    _table=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _add=null;
    _remove=null;
    _active=null;
    _listener=null;
  }
}
