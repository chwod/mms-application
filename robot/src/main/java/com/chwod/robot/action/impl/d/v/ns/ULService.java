package com.chwod.robot.action.impl.d.v.ns;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Event;
import com.chwod.robot.bean.Part;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.service.TalkService;
import com.chwod.robot.utils.Constants;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 当然是北京了
 * @author xliu
 *
 */
@Component("SS:D-V-NS-UL")
public class ULService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ULService.class);

	@Autowired
	private TalkService talkService;

	@Override
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		HttpSession session = sentence.getSession();
		@SuppressWarnings("unchecked")
		List<String> contextEventList = session.getAttribute(Constants.CURRENT_CONTEXT_EVENTS) == null ? null : (List<String>)session.getAttribute(Constants.CURRENT_CONTEXT_EVENTS);
		if(contextEventList == null) {
			sentence.setResponseWord(new StringBuffer("什么").append(sentence.getRequestWord()).append("?").toString());
			return sentence;
		}
		
		if(session.getAttribute(Constants.CURRENT_CONTEXT_STATUS_MISSING_SUBJECT) != null) {
			if(contextEventList.size() == 2) {
				String contextTalkWord = contextEventList.get(0);
				String targetTalkWord = contextEventList.get(1);
				List<String> possibleSubjectList = new ArrayList<>();
				for(Part part : sentence.getPartList()) {
					String subject = part.getWord();
					if(targetTalkWord.contains(subject)) {
						possibleSubjectList.add(subject);
					}
				}
				
				if(possibleSubjectList.size() <= 0) {
					sentence.setResponseWord("好像说的不是这个吧？");
					return sentence;
				}
				
				if(possibleSubjectList.size() > 1) {
					//more possible
					StringBuffer result = new StringBuffer("你指的是");
					for(String subject : possibleSubjectList) {
						result.append("‘").append(subject).append("’、还是");
					}
					result.replace(result.lastIndexOf("’、还是"), result.length(), "？");
					sentence.setResponseWord(result.toString());
					return sentence;
				}
				
				Event event = session.getAttribute(Constants.CONTEXT_EVENT) == null ? new Event() : (Event)session.getAttribute(Constants.CONTEXT_EVENT);
				event.setSubject(possibleSubjectList.get(0));
				Sentence contextSentence = new Sentence(sentence.getSession());
				contextSentence.setRequestWord(contextTalkWord);
				contextSentence.setProcessDeep(sentence.getProcessDeep());
				contextSentence = this.talkService.talk(contextSentence);
				sentence.setProcessDeep(contextSentence.getProcessDeep());
				sentence.setResponseWord(contextSentence.getResponseWord());
				
				// learning a new experience
				Sentence experienceSentence = new Sentence(sentence.getSession());
				experienceSentence.setRequestWord(targetTalkWord);
				this.talkService.learning(sentence, Constants.LEARNING.OK);
				
				//clear the context
				session.removeAttribute(Constants.CURRENT_CONTEXT_EVENTS);
				session.removeAttribute(Constants.CURRENT_CONTEXT_STATUS_MISSING_SUBJECT);
				
				return sentence;
				
				
			}
			
		}
		
		sentence.setResponseWord(new StringBuffer("什么").append(sentence.getRequestWord()).append("?").toString());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}

}
