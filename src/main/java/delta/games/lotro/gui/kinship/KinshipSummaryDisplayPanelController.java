package delta.games.lotro.gui.kinship;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.gui.character.status.housing.HousingUiUtils;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipMember;
import delta.games.lotro.kinship.KinshipRoster;
import delta.games.lotro.kinship.KinshipSummary;
import delta.games.lotro.kinship.events.KinshipEvent;
import delta.games.lotro.kinship.events.KinshipEventType;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a kinship summary panel.
 * @author DAM
 */
public class KinshipSummaryDisplayPanelController extends AbstractPanelController implements GenericEventsListener<KinshipEvent>
{
  // Data
  private Kinship _kinship;
  private KinshipSummary _summary;
  // UI
  private JLabel _nameLabel;
  private JLabel _statusDateLabel;
  private JLabel _founderLabel;
  private JLabel _creationDateLabel;
  private JLabel _leaderLabel;
  private JLabel _motdLabel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param kinship Kinship to display.
   */
  public KinshipSummaryDisplayPanelController(WindowController parent, Kinship kinship)
  {
    super(parent);
    _kinship=kinship;
    _summary=kinship.getSummary();
    EventsManager.addListener(KinshipEvent.class,this);
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Name
    _nameLabel=GuiFactory.buildLabel("",28.0f);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,2),0,0);
    panel.add(_nameLabel,c);
    // Attributes
    JPanel attributesPanel=buildAttributesPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(attributesPanel,c);
    // House button
    JButton houseButton=GuiFactory.buildButton("House...");
    c=new GridBagConstraints(1,1,1,1,0.0,0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(houseButton,c);
    ActionListener al=new ActionListener()
    {
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doHouseButton();
      }
    };
    houseButton.addActionListener(al);
    update();
    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    Insets insets=new Insets(2,5,2,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    // Labels
    panel.add(GuiFactory.buildLabel("Status Date:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Founder:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Creation Date:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Leader:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("MOTD:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    // Values
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    _statusDateLabel=GuiFactory.buildLabel("");
    panel.add(_statusDateLabel,gbc);
    gbc.gridx=1; gbc.gridy++;
    _founderLabel=GuiFactory.buildLabel("");
    panel.add(_founderLabel,gbc);
    gbc.gridx=1; gbc.gridy++;
    _creationDateLabel=GuiFactory.buildLabel("");
    panel.add(_creationDateLabel,gbc);
    gbc.gridx=1; gbc.gridy++;
    _leaderLabel=GuiFactory.buildLabel("");
    panel.add(_leaderLabel,gbc);
    gbc.gridx=1; gbc.gridy++;
    _motdLabel=GuiFactory.buildLabel("");
    panel.add(_motdLabel,gbc);
    gbc.gridx=1; gbc.gridy++;
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
    if (_summary==null)
    {
      return;
    }
    // Status date
    String statusDateStr="";
    long statusDate=_summary.getStatusDate();
    statusDateStr=Formats.getDateTimeString(new Date(statusDate));
    _statusDateLabel.setText(statusDateStr);
    // Name & server
    String name=_summary.getName();
    String server=_summary.getServerName();
    String label=name;
    if (server.length()>0)
    {
      label=label+" ("+server+")";
    }
    _nameLabel.setText(label);
    // Founder
    String founder=resolveMemberName(_summary.getFounderID());
    _founderLabel.setText(founder);
    // Creation date
    String dateStr="";
    Long creationDate=_summary.getCreationDate();
    if (creationDate!=null)
    {
      Date d=new Date(creationDate.longValue());
      dateStr=Formats.getDateString(d);
    }
    _creationDateLabel.setText(dateStr);
    // Leader
    String leader=resolveMemberName(_summary.getLeaderID());
    _leaderLabel.setText(leader);
    // Message of the day
    String motd=_summary.getMotd();
    _motdLabel.setText(motd);
  }

  private String resolveMemberName(InternalGameId memberId)
  {
    if (memberId==null)
    {
      return "-";
    }
    KinshipRoster roster=_kinship.getRoster();
    KinshipMember member=roster.getMemberByID(memberId.asLong());
    if (member!=null)
    {
      return member.getSummary().getName();
    }
    return "???";
  }

  private void doHouseButton()
  {
    KinshipSummary kinshipSummary=_kinship.getSummary();
    HouseAddress address=kinshipSummary.getAddress();
    if (address==null)
    {
      GuiFactory.showInformationDialog(getPanel(),"No known house!","Warning!");
      return;
    }
    String server=_kinship.getSummary().getServerName();
    HouseIdentifier id=new HouseIdentifier(server,address);
    HousingUiUtils.showHouse(id,getWindowController());
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Listeners
    EventsManager.removeListener(KinshipEvent.class,this);
    // UI
    // Data
    _kinship=null;
    _summary=null;
  }
}
