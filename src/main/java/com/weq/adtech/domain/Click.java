package com.weq.adtech.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "click")
public class Click {

	@Id
	private String Id;
	private String clickId;
	private long advertisementId;
	private String deliveryId;
	private Date time;
	private String browser;
	private String os;
	private String site;
}
