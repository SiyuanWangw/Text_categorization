import java.util.ArrayList;
import java.util.Arrays;

/****
 * 
 * @author SiyuanWang
 * Information Gain
 *
 */
public class Gain {
	private String[] getTrainSentences;
	private String[] getTrainLabels;
	
	private ArrayList<String> charbook;
	private ArrayList<String> allLabels;
	
	public Gain(ArrayList<String> charbook, ArrayList<String[]> trainList){
		this.getTrainSentences = trainList.get(0); 
		this.getTrainLabels = trainList.get(1);
		this.charbook = charbook;
	}

	public String[] getTrainSentences(){
		return this.getTrainSentences;
	}
	
	public String[] getTrainLabels(){
		return this.getTrainLabels;
	}
	
	public ArrayList<String> getAllLabels(){
		return this.allLabels;
	}
	
	public void setAllLabels(ArrayList<String> allLabels){
		this.allLabels = allLabels;
	}

	//得到所有Labels的种类
	public ArrayList<String> computeAllLabels(String[] labels){
		ArrayList<String> alllabels = new ArrayList<>();
		for(int i = 0; i < labels.length; i++){
			if(!alllabels.contains(labels[i])){
				alllabels.add(labels[i]);
			}
		}
		return alllabels;	
	}
	
	//得到各Label的数量
	public int[] getLabelsNum(String[] labels, ArrayList<String> alllabels){
		int[] labelsNum = new int[alllabels.size()];
		for(int i = 0; i < labels.length; i++){
			int index = alllabels.indexOf(labels[i]);
			labelsNum[index]++;
		}
		return labelsNum;	
	}
	
	//计算每个特征的信息增益值，并选出前N个
	public ArrayList<String> gainCharBook(int charNum){
		ArrayList<String> gainCharbook = new ArrayList<>();
 		
		setAllLabels(computeAllLabels(getTrainLabels()));
		int[] alllabelsNum = getLabelsNum(getTrainLabels(), getAllLabels());
		/*for(int i = 0; i < getAllLabels().size(); i++){
			System.out.println(getAllLabels().get(i)+"  "+alllabelsNum[i]);
		}*/
		double Hc = 0;
		double allNum = getTrainLabels().length;
		for(int i = 0; i < alllabelsNum.length; i++){
			double rate = (alllabelsNum[i]+0.01)/allNum;
			Hc = Hc + rate*Math.log(rate);
		}
		
		ArrayList<Double> gainValue = new ArrayList<>();
		for(int i = 0; i < charbook.size(); i++){
			String thisCharacter = "" + charbook.get(i);
			int[] arrayA = new int[alllabelsNum.length];
			int[] arrayB = new int[alllabelsNum.length];
			for(int j = 0; j < getTrainSentences().length; j++){
				String thisSentence = getTrainSentences()[j];
				String thisLabel = getTrainLabels()[j];
				if(thisSentence.contains(thisCharacter)){
					int index = getAllLabels().indexOf(thisLabel);
					arrayA[index]++;
				}
			}
			double allNumA = 0;
			for(int k = 0; k < arrayB.length; k++){
				allNumA += arrayA[k];
				arrayB[k] = alllabelsNum[k] - arrayA[k];
			}
			double allNumB = allNum - allNumA;
			
			double HcA = 0;
			double HcB = 0;
			for(int m = 0; m < alllabelsNum.length; m++){
				double rateA = (arrayA[m]+0.01)/allNumA;
				HcA = HcA + rateA*Math.log(rateA);
				double rateB = (arrayB[m]+0.01)/allNumB;
				HcB = HcB + rateB*Math.log(rateB);
			}
			double thisGainValue = -Hc + allNumA/allNum*HcA + allNumB/allNum*HcB;
			gainValue.add(thisGainValue); 
		}
		
		Object[] gainArray = gainValue.toArray();
		int length = gainArray.length;
		Arrays.sort(gainArray);
		for(int n = 0; n < gainArray.length && n < charNum; n++){
			int index = gainValue.indexOf(gainArray[length-n-1]);
			gainValue.set(index, (double) -1);
			//System.out.println(gain.charbook.get(index));
			gainCharbook.add(charbook.get(index));
		}
		
		return gainCharbook;
	}
}
