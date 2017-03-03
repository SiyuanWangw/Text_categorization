import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.transform.Templates;

/****
 * 
 * @author SiyuanWang
 * KNN with index matrix
 *
 */
public class Knn_index {
	private GetInput trainInput = new GetInput("train_data.char.ed");
	//private GetInput valInput = new GetInput("val_data.char.ed");
	private GetInput testInput = new GetInput("test_data.char.ed");
	private ArrayList<String[]> trainList = trainInput.getInput(30);
	//private ArrayList<String[]> valList = valInput.getInput(10);
	private ArrayList<String[]> testList = testInput.getInput(30);
	private String[] getTrainSentences;
	//private String[] getValSentences;
	private String[] getTestSentences;
	
	private String[] getTrainLabels;
	//private String[] getValLabels;
	
	private static int kBound = 20;
	
	//private FormCharbook formCharbook;
    private ArrayList<String> charbook;
    
    public Knn_index(){
    	//down sample
    	//this.setDownSample(2000);
    	
    	this.getTrainSentences = this.trainList.get(0);   	
    	//this.getValSentences = this.valList.get(0);
    	this.getTestSentences = this.testList.get(0);
    	
    	this.getTrainLabels = this.trainList.get(1);
    	//this.getValLabels = this.valList.get(1);
    	//this.charbook = (new FormCharbook(getTrainSentences)).getCharbook();
    	this.charbook = (new Ngram(this.getTrainSentences)).getNgramCharbook();
    	
    	//this.setGainCharbook();
    	//this.setFreqCharbook(0.1);
    	//this.setChisquare(800);
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
    
    //Information Gain
    public void setGainCharbook(){
    	Gain gain = new Gain(this.charbook, this.trainList);
    	this.charbook = gain.gainCharBook(21000);
    }
    
	//基于文档频率
    public void setFreqCharbook(double cut){
    	FreqCharBook charsFreq = new FreqCharBook(this.charbook, this.getTrainSentences);
    	this.charbook = charsFreq.freqCharBook(cut);
    }
    
    //generate index matrix instead of sparse matrix
    public ArrayList<ArrayList<Integer>> formMatrixs(String[] sentences){
    	ArrayList<ArrayList<Integer>> indexMatrix = new ArrayList<>();  
    	for (int i = 0; i < sentences.length; i++) {
    		ArrayList<Integer> thisArray = new ArrayList<>();
    		
            for (int j = 0; j < charbook.size(); j++) {
            	if(sentences[i].contains(""+charbook.get(j))){
            		//indexMatrix.get(i).add(j);
            		thisArray.add(j);
            	}
            }
            indexMatrix.add(thisArray);
        }
    	return indexMatrix;
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
    
    
    public static void main(String[] args){
    	Knn_index knn_index = new Knn_index();  	
    	ArrayList<ArrayList<Integer>> trainMatrix = knn_index.formMatrixs(knn_index.getTrainSentences());
    	//ArrayList<ArrayList<Integer>> valMatrix = knn_index.formMatrixs(knn_index.getValSentences());
    	ArrayList<ArrayList<Integer>> testMatrix = knn_index.formMatrixs(knn_index.getTestSentences());
    	
        System.out.println(knn_index.charbook.toString());
        System.out.println(knn_index.charbook.size());
    	System.out.println("Train sentences has "+trainMatrix.size());
    	System.out.println("Test sentences has "+testMatrix.size());
    	String[] lines = knn_index.getTrainSentences();
    	
    	//计算两句话之间的cos距离
    	int[][] distanceMatrix = new int[knn_index.getTestSentences().length][kBound];
    	double[][] cosDistance = new double[knn_index.getTestSentences().length][kBound];
    	for(int i = 0; i < knn_index.getTestSentences().length; i++){
    		ArrayList<Double> distanceRow = new ArrayList<>();
    		for(int j = 0; j < lines.length; j++){
    			//int distance = (valMatrix.getRow(i).minus(trainMatrix.getRow(j))).dotBySelf().sumInt();
    			double distance = knn_index.cosDistance(testMatrix.get(i),trainMatrix.get(j));
    			distanceRow.add(distance);
    		}
    		Object[] distanceArray = distanceRow.toArray();
    		int length = distanceArray.length;
    		Arrays.sort(distanceArray);
    		for (int k = 0; k < kBound; k++) {
    			int index = distanceRow.indexOf(distanceArray[length-k-1]);
    			distanceMatrix[i][k] = index;
    			cosDistance[i][k] = (double) distanceArray[length-k-1];
    			distanceRow.set(index, (double) -1);
			}
    		//System.out.println(distanceMatrix[i][0]+" "+cosDistance[i][0]);
    	}
    	
    	//相对cos距离
    	double[] rate = {0};
    	for(int n = 0; n < rate.length; n++){
    		String[] valPredictLabels = new String[testMatrix.size()];
        	for(int i = 0; i < testMatrix.size(); i++){
        		String[] inputLabels = new String[kBound];
        		double[] inputCosDistance = new double[kBound];
        		for(int k = 0; k < kBound; k++){
        			inputLabels[k] = knn_index.getTrainLabels()[distanceMatrix[i][k]];
        			inputCosDistance[k] = cosDistance[i][k];
        		}
        		valPredictLabels[i] = knn_index.getPredictLabel(inputLabels, inputCosDistance, rate[n]);
        	}
        	
        	String[] trueValLabels = knn_index.testList.get(1);
        	//int[] result = knn_index.computeError(trueValLabels, valPredictLabels);
        	//System.out.println("There are "+ result[0] +" errors in all "+result[1]+" labels; The error rate is "+ (double)result[0]/result[1]);
        	double[] result = knn_index.computeError(trueValLabels, valPredictLabels);
        	for(int i = 0; i < result.length-1; i++){
        		System.out.println(result[i]);
        	}
        	System.out.println("Total error rate is "+result[result.length-1]);
    	}
    
    }
    
    public double cosDistance(ArrayList<Integer> a1, ArrayList<Integer> a2){
    	double cosValue = 0;
    	int sameNum = 0;
    	for(int i =0; i < a1.size(); i++){
    		for(int j = 0; j < a2.size(); j++){
    			if(a1.get(i).equals(a2.get(j))){
    				sameNum++;
    			}
    		}
    	}
    	cosValue = sameNum/Math.sqrt((a1.size()+0.001)*(a2.size()+0.001));
    	return cosValue;
    }
    
  //得到预测Label
    public String getPredictLabel(String[] labels, double[] cosDistance, double rate){
		ArrayList<String> kLabels = new ArrayList<>();
		ArrayList<Double> weights = new ArrayList<>();
		
		DownSample downSample = new DownSample(this.trainList);
		ArrayList<String> allLabels = downSample.getAllLabels();
		ArrayList<ArrayList<String>> allTypeSentences = downSample.getTypeSentences();
 		
		for(int line = 0; line < labels.length; line++){
			String thisLabel = labels[line];
			int which = allLabels.indexOf(thisLabel);
			int thisTypeNum = allTypeSentences.get(which).size();
			
			if(kLabels.contains(thisLabel)){
				int index = kLabels.indexOf(thisLabel);
				//labelNumber.set(index, labelNumber.get(index)+1);
				weights.set(index, weights.get(index)+cosDistance[line]/Math.pow(thisTypeNum-1, rate));
			}
			else{
				    kLabels.add(thisLabel);
					//labelNumber.add(1);
					weights.add(cosDistance[line]/Math.pow(thisTypeNum-1, rate));
			} 
		}
		
		double max = weights.get(0);
		int largestIndex = 0;
		for(int i = 1; i < weights.size(); i++) {
			if(max < weights.get(i)) {
				max = weights.get(i);
				largestIndex = i;
			}
		}
		
		return kLabels.get(largestIndex);
	}
    
    //计算总error
    public double[] computeError(String[] trueLabels, String[] predictLabels){
    	DownSample downSample = new DownSample(this.testList);
		ArrayList<String> allLabels = downSample.getAllLabels();
		
    	int[] labelsNum = new int[allLabels.size()];
		for(int i = 0; i < trueLabels.length; i++){
			int index = allLabels.indexOf(trueLabels[i]);
			labelsNum[index]++;
		}
		
		double[] result  = new double[allLabels.size()+1];
		int[] error = new int[allLabels.size()];
		
    	int allError = 0;
    	for(int i = 0; i < trueLabels.length; i++){
    		if(!trueLabels[i].equals(predictLabels[i])){
    			int index = allLabels.indexOf(trueLabels[i]);
    			error[index]++;
    			allError++;
    		}
    	}
    	//int[] result = new int[2];
    	//result[0] = error;
    	//result[1] = trueLabels.length;
    	for(int i = 0; i < allLabels.size(); i++){
    		result[i] = (double)error[i]/labelsNum[i];
    	}
    	result[allLabels.size()] = (double)allError/trueLabels.length;
    	
    	return result;
    }

}
