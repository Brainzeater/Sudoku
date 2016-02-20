import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.Timer;

/**
 * Главный класс игры.
 * Отвечает за отображение приложения, а также реализует механику игры.
 * Для игры необходимо начать новую игры или загрузить уже существующую.
 * Далее необходимо ввести имя игрока и выбрать уровень сложности.
 * На сгенерированном поле для заполнения требуется выбрать пустую клетку,
 * а затем выбрать цифру на поле кнопок цифр снизу. Выбранная ячейка подсвечивается жёлтым.
 * Если головоломка решена верно, то отображается сообщение о победе, 
 * после чего игрок и данные о его игре заносятся в список лидеров. 
 * В противном случае, появляется сообщение о неверном решении.
 * Кроме того, при неверном решении можно воспользоваться подсказкой,
 * которая выделит ошибку и поменяет значение неверной ячейки.
 * Присутствует возможность очистить ячейку, поставить игру на паузу,
 * воспользоваться подсказкой, а также начать решение головоломки заново.
 * При сохранении игры, будет создан файл расширения txt, который будет назван
 * именем игрока. Для загрузки файла необходимо ввести имя игрока.
 * Помимо прочего, присутствует возможность ознакомиться со списком лидеров.
 */
public class SudokuFrame extends JFrame{
	SudokuPanel myPanel;			//Панель, которая хранит визуальную составляющую игры
	JPanel buttonPane;				//Панель, которая хранит кнопки, отвечающие за игровой процесс
	JPanel choiceButtonPane;		//Панель, которая хранит кнопки выбора цифры
	JPanel timerPane;				//Панель, которая хранит таймер
	JLabel timerText;				//Отображение таймера
	Timer timer;					//Таймер
	int seconds = 0, minutes = 0;	//Секунды и минуты таймера
	String playerName;				//Имя игрока
	String difficulty;				//Уровень сложности
	String stringForMessage;		//Строка для сообщения о победе
	JButton[][] choiceButtons;		//Массив кнопок выбора цифры на место пустой ячейки
	JButton clearSlot, clearField;	//Кнопки очистки ячейки и очистки поля целиком
	JButton pause, hint;			//Кнопки подсказки и паузы
	boolean hintWasUsed;			//Логическая переменная "Использовалась ли подсказка?"
	boolean gameOn;					//Логическая переменная активности игры
	String[][] scores;				//Массив очков сохранённой игры
	String leader;					//Строка для хранения данных о победителе
	int difValue;					//Значение сложности для таблицы лидеров
	/* Переменные для чтения из файла */
    FileWriter myFile = null;
    BufferedWriter buff = null;
    FileReader myF = null;
    BufferedReader b = null;
	JMenuBar menuBar;				//Строка меню
	JMenu menu;						//Меню
	//Элементы меню: Новая игра, Сохранить игру, Загрузить игру, Список лидеров, Выход
	JMenuItem newGame, saveGame, loadGame, leaderBoard, exit;
	//Переменная, которая хранит параметры шрифта большинства элементов приложения
	Font font;
	
	/**
	 * Создаётся новый Фрейм с данной игрой
	 */
	public SudokuFrame(){
		
		super();	//Активация конструктора суперкласса
		
		/* Инициализация параметров шрифта */
		font = new Font("PLAIN",Font.BOLD, 25);	
		
		/* Инициализация меню */
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar); 
		menu = new JMenu("Меню");
		menu.setFont(font);
		menuBar.add(menu);
		
		/* Добавление элемента "Новая игра" в меню */
		newGame = new JMenuItem("Новая игра");
		newGame.setFont(font);
		newGame.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 newGamePressed();
	         }
		});
		menu.add(newGame);
		
		/* Добавление элемента "Сохранить игру" в меню */
		saveGame = new JMenuItem("Сохранить игру");
		saveGame.setFont(font);
		saveGame.setEnabled(false);
		saveGame.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 saveGameButtonPressed();
	         }
		});
		menu.add(saveGame);
		
		/* Добавление элемента "Загрузить игру" в меню */
		loadGame = new JMenuItem("Загрузить игру");
		loadGame.setFont(font);
		loadGame.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 loadGameButtonPressed();
	         }
		});
		menu.add(loadGame);
		
		/* Добавление элемента "Список лидеров" в меню */
		leaderBoard = new JMenuItem("Список лидеров");
		leaderBoard.setFont(font);
		leaderBoard.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 	leaderBoardButtonPressed();
	         }
		});
		menu.add(leaderBoard);
		
		/* Добавление элемента "Выход" в меню */
		exit = new JMenuItem("Выход");
		exit.setFont(font);
		exit.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 System.exit(0);
	         }
		});
		menu.add(exit);
		
		/* Инициализация панели игры */
		myPanel = new SudokuPanel();
		
		/* Инициализация панели кнопок */
		buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		
		/* Инициализация панели кнопок выбора цифры */
		choiceButtonPane = new JPanel();
		choiceButtonPane.setLayout(new GridLayout(3, 3));

		/* Инициализация массива кнопок выбора и добавление его на панель кнопок выбора */
		choiceButtons = new JButton[3][3];
		int n = 1;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				int r = i;
				int c = j;
				/* Кнопке присваивается значение от 1 до 9 */
				choiceButtons[i][j] = new JButton(new Integer(n).toString());
				/* Изначально все кнопки отключены */
				choiceButtons[i][j].setEnabled(false);
				choiceButtons[i][j].addActionListener(new ActionListener() { 
		            public void actionPerformed(ActionEvent e)
		            {
		                choiceButtonPressed(r, c);
		            }
		        });
				choiceButtons[i][j].setFont(font);
				choiceButtons[i][j].setBackground(new Color(240, 255, 255));
				choiceButtonPane.add(choiceButtons[i][j]);
				n++;
			}
		}
		
		/* Добавление панели кнопок выбора на панель кнопок */
		buttonPane.add(choiceButtonPane, BorderLayout.CENTER);
		
		/* 
		 * Инициализация кнопки "Очистить" 
		 * и добавление её на панель кнопок
		 */
		clearSlot = new JButton("Очистить");
		clearSlot.setFont(font);
		clearSlot.setPreferredSize(new Dimension(215,150));
		clearSlot.setBackground(new Color(240, 255, 255));
		clearSlot.setForeground(Color.RED);
		clearSlot.setEnabled(false);
		clearSlot.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	clearSlotButtonPressed();
            }
        });
		buttonPane.add(clearSlot, BorderLayout.WEST);
		
		/* 
		 * Инициализация кнопки "Начать заново" 
		 * и добавление её на панель кнопок
		 */
		clearField = new JButton("Начать заново");
		clearField.setFont(font);
		clearField.setBackground(new Color(240, 255, 255));
		clearField.setForeground(Color.RED);
		clearField.setEnabled(false);
		clearField.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	clearFieldButtonPressed();
            }
        });
		buttonPane.add(clearField, BorderLayout.EAST);
		
		/* Инициализация панели таймера */
		timerPane = new JPanel();
		timerPane.setLayout(new BorderLayout());
		
		/* Инициализация отображения таймера */
		timerText = new JLabel(" Time: 00:00");
		timerText.setFont(font);
		
		/* Инициализация таймера и добавление его на панель таймера */
		timer = new Timer(1000, new ActionListener(){
		      public void actionPerformed(ActionEvent e)
		      {
		    	  if(seconds == 60){
		    		  seconds = 0;
		    		  minutes++;
		    	  }
		    	  seconds++;
		    	  timerText.setText(String.format(" Time: %02d:%02d", minutes, seconds));
		      }
		});
		timerPane.add(timerText, BorderLayout.CENTER);
		
		/* 
		 * Инициализация кнопки "Пауза"
		 * и добавление её на панель таймера 
		 */
		pause = new JButton("Пауза");
		pause.setFont(new Font("PLAIN",Font.BOLD, 20));
		pause.setBackground(new Color(240, 255, 255));
		pause.setForeground(Color.RED);
		pause.setPreferredSize(new Dimension(215,40));
		pause.setEnabled(false);
		pause.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	pauseButtonPressed();
            }
        });
		timerPane.add(pause, BorderLayout.WEST);
		
		/* 
		 * Инициализация кнопки "Подсказка"
		 */
		hint = new JButton("Подсказка");
		hint.setFont(new Font("PLAIN",Font.BOLD, 20));
		hint.setBackground(new Color(240, 255, 255));
		hint.setForeground(Color.RED);
		hint.setPreferredSize(new Dimension(215,40));
		hint.setEnabled(false);
		hint.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	hintButtonPressed();
            }
        });
		timerPane.add(hint, BorderLayout.EAST);
		
		
		this.setLayout(new BorderLayout());			//Установка типа разметки Фрейма
		this.add(myPanel, BorderLayout.NORTH);		//Добавление игровой панели на Фрейм
		this.add(buttonPane, BorderLayout.CENTER);	//Добавление панели кнопок на Фрейм
		this.add(timerPane, BorderLayout.SOUTH);	//Добавление панели таймера на Фрейм
		this.pack();								//Сжимает окно до подходящего размера
		this.setResizable(false);					//Размер окна нельзя поменять
        /* Закрыть приложение, когда окно закрыто */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);			//Отображать окно по центру экрана
		this.setVisible(true);						//Показать окно приложения
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Новая игра".
	 * Производит необходимые установки для дальнейшей игры.
	 */
	public void newGamePressed(){
		hintWasUsed = false;				//Подсказка ещё не была использована
		saveGame.setEnabled(true);			//Разрешить использование кнопки "Сохранить игру"
		timerText.setText(" Time: 00:00");	//Таймер на нуле
		/* Запрос имени пользователя */
		playerName = JOptionPane.showInputDialog("Введите ваше имя (без пробелов):");
		/* Выбор уровня сложности */
		Object[] possibleValues = { "Элементарно", "Просто", "Стандартно", "Непросто", "Непостижимо" }; 
		String s = (String)JOptionPane.showInputDialog(
                null,
                "Выберите уровень сложности:",
                "Сложность",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibleValues,
                possibleValues[0]);
		if((s != null) && (s.length() > 0)){
			difficulty = s;
			/* Установка уровня сложности */
			setDifficulty(difficulty);
		}
		/* Установки новой игры на панели игры */
		myPanel.newGame();
		/* Активация кнопок выбора */
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				choiceButtons[i][j].setEnabled(true);
			}
		}
		/* Активация всех кнопок */
		clearSlot.setEnabled(true);
		clearField.setEnabled(true);
		pause.setEnabled(true);
		hint.setEnabled(true);
		/* Установка таймера */
		minutes = 0;
		seconds = 0;
		timer.start();
		/* Игра началась */
		gameOn = true;
	}
	
	/**
	 * Метод, активируемый нажатием одной из кнопок выбора.
	 */
	public void choiceButtonPressed(int r, int c){
		/* Переменная окончания активности кнопки */
		boolean done = false;
		/* Присвоение выбранной кнопке на поле значения нажатой кнопки */
		if(myPanel.coords[0] != -1 && myPanel.coords[1] != -1 && !done){
			String str = choiceButtons[r][c].getText();
			myPanel.fieldButtons[myPanel.coords[0]][myPanel.coords[1]].setText(str);
			myPanel.fieldButtons[myPanel.coords[0]][myPanel.coords[1]].setBackground(Color.WHITE);
			myPanel.coords[0] = -1;
			myPanel.coords[1] = -1;
			
			/* Проверить, закончилась ли игра */
			if(myPanel.check() == 0){
				gameOver();
				done = true;
			/* Если все кнопки заполнены, но есть ошибка, то сообщить */
			}else if(myPanel.check() == 1){
				timer.stop();
				JOptionPane.showConfirmDialog(null, "Что-то пошло не так...\n"
						+ "Головоломка решена неверно. Попробуйте начать заново\n"
						+ "или очистите содержимое ячейк, значение которых вызывает сомнения.",
						"Упс!" ,JOptionPane.PLAIN_MESSAGE);
				timer.start();
			}
		}	
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Очистка".
	 */
	public void clearSlotButtonPressed(){
		/* Убрать значение с выбранной кнопки */
		if(myPanel.coords[0] != -1 && myPanel.coords[1] != -1){
			myPanel.fieldButtons[myPanel.coords[0]][myPanel.coords[1]].setText("");
		}	
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Начать заново".
	 */
	public void clearFieldButtonPressed(){
		/* Отобразить сообщение, подтверждающее выбор */
		JPanel forD = new JPanel();
		JDialog D = new JDialog();
		JButton yes = new JButton("Да"), no = new JButton("Нет");
	   	forD.setLayout(new FlowLayout()); 
	   	forD.add(new JLabel("Вы действительно хотите начать заново?"));
		yes.addActionListener(new ActionListener(){
			/* Если выборано "Да", то очистить значения кнопок */
			public void actionPerformed(ActionEvent ae){
				for(int i = 0; i < 9; i++){
					for(int j = 0; j < 9; j++){
						if(myPanel.fieldButtons[i][j].isEnabled()){
							myPanel.fieldButtons[i][j].setText("");
						}
						minutes = 0;
						seconds = 0;
						timer.start();
					}
				}
				D.setVisible(false);
			}
		}); 
		no.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				D.setVisible(false);
		    }
		});  
		forD.add (yes); 
		forD.add(no); 
			 
		/* Установки окна подтверждения выбора */
		D.add(forD);
		D.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		D.setSize(300, 100);
		D.setResizable(false);
		D.setLocationRelativeTo(null);
		D.setVisible(true);	
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Пауза".
	 */
	public void pauseButtonPressed(){
		/* 
		 * Превратить кнопку "Пауза" в кнопку "Продолжить",
		 * если игра была активна, и наоборот. Когда игра
		 * на паузе, кнопки выбора, подсказки и очистки теряют
		 * активность, а таймер останавливается.
		 */
		if(gameOn){
			timer.stop();
			pause.setText("Продолжить");
			gameOn = !gameOn;
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					choiceButtons[i][j].setEnabled(false);
				}
			}
			clearSlot.setEnabled(false);
			hint.setEnabled(false);
		}else{
			timer.start();
			pause.setText("Пауза");
			gameOn = !gameOn;
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					choiceButtons[i][j].setEnabled(true);
				}
			}
			clearSlot.setEnabled(true);
			hint.setEnabled(true);
		}
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Подсказка".
	 */
	public void hintButtonPressed(){
		Random gen = new Random();
		boolean done = false;
		
		/*
		 * Если нет ошибок, то выбрать случайно
		 * пустую ячейку и присвоить ей верное значение
		 */
		if(myPanel.check() != 1){
			while(!done){
				int r = gen.nextInt(9);
				int c = gen.nextInt(9);
				JButton button = myPanel.fieldButtons[r][c];
				if(button.getText().equals("")){
					button.setText(new Integer(myPanel.getMatrix(r, c)).toString());
					done = true;
				}
			}
		/* 
		 * Иначе выделить красным ошибку и исправить её.
		 */
		}else{
			for(int i = 0; i < 9 && !done; i++){
				for(int j = 0; j < 9 && !done; j++){
					String str = myPanel.fieldButtons[i][j].getText();
					int value = Integer.parseInt(str.trim());
					JButton button = myPanel.fieldButtons[i][j];
					if(Math.abs(myPanel.getMatrix(i, j)) != value){
						button.setText(new Integer(myPanel.getMatrix(i, j)).toString());
						button.setBackground(Color.RED);
						done = true;
					}
				}
			}
		}
		
		/* Игрок воспользовался подсказкой */
		hintWasUsed = true;
		
		/* Проверить, закночена ли игра */
		if(myPanel.check() == 0){
			gameOver();
		/* Если все кнопки заполнены, но есть ошибки, то сообщить */
		}else if(myPanel.check() == 1){
			timer.stop();
			JOptionPane.showConfirmDialog(null, "Что-то пошло не так...\n"
					+ "Головоломка решена неверно. Попробуйте начать заново\n"
					+ "или очистите содержимое ячейк, значение которых вызывает сомнения.",
					"Упс!" ,JOptionPane.PLAIN_MESSAGE);
			timer.start();
		}
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Сохранить игру".
	 */
	public void saveGameButtonPressed(){
		/* Заполнить массив очков */
		scores = new String[19][9];
		scores = myPanel.saveGame(scores);
		scores[18][0] = playerName;
		scores[18][1] = difficulty;
		scores[18][2] = new Integer(minutes).toString();
		scores[18][3] = new Integer(seconds).toString();
		scores[18][4] = new Boolean(hintWasUsed).toString();
		
		/* Записать массив очков в текстовый файл */
    	try {
    		myFile = new FileWriter(playerName + ".txt");
    		buff = new BufferedWriter(myFile);
    		for(int i = 0; i < 19; i++){
    			if(i == 18){
    				for(int j = 0; j < 5; j++){
    					buff.write(scores[i][j]);
	    	        	buff.write(" ");
    				}
    			}else{
	    			for(int j = 0; j < 9; j++){
	    				buff.write(scores[i][j]);
	    	        	buff.write(" ");
	    			}
    			}
    		}
    	}catch (IOException ioe){
    		ioe.printStackTrace();
    	} finally {
    		try{
    			buff.flush();
    			buff.close();
    			myFile.close();
    		}catch(IOException e1){
    			e1.printStackTrace();
    		}
    	}
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Загрузить игру".
	 */
	public void loadGameButtonPressed(){
		scores = new String[19][9];
		/* Получить имя файла */
		JOptionPane.showConfirmDialog(null, "Чтобы загрузить игру,\n"
				+"введите ваше имя!",
				"Внимание" ,JOptionPane.PLAIN_MESSAGE);
		playerName = JOptionPane.showInputDialog("Введите ваше имя:");
		/* Извлечь данные из выбранного файла */
		try {
			myF = new FileReader(playerName + ".txt");
			b = new BufferedReader(myF);
			while (true) {
				String line = b.readLine();
				if (line == null) break;
				/* Данные в файле разделены пробелом */
				/* Поэтому для извлечения необходимо убрать пробелы */
				/* И заполнить массив данными, которые содержатся между ними */
				String[] parts = line.split(" ");
				int partsCounter = 0;
				for(int i = 0; i < 18; i++){
					for(int j = 0; j < 9; j++){
						if(!parts[partsCounter].equals("u")){
							scores[i][j] = parts[partsCounter];
							partsCounter++;
						}else{
							scores[i][j] = "o";
							partsCounter++;
						}
					}
				}
				/* Первый элемент не нужен, поэтому он пропускается */
				partsCounter++;
				
				/* Произвести установку данных игры в соответствии с данными из файла */
				difficulty = parts[partsCounter];
				setDifficulty(difficulty);
				partsCounter++;
				minutes = new Integer(parts[partsCounter]);
				partsCounter++;
				seconds = new Integer(parts[partsCounter]);
				partsCounter++;
				hintWasUsed = new Boolean(parts[partsCounter]);
				
				/* Активировать кнопки */
				saveGame.setEnabled(true);
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						choiceButtons[i][j].setEnabled(true);
					}
				}
				clearSlot.setEnabled(true);
				clearField.setEnabled(true);
				pause.setEnabled(true);
				hint.setEnabled(true);
				timer.start();
				gameOn = true;
			}
		}catch (IOException ioe){
			ioe.printStackTrace();
			JOptionPane.showConfirmDialog(null, "Файл не найден!",
					"Упс!" ,JOptionPane.PLAIN_MESSAGE);
		} finally {
			try{
				b.close();
				myF.close();
			}catch(IOException e1){
				e1.printStackTrace();
			}
		}
		/* Установить кнопки игры */
		myPanel.loadGame(scores);
		
		/* Проверить, закночена ли игра */
		if(myPanel.check() == 0){
			gameOver();
			
		/* Если все кнопки заполнены, но есть ошибки, то сообщить */
		}else if(myPanel.check() == 1){
			timer.stop();
			JOptionPane.showConfirmDialog(null, "Что-то пошло не так...\n"
					+ "Головоломка решена неверно. Попробуйте начать заново\n"
					+ "или очистите содержимое ячейк, значение которых вызывает сомнения.",
					"Упс!" ,JOptionPane.PLAIN_MESSAGE);
			timer.start();
		}else{
			timer.start();
		}
	}
	
	/**
	 * Метод, активируемый нажатием кнопки "Список лидеров".
	 */
	public void leaderBoardButtonPressed(){
		/* Создаёт экземпляр списка лидеров */
		new LeaderBoard();
	}
	
	/**
	 * Метод, активируемый по окончании игры.
	 */
	public void gameOver(){
		/* Остановить таймер */
		timer.stop();
		/* Отобразить сообщение о победе. Учесть, пользовался ли игрок подсказкой или нет */
		JOptionPane.showConfirmDialog(null, String.format("Головоломка решена верно!\n" +
				"Вы справились с " + stringForMessage + " головоломкой за %02d:%02d.\n" + 
				(hintWasUsed? "В следующий раз постарайтесь не пользоваться подсказкой! ;)" : ""), minutes, seconds),
				"Поздравляем!" ,JOptionPane.PLAIN_MESSAGE);
		/* Лишить кнопки активности */
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				myPanel.fieldButtons[i][j].setEnabled(false);
				if(i < 3 && j < 3){
					choiceButtons[i][j].setEnabled(false);
				}
			}
		}
		clearSlot.setEnabled(false);
		clearField.setEnabled(false);
		pause.setEnabled(false);
		hint.setEnabled(false);
		
		/* Добавить игрока в список лидеров */
		try {
    		leader = "0 "+ playerName + " " + difValue + " " + 
    				(minutes * 60 + seconds) + " " + hintWasUsed + " ";
    		myFile = new FileWriter("leaderBoard.txt", true);
    		buff = new BufferedWriter(myFile);
        	buff.write(leader);
    	}catch (IOException ioe){
    		ioe.printStackTrace();
    	} finally {
    		try{
    			buff.flush();
    			buff.close();
    			myFile.close();
    		}catch(IOException e1){
    			e1.printStackTrace();
    		}
    	}
		/* Отобразить список лидеров */
		new LeaderBoard();
	}
	
	/**
	 * Устанавливает сложность. Взодной параметр - сложность типа String.
	 */
	public void setDifficulty(String difficulty){
		switch(difficulty){
			case "Элементарно":
				myPanel.setDifficulty(65);
				stringForMessage = "элементарной";
				difValue = 1;
				break;
			case "Просто":
				myPanel.setDifficulty(47);
				stringForMessage = "простой";
				difValue = 2;
				break;
			case "Стандартно":
				myPanel.setDifficulty(14);
				stringForMessage = "стандартной";
				difValue = 3;
				break;
			case "Непросто":
				myPanel.setDifficulty(7);
				stringForMessage = "непростой";
				difValue = 4;
				break;
			case "Непостижимо":
				myPanel.setDifficulty(5);
				stringForMessage = "непостижимой";
				difValue = 5;
				break;
		}
	}
}
