import java.util.ArrayList;
import java.util.Arrays;

/****
 * 
 * @author SiyuanWang
 * 基于文档频率特征选取
 *
 */
public class FreqCharBook {
	private String[] getTrainSentences;
	
	private ArrayList<String> charbook;
	
	public FreqCharBook(ArrayList<String> charbook, String[] trainSentences){
		this.getTrainSentences = trainSentences; 
		this.charbook = charbook;
	}

	public String[] getTrainSentences(){
		return this.getTrainSentences;
	}
	
	//计算每个字出现的句数，并剔除出现次数最少的部分特征
	public ArrayList<String> freqCharBook(double cut){
		ArrayList<Integer> charFreq = new ArrayList<>();
		ArrayList<String> newFreqCharbook = new ArrayList<>();
		
		/*for(int i = 0; i < this.charbook.size(); i++){
			charFreq.add(0);
		}
		
		for(int line = 0; line < this.getTrainSentences().length; line++){
			String thisSentence = this.getTrainSentences()[line];
			ArrayList<Integer> freqAdd = new ArrayList<>();
			for(int i = 0; i < thisSentence.length(); i++){
				int index = this.charbook.indexOf(thisSentence.charAt(i));
				if(!freqAdd.contains(index)){
					charFreq.set(index, charFreq.get(index)+1);
					freqAdd.add(index);
				}
			}
		}*/
		
		for(int j = 0; j < this.charbook.size(); j++){
			int num = 0;
			for(int line = 0; line < this.getTrainSentences().length; line++){
				String thissentence = this.getTrainSentences()[line];
				if(thissentence.contains(""+this.charbook.get(j))){
					num++;
				}
			}
			charFreq.add(num);
		}
		
		
		Object[] charFreqArray = charFreq.toArray();
		int length = charFreqArray.length;
		Arrays.sort(charFreqArray);
		
		for(int i = 0; i < this.charbook.size()*(1-cut); i++){
			int index = charFreq.indexOf(charFreqArray[length-1-i]);
			newFreqCharbook.add(this.charbook.get(index));
			charFreq.set(index, -1);
		}
		return newFreqCharbook;
	}
	
}
