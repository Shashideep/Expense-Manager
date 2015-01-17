package app;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FileOperations {

	private List<String> list;


	private Pattern p;
	private Matcher m;
	public Integer total;
	public static final String category="Category.txt";
	public static final String expenses="Expenses.txt";
	public static final String report="Report.txt";
	
	private String strCategoryFilePath;
	private String strExpensesFilePath;
	private String strReportFilePath;
	public Map<String,String> mapConfig;
	public FileOperations(String userHomeDirPath)
	{
		p = Pattern.compile("[0-9aA-zZ\\s]*[^$]");
		
		File f = new File(userHomeDirPath + category);
		
		if(!f.exists()){
			try {
				f.createNewFile();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		File f1 = new File(userHomeDirPath + expenses);
		
		if(!f1.exists()){
			try {
				f1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		File f2 = new File(userHomeDirPath + report);
		
		if(!f2.exists()){
			try {
				f2.createNewFile();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		this.strCategoryFilePath = userHomeDirPath + category;
		this.strExpensesFilePath = userHomeDirPath + expenses;
		this.strReportFilePath = userHomeDirPath + report;
		
		readConfig();
		
	}
	public String readConfig()
	{
		Properties propt = new Properties();
		String prop="";
		this.mapConfig = new HashMap<String, String>(); 
		try {
			propt.load(new FileInputStream(new File("D:\\Config.properties")));
			
		for(Entry<Object, Object> me : propt.entrySet())
		{
			this.mapConfig.put((String)me.getKey(), (String)me.getValue());
		}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}
	private void process(List<String> exp)
	{
		int temp=0;
		for(String i :exp)
		{
			m=p.matcher(i);
			m.find();
			m.find();
			temp = temp + Integer.parseInt(m.group());
		}
	
		this.total=temp;
	
	}
	public void doAudit()
	{
		
		List<String> exp = generateExpenseList(strExpensesFilePath);
			process(exp);
	}
	private boolean validateCategory(String strCat)
	{
		loadCategories();
		if(list.contains(strCat))
			return true;
		return false;
	}
	public boolean addCategory(String strCat)
	{
		BufferedWriter bw = null;
		try
		{
			if((strCat !=null && !strCat.equalsIgnoreCase("")) && !validateCategory(strCat))
			{
				FileOutputStream fos = new FileOutputStream(new File(strCategoryFilePath),true);
				bw = new BufferedWriter(new OutputStreamWriter(fos));

				
			
				bw.write(strCat.trim());
				
				bw.newLine();
				bw.close();
				
			}
			else
				return false;
			//return true;

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	public List<String> loadCategories()
	{
		BufferedReader br = null;
		list = new ArrayList<String>();

		try {
			FileInputStream fis = new FileInputStream(new File(strCategoryFilePath));
			br = new BufferedReader(new InputStreamReader(fis));

			String strCat =  "";

			while((strCat=br.readLine())!=null)
			{
				list.add(strCat);
			}
			
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return list;

	}
	public void removeCategory(String currentCat) {
		// TODO Auto-generated method stub
	

		
		BufferedWriter bw = null;
		
		try {
			FileOutputStream fos = new FileOutputStream(new File(strCategoryFilePath));
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(String i : list)
			{
				bw.write(i);
				bw.newLine();
			}	
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean addExpense(String expense) {
		// TODO Auto-generated method stub
	
		BufferedWriter bw = null;
		try
		{
			if(expense !=null && !expense.equalsIgnoreCase(""))
			{
				FileOutputStream fos = new FileOutputStream(new File(strExpensesFilePath),true);
				bw = new BufferedWriter(new OutputStreamWriter(fos));

				bw.write(expense.trim());
				bw.newLine();
				bw.flush();
				bw.close();
			}
			else
				return false;
			//return true;

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	public List<String> generateExpenseList(String strFilePath)
	{
		List<String> lst = new ArrayList<String>();
	
		BufferedReader br = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(strFilePath));
			br = new BufferedReader(new InputStreamReader(fis));
			String strCat =  "";

			while((strCat=br.readLine())!=null)
			{
				lst.add(strCat);
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return lst;
	}
	public void generateReport(boolean isCurrentMonth,String strMonth) {
		// TODO Auto-generated method stub
	
		Map<String,String> map = null;
		List<String> lst = new ArrayList<String>();
	
	
		lst = generateExpenseList(strExpensesFilePath);
		if(isCurrentMonth){
			lst = extractMonthExpense(lst,strMonth);
		}
			map = processing(lst);	
		FileOutputStream fos = null;
		BufferedWriter bw =null;
		
		try
		{
			fos = new FileOutputStream(new File(strReportFilePath));
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			bw.write(MainWindow.rb.getString("FOP.reportfl"));
			bw.newLine();
			
			//System.out.println("Parsed-->" + Integer.parseInt(strMonth.substring(0,2)));
			
			if(isCurrentMonth)
			bw.write(MainWindow.rb.getString("FOP.report")+ getMonthForReport(Integer.parseInt(strMonth.substring(0,strMonth.indexOf('/'))),"") + 
					"20"+strMonth.substring(strMonth.indexOf('/')+1, strMonth.length()));
			else
			bw.write(MainWindow.rb.getString("FOP.msgtotalreport"));
			bw.newLine();

			bw.write(MainWindow.rb.getString("FOP.reportfl"));
			bw.newLine();
			
			
			for (Map.Entry<String, String> me : map.entrySet())
			{
				bw.write(me.getKey()+MainWindow.rb.getString("FOP.separator")+me.getValue());
				bw.newLine();
			}
			
			bw.write(MainWindow.rb.getString("FOP.msgexpenditure") + this.total);
			bw.newLine();
			bw.write("Your Monthly Budget is Set Rs." + mapConfig.get("MonthlyBudget"));
			
			bw.close();
		} 
		catch(Exception e){
			
		}
			
	}
	
	private List<String> extractMonthExpense(List<String> lst,String strMonth) {
		// TODO Auto-generated method stub
		
		List<String> toRem = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		
		
		for(String du : lst)
		{
			if(!du.contains(strMonth))
			{
				toRem.add(du);
			}
		}
		
		for(String du : toRem)
			lst.remove(du);
		return lst;
	}
	public List<String> showReport()
	{
		return generateExpenseList(strReportFilePath);
	}
	
	
	public Map<String,String> generateDayReport(String strOfDate)
	{
	
		List<String> lstExp = generateExpenseList(strExpensesFilePath);
		List<String> lstDaily = new ArrayList<String>();
		
		
		for(String du : lstExp)
		{
			if(du.contains(strOfDate))
				lstDaily.add(du);
		}
		
		return processing(lstDaily);
		
	}
	private String getMonthForReport(int month,String strYear)
	{
		String strMonth = "";
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	
		if(strYear.equalsIgnoreCase("year"))
		{
			strMonth = String.valueOf(cal.get(Calendar.YEAR));
			return strMonth;
		}
		else
		{
	
		switch(month)
		{
		case 1:strMonth = "January ";
			break;
		case 2:strMonth = "February ";
			break;
		case 3:strMonth = "March ";
			break;
		case 4:strMonth = "April ";
			break;
		case 5:strMonth = "May ";
			break;
		case 6:strMonth = "June ";
			break;
		case 7:strMonth = "July ";
			break;
		case 8:strMonth = "August ";
			break;
		case 9:strMonth = "September ";
			break;
		case 10:strMonth = "October ";
			break;
		case 11:strMonth = "November ";
			break;
		case 12:strMonth = "December ";
			break;
		default: strMonth = "For Demo";
			
		}
		}
		

		return strMonth;
	}
	
/*	private boolean isMonthEnd()
	{
		Calendar cal = Calendar.getInstance();
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		cal.setTime(today);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		
		String end = sdf.format(cal.getTime());
		String strtoday = sdf.format(today);
		
		return strtoday.equalsIgnoreCase(end);
		
	}*/
	private Map<String, String> processing(List<String> lst) {
	
		
		Map<String, String> map = new TreeMap<String, String>();
		
		Pattern p = Pattern.compile("[0-9a-zA-Z]*[^$]");
		
		for(String tok :lst)
		{
			Matcher m = p.matcher(tok);
			String key="";
			String val="";

			m.find();
				key = m.group();
			m.find();
			 val = m.group();
			 
			
			
			if(map.containsKey(key))
			{
				Integer temp = Integer.parseInt(map.get(key));
				map.remove(key);
				
				map.put(key,String.valueOf(temp + Integer.parseInt(val)));
			}
			else
				map.put(key, val);
			
		}
		return map;
	}
	

	
	public List<String> populateExpenseForDate(String date)
	{
		List<String> lstone = generateExpenseList(strExpensesFilePath);
		
		List<String> lsttwo = new ArrayList<String>();
		
		
		for(String du : lstone)
		{
			if(du.contains(date))
			{
				lsttwo.add(du.replace("$"," "));
			}
		}
		
		return lsttwo;
	}
public void removeExpense(String toRem)
	{
		List<String> l = generateExpenseList(strExpensesFilePath);
		
	
			
			List<String> toRemove = new ArrayList<String>();
		for(String dummy:l)
		{
			String ksi = dummy.replace("$"," ");
			System.out.println(ksi);
			System.out.println(toRem);
			if(ksi.contains(toRem))
				toRemove.add(dummy);
		}
		
		for(String dummy:toRemove)
			l.remove(dummy);
		writeListToFile(l);
}
private void writeListToFile(List<String> l) {
		// TODO Auto-generated method stub
	BufferedWriter bw = null;
	
	try {
		FileOutputStream fos = new FileOutputStream(new File(strExpensesFilePath));
		bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		for(String i : l)
		{
			bw.write(i);
			bw.newLine();
		}	
		bw.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
	}
public List<String> alertGen() {
	List<String>	outlst = new ArrayList<String>(); 
	
	
	List<String> lst = generateExpenseList(strExpensesFilePath);
	
	Pattern p = Pattern.compile("[0-9a-zA-Z/\\s]*[^$]");
	
	for(String tok :lst)
	{
		Matcher m = p.matcher(tok);
		String key="";
		String val="";

		m.find();
			key = m.group();
		m.find();
		 val = m.group();
		 
		 m.find();
		 String remark = m.group();
		 m.find();
		 
		
		if(key.equalsIgnoreCase("lend")||key.equalsIgnoreCase("borrow"))
			outlst.add(key+" "+val+" "+remark);
	}
		 
		return outlst;
	}


public void saveConfig(List<String> lstConfig)
{
	try {
		FileOutputStream fos = new FileOutputStream(new File("D:\\Config.properties"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		for(String du : lstConfig){
			bw.write(du);
			bw.newLine();
		}
		bw.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
