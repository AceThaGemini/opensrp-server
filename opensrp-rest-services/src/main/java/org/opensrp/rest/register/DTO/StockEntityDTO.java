package org.opensrp.rest.register.DTO;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class StockEntityDTO {
	@JsonProperty
    private String provider;
    @JsonProperty
    private String date;
    @JsonProperty
    private String caseId;	    
    @JsonProperty
    private String report;
    @JsonProperty
    private String clientVersion;
    @JsonProperty
    private String type;
    @JsonProperty
    private String id;
    
	
	public StockEntityDTO() {
		
	}	  

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "id", "revision");
	}
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
