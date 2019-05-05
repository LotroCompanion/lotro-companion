package delta.games.lotro.gui.stats.deeds;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigator.NavigatorWindowController;
import delta.games.lotro.gui.common.navigator.ReferenceConstants;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.geo.DeedGeoData;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.geo.DeedGeoStatus;
import delta.games.lotro.utils.DateFormat;

/**
 * Controller for the "deed status edition" dialog.
 * @author DAM
 */
public class DeedStatusEditionDialogController extends DefaultFormDialogController<DeedStatus>
{
  // Data
  private DeedDescription _deed;
  // UI
  private JLabel _icon;
  // Controllers
  private CheckboxController _completed;
  private DateEditionController _completionDate;
  private WindowsManager _windowsManager;
  private DeedGeoStatusEditionPanelController _geoEditor;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   * @param deed Targeted deed.
   */
  public DeedStatusEditionDialogController(DeedDescription deed, DeedStatus status, WindowController parentController)
  {
    super(parentController,status);
    _deed=deed;
    _windowsManager=new WindowsManager();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Deed status edition...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Deed status");
    panel.setBorder(pathsBorder);

    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Deed summary line
    {
      JPanel deedSummaryLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      DeedType type=_deed.getType();
      ImageIcon icon=LotroIconsManager.getDeedTypeIcon(type);
      _icon=GuiFactory.buildIconLabel(icon);
      deedSummaryLine.add(_icon);
      // Name
      String name=_deed.getName();
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showDeed();
        }
      };
      LocalHyperlinkAction deedLinkAction=new LocalHyperlinkAction(name,al);
      HyperLinkController deedLink=new HyperLinkController(deedLinkAction);
      JLabel label=deedLink.getLabel();
      label.setFont(label.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      deedSummaryLine.add(label);
      panel.add(deedSummaryLine,c);
      c.gridy++;
    }

    // Completed
    _completed=new CheckboxController("Completed");
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setCompleted(_completed.isSelected());
      }
    };
    _completed.getCheckbox().addActionListener(l);
    // Completion date
    DateCodec codec=DateFormat.getDateTimeCodec();
    _completionDate=new DateEditionController(codec);

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    gbc.gridx=0; gbc.gridy=1;
    panel.add(_completed.getCheckbox(),gbc);
    gbc.gridx=0; gbc.gridy=2;
    panel.add(GuiFactory.buildLabel("Completion date:"),gbc);
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    gbc.gridx=1;
    panel.add(_completionDate.getTextField(),gbc);

    DeedGeoData geoData=_deed.getGeoData();
    if (geoData!=null)
    {
      JPanel geoPanel=buildGeoStatusEditionPanel();
      GridBagConstraints geoC=new GridBagConstraints(0,3,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
      panel.add(geoPanel,geoC);
      JButton toggleMap=GuiFactory.buildButton("Map");
      ActionListener mapActionListener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _geoEditor.showMap();
        }
      };
      toggleMap.addActionListener(mapActionListener);
      geoC.gridy++;
      panel.add(toggleMap,geoC);
    }
    // Set values
    boolean completed=_data.isCompleted()==Boolean.TRUE;
    _completed.setSelected(completed);
    setCompleted(completed);
    _completionDate.setDate(_data.getCompletionDate());
    if (_geoEditor!=null)
    {
      DeedGeoStatus status=_data.getGeoStatus();
      if (status==null)
      {
        status=new DeedGeoStatus();
        _data.setGeoStatus(status);
      }
      _geoEditor.setStatusData(status);
    }
    return panel;
  }

  private JPanel buildGeoStatusEditionPanel()
  {
    DeedGeoData geoData=_deed.getGeoData();
    _geoEditor=new DeedGeoStatusEditionPanelController(this,geoData);
    List<DeedGeoPointStatusGadgetsController> gadgets=_geoEditor.getGadgets();

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Geographic status");
    panel.setBorder(pathsBorder);

    int lineIndex=0;
    for(DeedGeoPointStatusGadgetsController gadget : gadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,lineIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(gadget.getCheckbox().getCheckbox(),c);
      c.gridx++;
      panel.add(gadget.getDateEditor().getTextField(),c);
      lineIndex++;
    }
    return panel;
  }

  private void setCompleted(boolean completed)
  {
    _completionDate.setState(completed,completed);
  }

  @Override
  protected void okImpl()
  {
    // Completed?
    boolean completed=_completed.isSelected();
    _data.setCompleted(Boolean.valueOf(completed));
    // Completion date
    Long completionDate=_completionDate.getDate();
    _data.setCompletionDate(completionDate);
    // Geographic data
    if (_geoEditor!=null)
    {
      _geoEditor.updateGeoStatus(_data.getGeoStatus());
    }
  }

  private void showDeed()
  {
    int nbWindows=_windowsManager.getAll().size();
    if (nbWindows==0)
    {
      NavigatorWindowController window=new NavigatorWindowController(this,0);
      String ref=ReferenceConstants.getAchievableReference(_deed);
      window.navigateTo(ref);
      window.show(false);
      _windowsManager.registerWindow(window);
    }
    else
    {
      NavigatorWindowController window=(NavigatorWindowController)_windowsManager.getAll().get(0);
      String ref=ReferenceConstants.getAchievableReference(_deed);
      window.navigateTo(ref);
      window.bringToFront();
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_completed!=null)
    {
      _completed.dispose();
      _completed=null;
    }
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
    if (_geoEditor!=null)
    {
      _geoEditor.dispose();
      _geoEditor=null;
    }
  }
}
