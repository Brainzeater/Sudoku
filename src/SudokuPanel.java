import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * ������ ����������� �������� ��������� ����
 */
public class SudokuPanel extends JPanel{
	private SudokuGame myGame;		//���� ��� �����������
	JButton[][] fieldButtons;		//������ ������ ����
	int[] coords = {-1, -1};		//������ ��������� ������� ������
	/**
	 * ������ ������, ������������ ������� ��������� ����
	 */
	public SudokuPanel(){
		super();				//��������� ������������ �����������
		fieldButtons = new JButton[9][9];	//������������� ������� ������
		this.setLayout(new GridLayout(3, 3));	//��������� �������� ������
		/* ������������� ����������� � ��������� ������ ���������� "����������"
		 * ������ ������ �������� "����������" ��� ������������ �����������
		 * � �� ��������� ��� ������� ������, ��������, ��� ������ ������
		 * �������� ������������������ �� ������� �� ������
		 */
		int n = 1;
		for(int row = 0; row < 3; row++)
		{
			n = row + 1;
			for(int column = 0; column < 3; column++)
			{
				n += column * 3;
				if(n > 9){
					n -= 3;
				}
				JPanel areaPane = new JPanel();
				areaPane.setLayout(new GridLayout(3, 3));
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						int goodI = i + row * 3;
						int goodJ = j + column * 3;
						fieldButtons[goodI][goodJ] = new JButton(new Integer(n).toString());
						fieldButtons[goodI][goodJ].setEnabled(false);
						fieldButtons[goodI][goodJ].setFont(new Font("PLAIN",Font.BOLD, 35));
						fieldButtons[goodI][goodJ].setBackground(Color.LIGHT_GRAY);
						fieldButtons[goodI][goodJ].addActionListener(new ActionListener() { 
				            public void actionPerformed(ActionEvent e)
				            {
				            	/* ��� ������� ������, � ������ ���������
				            	 *  ����������� ���������� ������� ������ */
				                coords[0] = goodI;
				                coords[1] = goodJ;
				                /* "��������" ��������� ������ */
				                for(int i = 0; i < 9; i++){
				    				for(int j = 0; j < 9; j++){
				    					fieldButtons[i][j].setBackground(Color.WHITE);
				    				}
				    			}
				                /* �������� ������ ������ */
				                fieldButtons[goodI][goodJ].setBackground(Color.YELLOW);
				            }
				        });
						areaPane.add(fieldButtons[goodI][goodJ]);
						n++;
						if(n > 9){
							n = 1;
						}
					}
				}
				/* �������� ���� 3�3 */
				areaPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
				this.add(areaPane);
			}
		}
		
		/* ������� ����� ���� */
		myGame = new SudokuGame(this);
	}
	
	/**
	 * ����� ������������ ��� ������� ������ "����� ����".
	 * �������� �� ��������� ����� �����������.
	 */
	public void newGame(){
		setDeafault();
		myGame.newGame();
		refresh();
		check();
	}
	
	/**
	 * ���������� ��������� ����������� �� ���������.
	 */
	public void setDeafault(){
		int n = 1;
		for(int row = 0; row < 3; row++)
		{
			n = row + 1;
			for(int column = 0; column < 3; column++)
			{
				n += column * 3;
				if(n > 9){
					n -= 3;
				}
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						fieldButtons[i + row * 3][j + column * 3].setText(
								new Integer(n).toString());
						fieldButtons[i + row * 3][j + column * 3].setEnabled(false);
						fieldButtons[i + row * 3][j + column * 3].setFont(new Font("PLAIN",Font.BOLD, 35));
						fieldButtons[i + row * 3][j + column * 3].setForeground(Color.BLACK);
						n++;
						if(n > 9){
							n = 1;
						}
					}
				}
			}
		}
	}
	
	public int check(){
		int result = 0;
		int counter = 0;
		int wrongCounter = -1;
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(myGame.matrix[i][j] < 0){
					result++;
					String str = fieldButtons[i][j].getText();
					if(!str.equals("")){
						int value = Integer.parseInt(str.trim());
						if(Math.abs(myGame.matrix[i][j]) == value){
							result--;
						}else{
							wrongCounter++;
						}
					}
				}
				if(!fieldButtons[i][j].getText().equals("")){
					counter++;
				}
			}
		}
		if(counter == 81){
			if(wrongCounter != -1){
				return result - wrongCounter;
			}else{
				return result;
			}
		}else{
			return -1;
		}
	}
	
	/**
	 * ������������� ��������� ����.
	 */
	public void setDifficulty(int value){
		myGame.difficulty = value;
		refresh();
	}
	
	/**
	 * ���������� � ������ ��������� �������� ����
	 */
	public String[][] saveGame(String[][] m){
		/* ������ ������������ ������� ����, ����� �������� ������ */
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				m[i][j] = new Integer(myGame.matrix[i][j]).toString();
			}
		}
		for(int i = 9; i < 18; i++){
			for(int j = 0; j < 9; j++){
				if(fieldButtons[i - 9][j].isEnabled()&&
						!fieldButtons[i - 9][j].equals("")){
					m[i][j] = "u" + fieldButtons[i - 9][j].getText();
				}else{
					m[i][j] = fieldButtons[i - 9][j].getText();
				}
			}
		}
		return m;
	}
	
	/**
	 * ���������� ��������� ���� ��� �������� ����.
	 */
	public void loadGame(String[][] m){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				myGame.matrix[i][j] = new Integer(m[i][j]);
			}
		}
		for(int i = 9; i < 18; i++){
			for(int j = 0; j < 9; j++){
				fieldButtons[i - 9][j].setBackground(Color.WHITE);
				if(m[i][j].substring(0, 1).equals("u")){
					fieldButtons[i - 9][j].setText(m[i][j].substring(1, 2));
					fieldButtons[i - 9][j].setEnabled(true);
				}else if(m[i][j].equals("o")){
					fieldButtons[i - 9][j].setText("");
					fieldButtons[i - 9][j].setEnabled(true);
				}else{
					fieldButtons[i - 9][j].setText(m[i][j]);
					fieldButtons[i - 9][j].setEnabled(false);
				}
			}
		}
	}
	
	/**
	 * ���������� �������� �������� ������� ��������
	 */
	public int getMatrix(int i, int j){
		return myGame.matrix[i][j] * (-1);
	}
	
	/**
	 * ���������� ������� ������ � ������������ � �������� �������� ������ SudokuGame
	 * ����� ��������� ��� ����������� ��������������� �����������
	 */
	public void refresh(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				fieldButtons[i][j].setBackground(Color.WHITE);
				if(myGame.matrix[i][j] < 0){
					fieldButtons[i][j].setText("");
					fieldButtons[i][j].setEnabled(true);
				}else{
					fieldButtons[i][j].setEnabled(false);
					fieldButtons[i][j].setText(new Integer(myGame.matrix[i][j]).toString());
					
				}
			}
		}
	}
}
