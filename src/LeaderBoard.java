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
 * �����, �� ������� ������������ ������ �������.
 * ���������� ������ ������� ������������ �� ��������� ����������:
 * 		- ��������� �����������
 * 		- �����, ����������� �� �������
 * 		- ���� �� ������������ ���������
 * ��������� ������� �� ���������� ������������� ��� ����� � �������,
 * �������������� ����.
 * ��� ������ ����� ������ ������������ ������ ������ leaderBoard.txt
 */

public class LeaderBoard extends JFrame implements ActionListener {
	/* ������ �� ������� �������� ���������� � ������� */
	JPanel myPanel;				
	JButton ok;					//������ ������
	/* ���������� ��� ������ �� ����� */
    FileReader myFile = null;
    BufferedReader buff = null;
    String[] scores;			//������ ������ �� ������ �� �����
    String[][] data;			//������� ������ � �������
	//����������, ������� ������ ��������� ������ ����������� ��������� ����������
	Font font;
    
    /**
     * ������ ����� ��� ����������� ������ �������
     */
	LeaderBoard(){
		//������������� ���������� ������
		font = new Font("PLAIN",Font.BOLD, 25);	
		/* ��������� ������ */
		myPanel = new JPanel();		//������������� ������
		
		JPanel title = new JPanel();
		title.setLayout(new GridLayout(1,5));
		JLabel place = new JLabel("�����");
		place.setFont(font);
		place.setForeground(Color.RED);
		title.add(place);
		JLabel name = new JLabel("��� ������");
		name.setFont(font);
		name.setForeground(Color.RED);
		title.add(name);
		JLabel dif = new JLabel("���������");
		dif.setFont(font);
		dif.setForeground(Color.RED);
		title.add(dif);
		JLabel tim = new JLabel("�����");
		tim.setFont(font);
		tim.setForeground(Color.RED);
		title.add(tim);
		JLabel hnt = new JLabel("���������");
		hnt.setFont(font);
		hnt.setForeground(Color.RED);
		title.add(hnt);
		
		setLayout(new BorderLayout());	//��������� ��������
		setBounds(0, 0, 1200, 800);		//��������� ������� ������
        setFocusable(false);			//��������� ��� ���������� �������� ������
        setResizable(false);			//������ �������� ������ ������
        setVisible(true);				//������� �������
        setTitle("������ �������");		//��������� ���������
        setLocationRelativeTo(null);	//����������� ������������
        /* ������� ����������, ����� ���� ������� */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* ������� ������ �� ����� */
        try {
        	/* ��� �����, ������� ����� ��������� */
			myFile = new FileReader("leaderBoard.txt");
			buff = new BufferedReader(myFile);
			while (true) {
				String line = buff.readLine();
				if (line == null) break;
				/* ��������� ������ �� �����, ���������� ��������� */
				scores = line.split(" ");
			}
			/* ��������� ������ �� ����� � ������� */
			int counter=0;
			data = new String[scores.length/5][5];
			for(int i = 0; i < scores.length/5; i++){
				for(int j = 0; j < 5; j++){
					data[i][j] = scores[counter];
					counter++;
				}
			}
			
			/* C��������� ������� �������� (�� ���������) */
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

			/* C��������� ������� �������� (�� �������) */
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
			/* C��������� ������� �������� (�� ����������) */
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
			/* ����������� ������ �� ������� */
			for(int i = 0; i < scores.length/5; i++){
				for(int j = 0; j < 5; j++){
					JLabel currentLabel = new JLabel();
					myPanel.setLayout(new GridLayout(scores.length/5,5));
					if(j == 0){
						currentLabel.setText(String.valueOf(i+1));
					}else if(j == 2){
						switch(Integer.parseInt(data[i][j])){
							case 1:
								currentLabel.setText("�����������");
								break;
							case 2:
								currentLabel.setText("������");
								break;
							case 3:
								currentLabel.setText("����������");
								break;
							case 4:
								currentLabel.setText("��������");
								break;
							case 5:
								currentLabel.setText("�����������");
								break;
						}
					}else if(j == 3){
						int time = new Integer(data[i][j]);
						currentLabel.setText(String.format("%02d:%02d", (time / 60), (time % 60)));
					}else if(j == 4){
						if(data[i][j].equals("false")){
							currentLabel.setText("�� �����������");
						}else{
							currentLabel.setText("�����������");
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
        
        /* ��������� ������ "�����" */
        JPanel okPane = new JPanel();
        ok = new JButton("�����");
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