package MetTap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
public class Put 
{
	public static void putContents(List<String> ContentList,File file)
	{
		try 
		{
			if(file.exists())
			{
				file.delete();
				file.createNewFile();
			}	
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < ContentList.size(); i++)
			{
				writer.write(ContentList.get(i));
				writer.newLine();
			}
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
