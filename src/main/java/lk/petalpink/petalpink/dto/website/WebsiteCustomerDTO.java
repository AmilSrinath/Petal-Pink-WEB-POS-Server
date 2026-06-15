package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteCustomerDTO {
    private Integer   cusId;
    private String    firstName;
    private String    lastName;
    private String    address1;
    private String    address2;
    private String    city;
    private String    email;
    private String    phone1;
    private String    phone2;
    private String    province;
    private String    country;
    private Timestamp createdDate;
    private Integer   status;
}
