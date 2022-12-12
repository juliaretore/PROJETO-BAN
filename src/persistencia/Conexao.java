package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	private final static String HOST = "localhost";
	private final static String PORTA = "5432";
	private final static String USUARIO = "postgres";
	private final static String SENHA = "postgres";
	private final static String NOME_DB = "BAN";

	private static Connection conexao;

	private Conexao() {
	}

	public static Connection getConexao() throws ClassNotFoundException, SQLException {
		if (conexao == null) {
			Class.forName("org.postgresql.Driver");
			conexao = DriverManager.getConnection("jdbc:postgresql://" + HOST + ":" + PORTA + "/" + NOME_DB, USUARIO,
					SENHA);
		}
		return conexao;
	}
}