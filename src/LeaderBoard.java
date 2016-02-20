import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

/**
 * Фрейм, на котором отображается список лидеров.
 * Сортировка списка лидеров производится по следующим параметрам:
 * 		- сложность головоломки
 * 		- время, потраченное на решение
 * 		- была ли использована подсказка
 * Приоритет каждого из параметров соответствует его месту в порядке,
 * представленном выше.
 * При вызове этого класса производится чтение файлся leaderBoard.txt
 */

public class LeaderBoard extends JFrame implements ActionListener {
	/* Панель на которой хранится информация о лидерах */
	JPanel myPanel;				
	JButton ok;					//Кнопка выхода
	/* Переменные для чтения из файла */
    FileReader myFile = null;
    BufferedReader buff = null;
    String[] scores;			//Строка данных об игроке из файла
    String[][] data;			//Массива данных о лидерах
	//Переменная, которая хранит параметры шрифта большинства элементов приложения
	Font font;
    
    /**
     * Создаёт фрейм для отображения списка лидеров
     */
	LeaderBoard(){
		//Инициализация параметров шрифта
		font = new Font("PLAIN",Font.BOLD, 25);	
		/* Настройка фрейма */
		myPanel = new JPanel();		//Инициализация панели
		
		JPanel title = new JPanel();
		title.setLayout(new GridLayout(1,5));
		JLabel place = new JLabel("Место");
		place.setFont(font);
		place.setForeground(Color.RED);
		title.add(place);
		JLabel name = new JLabel("Имя игрока");
		name.setFont(font);
		name.setForeground(Color.RED);
		title.add(name);
		JLabel dif = new JLabel("Сложность");
		dif.setFont(font);
		dif.setForeground(Color.RED);
		title.add(dif);
		JLabel tim = new JLabel("Время");
		tim.setFont(font);
		tim.setForeground(Color.RED);
		title.add(tim);
		JLabel hnt = new JLabel("Подсказка");
		hnt.setFont(font);
		hnt.setForeground(Color.RED);
		title.add(hnt);
		
		setLayout(new BorderLayout());	//Установка разметки
		setBounds(0, 0, 1200, 800);		//Установка размера фрейма
        setFocusable(false);			//Установка для применения настроек границ
        setResizable(false);			//Нельзя поменять размер фрейма
        setVisible(true);				//Сделать видимым
        setTitle("Список лидеров");		//Установка заголовка
        setLocationRelativeTo(null);	//Центральное расположение
        /* Закрыть приложение, когда окно закрыто */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* Считать данные из файла */
        try {
        	/* Имя файла, который нужно прочитать */
			myFile = new FileReader("leaderBoard.txt");
			buff = new BufferedReader(myFile);
			while (true) {
				String line = buff.readLine();
				if (line == null) break;
				/* Разделить строку на части, разделённые пробелами */
				scores = line.split(" ");
			}
			/* перевести данные из файла в матрицу */
			int counter=0;
			data = new String[scores.length/5][5];
			for(int i = 0; i < scores.length/5; i++){
				for(int j = 0; j < 5; j++){
					data[i][j] = scores[counter];
					counter++;
				}
			}
			
			/* Cортировка методом пузырька (по сложности) */
			for(int i = 0; i < (scores.length/5 - 1); i++){
				for(int j = 0; j < (scores.length/5 - i - 1); j++){
					if(Integer.parseInt(data[j][2]) < Integer.parseInt(data[j + 1][2])){
						String nameT, difT, timeT, hintT;
						nameT = data[j][1];
						difT = data[j][2];
						timeT = data[j][3];
						hintT = data[j][4];
						data[j][1] = data[j+1][1];
						data[j][2] = data[j+1][2];
						data[j][3] = data[j+1][3];
						data[j][4] = data[j+1][4];
						data[j+1][1] = nameT;
						data[j+1][2] = difT;
						data[j+1][3] = timeT;
						data[j+1][4] = hintT;
					}
				}
			}

			/* Cортировка методом пузырька (по времени) */
			for(int i = 0; i < (scores.length/5 - 1); i++){
				for(int j = 0; j < (scores.length/5 - i - 1); j++){
					if(Integer.parseInt(data[j][2]) == Integer.parseInt(data[j + 1][2])){
						if(Integer.parseInt(data[j][3]) > Integer.parseInt(data[j + 1][3])){
							String nameT, difT, timeT, hintT;
							nameT = data[j][1];
							difT = data[j][2];
							timeT = data[j][3];
							hintT = data[j][4];
							data[j][1] = data[j+1][1];
							data[j][2] = data[j+1][2];
							data[j][3] = data[j+1][3];
							data[j][4] = data[j+1][4];
							data[j+1][1] = nameT;
							data[j+1][2] = difT;
							data[j+1][3] = timeT;
							data[j+1][4] = hintT;
						}
					}
				}
			}
			/* Cортировка методом пузырька (по подсказкам) */
			for(int i = 0; i < (scores.length/5 - 1); i++){
				for(int j = 0; j < (scores.length/5 - i - 1); j++){
					if(Integer.parseInt(data[j][2]) == Integer.parseInt(data[j + 1][2])){
						if((data[j][4].equals("true") && data[j + 1][4].equals("false")) ||
								(data[j][4].equals("true") && data[j + 1][4].equals("true") && 
										Integer.parseInt(data[j][3]) > Integer.parseInt(data[j + 1][3]))){
							String nameT, difT, timeT, hintT;
							nameT = data[j][1];
							difT = data[j][2];
							timeT = data[j][3];
							hintT = data[j][4];
							data[j][1] = data[j+1][1];
							data[j][2] = data[j+1][2];
							data[j][3] = data[j+1][3];
							data[j][4] = data[j+1][4];
							data[j+1][1] = nameT;
							data[j+1][2] = difT;
							data[j+1][3] = timeT;
							data[j+1][4] = hintT;
						}
					}
				}
			}
			/* Расположить данные об игроках */
			for(int i = 0; i < scores.length/5; i++){
				for(int j = 0; j < 5; j++){
					JLabel currentLabel = new JLabel();
					myPanel.setLayout(new GridLayout(scores.length/5,5));
					if(j == 0){
						currentLabel.setText(String.valueOf(i+1));
					}else if(j == 2){
						switch(Integer.parseInt(data[i][j])){
							case 1:
								currentLabel.setText("Элементарно");
								break;
							case 2:
								currentLabel.setText("Просто");
								break;
							case 3:
								currentLabel.setText("Стандартно");
								break;
							case 4:
								currentLabel.setText("Непросто");
								break;
							case 5:
								currentLabel.setText("Непостижимо");
								break;
						}
					}else if(j == 3){
						int time = new Integer(data[i][j]);
						currentLabel.setText(String.format("%02d:%02d", (time / 60), (time % 60)));
					}else if(j == 4){
						if(data[i][j].equals("false")){
							currentLabel.setText("Не пользовался");
						}else{
							currentLabel.setText("Пользовался");
						}
					}else{
						currentLabel.setText(data[i][j]);
					}
					currentLabel.setFont(font);
					myPanel.add(currentLabel);
				}
			}
			
        }catch (IOException ioe){
			ioe.printStackTrace();
		} finally {
			try{
				buff.close();
				myFile.close();
			}catch(IOException e1){
				e1.printStackTrace();
			}
		}
        add(myPanel, BorderLayout.CENTER);
        add(title,BorderLayout.NORTH);
        
        /* Настройка кнопки "Назад" */
        JPanel okPane = new JPanel();
        ok = new JButton("Назад");
		ok.setPreferredSize(new Dimension(200,40));
		ok.setFont(font);
        ok.addActionListener(this);
        okPane.add(ok);
        add(okPane, BorderLayout.SOUTH);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
	}
}