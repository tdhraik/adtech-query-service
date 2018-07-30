package com.weq.adtech.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "delivery")
public class Delivery implements Serializable {

	@Id
	private String id;
	private long advertisementId;
	private String deliveryId;
	private Date time;
	private String browser;
	private String os;
	private String site;
}
