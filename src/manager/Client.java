package manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dto.InventoryDTO;
import dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import swing.LogInFrame;
import swing.MainFrame;

@Getter
@Setter
public class Client {

	private LogInFrame logInFrame;
	private MainFrame mainFrame;

	// 클라이언트 소켓
	private Socket socket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;

	// 프로토콜 변수
	private String order;

	// 유저DTO
	private UserDTO userDTO = new UserDTO();

	// 유저가 가지고 있는 보관함(카드)들
	private InventoryDTO inventoryDTO = new InventoryDTO();
	private List<InventoryDTO> invenCards = new ArrayList<>();

	private boolean isLogin;

	public Client() {
		logInFrame = new LogInFrame(this);
	}

	// 로그인 버튼 클릭, 소켓 생성, reaThread 생성
	public void clickLoginBtn(String name, String password) {
		try {
			socket = new Socket("localhost", 5000);
			socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketWriter = new PrintWriter(socket.getOutputStream(), true);
			System.out.println("로그인 시도");
			readThread();
			sendOrder("login#" + name + "#" + password);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(isLogin);
			if (isLogin) {
				logInFrame.setVisible(false);
				mainFrame = new MainFrame(this);
				System.out.println("로그인 완료");
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "서버를 확인할 수 없습니다");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버를 확인할 수 없습니다");
		}
	}

	// 클라이언트 -> 서버 메세지 전송하기
	public void sendOrder(String order) {
		socketWriter.println(order);
	}

	// 클라이언트 측 readThread
	private void readThread() {
		new Thread(() -> {
			try {
				order = socketReader.readLine();
				checkProtocol(order);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("서버가 종료되었습니다");
			}
		}).start();
	}

	// 프로토콜 체크
	private void checkProtocol(String order) {
		if (order.startsWith("login")) {
			String[] login = order.split("#");
			userDTO.setName(login[1]);
			userDTO.setNickname(login[2]);
			userDTO.setPoint(Integer.parseInt(login[3]));
			System.out.println(userDTO + " 도착");
			isLogin = true;
		} else if (order.startsWith("wrong")) {
			System.out.println("로그인 실패");
		}
	}

	public static void main(String[] args) {
		new Client();
	}

}
