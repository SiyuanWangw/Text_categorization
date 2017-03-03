import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/***
 * 
 * @author SiyuanWang
 * Matrix operations
 *
 */
public class Matrix {
	private int M;
	private int N;
	float[][] data;
	/**
	 * 无参构造，这是一个形式方法，实际中不可能有0*0的空矩阵，注意避免空指针异常
	 */
	public Matrix() {
		this.M = 0;
		this.N = 0;
		this.data = new float[M][N];
	}
	
	/**
	 * 含维度构造
	 * @param M 行
	 * @param N 列
	 */
	public Matrix(int M,int N) {
		this.M = M;
		this.N = N;
		this.data = new float[M][N];
        for(int i=0;i<M;i++) {
    	   for(int j=0;j<N;j++) {
    		   data[i][j]=0;
    	   }
        }
	}
	
	/**
	 * 用二维浮点型数组来构造矩阵
	 * @param data 浮点型数组
	 */
	public Matrix(float[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = data[i][j];
            }
        }
    }
	
	/**
	 * 用二维双精度浮点型数组来构造矩阵
	 * @param data 浮点型数组
	 */
	public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) data[i][j];
            }
        }
    }
	
	/**
	 * 用二维数组来构造矩阵
	 * @param data 整型数组
	 */
	public Matrix(int[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = data[i][j];
            }
        }
    }
	
	/**
	 * 初始化矩阵为相同的数
	 * @param M 行数
	 * @param N 列数
	 * @param allvalue 特定浮点型数值
	 */
	public Matrix(int M,int N,float allvalue)
    {
		this.M = M;
	    this.N = N;
	    float[][]tmp = new float[M][N];
        for(int i=0;i<M;i++) {
    	   for(int j=0;j<N;j++) {
    		   tmp[i][j]=allvalue;
    	   }
        }
       data=tmp;
    }
	
	/**
	 * 初始化矩阵为相同的数
	 * @param M 行数
	 * @param N 列数
	 * @param allvalue 特定整型数值
	 */
	public Matrix(int M,int N,int allvalue)
    {
		this.M = M;
	    this.N = N;
	    float[][]tmp = new float[M][N];
        for(int i=0;i<M;i++) {
    	   for(int j=0;j<N;j++) {
    		   tmp[i][j]=allvalue;
    	   }
        }
       data=tmp;
    }
	
	/**
	 * 返回矩阵行数
	 * @return 行数
	 */
	public int getRowNumber() {
		return this.M;
	}

	/**
	 * 返回矩阵列数
	 * @return 列数
	 */
	public int getColoumNumber() {
		return this.N;
	}

	/**
	 * 返回特定索引的元素值
	 * @param Row 行索引
	 * @param Coloum 列索引
	 * @return 元素
	 */
	public float getElement(int Row, int Coloum) {
		return this.data[Row][Coloum];
	}

	/**
	 * 产生随机矩阵
	 * @param Row 行索引
	 * @param Coloum 列索引
	 * @return 值
	 */
	public static Matrix random(int Row, int Coloum) {
		Matrix A = new Matrix(Row, Coloum);
        for(int i = 0; i < Row; i++) {
        	for(int j = 0; j < Coloum; j++) {
        		A.data[i][j] = (float) Math.random();
        	}
        }
        return A;
	}
	
	/**
	 * 产生一个有上限的随机矩阵，即[0,bound)之间的随机整数会出现
	 * @param Row 行索引
	 * @param Coloum 列索引
	 * @param bound 界限
	 * @return 随机矩阵
	 */
	public static Matrix randomWithUpperLimit(int Row,int Coloum,int bound) {
		Matrix A = new Matrix(Row, Coloum);
		Random random = new Random();
        for(int i = 0; i < Row; i++) {
        	for(int j = 0; j < Coloum; j++) {
        		A.data[i][j] = (float) random.nextInt(bound);
        	}
        }
        return A;
	}
	
	
	
	/**
	 * 产生特定范围内的浮点型随机矩阵，[lowerBound,upperBound)
	 * @param Row 行数
	 * @param Coloum 列数
	 * @param lowerBound 下限
	 * @param upperBound 上限
	 * @return 随机矩阵
	 */
	public static Matrix randomFloatWithRange(int Row,int Coloum,float lowerBound,float upperBound) {
		Random random = new Random();
		Matrix A = new Matrix(Row, Coloum);
		for(int i=0;i<Row;i++) {
			DoubleStream doubleStream = random.doubles(Coloum, lowerBound, upperBound);
			double[] array = doubleStream.toArray();
			for(int j=0;j<Coloum;j++) {
				A.data[i][j] = (float) array[j];
			}
		}
		return A;
	}
	
	/**
	 * 产生特定范围内的整型随机矩阵，[lowerBound,upperBound)
	 * @param Row 行数
	 * @param Coloum 列数
	 * @param lowerBound 下限
	 * @param upperBound 上限
	 * @return 随机矩阵
	 */
	public static Matrix randomIntWithRange(int Row,int Coloum,int lowerBound,int upperBound) {
		Random random = new Random();
		Matrix A = new Matrix(Row, Coloum);
		for(int i=0;i<Row;i++) {
			IntStream intStream = random.ints(Coloum, lowerBound, upperBound);
			int[] array = intStream.toArray();
			for(int j=0;j<Coloum;j++) {
				A.data[i][j] = array[j];
			}
		}
		return A;
	}
	
	/**
	 * 产生随机高斯矩阵，平均值为0，标准差为1
	 * @param Row 行索引
	 * @param Coloum 列索引
	 * @return 随机高斯矩阵
	 */
	public static Matrix randomGaussian(int Row, int Coloum) {
		Matrix A = new Matrix(Row, Coloum);
		Random random = new Random();
        for(int i = 0; i < Row; i++) {
        	for(int j = 0; j < Coloum; j++) {
        		A.data[i][j] = (float) random.nextGaussian();
        	}
        }
        return A;
	}

	/**
	 * 设定指定索引元素为特定值
	 * @param Row 行索引
	 * @param Coloum 列索引
	 * @param e 值
	 */
	public void setToSpecifiedValue(int Row, int Coloum, float e) {
		this.data[Row][Coloum] = e;
	}

	/**
	 * 设定指定行所有元素为特定值
	 * @param Row 行索引
	 * @param e 值
	 * @return 设定后的矩阵
	 */
	public Matrix setRowToSpecifiedValue(int Row, float e) {
		 for (int j = 0; j < N; j++) {
	            this.data[Row][j] = e;
		 }
	     return this;
	}

	/**
	 * 设定指定列所有元素为特定值
	 * @param Coloum 列索引
	 * @param e 值
	 * @return 设定后的矩阵
	 */
	public Matrix setColoumToSpecifiedValue(int Coloum, float e) {
		for (int i = 0; i < M; i++) {
			this.data[i][Coloum] = e;
		}
		return this;
	}

	/**
	 * 按行翻转
	 * @return 矩阵按行进行翻转后的矩阵
	 */
	public Matrix flipDimensionByRow() {
    	float[][] result = new float[M][N];
    	for(int i = 0; i < M/2; i++) {
			result[M-1-i] = this.data[i];
    		result[i] = this.data[M-1-i];
    	}
    	if(M%2 != 0) {
			result[M/2] = this.data[M/2];
		}
    	return new Matrix(result);
	}

	/**
	 * 按列翻转
	 * @return 矩阵按列进行翻转后的矩阵
	 */
	public Matrix flipDimensionByColoum() {
		float[][] result = new float[M][N];
		for(int i = 0; i < M;i++) {
			for(int j = 0; j < N/2; j++) {
				result[i][N-1-j] = this.data[i][j];
				result[i][j] = this.data[i][N-1-j];
			}
		}
		if(N % 2 != 0) {
			for(int i=0;i<M;i++) {
				result[i][N/2] = this.data[i][N/2];
			}
		}
    	return new Matrix(result);
	}

	/**
	 * 矩阵转置
	 * @return 转置后的矩阵
	 */
	public Matrix transpose() {
		Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                A.data[j][i] = this.data[i][j];
            }
        }
	    return A;
	}

	/**
	 * 求开方
	 * @return 矩阵
	 */
	public Matrix sqrt() {
    	for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) Math.sqrt(this.data[i][j]);
            }
    	}
        return this;
	}
	
	/**
	 * 求正切
	 * @return 矩阵
	 */
	public Matrix tan() {
		for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) Math.tan(this.data[i][j]);
            }
		}
        return this;
	}

	/**
	 * 求正弦
	 * @return 矩阵
	 */
	public Matrix sin() {
		for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) Math.sin(this.data[i][j]);
            }
		}
        return this;
	}

	/**
	 * 求余弦
	 * @return 矩阵
	 */
	public Matrix cos() {
		for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) Math.cos(this.data[i][j]);
            }
		}
        return this;
	}
	
	/**
	 * 返回矩阵的e指数
	 * @return 矩阵
	 */
	public Matrix exp() {
		for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) Math.exp(this.data[i][j]);
            }
		}
        return this;
	}
	/**
	 * 返回矩阵的log自然对数
	 * @return 矩阵
	 */
	public Matrix log() {
		for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	this.data[i][j] = (float) Math.log(this.data[i][j]);
            }
		}
        return this;
	}

	/**
	 * 两个矩阵相加
	 * @param B 指定矩阵
	 * @return 原矩阵与指定矩阵的相加
	 */
	public Matrix plus(Matrix B) {
		Matrix A = this;
        if (B.M != A.M || B.N != A.N) {
        	throw new RuntimeException("两矩阵维度必须一致");
        }
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = A.data[i][j] + B.data[i][j];
            }
        }
        return C;
	}

	/**
	 * 两个矩阵相减
	 * @param B 指定矩阵
	 * @return 原矩阵与指定矩阵的相减
	 */
	public Matrix minus(Matrix B) {
		Matrix A = this;
        if (B.M != A.M || B.N != A.N) {
        	throw new RuntimeException("两矩阵维度必须一致");
        }
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = A.data[i][j] - B.data[i][j];
            }
        }
        return C;
	}

	/**
	 * 两个矩阵相乘
	 * @param B 指定矩阵
	 * @return 原矩阵与指定矩阵的相乘
	 */
	public Matrix multiply(Matrix B) {
		Matrix A = this;
		if (A.N != B.M) {
        	throw new RuntimeException("第一个矩阵的列数必须等于第二个矩阵的行数");
		}
		Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++) {
            for (int j = 0; j < C.N; j++) {
                for (int k = 0; k < A.N; k++) {
                    C.data[i][j] = C.data[i][j] + A.data[i][k] * B.data[k][j];
                }
            }
        }
        return C;
	}
	
	/**
	 * 两个矩阵相除
	 * @param B 指定矩阵
	 * @return 原矩阵与指定矩阵的相除
	 */
	public Matrix divide(Matrix B) {
		//Matrix C = new Matrix(M, N);
    	for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            	if (B.data[i][j]==0) {
                	throw new RuntimeException("被除数不能为0");
        		}
                data[i][j] = data[i][j] / B.data[i][j];
            }
    	}
        return this;
	}

	/**
	 * 两个矩阵点乘
	 * @param B 指定矩阵
	 * @return 原矩阵与指定矩阵的点乘
	 */
	public Matrix dot(Matrix B) {
		Matrix C = new Matrix(M, N);
        for (int i = 0; i < C.M; i++) {
            for (int j = 0; j < C.N; j++) {
                 C.data[i][j] = data[i][j] * B.data[i][j];
            }
        }
        return C;
	}

	/**
	 * 矩阵自身点乘
	 * @return 原矩阵与自己进行点乘
	 */
	public Matrix dotBySelf() {
        return this.dot(this);
	}
	
	/**
	 * 两个向量的余弦距离
	 */
	public double cosDistance(Matrix B){
		if(M!=1 || B.getRowNumber()!=1) {
			throw new RuntimeException("输入不为行向量");
		}
		double distance = 0;
		distance = (this.dot(B).sumDouble())/Math.sqrt((this.dotBySelf().sumDouble()+0.001)*(B.dotBySelf().sumDouble()+0.001));
		return distance;
	}

	/**
	 * 矩阵加上一个数
	 * @param e 数
	 * @return 结果矩阵
	 */
	public Matrix plusNumber(float e) {
		Matrix C = new Matrix(M, N,e);
        return this.plus(C);
	}

	/**
	 * 矩阵减去一个数
	 * @param e 数
	 * @return 结果矩阵
	 */
	public Matrix minusNumber(float e) {
		Matrix C = new Matrix(M, N,e);
        return this.minus(C);
	}

	/**
	 * 矩阵乘以一个数
	 * @param e 数
	 * @return 结果矩阵
	 */
	public Matrix multiplyNumber(float e) {
		Matrix C = new Matrix(M, N,e);
        return this.multiply(C);
	}
	
	/**
	 * 矩阵除以一个数
	 * @param e 数
	 * @return 结果矩阵
	 */
	public Matrix divideNumber(float e) {
		Matrix C = new Matrix(M, N,e);
        return this.divide(C);
	}

	/**
	 * 判断两个矩阵是否相等
	 * @param t 待比较矩阵
	 * @return 布尔矩阵
	 */
	public boolean equalTo(Matrix t) {
		 for (int i = 0; i < M; i++) {
	            for (int j = 0; j < N; j++) {
	                if (data[i][j] != t.data[i][j]) {
	                	return false;
	                }
	            }
		 }
         return true;
	}

	/**
	 * 打印到控制台
	 */
	public void showToConsole() {
		for (float[] a : data) {
            for (float b : a) {
                System.out.printf("%9.4f ", b);
            }
            System.out.println();
        }
	}

	/**
	 * 求出矩阵的秩(3*3或2*2)
	 * @return 秩
	 */
	public float det() {
		if ((data.length == 3 && data[0].length == 3)||(data.length == 2&& data[0].length == 2)) { 
    		if(data.length == 3 && data[0].length == 3) {
        		float r = 0;
            	r = data[0][0]*data[1][1]*data[2][2]+data[1][0]*data[2][1]*data[0][2]+data[0][1]*data[1][2]*data[2][0];
            	r = r-data[0][2]*data[1][1]*data[2][0]-data[1][0]*data[2][2]*data[0][1]-data[0][0]*data[2][1]*data[1][2];
            	return r;
        	} else {
        		float r = 0;
            	r = data[0][0]*data[1][1]-data[0][1]*data[1][0];
             	return r;
        	}
		} else {
    		throw new RuntimeException("矩阵的维度必须为3*3的或者为2*2的");
		}
	}

	/**
	 * 对特定的间隔划分网格(参数均为int)
	 * @param start1 1起始
	 * @param distance1 1间距
	 * @param end1 1末尾
	 * @param start2 2起始
	 * @param distance2 2间距
	 * @param end2 2末尾
	 * @return 两个矩阵，原理同MATLAB的相同函数
	 */
	public Matrix[] meshgrid(int start1, int distance1, int end1,
			int start2, int distance2, int end2) {
		int Coloum = (end1-start1)/distance1+1;
		int Row = (end2-start2)/distance2+1;
		Matrix[] result = new Matrix[2];
		result[0] = new Matrix(Row,Coloum);
		result[1] = new Matrix(Row,Coloum);
		for(int i = 0; i < Row; i++) {
			for(int j = 0; j < Coloum;j++) {
				result[0].setToSpecifiedValue(i, j,start1+j*distance1);
				result[1].setToSpecifiedValue(i, j,start2+i*distance2);
			}
		}
		return result;
	}
	
	/**
	 * 对特定的间隔划分网格(参数均为float)
	 * @param start1 1起始
	 * @param distance1 1间距
	 * @param end1 1末尾
	 * @param start2 2起始
	 * @param distance2 2间距
	 * @param end2 2末尾
	 * @return 两个矩阵，原理同MATLAB的相同函数
	 */
	public Matrix[] meshgrid(float start1, float distance1, float end1,
			float start2, float distance2, float end2) {
		int Coloum = (int) ((end1-start1)/distance1+1);
		int Row = (int) ((end2-start2)/distance2+1);
		Matrix[] result = new Matrix[2];
		result[0] = new Matrix(Row,Coloum);
		result[1] = new Matrix(Row,Coloum);
		for(int i = 0; i < Row; i++) {
			for(int j = 0; j < Coloum;j++) {
				result[0].setToSpecifiedValue(i, j, start1+j*distance1);
				result[1].setToSpecifiedValue(i, j, start2+i*distance2);
			}
		}
		return result;
	}

	/**
	 * 对矩阵每个数取绝对值
	 * @return 矩阵
	 */
	public Matrix abs() {
		Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = Math.abs(data[i][j]) ;
            }
        }
        return C;
	}

	/**
	 * 对矩阵的每个数向上取整
	 * @return 矩阵
	 */
	public Matrix ceil() {
		Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = (float) Math.ceil(data[i][j]) ;
            }
        }
        return C;
	}

	/**
	 * 对矩阵的每个数四舍五入
	 * @return 矩阵
	 */
	public Matrix round() {
		Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = Math.round(data[i][j]) ;
            }
        }
        return C;
	}

	/**
	 * 向下取整
	 * @return 矩阵
	 */
	public Matrix floor() {
		Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = (float) Math.floor(data[i][j]) ;
            }
        }
        return C;
	}

	/**
	 * 化成数组
	 * @return 二维数组
	 */
	public float[][] toArray() {
    	return this.data;
	}
	
	/**
	 * 返回矩阵的每个元素的sigmoid()值，sigmoid(x)=1/(1+exp(-x))
	 * @return sigmoid矩阵
	 */
	public Matrix sigmoid() {
		Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                C.data[i][j] = (float) (1/(1+Math.exp(-data[i][j])));
            }
        }
        return C;
	}
	
	/**
	 * 随机打乱矩阵的元素位置
	 * @return 随机矩阵
	 */
	public Matrix shuffle() {
		Matrix C = new Matrix(M,N);
		Random random = new Random();
		boolean[][] flag = new boolean[M][N];
		int i=0;
		int j=0;
		while(true) {
			int row = random.nextInt(M);
			int col = random.nextInt(N);
			if(flag[row][col]==false) {
				C.data[i][j] = this.data[row][col];
				j++;
				flag[row][col]=true;
				if(j==N) {
					j=0;
					i++;
				}
				if(i==N&&j==0) {
					break;
				}
			}
		}
		return C;
	}
	
	/**
	 * 找出矩阵的最大值
	 * @return 最大值
	 */
	public float max() {
		float max = data[0][0];
		for(int i=0;i<M;i++) {
			for(int j=0;j<N;j++) {
				if(max<data[i][j]) {
					max = data[i][j];
				}
			}
		}
		return max;
	}

	/**
	 * 找出矩阵的最小值
	 * @return 最小值
	 */
	public float min() {
		float min = data[0][0];
		for(int i=0;i<M;i++) {
			for(int j=0;j<N;j++) {
				if(min>data[i][j]) {
					min = data[i][j];
				}
			}
		}
		return min;
	}
	
	/**
	 * 返回矩阵每一行的最大值
	 * @return 最大值数组
	 */
	public float[] maxOfEachRow() {
		float[] result = new float[M];
		float[][] tmp = data;
		for(int i=0;i<M;i++) {
			Arrays.parallelSort(tmp[i]);
			result[i] = tmp[i][N-1];
		}
		return result;
	}
	
	/**
	 * 返回矩阵每一列的最大值
	 * @return 最大值数组
	 */
	public float[] maxOfEachColoum() {
		float[] result = new float[N];
		float[] col = new float[M];
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				col[j] = data[j][i];
			}
			Arrays.parallelSort(col);
			result[i] = col[M-1];
		}
		return result;
	}
	
	/**
	 * 返回矩阵每一行的最小值
	 * @return 最小值数组
	 */
	public float[] minOfEachRow() {
		float[] result = new float[M];
		float[][] tmp = data;
		for(int i=0;i<M;i++) {
			Arrays.parallelSort(tmp[i]);
			result[i] = tmp[i][0];
		}
		return result;
	}
	
	/**
	 * 返回矩阵每一列的最小值
	 * @return 最小值数组
	 */
	public float[] minOfEachColoum() {
		float[] result = new float[N];
		float[] col = new float[M];
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				col[j] = data[j][i];
			}
			Arrays.parallelSort(col);
			result[i] = col[0];
		}
		return result;
	}
	
	/**
	 * 返回行向量的求和值
	 * @return 和值
	 */
	public float sum() {
		if(M!=1) {
			throw new RuntimeException("矩阵的维度必须为3*3的或者为2*2的");
		}
		float sum=0;
		for(int i=0;i<N;i++) {
			sum+=data[0][i];
		}
		return sum;
	}
	
	/**
	 * 返回行向量的求和值(整数)
	 * @return 和值
	 */
	public int sumInt() {
		if(M!=1) {
			throw new RuntimeException("矩阵的维度必须为3*3的或者为2*2的");
		}
		int sum=0;
		for(int i=0;i<N;i++) {
			sum+=data[0][i];
		}
		return sum;
	}
	
	public double sumDouble() {
		if(M!=1) {
			throw new RuntimeException("矩阵的维度必须为3*3的或者为2*2的");
		}
		double sum=0;
		for(int i=0;i<N;i++) {
			sum+=data[0][i];
		}
		return sum;
	}
	
	/**
	 * 返回每一行的和
	 * @return 和数组
	 */
	public float[] sumOfEachRow() {
		if(M==1) {
			throw new RuntimeException("矩阵行数必须大于1");
		}
		float[] sum=new float[M];
		for(int i=0;i<M;i++) {
			float s=0;
			for(int j=0;j<N;j++) {
				s+=data[i][j];
			}
			sum[i]=s;
		}
		return sum;
	}
	
	/**
	 * 返回每一列的和
	 * @return 和数组
	 */
	public float[] sumOfEachColoum() {
		if(N==1) {
			throw new RuntimeException("矩阵列数必须大于1");
		}
		float[] sum=new float[N];
		for(int i=0;i<N;i++) {
			float s=0;
			for(int j=0;j<M;j++) {
				s+=data[j][i];
			}
			sum[i]=s;
		}
		return sum;
	}
	
	/**
	 * 取得特定的行
	 * @param index 行索引
	 * @return 行矩阵
	 */
	public Matrix getRow(int index) {
		Matrix C = new Matrix(1,N);
		for(int i=0;i<N;i++) {
			C.data[0][i] = data[index][i];
		}
		return C;
	}
	
	/**
	 * 取得特定的列
	 * @param index 列索引
	 * @return 列矩阵 
	 */
	public Matrix getColoum(int index) {
		Matrix C = new Matrix(M,1);
		for(int i=0;i<M;i++) {
			C.data[i][0] = data[i][index];
		}
		return C;
	}
	
	public Matrix clone(){
		return new Matrix(data.clone());
	}
}
