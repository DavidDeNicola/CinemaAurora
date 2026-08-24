package org.elis.movieexplorer.utility;

public interface SwaggerTags {
		
	String ADMIN_TAG = "Admin";
	
	String ADMIN_TAG_DESC = "Questo endpoint deve essere chiamato da un Admin";
	
	
	String STAFF_TAG = "Staff";
	
	String STAFF_TAG_DESC = "Questo endpoint deve essere chiamato da uno Staffer";
	
	
	String CUSTOMER_TAG = "Cliente";

	String CUSTOMER_TAG_DESC = "Questo endpoint deve essere chiamato da un Cliente";
	
	
	String GUEST_TAG = "Ospite";
	
	String GUEST_TAG_DESC = "Questo endpoint può essere chiamato anche da un utente non loggato";
	
	String LOGGATO_TAG = "Loggato";
	
	String LOGGATO_TAG_DESC = "Questo endpoint può essere chiamato da qualsiasi utente loggato";

	
	String ERROR_TAG = "Contiene percorso, messaggio e istante dell'errore";
}
