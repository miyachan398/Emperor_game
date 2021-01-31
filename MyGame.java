import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.image.*;//�摜�����ɕK�v
import java.awt.geom.*;//�摜�����ɕK�v
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Container;
import java.awt.BorderLayout;
import java.applet.*;//wav�t�@�C���̍Đ��Ɏg�p

public class MyGame extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[], title;//�{�^���p�̔z��
	private Container c;
	private AudioClip bgm, se;
	private JLabel titleLabel, winLabel, loseLabel;//��������ƁA�^�C�g����\�����邽�߂�JLabel
	
	
	private int myStatus;//�c�鑤���z�ꑤ���̔���
	
	private int slaveNum = 11;
	private int emperorNum = 11;
	
	private ImageIcon titleIcon, citizenIcon, emperorIcon, slaveIcon, playIcon, cardIcon, backIcon, winIcon, loseIcon;
	
	PrintWriter out;//�o�͗p�̃��C�^
	
	public int theBNum = 12;
	public int theENum = 12;
	public int theSNum = 12;
	
	private int emperorTurn = 1;//�c�鑤�̃^�[����
	private int slaveTurn = 1;//�z�ꑤ�̃^�[����
	
	private int emperorSelect, slaveSelect, judgeE, judgeS;
	
	boolean isTitle,isStart, isCheck, myPlay, yourPlay, emperorPlay, slavePlay, numberHold;
	
	String[] str = {"5","6","7","8","9","10","11","12","13"};//������

	public MyGame() {
		
		emperorSelect = slaveSelect = 12;
		
		judgeE = judgeS = 0;
		
		myPlay = yourPlay = false;
		emperorPlay = slavePlay = false;
		isTitle = isStart = isCheck = numberHold = true;
		
		
		
		
		//���O�̓��̓_�C�A���O���J��
		String myName = JOptionPane.showInputDialog(null,"���O����͂��Ă�������","���O�̓���",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//���O���Ȃ��Ƃ��́C"No name"�Ƃ���
		}
		
		//IP�A�h���X�̓��̓_�C�A���O���J��
		String myAdress = JOptionPane.showInputDialog(null,"IP�A�h���X����͂��Ă�������","IP�A�h���X�̓���",JOptionPane.QUESTION_MESSAGE);

		//�E�B���h�E���쐬����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�E�B���h�E�����Ƃ��ɁC����������悤�ɐݒ肷��
		setTitle("MyGame");//�E�B���h�E�̃^�C�g����ݒ肷��
		setSize(1200,900);//�E�B���h�E�̃T�C�Y��ݒ肷��
		c = getContentPane();//�t���[���̃y�C�����擾����

		//�A�C�R���̐ݒ�
		titleIcon = new ImageIcon("Title.png");
		slaveIcon = new ImageIcon("slave_card.png");
		emperorIcon = new ImageIcon("emperor.png");
		citizenIcon = new ImageIcon("citizen.png");
		cardIcon = new ImageIcon("card.png");
		playIcon = new ImageIcon("play.png");
		backIcon = new ImageIcon("table.png");
		winIcon = new ImageIcon("Win.png");
		loseIcon = new ImageIcon("Lose.png");
		
		//JLabelno�ݒ�i���s���̃|�b�v�A�b�v�j
		winLabel = new JLabel(winIcon);
		loseLabel = new JLabel(loseIcon);
		
		//�������̃|�b�v�A�b�v�̉摜��\�����鏀��
		c.add(winLabel);
		winLabel.setBounds(570,380,800,300);
		winLabel.setVisible(false);
		
		//�s�k���̃|�b�v�A�b�v�̉摜��\�����鏀��
		c.add(loseLabel);
		loseLabel.setBounds(570,380,800,300);
		loseLabel.setVisible(false);
		
		
		c.setLayout(null);//�������C�A�E�g�̐ݒ���s��Ȃ�
		
		//�{�^���̐���
		buttonArray = new JButton[12];//�{�^���̔z����T�쐬����[0]����[10]�܂Ŏg����
		for(int i=0;i<12;i++){
			
			//�{�^���ɃA�C�R����ݒ肷��
			buttonArray[i] = new JButton(citizenIcon);
			c.add(buttonArray[i]);//�y�C���ɓ\��t����
			
			
			buttonArray[i].setBounds(i*200 + 450,760,180,230);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
			
			//Play�{�^���̐ݒ�
			if(i==5){
				buttonArray[i].setBounds(1650,425,180,130);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
				buttonArray[i].setIcon(playIcon);
			}
			
			//���葤�̎�D�̃{�^���ݒ�
			if(i > 5){
				buttonArray[i].setBounds((i%6)*200 + 450,10,180,230);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
				buttonArray[i].setIcon(cardIcon);
			}
			
			if(i == 11){
				buttonArray[i].setBounds(0, 0, 1950, 1000);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
				buttonArray[i].setIcon(titleIcon);
			}
			
			buttonArray[i].addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
			buttonArray[i].addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
			buttonArray[i].setActionCommand(Integer.toString(i));
			buttonArray[i].setVisible(false);
		}
		
		
		
		JLabel backLabel = new JLabel(backIcon); //�w�i�摜�̏���
		c.add(backLabel, BorderLayout.NORTH); //�p�X�{�^�����R���e�i�ɒǉ�
		backLabel.setBounds(0,0, 1950, 1000);//�w�i�摜�̒u���ꏊ������(x���W�Cy���W,x�̕�,y�̕�)
		
		//�����̓ǂݍ���
        bgm = Applet.newAudioClip(getClass().getResource("energy.wav"));
        se = Applet.newAudioClip(getClass().getResource("button.wav"));
		
		
		bgm.loop();	//�����̍Đ�
		
		
		
		//�T�[�o�ɐڑ�����
		Socket socket = null;
		try {
			
			//"localhost"�́C���������ւ̐ڑ��Dlocalhost��ڑ����IP Address�i"133.42.155.201"�`���j�ɐݒ肷��Ƒ���PC�̃T�[�o�ƒʐM�ł���
			//10000�̓|�[�g�ԍ��DIP Address�Őڑ�����PC�����߂āC�|�[�g�ԍ��ł���PC�㓮�삷��v���O��������肷��
			socket = new Socket("localhost", 10000);
			
		} catch (UnknownHostException e) {
			
			System.err.println("�z�X�g�� IP �A�h���X������ł��܂���: " + e);
			
		} catch (IOException e) {
			
			System.err.println("�G���[���������܂���: " + e);
			
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//��M�p�̃X���b�h���쐬����
		mrt.start();//�X���b�h�𓮂����iRun�������j
		
	}
		
	//���b�Z�[�W��M�̂��߂̃X���b�h
	public class MesgRecvThread extends Thread {
		
		Socket socket;
		String myName;
		
		public MesgRecvThread(Socket s, String n){
			
			socket = s;
			myName = n;
			
		}
		
		//�ʐM�󋵂��Ď����C��M�f�[�^�ɂ���ē��삷��
		public void run() {
			
			try{
				
				System.out.println(theBNum);
				
				
				
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//�ڑ��̍ŏ��ɖ��O�𑗂�
				
				String myNumberStr = br.readLine();//�T�[�o���������ԍ����󂯎���
				System.out.println(myNumberStr);
				int myNumberInt = Integer.parseInt(myNumberStr);
				
				//�c�邩�z�ꂩ���߂�
				if(myNumberInt % 2 == 0){
					
					myStatus = 0;//�c�鑤
					
				}else{
					
					myStatus = 1;//�z�ꑤ
					
				}
				
				if(myStatus == 0){//�c�鑤�̃J�[�h�̊G���̐ݒ�
				
					for(int a=0;a<5;a++){
						
						buttonArray[a].setIcon(citizenIcon);
						
					}
					for(int a=6;a<11;a++){
						
						buttonArray[a].setIcon(cardIcon);
						
					}
					
					buttonArray[0].setIcon(emperorIcon);
					buttonArray[5].setIcon(playIcon);//Play�{�^��
					
				}else{//�z�ꑤ�̃J�[�h�̊G���̐ݒ�
				
					for(int a=0;a<5;a++){
						
						buttonArray[a].setIcon(citizenIcon);
						
					}
					for(int a=6;a<11;a++){
						
						buttonArray[a].setIcon(cardIcon);
						
					}
					
					buttonArray[0].setIcon(slaveIcon);
					buttonArray[5].setIcon(playIcon);//Play�{�^��
					
				}
				
				while(true) {
					
					if(isStart){
						
						if(isTitle){
							
							buttonArray[11].setVisible(true);//�^�C�g����\��
							
						}else{
							for(int a = 0; a < 11; a++){
								
								buttonArray[a].setVisible(true);//�^�C�g���������Ă����D��\��
								isStart = false;
							}
						}
					}
						
					
					//��������Ɉڍs������
					if(judgeE != 0 && judgeS != 0){
						
						if(judgeE == judgeS){
							
							isCheck = false;
							judge(emperorSelect, slaveSelect);
							
							isCheck = true;
							judgeE = judgeS = 0;
							
						}
					
					}
					
					String inputLine = br.readLine();//�f�[�^����s�������ǂݍ���ł݂�
					
					if (inputLine != null) {//�ǂݍ��񂾂Ƃ��Ƀf�[�^���ǂݍ��܂ꂽ���ǂ������`�F�b�N����
					
						System.out.println(inputLine);//�f�o�b�O�i����m�F�p�j�ɃR���\�[���ɏo�͂���
						String[] inputTokens = inputLine.split(" ");	//���̓f�[�^����͂��邽�߂ɁA�X�y�[�X�Ő؂蕪����
						String cmd = inputTokens[0];//�R�}���h�̎��o���D�P�ڂ̗v�f�����o��
						
						if(cmd.equals("MOVE")){//cmd�̕�����"MOVE"�����������ׂ�D��������true�ƂȂ�
						
							isCheck = true;
							//MOVE�̎��̏���(�R�}�̈ړ��̏���)
							String theBName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							
							//������D�̒������ɏo���J�[�h��I��ł��邩�ǂ����̌��m
							if(Arrays.asList(str).contains(theBName)){
							}else{
								
								theBNum = Integer.parseInt(theBName);//�I�񂾃J�[�h�̔z��ԍ�
								
							}
							
						}
						
						//�c�鑤�ɉ��̃J�[�h���o�������𑗂�i�����̃J�[�h�͏�ɏo�Ă��邪�A�ΐ푊��̉�ʂɂ͂܂��\������Ă��Ȃ��ׁj
						if(cmd.equals("EMPERORRECEIVE")){//cmd�̕�����"EMPERORRECEIVE"�����������ׂ�D��������true�ƂȂ�
							
							emperorPlay = true;
							
							
							String theEName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							theENum = Integer.parseInt(theEName);
							
							slaveSelect = Integer.parseInt(inputTokens[2]);//�z�ꑤ�̃v���C���[���I�񂾃J�[�h�̔z��ԍ�
							
							if(theENum == 0){//�z�ꑤ���z����o������
								slaveNum = theENum;
							}else{
								slaveNum = theENum + 6;
							}
							
							
							slavePlay = false;
							
							receive();
							
							judgeE = 1;
							
						}
						
						//�z�ꑤ�ɉ��̃J�[�h����ɏo�������𑗂�i�����̃J�[�h�͏�ɏo�Ă��邪�A�ΐ푊��̉�ʂɂ͂܂��\������Ă��Ȃ��ׁj
						if(cmd.equals("SLAVERECEIVE")){//cmd�̕�����"SLAVERECEIVE"�����������ׂ�D��������true�ƂȂ�
							
							slavePlay = true;
							
							String theSName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							theSNum = Integer.parseInt(theSName);
							
							emperorSelect = Integer.parseInt(inputTokens[2]);//�c�鑤�̃v���C���[���I�񂾃J�[�h�̔z��ԍ�
							
							if(theSNum == 0){//�c�鑤���c����o������
								emperorNum = theSNum;
							}else{
								emperorNum = theSNum + 6;
							}
							emperorPlay = false;
							
							
							receive();
							
							judgeS = 1;
							
						}
						
					}else{
						
						break;
						
					}
				
				}
				
				socket.close();
				
			} catch (IOException e) {
				
				System.err.println("�G���[���������܂���: " + e);
				
			}
		}
	}


	public static void main(String[] args) {
		MyGame net = new MyGame();
		net.setVisible(true);
		new SoundTestWav();
		
	}
  	
	public void mouseClicked(MouseEvent e) {//�{�^�����N���b�N�����Ƃ��̏���
	
		
		
		JButton theButton = (JButton)e.getComponent();//�N���b�N�����I�u�W�F�N�g�𓾂�D�^���Ⴄ�̂ŃL���X�g����
		String theArrayIndex = theButton.getActionCommand();//�{�^���̔z��̔ԍ������o��
		
		//�^�C�g����ʂ��N���b�N�����ۂɔ�\���ɂ���
		if(theArrayIndex.equals("11")){
			isTitle = false;
			
			buttonArray[11].setVisible(false);
		}
		
		Point theMLoc = e.getPoint();
		System.out.println(theMLoc);//theIcon�ɂ́C���݂̃{�^���ɐݒ肳�ꂽ�A�C�R��������
		Point theBtnLocation = theButton.getLocation();
		
		//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
		String msg = "MOVE"+" "+theArrayIndex;
		System.out.println(theArrayIndex);
		//�T�[�o�[�ɏ��𑗂�
		out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
		out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
		
		
		//�I�񂾎�D�̔z��ԍ���
		if(isCheck){
			if(theBNum < 5){
				
				se.play();//�{�^���N���b�N����SE�̍Đ�
				
				if(theArrayIndex.equals("5")){
					
					
					myPlay = true;
					//System.out.println("theENum"+theENum+"theSNum"+theSNum);
					
					play();//�I�������J�[�h�̈ړ�
					
					isCheck = false;
				}
				
			}
		
		}

		repaint();//�I�u�W�F�N�g�̍ĕ`����s��
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
	}
	
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		
		// int theMLocX = e.getX();//�}�E�X��x���W
		// int theMLocY = e.getY();//�}�E�X��y���W
		//System.out.println(theMLocX+","+theMLocY);
	}
	
	
	//�v���C���[�̎�D����ɏo�����߂̊֐��i�^�[�����ƃv���C���[�������o�����ɂ���Ăɂ���ăJ�[�h�̔z�u�����܂�j
	public void play(){
		
		if(myPlay == true){
			int k = 0;
			
			
			int cardX = 870;
			int cardY = 500;
			int cardZ = 250;
			
			if(theBNum != 0 && theBNum < 5){
				if(myStatus == 0){
					switch(emperorTurn){
						
						case 1:
						
							for(k=0;k < 4;k++){
								buttonArray[k].setBounds(k*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[4].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+4+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:

							for(k=0;k < 3;k++){
								buttonArray[k].setLocation(k*200 + 660,760);
							}
							
							buttonArray[3].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+3+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
						
							for(k=0;k < 2;k++){
								buttonArray[k].setLocation(k*200 + 760,760);
							}
							
							buttonArray[2].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+2+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,760);
							
							buttonArray[1].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+1+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
						
					}
					emperorTurn++;
				}
				
				if(myStatus == 1){
					
					switch(slaveTurn){
						
						case 1:
						
							for(k=0;k < 4;k++){
								buttonArray[k].setBounds(k*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[4].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+4+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:

							for(k=0;k < 3;k++){
								buttonArray[k].setLocation(k*200 + 660,760);
							}
							
							buttonArray[3].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+3+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
						
							for(k=0;k < 2;k++){
								buttonArray[k].setLocation(k*200 + 760,760);
							}
							
							buttonArray[2].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+2+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,760);
							
							buttonArray[1].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+1+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
							
					}
					slaveTurn++;
				}
			}
			
			if(theBNum == 0){
				if(myStatus == 0){
					switch(emperorTurn){
						
						case 1:
						
							for(k=1;k < 5;k++){
								buttonArray[k].setBounds((k-1)*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+4+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:
						
							for(k=1;k < 4;k++){
								buttonArray[k].setLocation((k-1)*200 + 660,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+3+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
							System.out.println("�ʂ�����2");
							for(k=1;k < 3;k++){
								buttonArray[k].setLocation((k-1)*200 + 760,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+2+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,cardY);
							
							buttonArray[1].setLocation(cardX,760);
							
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+1+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 5:
							buttonArray[0].setLocation(cardX,cardY);
							if(myStatus == 0){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "SLAVERECEIVE"+" "+0+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
					}
					emperorTurn++;
				}
				
				if(myStatus == 1){
					
					switch(slaveTurn){
						
						case 1:
						
							for(k=1;k < 5;k++){
								buttonArray[k].setBounds((k-1)*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+4+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:

							for(k=1;k < 4;k++){
								buttonArray[k].setLocation((k-1)*200 + 660,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+3+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}
							
							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
						
							for(k=1;k < 3;k++){
								buttonArray[k].setLocation((k-1)*200 + 760,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+2+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,cardY);
							
							buttonArray[1].setLocation(cardX,760);
							
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+1+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
							
						case 5:
							buttonArray[0].setLocation(cardX,cardY);
							System.out.println("�ʂ�܂�����2");
							if(myStatus == 1){
								
								//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
								String msg = "EMPERORRECEIVE"+" "+0+" "+theBNum;
								//�T�[�o�[�ɏ��𑗂�
								out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
								out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
								
							}

							theBNum = 12;
							myPlay = false;
							break;
					}
					slaveTurn++;
				}
			}
		}
		
		repaint();//�I�u�W�F�N�g�̍ĕ`����s��

	}


	//����̎�D�������ŏ�ɏo�����߂̊֐��i�^�[�����Ƒ��肪�����o�������ɂ���Ĕz�u�����܂�j
	public void receive(){
		int k = 0;
		
		int cardX = 870;
		int cardY = 500;
		int cardZ = 265;
		
		if(emperorPlay){
			
			if(myStatus == 0){

				if(slaveTurn==1){
					for(k=6;k < 10;k++){
						buttonArray[k].setLocation((k%6)*200 + 560,10);
						System.out.println(buttonArray[k].getLocation());
					}

					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn==2){
					for(k=6;k < 9;k++){
						buttonArray[k].setLocation((k%6)*200 + 660,10);
					}
					
					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn == 3){
					for(k=6;k < 8;k++){
						buttonArray[k].setLocation((k%6)*200 + 760,10);
					}
					
					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn == 4){
					buttonArray[6].setLocation(cardX,10);
					
					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn == 5){
					buttonArray[6].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				
				slaveTurn++;
			}

		}
		
		if(slavePlay){
			
			if(myStatus == 1){
				
					if(emperorTurn==1){
						for(k=6;k < 10;k++){
							buttonArray[k].setLocation((k%6)*200 + 560,10);
							System.out.println(buttonArray[k].getLocation());
						}

						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn==2){
						for(k=6;k < 9;k++){
							buttonArray[k].setLocation((k%6)*200 + 660,10);
						}
						
						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn == 3){
						for(k=6;k < 8;k++){
							buttonArray[k].setLocation((k%6)*200 + 760,10);
						}
						
						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn == 4){
						buttonArray[6].setLocation(cardX,10);
						
						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn == 5){
						buttonArray[6].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					
					emperorTurn++;
					
				}

			}
		repaint();
	}
	
	
	//��������
	public void judge(int emperorSelect, int slaveSelect){
		
		//�ǂ�����s�����o�����ꍇ
		if(emperorSelect != 0 && slaveSelect != 0){
			
			if(myStatus == 0){
				
				buttonArray[slaveNum].setIcon(citizenIcon);
				
				try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
					Thread.sleep(2000); //2000�~���bSleep����
				}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
				
				//Play�����J�[�h������
				buttonArray[slaveNum].setVisible(false);
				buttonArray[theSNum].setVisible(false);
				
			}
			if(myStatus == 1){
				
				buttonArray[emperorNum].setIcon(citizenIcon);
				
				try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
					Thread.sleep(2000); //2000�~���bSleep����
				}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
				
				//Play�����J�[�h������
				buttonArray[theENum].setVisible(false);
				buttonArray[emperorNum].setVisible(false);
				
			}
		}
		
		//�c�鑤���c����o������
		if(emperorSelect == 0){
			
			//�z�ꑤ���z����o������
			if(slaveSelect == 0){
				
				if(myStatus == 0){
					
					buttonArray[slaveNum].setIcon(slaveIcon);
					
					try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
						Thread.sleep(1000); //1000�~���bSleep����
					}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
					
					buttonArray[slaveNum].setEnabled(false);
					buttonArray[emperorSelect].setEnabled(false);

					loseLabel.setVisible(true);
					
					System.out.println("���Ȃ��̕���");
					
				}
				if(myStatus == 1){
					
					buttonArray[emperorNum].setIcon(emperorIcon);
					
					try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
						Thread.sleep(1000); //1000�~���bSleep����
					}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
					
					buttonArray[emperorNum].setEnabled(false);
					buttonArray[slaveSelect].setEnabled(false);
					
					winLabel.setVisible(true);
					
					System.out.println("���Ȃ��̏���");
					
				}
				
			}else{//�z�ꑤ���s�����o�����Ƃ�
				
				if(myStatus == 0){
					
					buttonArray[slaveNum].setIcon(citizenIcon);
					
					try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
						Thread.sleep(1000); //1000�~���bSleep����
					}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
					
					buttonArray[slaveNum].setEnabled(false);
					buttonArray[emperorSelect].setEnabled(false);

					winLabel.setVisible(true);
					
					System.out.println("���Ȃ��̏���");//�������̃|�b�v�A�b�v�̉摜
					
				}
				if(myStatus == 1){
					
					buttonArray[emperorNum].setIcon(emperorIcon);
					
					try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
						Thread.sleep(1000); //1000�~���bSleep����
					}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
					
					buttonArray[emperorNum].setEnabled(false);
					buttonArray[slaveSelect].setEnabled(false);

					loseLabel.setVisible(true);//�s�k���̃|�b�v�A�b�v�̉摜
					
					System.out.println("���Ȃ��̕���");
					
				}
				
				
			}
		}

		//�z�ꑤ���z����o������
		if(slaveSelect == 0){
			
			//�c�鑤���c����o������
			if(emperorSelect == 0){
				
			}else{//�c�鑤���s�����o�����Ƃ�
			
				if(myStatus == 0){
					
					buttonArray[slaveNum].setIcon(slaveIcon);
					
					try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
						Thread.sleep(1000); //1000�~���bSleep����
					}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
					
					System.out.println("���Ȃ��̏���");
					
					buttonArray[slaveNum].setEnabled(false);
					buttonArray[emperorSelect].setEnabled(false);
					
					
					winLabel.setVisible(true);//�������̃|�b�v�A�b�v�̉摜
					
				}else{
					
					buttonArray[emperorNum].setIcon(citizenIcon);
					
					try{//Thread.sleep��Try-catch�łȂ��Ƃ����Ȃ�����
						Thread.sleep(1000); //1000�~���bSleep����
					}catch(InterruptedException ee){}//�G���[���łĂ��Ȃɂ����Ȃ�
					
					buttonArray[emperorNum].setEnabled(false);
					buttonArray[slaveSelect].setEnabled(false);
					
					System.out.println("���Ȃ��̕���");
					
					loseLabel.setVisible(true);//�s�k���̃|�b�v�A�b�v�̉摜
					
				}

			}
		}
		
		return;
	}
	
}