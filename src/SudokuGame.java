import java.util.Random;

/**
 * Реализация игрового процесса, создание новой головоломки, проверка на победу
 */
public class SudokuGame {
	int[][] matrix;				//Матрица значений кнопок
	Random generator;			//Генератор случайных чисел
	int difficulty = 0;			//Переменная, которая отвечает за уровень сложности
	//Количество перестановок во время генерации новой головоломки
	int numberOfPermutation;
	
	/**
	 * Создаёт матрицу, которая хранит значения кнопок
	 */
	public SudokuGame(SudokuPanel pane){
		generator = new Random();	//Инициализация генератора случайных чисел
		matrix = new int[9][9];		//Инициализация матрицы значений кнопок
		numberOfPermutation = 30;	//Инициализация количества перестановок
		
		/* Матрице значений присваивается начальное значение кнопок - "заготовка" */
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				String value = pane.fieldButtons[i][j].getText();
				matrix[i][j] = new Integer(value);
			}
		}
	}
	
	/**
	 * Генерирует случайную головоломку для новой игры.
	 * Проводится ряд манипуляций над "заготовкой".
	 * Учитывается выбранная сложность. 
	 */
	public void newGame(){
		int n = 0;
		/* Случайно выбрать тип перестановки указанное количество раз */
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
		
		/* Случайно извлечь ячейку из головоломки до тех пор, пока она ещё решаема */
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
			/* Установка сложности (возврат удалённых ячеек) */
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
	 * Транспонирование матрицы. 
	 * Один из методов генерирования головоломки
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
	 * Случайная замена колонн матрицы в пределах одной зоны.
	 * Один из методов генерирования головоломки
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
	 * Случайная замена строк матрицы в пределах одной зоны.
	 * Один из методов генерирования головоломки
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
	 * Случайная замена колонн зон матрицы.
	 * Один из методов генерирования головоломки
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
	 * Случайная замена строк зон матрицы.
	 * Один из методов генерирования головоломки
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
	 * Выводит матрицу на консоль
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
