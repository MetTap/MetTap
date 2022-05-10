package MetTap;
import java.io.File;
import java.util.ArrayList;
import java.io.*;
import java.util.*;
import jxl.*;
import java.util.List;
public class Trace_Slice 
{
	public  double[][] readSpecifyColumns(String s) throws Exception
	{
		int rsColumns ;  
		int rsRows;		
		int t1,t2,t3;
		List<String> feaList= new ArrayList<String>();
		File file=new File("src/SensorData.xls"); 				
		ArrayList<String> trList = new ArrayList<String>();
		ArrayList<String> trsList = new ArrayList<String>();
		ArrayList<String> actList = new ArrayList<String>();
		Workbook readwb = null;
		InputStream io = new FileInputStream(file.getAbsoluteFile());
		readwb = Workbook.getWorkbook(io);
		Sheet readsheet = readwb.getSheet(0);
		rsColumns = readsheet.getColumns();  
		rsRows = readsheet.getRows(); 
		
		for (int i = 1; i < rsColumns; i++) 
		{
			Cell fea = readsheet.getCell(i, 0);
			feaList.add(fea.getContents());
		}
		String rawfea[]=s.split("-");
		t1=transCount(rawfea[0],feaList);
		t2=transCount(rawfea[1],feaList);
		t3=transCount(rawfea[2],feaList);				
		for (int i = 1; i < rsRows; i++) 
		{			
			Cell activity = readsheet.getCell(t1, i);  
			trList.add(activity.getContents());			
			Cell window = readsheet.getCell(t2,i);  
			trsList.add(window.getContents());			
			Cell curtain = readsheet.getCell(t3,i);  
			actList.add(curtain.getContents());			
		}
		for(int i = 0; i < trList.size(); i++)
		{
			if(trList.get(i).equals("on"))
				trList.set(i, "1");
			else if(trList.get(i).equals("off"))
				trList.set(i, "0");
			
			if(trsList.get(i).equals("on"))
				trsList.set(i, "1");
			else if(trsList.get(i).equals("off"))
				trsList.set(i, "0");
			
			if(actList.get(i).equals("on"))
				actList.set(i, "1");
			else if(actList.get(i).equals("off"))
				actList.set(i, "0");

		}		
						
		double data[][]=new double[3][rsRows];
		data[0] = trList.stream().mapToDouble(Double::parseDouble).toArray();
		data[1]= trsList.stream().mapToDouble(Double::parseDouble).toArray();
		data[2]= actList.stream().mapToDouble(Double::parseDouble).toArray();
		return data;
	}
	public  int transCount(String s, List list)
	{
		int count=0;
		String f[]=new String[list.size()];
		for(int i=0;i<list.size();i++)
			f[i]=(String) list.get(i);
		for(int i=0; i<f.length; i++)
		{
			if(s.equals(f[i]))
				count=i;
			break;
		}
		return count+1;
	}
}


