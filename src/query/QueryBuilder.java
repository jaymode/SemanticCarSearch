package query;

/**
 * This class is responsible for constructing a SPARQL query based on the
 * parameters passed in
 * 
 * @author Jay
 * 
 */
public class QueryBuilder {

	private String make;
	private String model;
	private String year;
	private String color;
	private String location;

	/**
	 * Construct the QueryBuilder object
	 * 
	 * @param make
	 *            The make of the car
	 * @param model
	 *            The model of the car
	 * @param year
	 *            The year of the car
	 * @param color
	 *            The color of the car
	 * @param location
	 *            The location of the car
	 */
	public QueryBuilder(String make, String model, String year, String color,
			String location) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
		this.location = location;
	}

	/**
	 * Build the SPARQL query based off of the values passed in on creation of
	 * the class
	 * 
	 * @return SPARQL Query in the form of a String
	 */
	public String getSPARQLQuery() {
		StringBuilder builder = new StringBuilder();

		builder.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX cars: <http://jay.modi/myschema/cars#> ");
		builder.append("SELECT ?make ?model ?year ?color ?miles ?price ?phone ?location \n");
		builder.append("WHERE {\n");
		builder.append("?ads rdf:type cars:carAd ; ");
		builder.append("cars:hasMake ?manufacturer . ");
		builder.append("?maker rdf:type cars:make ; ");
		if (make != null && !make.isEmpty()) {
			builder.append("cars:companyName '");
			builder.append(make);
			builder.append("' ; ");
		}
		builder.append("cars:companyName ?make . ");

		if (model != null && !model.isEmpty()) {
			builder.append("?ads cars:hasModel '");
			builder.append(model);
			builder.append("' . ");
		}
		builder.append("?ads cars:hasModel ?model . ");

		if (year != null && !year.isEmpty()) {
			builder.append("?ads cars:hasYear '");
			builder.append(year);
			builder.append("' . ");
		}
		builder.append("?ads cars:hasYear ?year . ");

		if (color != null && !color.isEmpty()) {
			builder.append("?ads cars:hasColor '");
			builder.append(color);
			builder.append("' . ");
		}
		builder.append("?ads cars:hasColor ?color . ");

		if (location != null && !location.isEmpty()) {
			builder.append("?ads cars:hasLocation '");
			builder.append(location);
			builder.append("' . ");
		}
		builder.append("?ads cars:hasLocation ?location . ");

		builder.append("?ads cars:hasMileage ?miles . ");
		builder.append("?ads cars:hasPrice ?price . ");
		builder.append("?ads cars:hasPhoneNumber ?phone . ");

		builder.append("FILTER(");
		builder.append("?maker = ?manufacturer");
		builder.append(") ");
		builder.append("}");
		return builder.toString();
	}
}
