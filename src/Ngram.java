import java.util.ArrayList;

/****
 * 
 * @author SiyuanWang
 * N-Gram
 *
 */
public class Ngram {
	private String[] sentences;
	//private ArrayList<Integer> charNumber = new ArrayList<>();
	
	public Ngram(String[] sentences){
		this.sentences = sentences;
	}
	
	//Bi-gram and Tri-gram
	public ArrayList<String> getNgramCharbook(){
		ArrayList<String> allcChars = new ArrayList<>();
		ArrayList<String> gramChars = new ArrayList<>();
		ArrayList<String> triGramChars = new ArrayList<>();
		ArrayList<String> charbook = new ArrayList<>();
 		
		for(int line = 0; line < sentences.length; line++){
			String thisSentence = sentences[line];
			for(int i = 0; i < thisSentence.length(); i++){
				if(!allcChars.contains(thisSentence.charAt(i)+"")){
					allcChars.add(thisSentence.charAt(i)+"");
				}
			}
			//Bi-gram
			for(int i = 1; i < thisSentence.length(); i++){
				String nowNgramChar = ("" + thisSentence.charAt(i-1)) + thisSentence.charAt(i);
				if(!gramChars.contains(nowNgramChar)){
					gramChars.add(nowNgramChar);
				}
			}
			//Tri-gram
			for(int i = 2; i < thisSentence.length(); i++){
				String nowNgramChar = ("" + thisSentence.charAt(i-2)) + thisSentence.charAt(i-1) + thisSentence.charAt(i);
				if(!triGramChars.contains(nowNgramChar)){
					triGramChars.add(nowNgramChar);
				}
			}
		}
		
		for(int j = 0; j < allcChars.size(); j++){
			charbook.add(allcChars.get(j));
		}
		for(int k = 0; k < gramChars.size(); k++){
			charbook.add(gramChars.get(k));
		}
		for(int m = 0; m < triGramChars.size(); m++){
			charbook.add(triGramChars.get(m));
		}
		return charbook;
	}
	
	/*public ArrayList<Integer> getNumber(){
		return charNumber;
	}*/
}
