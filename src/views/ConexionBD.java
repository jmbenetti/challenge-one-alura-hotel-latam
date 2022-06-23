//Imports para clases que leen de BD

// import java.sql.ResultSet;

// Ejemplo para leer de la base de datos

//ConexionBD miConexion = new ConexionBD();
//		ResultSet prueba = miConexion.consultar("show databases;");
//		try {
//			while (prueba.next()) {
//					System.out.println(prueba.getString(1));
//			}
//		}
//		catch(Exception e)
//		{
//			System.out.println(e);
//		}

package views;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConexionBD {
	Connection conexion;

	public ConexionBD() {
		// Constructor para conectar con la base de datos

		try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
			this.conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/alurahotel", "root", "1234");
//			Statement stmt = conexion.createStatement();
//			ResultSet rs = stmt.executeQuery("show databases;");
//			System.out.println("Connected");
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	public boolean actualizar(String szConsulta) {
		try {
			Statement miStatement = this.conexion.createStatement();
			miStatement.executeUpdate(szConsulta);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public ResultSet consultar(String szConsulta) {
		ResultSet miResultSet = null;
		try {
			Statement miStatement = this.conexion.createStatement();
			miResultSet = miStatement.executeQuery(szConsulta);
		} catch (Exception e) {
			System.out.println(e);
		}
		return miResultSet;
	}
}
