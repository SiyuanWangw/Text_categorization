import java.util.ArrayList;
import java.util.Arrays;

/***
 * 
 * @author SiyuanWang
 * KNN with sparse matrix
 *
 */
public class Knn_char {
	private GetInput trainInput = new GetInput("train_data.char.ed");
	private GetInput valInput = new GetInput("val_data.char.ed");
	//private GetInput testInput = new GetInput("test_data.char.ed");
	private ArrayList<String[]> trainList = trainInput.getInput(50);
	private ArrayList<String[]> valList = valInput.getInput(50);
	private String[] getTrainSentences;
	private String[] getValSentences;
	//private String[] getTestSentences;
	
	private String[] getTrainLabels;
	//private String[] getValLabels;
	
	private static int kBound = 17;
	
	//private FormCharbook formCharbook;
    private ArrayList<String> charbook;
    
    public Knn_char(){
    	this.getTrainSentences = this.trainList.get(0);   	
    	this.getValSentences = this.valList.get(0);
    	//this.getTestSentences = testInput.getInput(50).get(0);
    	this.getTrainLabels = this.trainList.get(1);
    	//this.getValLabels = this.valList.get(1);
    	this.charbook = (new FormCharbook(getTrainSentences)).getCharbook();
    	this.setGainCharbook();
    }
    
    //Information gain
    public void setGainCharbook(){
    	Gain gain = new Gain(this.charbook, this.trainList);
    	this.charbook = gain.gainCharBook(2000);
    }
    
    //generate sparse matrix
    public Matrix formMatrixs(String[] sentences){
    	Matrix newMatrix = new Matrix(sentences.length, charbook.size());
    	for (int i = 0; i < newMatrix.getRowNumber(); i++) {
            for (int j = 0; j < newMatrix.getColoumNumber(); j++) {
            	if(sentences[i].contains(""+charbook.get(j))){
            		newMatrix.data[i][j] = 1;
            	}
            }
        }
    	return newMatrix;
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
    
   /* public String[] getValLabels(){
    	return this.getValLabels();
    }*/
    
    public static void main(String[] args){
    	Knn_char knn_char = new Knn_char();
    	Matrix trainMatrix = knn_char.formMatrixs(knn_char.getTrainSentences());
        Matrix valMatrix = knn_char.formMatrixs(knn_char.getValSentences());
        //Matrix testMatrix = knn_char.formMatrixs(knn_char.getTestSentences());
    	
        System.out.println(knn_char.charbook.toString());
    	System.out.println(trainMatrix.getRowNumber()+"   "+trainMatrix.getColoumNumber());
    	System.out.println(valMatrix.getRowNumber()+"   "+valMatrix.getColoumNumber());
    	//System.out.println(testMatrix.getRowNumber()+"   "+testMatrix.getColoumNumber());
    	String[] lines = knn_char.getTrainSentences();
    	
    	//计算两句话之间的cos距离
    	int[][] distanceMatrix = new int[knn_char.getValSentences().length][kBound];
    	double[][] cosDistance = new double[knn_char.getValSentences().length][kBound];
    	for(int i = 0; i < knn_char.getValSentences().length; i++){
    		ArrayList<Double> distanceRow = new ArrayList<>();
    		for(int j = 0; j < lines.length; j++){
    			//int distance = (valMatrix.getRow(i).minus(trainMatrix.getRow(j))).dotBySelf().sumInt();
    			double distance = valMatrix.getRow(i).cosDistance(trainMatrix.getRow(j));
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
    		System.out.println(distanceMatrix[i][0]+" "+cosDistance[i][0]);
    	}
    	
    	String[] valPredictLabels = new String[valMatrix.getRowNumber()];
    	for(int i = 0; i < valMatrix.getRowNumber(); i++){
    		String[] inputLabels = new String[kBound];
    		double[] inputCosDistance = new double[kBound];
    		for(int k = 0; k < kBound; k++){
    			inputLabels[k] = knn_char.getTrainLabels[distanceMatrix[i][k]];
    			inputCosDistance[k] = cosDistance[i][k];
    		}
    		valPredictLabels[i] = knn_char.getPredictLabel(inputLabels, inputCosDistance);
    		//System.out.println(i+" "+valPredictLabels[i]);
    	}
    	
    	String[] trueValLabels = knn_char.valList.get(1);
    	int[] result = knn_char.computeError(trueValLabels, valPredictLabels);
    	System.out.println("There are "+ result[0] +" errors in all "+result[1]+" labels; The error rate is "+ (double)result[0]/result[1]);
    }
    
    //得到预测Label
    public String getPredictLabel(String[] labels, double[] cosDistance){
		//ArrayList<Integer> labelNumber = new ArrayList<>();
		ArrayList<String> allLabels = new ArrayList<>();
		ArrayList<Double> weights = new ArrayList<>();
 		
		for(int line = 0; line < labels.length; line++){
			String thisLabel = labels[line];
			if(allLabels.contains(thisLabel)){
				int index = allLabels.indexOf(thisLabel);
				//labelNumber.set(index, labelNumber.get(index)+1);
				weights.set(index, weights.get(index)+cosDistance[line]);
			}
			else{
					allLabels.add(thisLabel);
					//labelNumber.add(1);
					weights.add(cosDistance[line]);
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
		
		return allLabels.get(largestIndex);
	}
    
    /*public String getPredictLabel(String[] labels){
		ArrayList<Integer> labelNumber = new ArrayList<>();
		ArrayList<String> allLabels = new ArrayList<>();
 		
		for(int line = 0; line < labels.length; line++){
			String thisLabel = labels[line];
			if(allLabels.contains(thisLabel)){
				int index = allLabels.indexOf(thisLabel);
				labelNumber.set(index, labelNumber.get(index)+1);
			}
			else{
					allLabels.add(thisLabel);
					labelNumber.add(1);
			} 
		}
		
		int max = labelNumber.get(0);
		int largestIndex = 0;
		for(int i = 1; i < labelNumber.size(); i++) {
			if(max < labelNumber.get(i)) {
				max = labelNumber.get(i);
				largestIndex = i;
			}
		}
		
		return allLabels.get(largestIndex);
	}*/
    
    //计算总错误率
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
