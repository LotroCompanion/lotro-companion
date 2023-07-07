package delta.games.lotro.gui.lore.collections.pets.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.collections.pets.PetFilter;
import delta.games.lotro.gui.lore.collections.pets.PetFilterController;
import delta.games.lotro.gui.lore.collections.pets.PetsTableController;
import delta.games.lotro.gui.lore.collections.pets.form.PetDisplayWindowController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.collections.pets.CosmeticPetDescription;

/**
 * Controller for the pets explorer window.
 * @author DAM
 */
public class PetsExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="PETS_EXPLORER";

  private PetFilterController _filterController;
  private PetsExplorerPanelController _panelController;
  private PetsTableController _tableController;
  private PetFilter _filter;
  private WindowsManager _formWindows;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public PetsExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new PetFilter();
    _formWindows=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Pets explorer"); // 18n
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(825,700);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initPetsTable();
    _panelController=new PetsExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new PetFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // 18n
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initPetsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("PetsExplorer");
    _tableController=new PetsTableController(prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          CosmeticPetDescription pet=(CosmeticPetDescription)event.getSource();
          showPet(pet);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showPet(CosmeticPetDescription pet)
  {
    String id=PetDisplayWindowController.getId(pet);
    WindowController window=_formWindows.getWindow(id);
    if (window==null)
    {
      window=new PetDisplayWindowController(this,pet);
      _formWindows.registerWindow(window);
    }
    window.show();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_formWindows!=null)
    {
      _formWindows.disposeAll();
      _formWindows=null;
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
