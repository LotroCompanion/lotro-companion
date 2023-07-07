package delta.games.lotro.utils.gui.chooser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for an object choice window.
 * @param <T> Type of managed objects.
 * @author DAM
 */
public class ObjectChoiceWindowController<T> extends DefaultFormDialogController<T>
{
  /**
   * Preference file for the columns of the item chooser.
   */
  public static final String ITEM_CHOOSER_PROPERTIES_ID="ObjectChooserColumn";
  /**
   * Name of the property for column IDs.
   */
  public static final String COLUMNS_PROPERTY="columns";

  private ObjectFilterPanelController _filterController;
  private ObjectChoicePanelController<T> _panelController;
  private GenericTableController<T> _tableController;
  private TypedProperties _prefs;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param prefs User preferences.
   * @param tableController Table controller.
   */
  public ObjectChoiceWindowController(WindowController parent, TypedProperties prefs, GenericTableController<T> tableController)
  {
    super(parent,null);
    _prefs=prefs;
    _tableController=tableController;
  }

  /**
   * Set the filter to use.
   * @param filter Filter.
   * @param filterController Filter UI controller.
   */
  public void setFilter(Filter<T> filter, ObjectFilterPanelController filterController)
  {
    _filterController=filterController;
    _tableController.setFilter(filter);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose:"); // I18n
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnsIds=_prefs.getStringList(COLUMNS_PROPERTY);
      if (columnsIds!=null)
      {
        _tableController.getColumnsManager().setColumns(columnsIds);
        _tableController.updateColumns();
      }
    }
    // Table
    _panelController=new ObjectChoicePanelController<T>(this,_tableController);
    if (_filterController!=null)
    {
      _filterController.setFilterUpdateListener(_panelController);
    }
    JPanel tablePanel=_panelController.getPanel();
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ok();
      }
    };
    _tableController.addActionListener(al);
    // Filter
    JPanel filterPanel=null;
    if (_filterController!=null)
    {
      filterPanel=_filterController.getPanel();
      TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
      filterPanel.setBorder(filterBorder);
    }
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    if (filterPanel!=null)
    {
      panel.add(filterPanel,c);
    }
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  @Override
  protected void okImpl()
  {
    _data=_tableController.getSelectedItem();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ObjectChoiceWindowController.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
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
  }
}
