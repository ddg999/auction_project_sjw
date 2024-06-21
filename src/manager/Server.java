package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;

import dao.AuctionDAO;
import dao.CardDAO;
import dao.InventoryDAO;
import dao.UserDAO;
import dto.UserDTO;

public class Server {
	// 서버 소켓
	private ServerSocket serverSocket;
	private Socket socket;

	// 프로토콜 변수
	private String protocol;
	private String data;
	private String message;

	// DAO
	private UserDAO userDAO = new UserDAO();
	private CardDAO cardDAO = new CardDAO();
	private AuctionDAO auctionDAO = new AuctionDAO();
	private InventoryDAO inventoryDAO = new InventoryDAO();

	// 접속된 클라이언트(유저) 벡터
	private Vector<ConnectedUser> connectedUsers = new Vector<>();

	private String order;

	// 서버 생성자, 서버프레임 실행
	public Server() {
		startServer();
	}

	// 서버 시작하기 (서버 소켓 생성)
	public void startServer() {
		try {
			serverSocket = new ServerSocket(5000);
			connectClient();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미 존재하는 서버입니다");
		}
	}

	// 소켓 accept 대기하는 스레드, accept 되면 연결된 유저 객체 생성
	private void connectClient() {
		new Thread(() -> {
			while (true) {
				try {
					socket = serverSocket.accept();
					ConnectedUser user = new ConnectedUser(socket);
					user.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 서버 -> 접속된 유저 전체에게 오더
	private void broadCast(String order) {
		for (ConnectedUser connectedUser : connectedUsers) {
			connectedUser.writer(order);
		}
	}

	// 접속된 유저를 관리하기 위한 클래스
	private class ConnectedUser extends Thread {
		private UserDTO userDTO;
		private Socket socket;

		// 서버 측 입출력 변수
		private PrintWriter socketWriter;
		private BufferedReader socketReader;

		// 접속된 유저 생성자
		public ConnectedUser(Socket socket) {
			this.socket = socket;
			connectIO();
		}

		// 서버 측 입출력 스트림 생성
		private void connectIO() {
			try {
				socketWriter = new PrintWriter(socket.getOutputStream(), true);
				socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println("서버에 클라이언트 연결됨");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 서버 -> 클라이언트 오더
		private void writer(String order) {
			socketWriter.println(order);
		}

		// 프로토콜 체크
		public void checkProtocol(String order) {
			System.out.println("서버 프로토콜 와일문 작동");
//				if (message.startsWith("chat")) {
//					broadCast(message);
//				} else if (message.startsWith("bid")) {
//					String[] bid = message.split("#");
//					int id = Integer.valueOf(bid[1]);
//					int bidmoney = Integer.valueOf(bid[2]);
//					if (productId.get(id) < bidmoney) {
//						productId.set(id, bidmoney);
//					}
//				} else if (message.startsWith("sell")) {
//					String[] sell = message.split("#");
//					productName.add(sell[1]);
//				} else if (message.startsWith("sendDB")) {
//					try {
//						String[] DB = message.split("#");
//						user.setNickname(DB[1]);
//						user.setName(DB[2]);
//						user.setPassword(DB[3]);
//						user.setPoint(500);
//						// 회원가입시 카드 5개 랜덤으로 증정
//						if (dao.addUser(user)) {
//							for (int i = 0; i < 5; i++) {
//								int cardId = random.nextInt(4) + 1;
//								invenDTO.setName(user.getName());
//								invenDTO.setCardId(cardId);
//								inven.invenAdd(invenDTO);
//							}
//							System.out.println("DB보냄");
//						} else {
//							System.out.println("회원 가입 실패 !! 중복아이디");
//						}
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				} else 
			if (order.startsWith("login")) {
				String[] login = order.split("#");
				System.out.println(login[1] + ", " + login[2]);
				if (userDAO.loginUser(login[1], login[2])) {
					try {
						userDTO = userDAO.infoUser(login[1]);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					socketWriter.println(
							"login#" + userDTO.getName() + "#" + userDTO.getNickname() + "#" + userDTO.getPoint());
					System.out.println(userDTO + " 보냅니다");
				} else {
					socketWriter.println("wrong");
					System.out.println("아이디틀림");
				}
			}
//				 else if (message.startsWith("cash")) {
//					String[] cash = message.split("#");
//
//				} else if (message.startsWith("addCard")) {
//					String[] card = message.split("#");
//
//				} else if (message.startsWith("auction")) {
//					String[] cardId = message.split("#");
//					int id = Integer.valueOf(cardId[1]);
//					System.out.println("카드 id 받아옴 : " + id);
//					auctionList.add(id);
//					printWriter.println("list#" + id);
//				}
		}

		@Override
		public void run() {
			try {
				order = socketReader.readLine();
				checkProtocol(order);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Server();
	}
}
