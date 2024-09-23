package com;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

	@Override
	protected void service( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		//read data from form
		int id = Integer.parseInt( req.getParameter( "t1" ) );
		String name = req.getParameter( "t2" );
		int sal = Integer.parseInt( req.getParameter( "t3" ) );

		try {
			Class.forName( "com.mysql.jdbc.Driver" );

			Connection con = DriverManager
					.getConnection( "jdbc:mysql://localhost:3306/db_employee", "root", "Vrushabhu@2019" );

			PreparedStatement ps = con.prepareStatement( "insert into employee values(?,?,?)" );
			ps.setInt( 1, id );
			ps.setString( 2, name );
			ps.setInt( 3, sal );
			ps.executeUpdate();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		res.setContentType( "text/html" );
		PrintWriter pw = res.getWriter();
		pw.write( "<h3 style=color:'green'>Registration successfull</h3>" );
		pw.close();
	}
}
