package swing;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import manager.Client;

@Getter
@Setter
public class LogInFrame extends JFrame {

	private Client mContext;

	private JPanel backgroundPanel1;
	private JPanel backgroundPanel2;

	private JPanel mainPanel;

	private JPanel AuctionPanel;
	private JPanel LogInPanel;
	private JPanel PricePanel;
	private JPanel FinishPanel;
	private JPanel MyPagePanel;
	private JPanel JoinAuctionPanel;
	private UserDTO user;

	private JLabel id;
	private JLabel password;
	private JLabel backgroundLabel;
	private Icon backgroundIcon;
	private JTextField idField;
	private JTextField passwordField;

	private JButton logInButton;
	private JButton signUpButton;

	private JTabbedPane tabPane;

//	private JLabel title;
	private MainFrame mainFrame;

	public LogInFrame(Client mContext) {
		this.mContext = mContext;
		initData();
		setInitLayout();
		initListener();
	}

	private void initData() {

		backgroundPanel1 = new JPanel();
		backgroundPanel2 = new JPanel();

		mainPanel = new JPanel();
		add(backgroundPanel1);
		add(backgroundPanel2);

//		title = new JLabel("카드 경매 사이트에 오신 것을 환영합니다.");
//		title.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 20));
	}

	private void setInitLayout() {
		setTitle("[로그인하기]");
		setSize(1920, 1000);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		backgroundPanel1.setSize(getWidth(), getHeight());
		backgroundPanel1.setLayout(null);
		backgroundPanel1.setBackground(Color.WHITE);

		backgroundPanel2.setSize(getWidth(), getHeight());
		backgroundPanel2.setLayout(null);
		backgroundPanel2.setBackground(Color.BLUE.darker());
		setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("image/파비콘2.png"));
		Icon backgroundIcon = new ImageIcon("image/logInBack.gif");
		Icon logInIcon = new ImageIcon("image/logInButton.png");
		Icon makeAccountIcon = new ImageIcon("image/makeAccount.png");
		idField = new JTextField(10);
		passwordField = new JTextField(10);
		logInButton = new JButton();
		signUpButton = new JButton();
		JLabel logIn = new JLabel(logInIcon);
		JLabel signUp = new JLabel(makeAccountIcon);
		signUpButton = new JButton();
		// logInButton.setForeground(null);
		// signUpButton.setForeground(null);

		backgroundLabel = new JLabel(backgroundIcon);
		backgroundLabel.setSize(1920, 1000);
		backgroundLabel.setLocation(0, 0);
		backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
		backgroundPanel1.add(backgroundLabel);

		logIn.setBounds(710, 850, 233, 95);
		signUp.setBounds(965, 852, 233, 95);
		idField.setBounds(835, 635, 260, 47);
		passwordField.setBounds(835, 760, 260, 47);
		idField.setBorder(null);
		passwordField.setBorder(null);
//		title.setBounds(850, 480, 300, 60);
		mainPanel = new JPanel();

		backgroundLabel.add(idField);
		backgroundLabel.add(passwordField);
		backgroundLabel.add(logIn);
		backgroundLabel.add(signUp);

		logInButton = new JButton();
		logInButton.setBounds(710, 850, 233, 95);
		logInButton.setBackground(null);
		logInButton.setBorderPainted(false);
		logInButton.setContentAreaFilled(false);
		backgroundLabel.add(logInButton);

		signUpButton = new JButton();
		signUpButton.setBounds(965, 852, 233, 95);
		signUpButton.setBackground(null);
		signUpButton.setBorderPainted(false);
		signUpButton.setContentAreaFilled(false);
		backgroundLabel.add(signUpButton);
//		passwordField.addKeyListener(new KeyListener() {
//
//			@Override
//			public void keyTyped(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					String id = idField.getText();
//					String password = passwordField.getText();
//					mContext.sendOrder("login#" + id + "#" + password);
//					UserDAO dao = new UserDAO();
//					try {
//						new MainFrame(dao.infoUser(id), mContext);
//					} catch (SQLException e1) {
//						e1.printStackTrace();
//					}
//					dispose();
//				}
//
//			}
//
////			@Override
////			public void keyPressed(KeyEvent e) {
////				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
////					String id = idField.getText();
////					String password = passwordField.getText();
////					mContext.sendOrder("login#" + id + "#" + password);
////					UserDAO dao = new UserDAO();
////					try {
////						new MainFrame(dao.infoUser(id), mContext);
////					} catch (SQLException e1) {
////						e1.printStackTrace();
////					}
////					dispose();
////				}
////			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//			}
//
//		});

		setVisible(true);

	}

	private void initListener() {
		signUpButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new MakeNewAccountFrame();
			}
		});
		logInButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				mContext.clickLoginBtn(idField.getText(), passwordField.getText());
			}
		});
	}
}