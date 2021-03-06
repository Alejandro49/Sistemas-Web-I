package principal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ServletLogin
 */
@WebServlet(name="ServletLogin", urlPatterns={"/html/login"})
public class ServletLogin extends HttpServlet {
	
	UsuarioDao usuarioDao = new UsuarioDao();
	Usuario user;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		 user = new Usuario(username, password);
		
		if (usuarioDao.validarUsuario(user)) {
			
			HttpSession sesion = request.getSession();
			sesion.setAttribute("userName", username);
			sesion.setAttribute("rol", usuarioDao.obtenerRol(user));
			
			Cookie loginCookie = new Cookie("userName",user.getUserName());
			loginCookie.setMaxAge(30*60);
			response.addCookie(loginCookie);
			
			if ((int)sesion.getAttribute("rol") == 1) {
				response.sendRedirect("dashboard_usuario.html");
			}
			if ((int)sesion.getAttribute("rol") == 2) {
				response.sendRedirect("dashboard_premium.html");
			} else {
				
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				
				try {
					out.println("<html><head><title>Error en la gestion de la sesion </title></head><body>");
					out.println("<h1>" + "Se ha producido un error en la gestion de la sesi�n "   + "</h1>");
					out.println(" <input type=\"button\" onclick=\"location.href='login.html'\"class=\"btn btn-outline-secondary\" value=\"Volver\">");
					out.println("</body></html>");
					} finally{out.close();}
				
			}
			
		}
		else {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			try {
				out.println("<html><head><title>Login incorrecto </title></head><body>");
				out.println("<h1>" + "Usuario o contrase�a incorrectos "   + "</h1>");
				out.println(" <input type=\"button\" onclick=\"location.href='login.html'\"class=\"btn btn-outline-secondary\" value=\"Volver\">");
				out.println("</body></html>");
				} finally{out.close();}
		}
		
	}
	

}
