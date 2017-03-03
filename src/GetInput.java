import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/***
 * 
 * @author SiyuanWang
 * Read data for file
 *
 */
public class GetInput {
	public static String regex = "[^\u4E00-\u9FFF]";
	private String filename;
	
	public GetInput(String filename){
		this.filename = filename;
	}
	
	public void setFilename(String filename){
		this.filename = filename;
	}
	
	//读出数据并生成sentences和labels
	public ArrayList<String[]> getInput(int rate){
		 File file = new File(filename);
		 ArrayList<String[]> list = new ArrayList<>();
		 
		 try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			ArrayList<String> allsentences = new ArrayList<>();
			ArrayList<String> alllabels = new ArrayList<>();
			
			ArrayList<String> sentences = new ArrayList<>();
			ArrayList<String> labels = new ArrayList<>();
			
			String thisline = null;
			while((thisline = reader.readLine()) != null){
				String[] seperate = thisline.split("\\\\t");
				alllabels.add(seperate[0]);
				allsentences.add(seperate[1].replaceAll(regex, ""));	
			}
			
			if(rate != 1){
				int getNum = (int) (allsentences.size()/rate);
				for(int i = 0; i < getNum; i++){
					//int index = (int) (Math.random()*(allsentences.size()-1));
					sentences.add(allsentences.get(i));
					labels.add(alllabels.get(i));
					//allsentences.remove(index);
					//alllabels.remove(index);
				}
				list.add(sentences.toArray(new String[sentences.size()]));
				list.add(labels.toArray(new String[labels.size()]));
			}
			else{
				list.add(allsentences.toArray(new String[allsentences.size()]));
				list.add(alllabels.toArray(new String[alllabels.size()]));
			}
			
			reader.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
