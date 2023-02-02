package ma.cam.model;

public class ReponseDemande {

    private String errorCode;
    
    private String statusCode;
    
    private String idResponse;

    public ReponseDemande(String errorCode, String statusCode, String idResponse) {
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.idResponse = idResponse;
    }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getIdResponse() {
		return idResponse;
	}

	public void setIdResponse(String idResponse) {
		this.idResponse = idResponse;
	}

    
}
