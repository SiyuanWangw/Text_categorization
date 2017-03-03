import java.util.ArrayList;
import java.util.Arrays;

/****
 * 
 * @author SiyaunWang
 * 卡方检验
 *
 */
public class Chisquare_test {
	private String[] sentences;
	private String[] labels;
	private ArrayList<String> allLabels;
	private ArrayList<String> charbook;
	
	public Chisquare_test(ArrayList<String> charbook, ArrayList<String[]> trainList){
		this.sentences = trainList.get(0);
		this.labels = trainList.get(1);
		this.charbook = charbook;
		this.allLabels = this.computeAllLabels();
	}
	
	//得到所有Labels的种类
	public ArrayList<String> computeAllLabels(){
		ArrayList<String> alllabels = new ArrayList<>();
		for(int i = 0; i < this.labels.length; i++){
			if(!alllabels.contains(this.labels[i])){
				alllabels.add(this.labels[i]);
			}
		}
		return alllabels;	
	}
	
	//得到各Label的数量
	public int[] getLabelsNum(){
		int[] labelsNum = new int[this.allLabels.size()];
		for(int i = 0; i < this.labels.length; i++){
			int index = this.allLabels.indexOf(this.labels[i]);
			labelsNum[index]++;
		}
		return labelsNum;	
	}
	
	public ArrayList<String> getAllLabels(){
		return this.allLabels;
	}
	
	//生成Bayes矩阵
	public Matrix formBayesMatrix(){
    	Matrix bayesMatrix = new Matrix(this.allLabels.size(), this.charbook.size());
    	for (int i = 0; i < this.sentences.length; i++) {
            int row = allLabels.indexOf(this.labels[i]);
            for(int j = 0; j < this.sentences[i].length(); j++){
            	int column = this.charbook.indexOf(sentences[i].charAt(j)+"");
            	if(column >= 0){
            		bayesMatrix.setToSpecifiedValue(row, column, bayesMatrix.getElement(row, column)+1);
            	}
            }
        }
    	return bayesMatrix;
    }
	
	//计算字典中各特征的卡方计算值，并选出前n个
	public ArrayList<String> setChisquare(int chisquareNum){
		Matrix chisquareMatrix = formBayesMatrix();
		int[] rowSum = getLabelsNum();
		float[] columnSum = chisquareMatrix.sumOfEachColoum();
		int length = this.sentences.length;
		
		int[][] newCharbookMatrix = new int[this.allLabels.size()][chisquareNum];
		
		for(int i = 0; i < this.allLabels.size(); i++){
			ArrayList<Double> allChisquareValues = new ArrayList<>();
			for(int j = 0; j < this.charbook.size(); j++){
				double a = chisquareMatrix.getElement(i, j);
				double b = columnSum[j] - a;
				double c = rowSum[i] - a;
				double d = length - (a+b+c);
				double chisquareValue = Math.pow((a*d-b*c), 2)/(a+b)/(c+d);
				allChisquareValues.add(chisquareValue);
			}
			
			Object[] allChisquareArray = allChisquareValues.toArray();
    		int size = allChisquareArray.length;
    		Arrays.sort(allChisquareArray);
    		for (int k = 0; k < chisquareNum; k++) {
    			int index = allChisquareValues.indexOf(allChisquareArray[size-k-1]);
    			newCharbookMatrix[i][k] = index;
    			allChisquareValues.set(index, (double) -1);
			}
		}
		
		ArrayList<Integer> allCharIndex = new ArrayList<>();
		ArrayList<String> newCharbook = new ArrayList<>();
		for(int i = 0; i < newCharbookMatrix.length; i++){
			for(int j = 0; j < newCharbookMatrix[0].length; j++){
				int index = newCharbookMatrix[i][j];
				if(!allCharIndex.contains(index)){
					allCharIndex.add(index);
					newCharbook.add(this.charbook.get(index));
				}
			}
		}
		
		return newCharbook;
	}
}
