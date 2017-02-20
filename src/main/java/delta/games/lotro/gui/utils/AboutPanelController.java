package delta.games.lotro.gui.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Controller for a 'about' panel.
 * @author DAM
 */
public class AboutPanelController
{
  private JPanel _panel;
  private Timer _timer;

  /**
   * Constructor.
   */
  public AboutPanelController()
  {
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c;

    int x=0;
    int y=0;
    
    // Icon (if any)
    String iconPath="/resources/gui/ring/ring48.png";
    Image icon=IconsManager.getImage(iconPath);
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,20,10,5),0,0);
    ImageIcon imageIcon=new ImageIcon(icon);
    JLabel lbIcon=new JLabel(imageIcon);
    panel.add(lbIcon,c);
    x++;
    
    // Project name
    JLabel lbName=new JLabel("LotRO Companion");
    lbName.setFont(lbName.getFont().deriveFont(Font.BOLD,36));
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,20),0,0);
    panel.add(lbName,c);
    y++;

    // Project version
    String projectVersion="Version 4.0.1 (20/02/2017)";
    JLabel lbVersion=new JLabel(projectVersion);
    lbVersion.setFont(lbVersion.getFont().deriveFont(Font.BOLD,24));
    c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,5),0,0);
    panel.add(lbVersion,c);
    y++;

    // Project contact
    JLabel lbEmail=new JLabel("lotrocompanion@gmail.com");
    lbEmail.setFont(lbVersion.getFont().deriveFont(Font.BOLD,16));
    c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,5),0,0);
    panel.add(lbEmail,c);
    y++;

    // Contributors label contrib
    JLabel lbContributors=new JLabel("Contributors:");
    lbContributors.setFont(lbVersion.getFont().deriveFont(Font.BOLD,24));
    c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(10,15,10,5),0,0);
    panel.add(lbContributors,c);
    y++;

    // Project contributors
    JPanel contributorsPanel=buildContributorsPanel();
    c=new GridBagConstraints(0,y,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(contributorsPanel,c);
    y++;
    
    return panel;
  }

  private JPanel buildContributorsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(null);
    final List<JPanel> panels=buildToonPanels();
    int maxWidth=0,maxHeight=0;
    for(JPanel panel : panels)
    {
      Dimension d=panel.getPreferredSize();
      int width=d.width;
      if (width>maxWidth) maxWidth=width;
      int height=d.height;
      if (height>maxHeight) maxHeight=height;
      panel.setSize(d);
      panel.setVisible(false);
      ret.add(panel);
    }
    ret.setSize(maxWidth,maxHeight);
    ret.setPreferredSize(new Dimension(maxWidth,maxHeight));
    JPanel groupPanel=panels.get(panels.size()-1);
    int x=(maxWidth-groupPanel.getWidth())/2;
    int y=(maxHeight-groupPanel.getHeight())/2;
    groupPanel.setLocation(x,y);
    panels.get(0).setVisible(true);

    ActionListener al=new ActionListener()
    {
      private int _index=0;
      public void actionPerformed(ActionEvent e)
      {
        JPanel oldPanel=panels.get(_index);
        oldPanel.setVisible(false);
        _index++;
        if (_index==panels.size()) _index=0;
        JPanel newPanel=panels.get(_index);
        newPanel.setVisible(true);
      }
    };
    _timer=new Timer(3000,al);
    _timer.setRepeats(true);
    _timer.start();
    return ret;
  }

  private List<JPanel> buildToonPanels()
  {
    List<JPanel> panels=new ArrayList<JPanel>();

    // Glumlug
    {
      String contrib="<html><ul>"+"<li>Design,<li>Coding,<li>Build,<li>Advertisement"+"</ul></html>";
      JPanel glumlug=buildToonContribPanel("Glumlug","glumlug",contrib);
      panels.add(glumlug);
    }
    // Allyriel
    {
      String contrib="<html><ul>"+"<li>General support,<li>Ideas,<li>Beta testing,<li>" +
          "Advertisement,<li>Warbands screenshots"+"</ul></html>";
      JPanel allyriel=buildToonContribPanel("Allyriel","allyriel",contrib);
      panels.add(allyriel);
    }
    // Serilis
    {
      String contrib="<html><ul>"+"<li>General support,<li>Ideas,<li>Beta testing,<li>Forum warden"+"</ul></html>";
      JPanel serilis=buildToonContribPanel("Serilis","serilis",contrib);
      panels.add(serilis);
    }
    // Warthal
    {
      String contrib="<html><ul>"+"<li>General support,<li>Ideas,<li>Beta testing,<li>Warbands graphics"+"</ul></html>";
      JPanel warthal=buildToonContribPanel("Warthal","warthal",contrib);
      panels.add(warthal);
    }
    JPanel groupPanel=buildGroupContribPanel("Ever Watchful","group");
    panels.add(groupPanel);
    return panels;
  }

  private JPanel buildGroupContribPanel(String groupName, String groupIconName)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    String iconPath="/resources/gui/toons/"+groupIconName+".png";
    Image icon=IconsManager.getImage(iconPath);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ImageIcon imageIcon=new ImageIcon(icon);
    JLabel lbIcon=new JLabel(imageIcon);
    panel.add(lbIcon,c);
    
    return panel;
  }

  private JPanel buildToonContribPanel(String toonName, String toonIconName, String contribText)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;

    int x=0,y=0;

    // Icon
    String iconPath="/resources/gui/toons/"+toonIconName+".png";
    Image icon=IconsManager.getImage(iconPath);
    c=new GridBagConstraints(x,y,1,2,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ImageIcon imageIcon=new ImageIcon(icon);
    JLabel lbIcon=new JLabel(imageIcon);
    panel.add(lbIcon,c);
    x++;
    
    // Toon name
    JLabel lbName=new JLabel(toonName);
    lbName.setFont(lbName.getFont().deriveFont(Font.BOLD,36));
    c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(lbName,c);
    y++;

    // Toon contrib
    JLabel contribLabel=new JLabel(contribText);
    contribLabel.setFont(contribLabel.getFont().deriveFont(Font.BOLD,24));
    c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(contribLabel,c);
    y++;
    
    return panel;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_timer!=null)
    {
      _timer.stop();
      _timer=null;
    }
  }
}
