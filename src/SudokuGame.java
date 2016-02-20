import java.util.Random;

/**
 * ���������� �������� ��������, �������� ����� �����������, �������� �� ������
 */
public class SudokuGame {
	int[][] matrix;				//������� �������� ������
	Random generator;			//��������� ��������� �����
	int difficulty = 0;			//����������, ������� �������� �� ������� ���������
	//���������� ������������ �� ����� ��������� ����� �����������
	int numberOfPermutation;
	
	/**
	 * ������ �������, ������� ������ �������� ������
	 */
	public SudokuGame(SudokuPanel pane){
		generator = new Random();	//������������� ���������� ��������� �����
		matrix = new int[9][9];		//������������� ������� �������� ������
		numberOfPermutation = 30;	//������������� ���������� ������������
		
		/* ������� �������� ������������� ��������� �������� ������ - "���������" */
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				String value = pane.fieldButtons[i][j].getText();
				matrix[i][j] = new Integer(value);
			}
		}
	}
	
	/**
	 * ���������� ��������� ����������� ��� ����� ����.
	 * ���������� ��� ����������� ��� "����������".
	 * ����������� ��������� ���������. 
	 */
	public void newGame(){
		int n = 0;
		/* �������� ������� ��� ������������ ��������� ���������� ��� */
		while(n < numberOfPermutation){
			int r = generator.nextInt(5);
			switch(r){
				case 0:
					this.transposition();
					break;
				case 1:
					this.swapColumnsSmall();
					break;
				case 2:
					this.swapRowsSmall();
					break;
				case 3:
					this.swapColumnsArea();
					break;
				case 4:
					this.swapRowsArea();
					break;
			}
			n++;
		}
		
		/* �������� ������� ������ �� ����������� �� ��� ���, ���� ��� ��� ������� */
		int pickI = 0;
		int pickJ = 0;
		boolean done = false;
		int resolvable = 0;
		while(!done){
			resolvable = 0;
			pickI = generator.nextInt(9);
			pickJ = generator.nextInt(9);
			if(matrix[pickI][pickJ] > 0){
				matrix[pickI][pickJ] = matrix[pickI][pickJ] * (-1);
			}
			for(int i = 0; i < 9; i++){
				for(int j = 0; j < 9; j++){
					SudokuSolution sol = new SudokuSolution(matrix);
					if(matrix[i][j] < 0 && sol.howManyValuesCanBeHere(i, j) == 1){
						resolvable++;
					}
				}
			}
			/* ��������� ��������� (������� �������� �����) */
			if(resolvable == 0){
				for(int num = 0; num < difficulty; num++){
					pickI = generator.nextInt(9);
					pickJ = generator.nextInt(9);
					if(matrix[pickI][pickJ] < 0){
						matrix[pickI][pickJ] = matrix[pickI][pickJ] * (-1);
					}
				}
				done = true;
			}
		}
		System.out.println();
	}
	
	/**
	 * ���������������� �������. 
	 * ���� �� ������� ������������� �����������
	 */
	public void transposition(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j <= i; j++){
				int temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
	}
	
	/**
	 * ��������� ������ ������ ������� � �������� ����� ����.
	 * ���� �� ������� ������������� �����������
	 */
	public void swapColumnsSmall(){
		int column = generator.nextInt(3);
		int first = generator.nextInt(3);
		int second;
		do{
			second = generator.nextInt(3);
		}while(first == second);
		for(int i = 0; i < 9; i++)
		{
			int temp = matrix[i][first + column * 3];
			matrix[i][first + column * 3] = matrix[i][second + column * 3];
			matrix[i][second + column * 3] = temp;
		}
	}
	
	/**
	 * ��������� ������ ����� ������� � �������� ����� ����.
	 * ���� �� ������� ������������� �����������
	 */
	public void swapRowsSmall(){
		int row = generator.nextInt(3);
		int first = generator.nextInt(3);
		int second;
		do{
			second = generator.nextInt(3);
		}while(first == second);
		for(int j = 0; j < 9; j++)
		{
			int temp = matrix[first + row * 3][j];
			matrix[first + row * 3][j] = matrix[second + row * 3][j];
			matrix[second + row * 3][j] = temp;
		}
	}
	
	/**
	 * ��������� ������ ������ ��� �������.
	 * ���� �� ������� ������������� �����������
	 */
	public void swapColumnsArea(){
		int first = generator.nextInt(3);
		int second;
		do{
			second = generator.nextInt(3);
		}while(first == second);
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 3; j++){
				int temp = matrix[i][j + first * 3];
				matrix[i][j + first * 3] = matrix[i][j + second * 3];
				matrix[i][j + second * 3] = temp;
			}
		}
	}
	
	/**
	 * ��������� ������ ����� ��� �������.
	 * ���� �� ������� ������������� �����������
	 */
	public void swapRowsArea(){
		int first = generator.nextInt(3);
		int second;
		do{
			second = generator.nextInt(3);
		}while(first == second);
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				int temp = matrix[i + first * 3][j];
				matrix[i + first * 3][j] = matrix[i + second * 3][j];
				matrix[i + second * 3][j] = temp;
			}
		}
	}
	
	/**
	 * ������� ������� �� �������
	 */
	public void print(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				System.out.print(matrix[i][j]);
				if((j + 1) % 3 == 0){
					System.out.print(" ");
				}
			}
			System.out.println();
			if((i + 1) % 3 == 0){
				System.out.println();
			}
		}
	}
}
