package apresentacao;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exceptions.SelectException;
import negocio.Sistema;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.border.MatteBorder;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static JButton btnMusicas = new JButton("Músicas");
	static JButton btnPlaylists= new JButton("Playlists");
	private static List<Object> musicas = new ArrayList<Object>();
	static JTable table, table_1;
	private static Sistema sistema;
	
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JLabel lblTopMsicas = new JLabel("TOP 5 MÚSICAS MAIS ESCUTADAS");

	public static void main(String[] args) { 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
	}
			
		


	public TelaPrincipal() {
		
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
				atualizarTabela2();
			}
		});

	
		JLabel lblNewLabel = new JLabel("New label");
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu.jpg")));
		
		
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  692, 700);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnMusicas.setBackground(Color.WHITE);
		btnMusicas.setForeground(Color.BLACK);
		btnMusicas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnMusicas.addActionListener(new ActionListener() {
			
		public void actionPerformed(ActionEvent e) {
				MusicaView objeto = MusicaView.getInstance();
				objeto.setVisible(true);
				objeto.setLocationRelativeTo(null);
			}
		});
		
		JLabel lblTopArtistas = new JLabel("TOP 3 ARTISTAS MAIS ESCUTADOS");
		lblTopArtistas.setBackground(Color.BLACK);
		lblTopArtistas.setBounds(203, 322, 426, 60);
		contentPane.add(lblTopArtistas);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(56, 383, 573, 74);
		contentPane.add(scrollPane_1);
		
		
		
		lblTopMsicas.setBackground(new Color(0, 0, 0));
		lblTopMsicas.setBounds(203, 151, 426, 60);
		
		contentPane.add(lblTopMsicas);
		btnMusicas.setBounds(56, 56, 256, 67);
		contentPane.add(btnMusicas);
		
		btnPlaylists.setBackground(Color.WHITE);
		btnPlaylists.setForeground(Color.BLACK);
		btnPlaylists.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPlaylists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			PlaylistView objeto = PlaylistView.getInstance();
			objeto.setVisible(true);
			objeto.setLocationRelativeTo(null);

			}
			
		});
		btnPlaylists.setBounds(361, 56, 256, 67);
		contentPane.add(btnPlaylists);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(56, 208, 573, 102);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setSelectionBackground(SystemColor.activeCaption);
		table.setBackground(SystemColor.window);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
//				setCamposFromTabela();
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				 "Música", "Nome Artista", "Visualizacoes"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		
		
		scrollPane_1.setViewportView(table_1);

		table_1 = new JTable();
		table_1.setSelectionBackground(SystemColor.activeCaption);
		table_1.setBackground(SystemColor.window);
		table_1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
//				setCamposFromTabela();
			}
		});
		scrollPane_1.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				 "Artista", "Visualizacoes"
			}
		));
		table_1.getColumnModel().getColumn(0).setPreferredWidth(15);
		
		
		
		lblNewLabel.setBounds(0, -58, 698, 774);
		contentPane.add(lblNewLabel);
		

	}
	
	
	public static void atualizarTabela() {
		try {
			musicas = sistema.listarTop5();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++){
				model.addRow((Object[]) musicas.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void atualizarTabela2() {
		try {
			musicas = sistema.listarArtistaMaisOuvido();
			DefaultTableModel model = (DefaultTableModel) table_1.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++){
				model.addRow((Object[]) musicas.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
