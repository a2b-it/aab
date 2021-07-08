package ma.akhdarbank.apps.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketBatch {
	public String codeMessage;
    public String message;
    public String result;
    public Long ticket;
}
