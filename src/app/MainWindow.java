
package app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import layout.SpringUtilities;

import com.javaswingcomponents.calendar.JSCCalendar;
import com.javaswingcomponents.datepicker.JSCDatePicker;

public class MainWindow extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton bttn1,bttn2,bttn3,bttn4,bttn5,bttn6,bttn7,bttn8,bttn9,bttn10;
	private JTextField jtf1,jtf2,jtf3;
	private JPanel jp1,jp2,jp3,jp4,jp5,jp6,jp7,jp8;
	private JComboBox jcb1;
	public JFrame parent;
	private JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7,jl8,jl9,jl10,jl11;
	private JTextArea jta1,jta2;
	private FileOperations fil;
	private JSCDatePicker dp1,dp2,dp3;
	private JList jlist1;
	
	private JRadioButton jr1,jr2,jr3;
	private ButtonGroup bg1;
	public static ResourceBundle rb;
	
	public String strRepMonth,strRepYear,currentCat,amount,username,timestamp;
	
	private long alertInt;
	public void run()
	{
		while(true)
		{
			if(Thread.currentThread().getName().equals("TIMER"))
			{
				
			jl11.setText(new Date().toString());
			try {
				
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			if(Thread.currentThread().getName().equals("EXPENSE"))
			{
				
			fil.doAudit();
			//if(fil.total > 1000)
				//System.out.println("Meanwhile..");
				//JOptionPane.showMessageDialog(parent,"Expenses exceeded budget...");
			try {
				
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			if(Thread.currentThread().getName().equals("ALERTS") && Thread.currentThread().isAlive()){
				List<String> lb = fil.alertGen();
				
				if(lb.size()!=0){
					for(String me : lb)
					JOptionPane.showMessageDialog(parent,me);
				}
				try {
					Thread.sleep(alertInt);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
		    // Set the Look and Feel of the application to the operating
		    // system's look and feel.
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException e) {
		}
		catch (InstantiationException e) {
		}
		catch (IllegalAccessException e) {
		}
		catch (UnsupportedLookAndFeelException e) {
		}
		MainWindow mw = new MainWindow();
		mw.showGUI();

		Thread t1 = new Thread(mw);
		
		Thread t2 = new Thread(mw);
		Thread t3 = new Thread(mw);
		
		t1.setName("EXPENSE");
		t2.setName("ALERTS");
		t3.setName("TIMER");
		
		t1.start();
		t2.start();
		t3.start();
	}

	
	public MainWindow()
	{
		parent = this;
		rb = ResourceBundle.getBundle("app.MyResource");
		fil = new FileOperations(System.getProperty("user.home")+ System.getProperty("file.separator"));
		alertInt = Long.parseLong(fil.mapConfig.get("AlertInterval"));
		this.currentCat ="-select-";
		this.username= System.getProperty("user.name");
		this.timestamp  = new Date().toString();
	}
	private void showGUI()
	{
		
		
		//Buttons creation
		bttn1 = new JButton(rb.getString("MW.add"));
		bttn2 = new JButton(rb.getString("MW.newcat"));
		bttn3 = new JButton(rb.getString("MW.addexpens"));
		bttn4 = new JButton(rb.getString("MW.remcat"));
		bttn5 = new JButton(rb.getString("MW.repgen"));
		bttn6 = new JButton(rb.getString("MW.remexpense"));
		bttn7 = new JButton("Settings");
		bttn8 = new JButton("Generate");
		bttn9 = new JButton("Save Configuration");
		bttn10 = new JButton("Show expenses");
		bttn1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub
				
				//paneManage(new Integer[]{2});
				parent.add(jp2);
				parent.remove(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				parent.pack();
			}
		});


		bttn2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub

				String strCat = JOptionPane.showInputDialog(parent, "Add New Category");
				if(strCat !=null && !strCat.equalsIgnoreCase(""))
					if(fil.addCategory(strCat))
					{
						JOptionPane.showMessageDialog(parent,"Added Successfully");
						jcb1.addItem(strCat);
					}
					else
						JOptionPane.showMessageDialog(parent,"Category Already exists");
			}
		});

		
		bttn3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub
				
				amount = jtf1.getText();
				if(amount  == null || amount.equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(parent,"Enter Amount","Error message",JOptionPane.ERROR_MESSAGE);
				}
				else if(currentCat == null ||currentCat.equalsIgnoreCase("-select-"))
				{
					JOptionPane.showMessageDialog(parent,"Select Category","Error message",JOptionPane.ERROR_MESSAGE);

				}
				else if(fil.addExpense(currentCat+"$"+amount+"$"+jta1.getText()+"$"+DateFormat.getDateInstance(DateFormat.SHORT).format(dp1.getSelectedDate())))
					JOptionPane.showMessageDialog(parent,"Expense Added Successfully");
					
				
				jtf1.setText("");
				jta1.setText("Default Remark");
				jcb1.setSelectedIndex(0);
				
				
				
					
				
			}
		});
		bttn4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub
				if(currentCat.equalsIgnoreCase("-select-"))
					JOptionPane.showConfirmDialog(parent,"Remove Category : " + currentCat);
				else
				{	
				int d = JOptionPane.showConfirmDialog(parent,"Remove Category : " + currentCat);
				System.out.println(d);
				if(d==0){
					fil.removeCategory(currentCat);
					jcb1.removeItem(currentCat);
				}
			}
			}
		});
		
		bttn5.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				
				
				//paneManage(new Integer[]{7});
				parent.remove(jp2);
				parent.remove(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp8);
				parent.add(jp7);
					parent.pack();
				
				
			}
		});

		bttn6.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub
			
				//paneManage(new Integer[]{8});
				parent.remove(jp2);
				parent.remove(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.add(jp8);
				parent.pack();
				
					
			}
		});
		bttn7.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub
				//paneManage(new Integer[]{5});
				
				parent.remove(jp2);
				parent.remove(jp3);
				parent.remove(jp4);
				parent.add(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				parent.pack();
			}
		});

		bttn8.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) {
				// TODO Auto-generated method stub
				String strOffDate = DateFormat.getDateInstance(DateFormat.SHORT).format(dp2.getSelectedDate());
				fil.generateReport(true, strOffDate);
				dailyReportWriting(fil.generateDayReport(strOffDate),strOffDate);
				parent.remove(jp2);
				parent.add(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				parent.pack();
			}
		});
		
		bttn9.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String strBase = jtf2.getText();
				int iSec = Integer.parseInt(strBase.substring(0,strBase.indexOf('s')));
				List<String> lstConfig = new ArrayList<String>();
				lstConfig.add("AlertInterval:"+iSec*1000);
				lstConfig.add("MonthlyBudget:"+jtf3.getText());
				alertInt = iSec*1000;
				fil.saveConfig(lstConfig);
				parent.remove(jp2);
				parent.remove(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				parent.pack();
			}
		});
		
		
		
		bttn10.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String date = DateFormat.getDateInstance(DateFormat.SHORT).format(dp3.getSelectedDate());
				List<String> l1 = fil.populateExpenseForDate(date);
				jlist1.setListData(l1.toArray());
			}
		});
		//Date picker creation
		
		dp1 = new JSCDatePicker(DateFormat.getDateInstance(DateFormat.SHORT),new JSCCalendar(TimeZone.getDefault(), Locale.getDefault()));
		dp1.setSelectedDate(new Date());
		dp2 = new JSCDatePicker(DateFormat.getDateInstance(DateFormat.SHORT),new JSCCalendar(TimeZone.getDefault(), Locale.getDefault()));
		dp2.setSelectedDate(new Date());
		dp3= new JSCDatePicker(DateFormat.getDateInstance(DateFormat.SHORT),new JSCCalendar(TimeZone.getDefault(), Locale.getDefault()));
		dp3.setSelectedDate(new Date());
		
		//Textfield creation
		jtf1 = new JTextField("");
		jtf1.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent k) {}
			public void keyReleased(KeyEvent k) {}
			public void keyPressed(KeyEvent k) {
				if(checkValidInput((KeyEvent.getKeyText(k.getKeyCode()))))
				{
					JOptionPane.showMessageDialog(parent,"Only Digits Allowed..!!!");
					jtf1.setText("");
				}
			}
			private boolean checkValidInput(String string) {
				Pattern p = Pattern.compile("[a-zA-Z$!@#%&*()]");
				Matcher m = p.matcher(string);
				return m.find();
			}
		});
		
	
		jtf2 = new JTextField();

		jtf2.setText("20s");
		
		jtf2.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(KeyEvent a) {
				// TODO Auto-generated method stub
				if(!jtf2.getText().contains("s"))
					JOptionPane.showMessageDialog(parent,"Please Specify value in Seconds");
				
			}
		});
		
		jtf3 = new JTextField("10000");
		//Label creation
		jl1 = new JLabel("Select category");
		jl2 = new JLabel("Enter Amount");
		jl3 = new JLabel("Select Date");
		jl4 = new JLabel("Remarks");
		jl5 = new JLabel("Alert Inerval");
		jl6 = new JLabel("Master");
		jl7 = new JLabel("Monthly");
		jl8 = new JLabel("Day");
		jl9 = new JLabel("Monthly Budget");
		jl10 = new JLabel("Welcome "+username);
		jl11 = new JLabel(timestamp);
		
		//Textarea creation
		jta1  = new JTextArea(5,10);
		jta1.setText("Default Remark");
		jta2 = new JTextArea(20,20);
		jta2.setEditable(false);
		
		
		//Jcombo box creation
		jcb1 = new JComboBox();
		jcb1.addItem("-Select-");
		for(String comps: fil.loadCategories())
			jcb1.addItem(comps);
		jcb1.setSelectedIndex(0);
		jcb1.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent i) {
				// TODO Auto-generated method stub
				currentCat = i.getItem().toString();	
			}
		});

		//Radio buttons
		jr1 = new JRadioButton();
		jr1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				fil.generateReport(false, "");
				
				//paneManage(new Integer[]{3});
				parent.remove(jp2);
				parent.add(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				reporter();
				parent.pack();
			}
		});
		jr2 = new JRadioButton();
		jr2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//paneManage(new Integer[]{4});
				parent.remove(jp2);
				parent.remove(jp3);
				parent.add(jp4);
				parent.remove(jp5);
				parent.remove(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				parent.pack();
				
			}
		});
		jr3 = new JRadioButton();
		jr3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				parent.remove(jp2);
				parent.remove(jp3);
				parent.remove(jp4);
				parent.remove(jp5);
				parent.add(jp6);
				parent.remove(jp7);
				parent.remove(jp8);
				parent.pack();
			}
		});
		bg1 = new ButtonGroup();
		bg1.add(jr1);
		bg1.add(jr2);
		bg1.add(jr3);

		//Panel 1 for all main functionality buttons
		jp1 = new JPanel();
		jp1.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =0;
		c.gridy =0;
		c.gridwidth=2;
		c.insets = new Insets(10,10,10,10);
		jp1.add(jl10,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =2;
		c.gridy =0;
		c.gridwidth=1;
		c.insets = new Insets(10,10,10,10);
		jp1.add(jl11,c);
		
		JSeparator jsep1 = new JSeparator(JSeparator.HORIZONTAL);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =0;
		c.gridy =1;
		c.gridwidth=3;
		c.insets = new Insets(10,10,10,10);
		jp1.add(jsep1,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =0;
		c.gridy =2;
		c.gridwidth=1;
		c.insets = new Insets(10,10,0,0);
		jp1.add(bttn1,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =1;
		c.gridy =2;
		c.gridwidth=1;
		c.insets = new Insets(10,10,0,0);
		jp1.add(bttn2,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =2;
		c.gridy =2;
		c.gridwidth=1;
		c.insets = new Insets(10,10,0,10);
		jp1.add(bttn4,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =0;
		c.gridy =3;
		c.gridwidth=1;
		c.insets = new Insets(10,10,10,0);
		jp1.add(bttn5,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =1;
		c.gridy =3;
		c.gridwidth=1;
		c.insets = new Insets(10,10,10,0);
		jp1.add(bttn6,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =2;
		c.gridy =3;
		c.insets = new Insets(10,10,10,10);
		jp1.add(bttn7,c);
		
		JSeparator jsep = new JSeparator(JSeparator.HORIZONTAL);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx =0;
		c.gridy =4;
		c.gridwidth=3;
		c.insets = new Insets(10,10,10,10);
		jp1.add(jsep,c);
		//Panel 2 for adding expense 
		jp2 = new JPanel();
		jp2.setLayout(new SpringLayout());
		jp2.add(jl1);
		jp2.add(jcb1);
		jp2.add(jl2);
		jp2.add(jtf1);
		jp2.add(jl3);
		jp2.add(dp1);
		jp2.add(jl4);
		jp2.add(new JScrollPane(jta1));
		jp2.add(new JLabel("Developed By Shashi "));
		jp2.add(bttn3);
		SpringUtilities.makeCompactGrid(jp2,5,2,6,6,6,6);
		
		// Panel 3 for report showing
		jp3 = new JPanel();
		jp3.setLayout(new BorderLayout());
		jp3.add(new JScrollPane(jta2));
				
		//Panel 4 for showing months for report generation
		jp4 = new JPanel();
		addMonths();
			
		//Panel 5 for settings
		jp5 = new JPanel();
		jp5.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx =0;
		c1.gridy =0;
		c1.insets = new Insets(10,10,10,10);
		jp5.add(jl5,c1);
		
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx =1;
		c1.gridy =0;
		c1.insets = new Insets(10,10,10,10);
		jp5.add(jtf2,c1);
		
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx =0;
		c1.gridy =1;
		c1.insets = new Insets(10,10,10,10);
		jp5.add(jl9,c1);
		
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx =1;
		c1.gridy =1;
		c1.insets = new Insets(10,10,10,10);
		jp5.add(jtf3,c1);
		
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx =0;
		c1.gridy =2;
		c1.gridwidth=2;
		c1.insets = new Insets(10,10,10,10);
		jp5.add(bttn9,c1);
		
		//Panel 6 for specific day expense
		jp6 = new JPanel();
		jp6.setLayout(new FlowLayout());
		jp6.add(dp2);
		jp6.add(bttn8);
		
		//Panel 7 for report generation choice
		jp7 = new JPanel();
		jp7.setLayout(new FlowLayout());
		jp7.add(jr1);
		jp7.add(jl6);
		jp7.add(jr2);
		jp7.add(jl7);
		jp7.add(jr3);
		jp7.add(jl8);

		//Panel for removing expense
		jp8 = new JPanel();
		jlist1 = new JList();
		
		jlist1.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent a) {
				// TODO Auto-generated method stub
				System.out.println(a.getValueIsAdjusting());
				if(a.getValueIsAdjusting())
				{
					String fin = (String)jlist1.getSelectedValue();
					fil.removeExpense(fin);
					JOptionPane.showMessageDialog(parent,"Deleted The Expense "+fin);
				}
			}
		});
		jp8.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx =0;
		c2.gridy =0;
		c2.gridheight=2;
		c2.insets = new Insets(10,10,10,10);
		jp8.add(new JScrollPane(jlist1),c2);
		
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx =1;
		c2.gridy =0;
		c2.gridheight=1;
		c2.insets = new Insets(10,10,10,10);
		jp8.add(dp3,c2);
		
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx =1;
		c2.gridy =1;
		c2.insets = new Insets(10,10,10,10);
		jp8.add(bttn10,c2);
		//About Parent window
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		add(jp1);
		add(jp2);
		setSize(400,400);
		setTitle("ExpenSlogger");
		setLocation(500,150);
		//ImageIcon icon = new ImageIcon("D:\\MyProject\\MyApplications\\expensemanager.png");
		//setIconImage(icon.getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		pack();
	}
	
	
	public void addMonths()
	{
		String [] months = {"January","February","March","April","May","June",
				"July","August","September","October","November","December"};
		JLabel [] lbl = new JLabel[12];
		JRadioButton [] rdb = new JRadioButton[12];
		ButtonGroup bg = new ButtonGroup();

		jp4.setLayout(new GridLayout(13,2));
		
		final JComboBox jcb2 = new JComboBox();
		for(int i=2010;i<2020;i++)
			jcb2.addItem(String.valueOf(i));
		
		JLabel jl5 = new JLabel("Select Year");
		jp4.add(jl5);
		jp4.add(jcb2);

		for(int i=0;i<months.length;i++)
		{
			lbl[i] = new JLabel(months[i]);
			jp4.add(lbl[i]);
			rdb[i] = new JRadioButton();
			rdb[i].setActionCommand(String.valueOf(i+1));
			bg.add(rdb[i]);
			rdb[i].addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent a) {
					// TODO Auto-generated method stub
					strRepMonth = a.getActionCommand();
					fil.generateReport(true,strRepMonth+"/"+((String) jcb2.getItemAt(jcb2.getSelectedIndex())).substring(2,4));
					reporter();
					parent.remove(jp2);
					parent.add(jp3);
					parent.remove(jp4);
					parent.remove(jp5);
					parent.remove(jp6);
					parent.remove(jp7);
					parent.remove(jp8);
					parent.pack();
					
				}
			});
			jp4.add(rdb[i]);
			
		}

	}
	
	private void dailyReportWriting(Map<String,String> map,String string)
	{
		jta2.setText("");
		jta2.append(rb.getString("FOP.reportfl")+"\n");
		jta2.append("REPORT FOR DAY "+string+"\n");
		jta2.append(rb.getString("FOP.reportfl")+"\n");
		
		for(Map.Entry<String, String> me: map.entrySet())
		{
		
			jta2.append(me.getKey()+"<----->"+me.getValue()+"\n");
			
		}
		
		jta2.append(rb.getString("FOP.msgexpenditure") + fil.total+"\n");
		jta2.append("Your Monthly Budget is Set Rs." + fil.mapConfig.get("MonthlyBudget"));
	}
	public void reporter()
	{
		
		List<String> replist = fil.showReport();
		
		jta2.setText("");
		for(String a1 : replist)
		{
		
			jta2.append(a1+"\n");
			
		}
	}
}
