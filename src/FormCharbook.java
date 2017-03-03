import java.util.ArrayList;

/****
 * 
 * @author SiyuanWang
 * generate dictionary
 *
 */
public class FormCharbook {
	private String[] sentences;
	private ArrayList<Integer> charNumber = new ArrayList<>();
	//private ArrayList<Character> freq = new ArrayList<>();
	
	public FormCharbook(String[] sentences){
		this.sentences = sentences;
	}
	
	public ArrayList<String> getCharbook(){
		ArrayList<Character> allcChars = new ArrayList<>();
		ArrayList<String> charbook = new ArrayList<>();
 		
		for(int line = 0; line < sentences.length; line++){
			String thisSentence = sentences[line];
			for(int i = 0; i < thisSentence.length(); i++){
				if(allcChars.contains(thisSentence.charAt(i))){
					int index = allcChars.indexOf(thisSentence.charAt(i));
					charNumber.set(index, charNumber.get(index)+1);
				}
				else{
					allcChars.add(thisSentence.charAt(i));
					charNumber.add(1);
				} 
			}
		}
		
		//停用词
		/*String stopStr = "";
		try {
			File file = new File("stop_words_ch.txt");
	    	BufferedReader reader = new BufferedReader(new FileReader(file));
	    	String thisline = null;
			while((thisline = reader.readLine()) != null){
				stopStr += thisline;	
			}
			reader.close();
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Character> stopChars = new ArrayList<>();
		for(int i = 0; i < stopStr.length(); i++){
			if(!stopChars.contains(stopStr.charAt(i))){
				stopChars.add(stopStr.charAt(i));
			}
		}
		System.out.println(stopChars.size()+stopChars.toString());*/
		
		for(int j = 0; j < allcChars.size(); j++){
			if(charNumber.get(j) > 0){//基于字频率
				charbook.add(allcChars.get(j)+"");
			}
		}
		return charbook;
	}
	
	public ArrayList<Integer> getNumber(){
		return charNumber;
	}
}
