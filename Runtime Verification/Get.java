package MetTap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Get 
{
	public static List<String> getContents(File file)
	{
		List<String> ConList = new ArrayList<String>();
		try 
		{
			if(!file.exists())
			{
				try 
				{
					file.createNewFile();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}				
			}
			FileReader in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			try 
			{
				String con = reader.readLine();
				while(con != null)
				{
					ConList.add(con);
					con = reader.readLine();
				}
				reader.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ConList;
	}
}
