import java.util.ArrayList;

/***
 * 
 * @author SiyuanWang
 * Softmax regression
 *
 */
public class Softmax {
	public static int iteration = 500000;
	
	private GetInput trainInput = new GetInput("train_data.char.ed");
	private GetInput valInput = new GetInput("val_data.char.ed");
	//private GetInput testInput = new GetInput("test_data.char.ed");
	private ArrayList<String[]> trainList = trainInput.getInput(1);
	private ArrayList<String[]> valList = valInput.getInput(1);
	//private ArrayList<String[]> testList = testInput.getInput(1);
	private String[] getTrainSentences;
	private String[] getValSentences;
	//private String[] getTestSentences;
	
	private String[] getTrainLabels;
    private ArrayList<String> charbook;
    private ArrayList<String> allLabels;
    
    private double[][] thetas;
    
    public Softmax(){
    	this.getTrainSentences = this.trainList.get(0);   	
    	this.getValSentences = this.valList.get(0);
    	//this.getTestSentences = this.testList.get(0);
    	this.getTrainLabels = this.trainList.get(1);
    	this.charbook = (new FormCharbook(getTrainSentences)).getCharbook();
    	//this.charbook = (new Ngram(this.getTrainSentences)).getNgramCharbook();
    	this.allLabels = this.computeAllLabels(this.getTrainLabels);
    	this.thetas = new double[this.allLabels.size()][this.charbook.size()];
    	this.setOriginThetas();
    	//this.setGainCharbook();
    	this.setFreqCharbook(0.14);
    	//this.setChisquare(1100);
    }
    
	//卡方检验
    public void setChisquare(int chisquareNum){
    	Chisquare_test chisquare_test = new Chisquare_test(this.charbook, this.trainList);
    	this.charbook = chisquare_test.setChisquare(chisquareNum);
    }
    
    //Information Gain
    public void setGainCharbook(){
    	Gain gain = new Gain(this.charbook, this.trainList);
    	this.charbook = gain.gainCharBook(8000);
    	/*int[] labelsNum = gain.getLabelsNum(this.getTrainLabels, this.allLabels);
    	for(int i = 0 ; i < labelsNum.length; i++){
    		System.out.println(this.allLabels.get(i)+"   "+(double)labelsNum[i]/this.getTrainLabels.length*100);
    	}*/
    }
    
	//基于文档频率
    public void setFreqCharbook(double cut){
    	FreqCharBook charsFreq = new FreqCharBook(this.charbook, this.getTrainSentences);
    	this.charbook = charsFreq.freqCharBook(cut);
    }
    
    //初始化参数值
    public void setOriginThetas(){
    	for(int i = 0 ; i < this.thetas.length; i++){
    		for(int j = 0; j < this.thetas[0].length; j++){
    			this.thetas[i][j] = Math.random()/10;
    		}
    	}
    }
    
    //generate index matrix
    public ArrayList<ArrayList<Integer>> formSentencesMatrixs(String[] sentences){
    	ArrayList<ArrayList<Integer>> indexMatrix = new ArrayList<>();  
    	for (int i = 0; i < sentences.length; i++) {
    		ArrayList<Integer> thisArray = new ArrayList<>();
    		
            for (int j = 0; j < charbook.size(); j++) {
            	if(sentences[i].contains(""+charbook.get(j))){
            		thisArray.add(j);
            	}
            }
            indexMatrix.add(thisArray);
        }
    	return indexMatrix;
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
    
    
    //Generate labels matrix
    public ArrayList<int[]> formLabelsMatrix(ArrayList<String> allLabels, String[] labels){
    	ArrayList<int[]> labelsMatrix = new ArrayList<>();
    	for(int i = 0; i < labels.length; i++){
    		int[] labelsArray = new int[allLabels.size()];
    		labelsArray[allLabels.indexOf(labels[i])] = 1;
    		labelsMatrix.add(labelsArray);
    	}
    	return labelsMatrix;
    }
    
    public String[] getTrainSentences(){
    	return this.getTrainSentences;
    }
    
    public String[] getValSentences(){
    	return this.getValSentences;
    }
    
    /*public String[] getTestSentences(){
    	return this.getTestSentences;
    }*/
    
    public String[] getTrainLabels(){
    	return this.getTrainLabels;
    }
    
    public void setTheta(int x, int y, double value){
    	this.thetas[x][y] = value;
    }
    
    public double getTheta(int x, int y){
    	return this.thetas[x][y];
    }
    
    public static void main(String[] args){
    	Softmax softmax = new Softmax();
    	ArrayList<ArrayList<Integer>> trainSentenceMatrix = softmax.formSentencesMatrixs(softmax.getTrainSentences());
    	ArrayList<ArrayList<Integer>> valSentencMatrix = softmax.formSentencesMatrixs(softmax.getValSentences());
    	ArrayList<int[]> trainLabelMatrix = softmax.formLabelsMatrix(softmax.allLabels, softmax.getTrainLabels());
    	//ArrayList<ArrayList<Integer>> testSentenceMatrix = softmax.formSentencesMatrixs(softmax.getTestSentences());
    	
        System.out.println(softmax.charbook.size());
    	System.out.println("Train sentences has "+trainSentenceMatrix.size());
    	System.out.println("Validation sentences has "+valSentencMatrix.size());
    	//System.out.println("Test sentences has "+testSentenceMatrix.size());
    	String[] lines = softmax.getTrainSentences();
    	
    	//double stepSize = 0.02;
    	double[] stepSizes = {0.02, 0.021, 0.022, 0.023, 0.024, 0.025, 0.026, 0.027, 0.028};
    	//double[] regularParas = {0.0005, 0.0008, 0.001, 0.002, 0.01};
    	//随机梯度下降
    	for(int q = 0; q < stepSizes.length; q++){
        	for(int i = 0; i < iteration; i++){
        		int index = (int) (Math.random() * lines.length);
        		ArrayList<Integer> thisSentence = trainSentenceMatrix.get(index);
        		int[] thisLabels = trainLabelMatrix.get(index);
        		
        		double[] softmaxValue = softmax.computeSoftmax(thisSentence);
        		
        		//梯度还有正则项gradient2,即regualrPara
        		double[] gradient1 = new double[softmaxValue.length];
        		for(int j = 0; j < softmaxValue.length; j++){
        			gradient1[j] = softmaxValue[j] - thisLabels[j];
        		}
        		
        		boolean stop = true;
        		for(int m = 0; m < softmaxValue.length; m++){
        			if(Math.abs(gradient1[m]) > 0.0000001)
        			{
        				stop = false;
        			}
        			//正则项
        			/*for(int k = 0; k < softmax.charbook.size(); k++){
        				double newTheta = softmax.getTheta(m, k)-stepSize*regularParas[q]*softmax.getTheta(m, k);
        				softmax.setTheta(m, k, newTheta);
        			}*/
        			for(int n = 0; n < thisSentence.size(); n++){
        				double newTheta = softmax.getTheta(m, thisSentence.get(n)) - stepSizes[q]*gradient1[m];
        				softmax.setTheta(m, thisSentence.get(n), newTheta);
        			}
        		}
        		if(stop){
        			//System.out.println("End"+i);
        			break;
        		}
        		/*if((i+1)%10000 == 0){
        			stepSizes[q] *= 0.96;
        		}*/
        	}
        	
        	String[] valPredictLabels = new String[valSentencMatrix.size()];
        	for(int i = 0; i < valSentencMatrix.size(); i++){
        		ArrayList<Integer> thisValSentence = valSentencMatrix.get(i);
        		double[] softmaxValue = softmax.computeSoftmax(thisValSentence);
        		
        		valPredictLabels[i] = softmax.getPredictLabel(softmaxValue);
        	}
        	
        	String[] trueValLabels = softmax.valList.get(1);
        	int[] result = softmax.computeError(trueValLabels, valPredictLabels);
        	//System.out.println("There are "+ result[0] +" errors in all "+result[1]+" labels; The error rate is "+ (double)result[0]/result[1]);
        	System.out.print((double)result[0]/result[1]+"   ");
    	}
    	
    }
    
    //计算softmax值
    public double[] computeSoftmax(ArrayList<Integer> thisSentence){
    	double[] thetaSum = new double[this.allLabels.size()];
		for(int j = 0; j < thetaSum.length; j++){
			for(int k = 0; k < thisSentence.size(); k++){
				thetaSum[j] += this.getTheta(j, thisSentence.get(k));
			}
		}
		
		double sumOfExponent = 0;
		for(int j = 0; j < thetaSum.length; j++){
			sumOfExponent += Math.exp(thetaSum[j]);
		}
		
		double[] softmaxValue = new double[thetaSum.length];
		for(int j = 0; j < thetaSum.length; j++){
			softmaxValue[j] = Math.exp(thetaSum[j])/sumOfExponent;
		}
		
		return softmaxValue;
    }
    
    //得到预测label
    public String getPredictLabel(double[] softmaxValue){
		double max = softmaxValue[0];
		int largestIndex = 0;
		for(int i = 1; i < softmaxValue.length; i++) {
			if(max < softmaxValue[i]) {
				max = softmaxValue[i];
				largestIndex = i;
			}
		}
		return this.allLabels.get(largestIndex);
	}
    
    //计算总error
    public int[] computeError(String[] trueLabels, String[] predictLabels){
    	int error = 0;
    	for(int i = 0; i < trueLabels.length; i++){
    		if(!trueLabels[i].equals(predictLabels[i])){
    			error++;
    		}
    	}
    	int[] result = new int[2];
    	result[0] = error;
    	result[1] = trueLabels.length;
    	return result;
    }

}
