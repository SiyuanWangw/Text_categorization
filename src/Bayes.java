import java.util.ArrayList;

/****
 * 
 * @author SiyuanWang
 * 朴素贝叶斯
 *
 */
public class Bayes {
	private GetInput trainInput = new GetInput("train_data.char.ed");
	//private GetInput valInput = new GetInput("val_data.char.ed");
	private GetInput testInput = new GetInput("test_data.char.ed");
	private ArrayList<String[]> trainList = trainInput.getInput(50);
	//private ArrayList<String[]> valList = valInput.getInput(1);
	private ArrayList<String[]> testList = testInput.getInput(50);
	private String[] getTrainSentences;
	//private String[] getValSentences;
	private String[] getTestSentences;
	
	private String[] getTrainLabels;
	private String[] trueLabels;
	
    private ArrayList<String> charbook;
    private ArrayList<String> allLabels;
	
	public Bayes(){
		//this.setDownSample(4000);
		
		this.getTrainSentences = this.trainList.get(0); 
		//this.getValSentences = this.valList.get(0);
		this.getTestSentences = this.testList.get(0);
		this.getTrainLabels = this.trainList.get(1);
		this.trueLabels = this.testList.get(1);
		//this.charbook = (new FormCharbook(getTrainSentences)).getCharbook();
		this.charbook = (new Ngram(this.getTrainSentences)).getNgramCharbook();
		//this.setGainCharbook();
		//this.setFreqCharbook(0.02);
		//this.setChisquare(800);
	}
	
	//Information Gain
	public void setGainCharbook(){
    	Gain gain = new Gain(this.charbook, this.trainList);
    	this.charbook = gain.gainCharBook(8000);
    }
	
	//基于文档频率
	public void setFreqCharbook(double cut){
    	FreqCharBook charsFreq = new FreqCharBook(this.charbook, this.getTrainSentences);
    	this.charbook = charsFreq.freqCharBook(cut);
    }
	
	//卡方检验
	 public void setChisquare(int chisquareNum){
	    	Chisquare_test chisquare_test = new Chisquare_test(this.charbook, this.trainList);
	    	this.charbook = chisquare_test.setChisquare(chisquareNum);
	 }
	 
	 //下采样
	 public void setDownSample(int sampleNum){
	    	DownSample downSample = new DownSample(this.trainList);
	    	this.trainList = downSample.getTypeList(sampleNum);	 
	    }
	
	public String[] getTrainSentences(){
		return this.getTrainSentences;
	}
	
	/*public String[] getValSentences(){
		return this.getValSentences;
	}*/
	
	public String[] getTestSentences(){
		return this.getTestSentences;
	}
	
	public String[] getTrainLabels(){
		return this.getTrainLabels;
	}
	
	public String[] getTrueLabels(){
		return this.trueLabels;
	}
	
	public ArrayList<String> getAllLabels(){
		return this.allLabels;
	}
	
	//得到所有Labels的种类
	public ArrayList<String> computeAllLabels(String[] sentences, String[] labels){
		ArrayList<String> alllabels = new ArrayList<>();
		for(int i = 0; i < sentences.length; i++){
			if(!alllabels.contains(labels[i])){
				alllabels.add(labels[i]);
			}
		}
		return alllabels;
		
	}
	
	//生成Bayes Matrix
	public Matrix formBayesMatrix(String[] sentences, String[] labels){
		this.allLabels = computeAllLabels(sentences, labels);
    	Matrix bayesMatrix = new Matrix(allLabels.size(), charbook.size());
    	for (int i = 0; i < sentences.length; i++) {
            int row = allLabels.indexOf(labels[i]);
            for(int j = 0; j < sentences[i].length(); j++){
            	int column = charbook.indexOf(sentences[i].charAt(j)+"");
            	if(column >= 0){
            		bayesMatrix.setToSpecifiedValue(row, column, bayesMatrix.getElement(row, column)+1);
            	}
            }
        }
    	return bayesMatrix;
    }
	
	
	public static void main(String[] args){
		Bayes bayes = new Bayes();
		Matrix bayesMatrix = bayes.formBayesMatrix(bayes.getTrainSentences, bayes.getTrainLabels);
		System.out.println(bayesMatrix.getRowNumber()+"  "+bayesMatrix.getColoumNumber());
		//System.out.println(bayes.charbook.size());
		
		float[] charNumEachLabel = bayesMatrix.sumOfEachRow();
		float totalNum = 0;
		for(int i = 0; i < charNumEachLabel.length; i++){
			totalNum += charNumEachLabel[i];
		}
		
		//计算先验概率
		double[] priorRate = new double[bayesMatrix.getRowNumber()];
		for(int i = 0; i < priorRate.length; i++){
			priorRate[i] = charNumEachLabel[i]/totalNum;
		}
		
		double[][] bayesRate = new double[bayesMatrix.getRowNumber()][bayesMatrix.getColoumNumber()];
		for(int j = 0; j < bayesMatrix.getRowNumber(); j++){
			for(int i = 0; i < bayesMatrix.getColoumNumber(); i++){
				bayesRate[j][i] = (bayesMatrix.data[j][i]+0.1)/(charNumEachLabel[j]+bayesMatrix.getColoumNumber());
			}
		}
		
		//bayesMatrix.showToConsole();
		/*for(int i = 0; i < 1000; i++){
			System.out.println(bayesRate[16][i]+"   "+bayesRate[17][i]);
		}*/
		
		//计算后验概率
		double[][] backRate = new double[bayes.trueLabels.length][bayesMatrix.getRowNumber()];
		for(int  i= 0; i < bayes.trueLabels.length; i++){
			String thisSentence = bayes.getTestSentences[i];
			for(int j = 0; j < bayesMatrix.getRowNumber(); j++){
				double eachBackRate = 1;
				for(int k = 0; k < thisSentence.length(); k++){
					int index = bayes.charbook.indexOf(thisSentence.charAt(k)+"");
					if(index >= 0){
						eachBackRate = eachBackRate * bayesRate[j][index];
					}
				}
				eachBackRate = eachBackRate*priorRate[j];
				backRate[i][j] = eachBackRate;
			}
		}
 		
		String[] valPredictLabels = bayes.getPredictLabel(backRate);
		//String[] trueValLabels = bayes.valList.get(1);
    	int[] result = bayes.computeError(bayes.trueLabels, valPredictLabels);
    	System.out.println("There are "+ result[0] +" errors in all "+result[1]+" labels; The error rate is "+ (double)result[0]/result[1]);
 
 		
	}
	
	//得到预测Label
	public String[] getPredictLabel(double[][] rate){
		String[] predictLabels = new String[rate.length];
		for(int i = 0; i < predictLabels.length; i++){
			double max = rate[i][0];
			int largestIndex = 0;
			for(int j = 1; j < rate[0].length; j++) {
				if(max < rate[i][j]) {
					max = rate[i][j];
					largestIndex = j;
				}
			}
			predictLabels[i] = this.allLabels.get(largestIndex);
		}	
		return predictLabels;
	}
	
	
	//计算各Label错误率及总错误率
	 public int[] computeError(String[] trueLabels, String[] predictLabels){
	    	int error = 0;
	    	for(int i = 0; i < trueLabels.length; i++){
	    		if(!trueLabels[i].equals(predictLabels[i])){
	    			//System.out.println(trueLabels[i]+"  "+predictLabels[i]);
	    			error++;
	    		}
	    	}
	    	int[] result = new int[2];
	    	result[0] = error;
	    	result[1] = trueLabels.length;
	    	return result;
	 }
	
}
