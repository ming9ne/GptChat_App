package com.chatgpt.service;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import com.chatgpt.model.UserInfo;
import jakarta.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import com.chatgpt.model.ChattHistory;

import com.chatgpt.repository.ChattHistoryRepository;
import com.chatgpt.repository.UserRepository;

@Service
public class GptChattService {
	@Autowired
	private ChattHistoryRepository chatthistoryresp;
	@Autowired
	private UserRepository userresp;

	@Value("${gpt.server.url}")
	private String gptserverurl;
	@Value("${gpt.server.userinfo}")
	private String gptserveruserinfo;
	@Value("${gpt.chatt.tokens}")
	private int nchattokens;

	/////////////////////////////////////
	//
	/////////////////////////////////////
	public GPTMessage chatting(GPTMessage gptmsg ) {
		String retmsg;

		String wktmsg = "@@@@chatting called @@" + gptmsg.getUserid() + "@@" + gptmsg.getRqst();
		System.out.println(wktmsg);

		// Check
		if ( gptmsg.getUserid() == null || gptmsg.getHistory() == null || gptmsg.getRqst() == null ) {
			retmsg = "@@ userid,history,rqst는 필수 항목입니다";
			System.out.println(retmsg);

			gptmsg.setStatus("ERROR");
			gptmsg.setMsg(retmsg);

			return gptmsg;
		}

		// Remain Token Check
		long remain = GetRemainToken(gptmsg);
		if ( remain <= 0) {
			retmsg = "@@ 남은 토큰이 없습니다 ";
			System.out.println(retmsg);

			gptmsg.setStatus("ERROR");
			gptmsg.setMsg(retmsg);

			return gptmsg;
		}

		// Chatting
		GPTMessage gptrespmsg = chatting_sub(gptmsg);

		if ( !gptrespmsg.getStatus().equalsIgnoreCase("OK")) {
			retmsg = "@@chatt start error: " + gptrespmsg.getMsg();
			System.out.println(retmsg);

			return gptrespmsg;
		}

		// History Insert and Used Token Update
		int n = InsertChattHistory(gptrespmsg);

		wktmsg = "@@@@chatt call end: " + gptmsg.getUserid() + "@@" + gptmsg.getResp();
		System.out.println(wktmsg);

		return gptrespmsg;
	}

	/////////////////////////////////////
	//
	/////////////////////////////////////
	private GPTMessage chatting_sub(GPTMessage gptmsg ) {

		String wktmsg = "@@@@chatting_sub called @@" + gptmsg.getUserid() + "@@" + gptmsg.getRqst();
		System.out.println(wktmsg);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + gptserveruserinfo);

		// Request Body
		JSONObject request = MakeGptRequestBody(gptmsg);

		//String strrqst = request.toString();
		//
		try {
			HttpEntity<String> httpentity = new HttpEntity<>(request.toString(),headers);

			RestTemplate rest = new RestTemplate();
			ResponseEntity<Map> responseentity = rest.exchange(gptserverurl,
					HttpMethod.POST,
					httpentity, Map.class);

			Map<String,Object> respbody = responseentity.getBody();

			gptmsg.setStatus("OK");
			gptmsg.setMsg("");
			gptmsg.setResp("Response Not Found");
			if ( respbody.containsKey("choices") ) {
				JSONObject outobj = new JSONObject(respbody);
				String retmsg = GetContent(outobj, null, "content");

				String strtoken = GetContent(outobj, null, "total_tokens");
				int ntoken = Integer.parseInt(strtoken);

				gptmsg.setResp(retmsg);
				gptmsg.setToken(ntoken);
			}
		} catch(Exception e) {
			e.printStackTrace();

			gptmsg.setStatus("ERROR");
			gptmsg.setMsg(e.getMessage());
		}

		wktmsg = "@@@@chatting_sub call end: " + gptmsg.getUserid() + "@@" + gptmsg.getResp();
		System.out.println(wktmsg);

		return gptmsg;
	}

	///////////////////////////////////////////////
	// Insert Chatting History & Update used token
	///////////////////////////////////////////////
	@Transactional
	public int InsertChattHistory(GPTMessage gptmsg ) {

		try {
			ChattHistory hist = new ChattHistory(gptmsg.getUserid(), gptmsg.getToken(),
					gptmsg.getRqst(), gptmsg.getResp());
			chatthistoryresp.save(hist);
			//userresp.UpdateUsedToken(gptmsg.getUserID(), gptmsg.getNToken());	// native sql
			userresp.UpdateUsedToken(gptmsg.getUserid(),Long.valueOf(gptmsg.getToken()) );  //JPQL
			return 0;
		} catch ( Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	/////////////////////////////////////
	// Get Remain Token
	/////////////////////////////////////
	public long GetRemainToken(GPTMessage gptmsg ) {
		long nremain = 0;

		Optional<UserInfo> useropt = userresp.findById(gptmsg.getUserid());
		if (useropt.isPresent() ) {
			UserInfo user = useropt.get();
			nremain = user.getMaxToken() - user.getUsedToken();
			if ( nremain < 0)
				nremain = 0;
		}

		return nremain;

	}

	/////////////////////////////////////
	// Make GPT Headers
	/////////////////////////////////////
	public JSONObject MakeGptRequestBody(GPTMessage gptmsg ) {

		// Request Body
		JSONObject request = new JSONObject();
		request.put("model", "gpt-3.5-turbo");
		request.put("temperature", 0.5);
		request.put("max_tokens", nchattokens);

		JSONArray msgarr = new JSONArray();

		// 과거 History 처리
		Pageable page = PageRequest.of(0, gptmsg.getHistory(),Sort.by("createDate").descending());
		Page<ChattHistory> chattpage = chatthistoryresp.findByUserId(gptmsg.getUserid(), page);
		List<ChattHistory> chattlist = chattpage.getContent();
		ListIterator<ChattHistory> lt = chattlist.listIterator(chattlist.size());
		while( lt.hasPrevious()) {
			ChattHistory ch = lt.previous();

			System.out.println("@@@ history[" + ch.getSeq() + "][" + ch.getCreateDate() + "][" + ch.getRqst() + "][" + ch.getResp() + "]");

			JSONObject obj1 = new JSONObject();
			obj1.put("role", "user");
			obj1.put("content", ch.getRqst());   // 과거 요청
			msgarr.put(obj1);

			JSONObject obj2 = new JSONObject();
			obj2.put("role", "assistant");
			obj2.put("content", ch.getResp());   // 과거 응답
			msgarr.put(obj2);
		}

		// 현재 요청 사항
		JSONObject obj3 = new JSONObject();
		obj3.put("role", "user");
		obj3.put("content", gptmsg.getRqst());
		msgarr.put(obj3);

		request.put("messages", msgarr);

		return request;
	}


	/////////////////////////////////////
	// Find Json Element
	/////////////////////////////////////
	public String GetContent(JSONObject obj, JSONArray arr, String findKey)
	{
		String key= null;
		String val = null;
		Object subobj = null;

		if ( obj != null ) {
			if ( obj.has(findKey))
				return obj.get(findKey).toString();

			Iterator<String> keys = obj.keys();
			while( keys.hasNext()) {
				key = keys.next();
				subobj = obj.get(key);
				if (  subobj instanceof JSONObject ) {
					val = GetContent((JSONObject)subobj, null, findKey);
					if ( val != null )
						return val;
				} else if (subobj instanceof JSONArray) {
					String outval = GetContent(null,(JSONArray)subobj, findKey);
					if ( outval != null )
						return outval;
				}
			} // while
		} else {
			for( int i = 0; i < arr.length(); i++ ) {
				subobj = arr.get(i);
				if (  subobj instanceof JSONObject ) {
					val = GetContent((JSONObject)subobj, null, findKey);
					if ( val != null )
						return val;
				}
			}	// for
		}

		return null;
	} // getcontent

}
