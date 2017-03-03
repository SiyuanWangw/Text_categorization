import java.util.ArrayList;

/****
 * 
 * @author SiyuanWang
 * down sampling for imbalance data
 *
 */
public class DownSample {
	private String[] sentences;
	private String[] labels;
	private ArrayList<String> allLabels;
	
	public DownSample(ArrayList<String[]> trainList){
		this.sentences = trainList.get(0);
		this.labels = trainList.get(1);
		this.allLabels = computeAllLabels();
	}
	
	//得到所有Labels的种类
	public ArrayList<String> computeAllLabels(){
		ArrayList<String> alllabels = new ArrayList<>();
		for(int i = 0; i < this.labels.length; i++){
			if(!alllabels.contains(this.labels[i])){
				alllabels.add(this.labels[i]);
			}
		}
		//System.out.println(alllabels.toString());
		return alllabels;	
	}
	
	public ArrayList<String> getAllLabels(){
		return this.allLabels;
	}
	
	//将所有training set分到不同的Label类中
	public ArrayList<ArrayList<String>> getTypeSentences(){
		ArrayList<ArrayList<String>> allTypeSentences = new ArrayList<>();
		for(int i = 0; i < this.allLabels.size(); i++){
			ArrayList<String> thisTypeSentences = new ArrayList<>();
			allTypeSentences.add(thisTypeSentences);
		}
		
		for(int i = 0; i < this.sentences.length; i++){
			int index = this.allLabels.indexOf(this.labels[i]);
			allTypeSentences.get(index).add(this.sentences[i]);
		}
		return allTypeSentences;
	}
	
	//根据数目N选出每种Label的N个句子，使样本较为均匀
	public ArrayList<String[]> getTypeList(int sampleNum){
		
		ArrayList<ArrayList<String>> allTypeSentences = getTypeSentences();
		ArrayList<String> sampleSentences = new ArrayList<>();
		ArrayList<String> sampleLabels = new ArrayList<>();
		for(int i = 0; i < allTypeSentences.size(); i++){
			for(int j = 0; j < sampleNum && j < allTypeSentences.get(i).size(); j++){
				sampleSentences.add(allTypeSentences.get(i).get(j));
				sampleLabels.add(this.allLabels.get(i));
			}
		}
		ArrayList<String[]> list = new ArrayList<>();
		list.add(sampleSentences.toArray(new String[sampleSentences.size()]));
		list.add(sampleLabels.toArray(new String[sampleLabels.size()]));
		
		return list;
	}
}
