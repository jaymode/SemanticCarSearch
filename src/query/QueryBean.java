package query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;

/**
 * The QueryBean class is responsible for querying the RDF data based on the
 * provided query parameters
 * 
 * @author Jay
 * 
 */
public class QueryBean {
	private String owlFile = null;

	/**
	 * Constructs the QueryBean object.
	 * 
	 * @param path
	 *            Path is the path to the RDF file to read. Must not be null or
	 *            empty.
	 */
	public QueryBean(String path) {
		assert path != null;
		assert !path.isEmpty();
		owlFile = path;
	}

	public String executeQuery(QueryBuilder builder) {
		String result = null;
		InputStream in = null;
		QueryExecution qe = null;
		try {
			/*
			 * TODO Have the triples stored in a relational database and use
			 * Jena to query a model constructed from that store. Additionally
			 * the Model is not thread safe but the Jena APIs provide a locking
			 * mechanism so we can avoid the expensive operation of creating the
			 * model.
			 * 
			 * At the very least since the file is not changing, it does not
			 * need to be read in continuously
			 */
			in = new FileInputStream(new File(owlFile));

			Model model = ModelFactory.createMemModelMaker().createFreshModel();
			model.read(in, "http://jay.modi/myschema/cars");

			// Create a reasoner and attach it to the model
			// This will allow JENA to use inference with SPARQL queries
			Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
			reasoner = reasoner.bindSchema(model);
			InfModel infmodel = ModelFactory.createInfModel(reasoner, model);

			// Create a new query
			String queryString = builder.getSPARQLQuery();

			com.hp.hpl.jena.query.Query query = QueryFactory
					.create(queryString);

			// Execute the query and obtain results
			qe = QueryExecutionFactory.create(query, infmodel);
			ResultSet results = qe.execSelect();

			result = buildHTMLOutputString(results);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Important - free up resources used running the query
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			if (qe != null) {
				qe.close();
			}
		}
		return result;
	}

	/**
	 * Convert the Result Set of results into a HTML string for display
	 * 
	 * @param results
	 *            The results of the query to display
	 * @return HTML containing the results
	 */
	private String buildHTMLOutputString(ResultSet results) {
		StringBuilder output = new StringBuilder();
		output.append("<html><body><table border=\"1\"><tr><td>Make</td><td>Model</td><td>Year</td><td>Color</td><td>Mileage</td><td>Price</td><td>Phone</td><td>Location</td></tr>");
		while (results.hasNext()) {
			output.append("<tr>");
			QuerySolution sol = results.next();
			output.append("<td>");
			output.append(sol.getLiteral("make"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("model"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("year"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("color"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("miles"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("price"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("phone"));
			output.append("</td>");
			output.append("<td>");
			output.append(sol.getLiteral("location"));
			output.append("</td>");
		}
		output.append("</table></body></html>");
		return output.toString();
	}
}
