//
//package utilities;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.ContextResolver;
//import org.glassfish.jersey.client.ClientProperties;
//
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * Web Service client class that returns JSON response.<br>
// * (1) Supports "get" calls <br>
// * (2) Nested "Results" class supports converting JSON response to generic
// * object.<br>
// * 
// * @author scott.brazelton
// *
// */
//public class WebServiceClient {
//
//	private String baseUrl;
//
//	/**
//	 * Constructor: Accepts base URL needed for the API and then instantiates
//	 */
//	public WebServiceClient(String baseUrl) {
//		this.baseUrl = baseUrl;
//	}
//
//	/**
//	 * Gets an instance of "Results" based on the get path sent
//	 * 
//	 * @param path
//	 *            - path to get method from base Url
//	 * @return Results
//	 */
//	public Results get(String path) {
//		Client client = ClientBuilder.newClient();
//
//		Response response = client.target(baseUrl).path(path)
//				.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
//				.register(new MapperProvider())
//				.request(MediaType.APPLICATION_JSON).get();
//
//		return new Results(response);
//
//	}
//
//	/**
//	 * Results of an API call. Supports reading the results<br>
//	 * 
//	 * @author scott.brazelton
//	 *
//	 */
//	public class Results {
//		private Response response;
//
//		/**
//		 * Constructor: Accepts response needed to read the results and then
//		 * instantiates
//		 */
//		public Results(Response response) {
//			this.response = response;
//		}
//
//		/**
//		 * Reads the results of the API request into specified generic class
//		 * 
//		 * @param t
//		 * @throws WebServiceException
//		 *             - if web service doesn't return 200 response
//		 * @return specified generic class
//		 */
//		public <T> T readResults(Class<T> t) {
//
//			T genericClass = null;
//
//			if (isResponseStatusOK()) {
//				genericClass = response.readEntity(t);
//			} else {
////				throw new WebServiceException(String.format(
////						"The web service API returned a %d (%s) response.  Body of response (if any): %s",
////						response.getStatus(),
////						response.getStatusInfo().getReasonPhrase(),
////						response.readEntity(String.class)));
//			}
//
//			return genericClass;
//		}
//
//		/**
//		 * Is the web service response status OK
//		 * 
//		 * @return boolean - true = 200 (OK) response returned | false =
//		 *         response not 200 (OK)
//		 * 
//		 */
//		private boolean isResponseStatusOK() {
//			boolean isOK = false;
//			switch (response.getStatus()) {
//
//				case 200 :
//					isOK = true;
//					break;
//				case 400 :
//				case 404 :
//				case 500 :
//
//				default :
//					isOK = false;
//					break;
//			}
//
//			return isOK;
//		}
//	}
//
//	/**
//	 * Private object mapper class to accept case insensitive properties<br>
//	 * This is done because Java class
//	 * 
//	 * 
//	 * @author scott.brazelton
//	 *
//	 */
//	class MapperProvider implements ContextResolver<ObjectMapper> {
//
//		final ObjectMapper mapper;
//
//		/**
//		 * Constructor: Turns on accepting case insensitive properties
//		 */
//		public MapperProvider() {
//			mapper = new ObjectMapper();
//			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
//					true);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		@Override
//		public ObjectMapper getContext(Class<?> type) {
//
//			return mapper;
//		}
//
//	}
//
//}
