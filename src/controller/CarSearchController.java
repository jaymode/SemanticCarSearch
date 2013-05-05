package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import query.QueryBean;
import query.QueryBuilder;

/**
 * Servlet implementation class CarSearchController
 */
public class CarSearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private QueryBean query;

	@Override
	public void init() {
		//Create the QueryBean class to handle the searching
		query = new QueryBean(getServletContext().getRealPath("/WEB-INF/car_ads.rdf"));
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarSearchController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Grab all the parameters
		QueryBuilder qBuilder = new QueryBuilder(request.getParameter("carMake"),
				request.getParameter("carModel"), request.getParameter("carYear"),
				request.getParameter("carColor"), request.getParameter("carLocation"));
		
		//Execute the query
		String result = query.executeQuery(qBuilder);

		//Output response
		PrintWriter writer = response.getWriter();
		writer.write(result);
	}

}
