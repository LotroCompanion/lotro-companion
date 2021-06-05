package delta.games.lotro.gui.kinship;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipSummary;
import delta.games.lotro.kinship.events.KinshipEvent;
import delta.games.lotro.kinship.events.KinshipEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a kinship summary panel.
 * @author DAM
 */
public class KinshipSummaryDisplayPanelController implements GenericEventsListener<KinshipEvent>
{
  // Data
  private Kinship _kinship;
  private KinshipSummary _summary;
  // UI
  private JPanel _panel;
  private JLabel _nameLabel;

  /**
   * Constructor.
   * @param kinship Kinship to display.
   */
  public KinshipSummaryDisplayPanelController(Kinship kinship)
  {
    _kinship=kinship;
    _summary=kinship.getSummary();
    EventsManager.addListener(KinshipEvent.class,this);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);

    // Name
    _nameLabel=GuiFactory.buildLabel("",28.0f);
    panel.add(_nameLabel,c);
    update();
    return panel;
  }

  /**
   * Handle kinship events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(KinshipEvent event)
  {
    KinshipEventType type=event.getType();
    if (type==KinshipEventType.KINSHIP_SUMMARY_UPDATED)
    {
      Kinship kinship=event.getKinship();
      if (kinship==_kinship)
      {
        update();
      }
    }
  }

  /**
   * Update contents.
   */
  public void update()
  {
    if (_summary!=null)
    {
      // Name
      String name=_summary.getName();
      _nameLabel.setText(name);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Listeners
    EventsManager.removeListener(KinshipEvent.class,this);
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Data
    _kinship=null;
    _summary=null;
  }
}
