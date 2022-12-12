package apresentacao;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import persistencia.*;
import dados.Musica;
import exceptions.DeleteException;
import exceptions.InsertException;
import exceptions.SelectException;
import exceptions.UpdateException;
import negocio.Sistema;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;

public class MusicaView extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfNome;
	private JTextField textPNome;
	private JTextField textPCodigo;
	static JTable table;

	private static List<Object> artistas = new ArrayList<Object>();
	private static List<Object> musicas = new ArrayList<Object>();
	Musica musica = new Musica();		
	private static Sistema sistema;
	static JTextField tfCodigo;
	private JTextField tfduracao;
	private JTextField tfletra;
	static JButton alterar = new JButton("Alterar");
	static JButton excluir = new JButton("Excluir");
	static JButton cadastrar = new JButton("Cadastrar");
	static JButton limpar = new JButton("Limpar");
	static JPanel panel = new JPanel();
	private static MusicaView musicaView;
	private JTextField tfartista;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicaView frame = new MusicaView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}
	
	


	 public static MusicaView getInstance() {
         if(musicaView==null) musicaView=new MusicaView();
         atualizarTabela();
//         ((DefaultTableModel) table_1.getModel()).setNumRows(0);
         return musicaView;
     } 
	public MusicaView() {
		try {
			sistema = new Sistema();
		} catch (ClassNotFoundException | SQLException | SelectException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
		
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
				limpar();
			}
		});
		JLabel lblNewLabel = new JLabel("New label");	
		lblNewLabel.setBounds(10, -27, 684, 692);
		ImageIcon imagemTituloJanela = new javax.swing.ImageIcon(getClass().getResource("/img/logo.jpg"));
		setIconImage(imagemTituloJanela.getImage());
		lblNewLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/janelas.jpg")));

		setTitle("Gerenciar M�sicas");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,  700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBusca = new JLabel("BUSCA");
		lblBusca.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblBusca.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBusca.setBounds(300, 11, 70, 20);
		contentPane.add(lblBusca);
		
		JLabel lblArtistasCadastrados = new JLabel("MUSICAS CADASTRADAS");
		lblArtistasCadastrados.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblArtistasCadastrados.setBounds(227, 94, 232, 20);
		contentPane.add(lblArtistasCadastrados);
		
		JButton sair = new JButton("Sair");
		sair.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaPrincipal.atualizarTabela();
				TelaPrincipal.atualizarTabela2();
				sair();
			}
		});
		sair.setBackground(SystemColor.window);
		sair.setBounds(579, 631, 93, 20);
		contentPane.add(sair);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 126, 632, 261);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setSelectionBackground(SystemColor.activeCaption);
		table.setBackground(SystemColor.window);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome", "Duracao", "Visualizacoes", "Letra", "Nome Artista"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		
		JLabel lblCdigo = new JLabel("C\u00F3digo");
		lblCdigo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblCdigo.setBounds(127, 21, 70, 20);
		contentPane.add(lblCdigo);
		lblCdigo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textPCodigo = new JTextField();
		textPCodigo.setBounds(76, 42, 198, 20);
		contentPane.add(textPCodigo);
		textPCodigo.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table.setRowSorter(filtro);
				
				if (textPCodigo.getText().length()==0) filtro.setRowFilter(null);
				else filtro.setRowFilter(RowFilter.regexFilter(textPCodigo.getText(), 0));  
			}
		});
		textPCodigo.setColumns(10);
		
		textPNome = new JTextField();
		textPNome.setBounds(407, 42, 198, 20);
		contentPane.add(textPNome);
		textPNome.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {				
				TableRowSorter<TableModel> filtro = null;  
				DefaultTableModel model = (DefaultTableModel) table.getModel();  
				filtro = new TableRowSorter<TableModel>(model);  
				table.setRowSorter(filtro); 
				
				if (textPNome.getText().length()==0) filtro.setRowFilter(null);
				else filtro.setRowFilter(RowFilter.regexFilter("(?i)" + textPNome.getText(), 1));  
				
			}
		});
		textPNome.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblNome.setBounds(465, 21, 70, 20);
		contentPane.add(lblNome);
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(Color.LIGHT_GRAY);
		separator_2_1_1.setBounds(0, 87, 659, 2);
		contentPane.add(separator_2_1_1);
		
		JButton btnNewButton = new JButton("Tocar Música");
		btnNewButton.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnNewButton.setBackground(SystemColor.window);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//aumentar em 1 as visualizacoes da musica
				if (table.getSelectedRow()!=-1){
					try {
						sistema.visualizacoes(Integer.parseInt(tfCodigo.getText()));
						atualizarTabela();
					} catch (NumberFormatException | UpdateException | SelectException
							| SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				}
				
			}
		});
		btnNewButton.setBounds(273, 399, 118, 21);
		contentPane.add(btnNewButton);
		

		panel.setBounds(148, 427, 425, 200);
		contentPane.add(panel);
		panel.setLayout(null);
		
		tfNome = new JTextField();
		tfNome.setBounds(67, 12, 346, 19);
		panel.add(tfNome);
		tfNome.setColumns(10);
		
		JLabel label_1 = new JLabel("Nome:");
		label_1.setBounds(1, 8, 56, 21);
		panel.add(label_1);
		label_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tfCodigo = new JTextField();
		tfCodigo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		tfCodigo.setBounds(77, 39, 63, 19);
		panel.add(tfCodigo);
		tfCodigo.setEditable(false);
		tfCodigo.setColumns(10);
		
				
				JLabel lblCodigo = new JLabel("Codigo:");
				lblCodigo.setBounds(-16, 38, 86, 20);
				panel.add(lblCodigo);
				lblCodigo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
				lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
				
				tfduracao = new JTextField();
				tfduracao.setBounds(221, 37, 72, 19);
				panel.add(tfduracao);
				tfduracao.setColumns(10);
				
				tfletra = new JTextField();
				tfletra.setBounds(76, 98, 339, 20);
				panel.add(tfletra);
				tfletra.setColumns(10);
				
				JLabel lblDurao = new JLabel("Duração:");
				lblDurao.setBounds(120, 37, 97, 20);
				panel.add(lblDurao);
				lblDurao.setHorizontalAlignment(SwingConstants.RIGHT);
				lblDurao.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
				
				JLabel lblLetra = new JLabel("Letra:");
				lblLetra.setBounds(-18, 99, 83, 20);
				panel.add(lblLetra);
				lblLetra.setHorizontalAlignment(SwingConstants.RIGHT);
				lblLetra.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
				
				JButton btnNewButton_1 = new JButton("Inserir Arquivo");
				btnNewButton_1.setBackground(SystemColor.window);
				btnNewButton_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				btnNewButton_1.setBounds(306, 39, 106, 21);
				panel.add(btnNewButton_1);
				limpar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				limpar.setBounds(319, 167, 83, 21);
				panel.add(limpar);
				
				
				limpar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						limpar();
					}
				});
				limpar.setBackground(SystemColor.window);
				cadastrar.setBorder(new LineBorder(new Color(0, 0, 0)));
				cadastrar.setBounds(202, 167, 104, 21);
				panel.add(cadastrar);
				

				cadastrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							musica.setNome(tfNome.getText());
							musica.setDuracao(Integer.parseInt(tfduracao.getText()));
							musica.setVisualizacoes(0);
							musica.setLetra(tfletra.getText());
							musica.setNomeArtista(tfartista.getText());
							sistema.uploadMusicas(musica);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						atualizarTabela();
						limpar();
					}
				});
				cadastrar.setBackground(SystemColor.window);
				excluir.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				excluir.setBounds(102, 167, 86, 21);
				panel.add(excluir);
				

				excluir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow()!=-1){
							Object[] options3 = {"Excluir", "Cancelar"};
							if(JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir o registro:\n>   " 
									+ table.getValueAt(table.getSelectedRow(), 0) + "   -   "
									+ table.getValueAt(table.getSelectedRow(), 1), null,
									JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[0]) == 0){
								try {
									sistema.excluirMusica(Integer.parseInt(tfCodigo.getText()));											
									atualizarTabela();
									limpar();
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}
							}
						}else{
							JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
						}
					}
				});
				excluir.setBackground(SystemColor.window);
				alterar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				alterar.setBounds(18, 167, 72, 21);
				panel.add(alterar);
				

				alterar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow() != -1){
							try {
								musica.setNome(tfNome.getText());
								musica.setCodMusica(Integer.parseInt(tfCodigo.getText()));									
								musica.setDuracao(Integer.parseInt(tfduracao.getText()));
								musica.setLetra(tfletra.getText());
								musica.setNomeArtista(tfartista.getText());
								sistema.alterarMusica(musica);		

							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							atualizarTabela();
						}else JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
						
					}
				});
				alterar.setBackground(SystemColor.window);
				
				JLabel lblArtista = new JLabel("Artista:");
				lblArtista.setHorizontalAlignment(SwingConstants.RIGHT);
				lblArtista.setFont(new Font("Dialog", Font.BOLD, 15));
				lblArtista.setBounds(-16, 66, 86, 20);
				panel.add(lblArtista);
				
				tfartista = new JTextField();
				tfartista.setColumns(10);
				tfartista.setBounds(75, 70, 338, 19);
				panel.add(tfartista);
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(tfNome.getText().equals("") || tfduracao.getText().equals("") || tfletra.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Necess�rio preencher todos os campos antes de inserir o arquivo.");
						}else {
							JFileChooser chooser = new JFileChooser();
						    FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files", "mp3", "wav");
						    chooser.setFileFilter(filter);
						    int returnVal = chooser.showOpenDialog(chooser);
						    if (returnVal == JFileChooser.APPROVE_OPTION) {
						        File file = chooser.getSelectedFile();
						        File arquivos = new File(tfNome.getText()+"-"+tfduracao.getText()+"-"+tfletra.getText()+".mp3");
						        File dir = new File("arq");
						        boolean ok = chooser.getSelectedFile().renameTo(new File(dir, arquivos.getName()));
						        if(!ok)JOptionPane.showMessageDialog(null, "Nao foi possivel mover o arquivo");
						    }						
						}
					}
				});
				
				lblNewLabel.setBounds(0, -29, 684, 690);
				contentPane.add(lblNewLabel);

	}

	public void sair() {
		dispose();
	}

	public void setCamposFromTabela() {
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		tfduracao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		tfletra.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)));
		tfartista.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 5)));
	}

	public void limpar() {
		tfNome.setText("");
		tfCodigo.setText("");
		tfduracao.setText("");
		tfletra.setText("");
		tfartista.setText("");
	}

	public static void atualizarTabela() {
		try {
			musicas = sistema.listarMusicas();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
		for (int i=0;i!=musicas.size();i++){
				model.addRow((Object[]) musicas.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
}