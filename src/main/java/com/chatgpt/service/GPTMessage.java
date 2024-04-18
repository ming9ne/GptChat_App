package com.chatgpt.service;
import java.io.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

///////////////////////////////////////////////////////////////
// GPT Message Property
///////////////////////////////////////////////////////////////
@Getter
@Setter
@NoArgsConstructor
public class GPTMessage implements Serializable {
	  private static final long serialVersionUID = 1L;
	  private String userid ;
	  private Integer	history;	// History Number
	  private String rqst;
	  private String resp;
	  private Integer	token;
	  private String status;
	  private String msg;
	  
	  public GPTMessage(String UserID,int nHistory, String Rqst) {
		  this.userid = UserID;
		  this.history = nHistory;
		  this.rqst = Rqst;
	  }
}
